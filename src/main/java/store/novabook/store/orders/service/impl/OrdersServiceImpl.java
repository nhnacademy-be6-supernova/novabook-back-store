package store.novabook.store.orders.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.TossPaymentRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.DeliveryFeeRepository;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.orders.service.OrdersService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

	private final OrdersRepository ordersRepository;
	private final DeliveryFeeRepository deliveryFeeRepository;
	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrdersStatusRepository ordersStatusRepository;
	private final MemberRepository memberRepository;

	@Override
	public JSONObject create(TossPaymentRequest request) {
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();

		obj.put("orderId", request.orderId());
		obj.put("amount", request.amount());
		obj.put("paymentKey", request.paymentKey());

		log.info("[TossPayment] 전달 받은 파라미터 값 : {}, {} , {}",
			request.orderId()
			,request.amount()
			,request.paymentKey()
		);

		// 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
		// 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
		String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		try {
			URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Authorization", authorizations);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));

			// 결제를 승인하면 결제수단에서 금액이 차감돼요.
			int code = connection.getResponseCode();
			boolean isSuccess = code == 200;

			InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

			// 결제 성공 및 실패 비즈니스 로직을 구현하세요.
			Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
			JSONObject jsonObject = (JSONObject)parser.parse(reader);
			responseStream.close();
			return jsonObject;

		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CreateResponse create(CreateOrdersRequest request) {
		return null;
	}

	@Override
	public Page<GetOrdersResponse> getOrdersResponsesAll() {
		List<Orders> orders = ordersRepository.findAll();
		List<GetOrdersResponse> responses = new ArrayList<>();
		for (Orders order : orders) {
			responses.add(GetOrdersResponse.form(order));
		}
		return new PageImpl<>(responses);
	}

	@Override
	public GetOrdersResponse getOrdersById(Long id) {
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, id));
		return GetOrdersResponse.form(orders);
	}

	@Override
	public void update(Long id, UpdateOrdersRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class, request.memberId()));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(DeliveryFee.class, request.deliveryFeeId()));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(WrappingPaper.class, request.wrappingPaperId()));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(OrdersStatus.class, request.ordersStatusId()));
		Orders orders = ordersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Orders.class, id));
		orders.update(member, deliveryFee, wrappingPaper, ordersStatus, request);
	}

}
