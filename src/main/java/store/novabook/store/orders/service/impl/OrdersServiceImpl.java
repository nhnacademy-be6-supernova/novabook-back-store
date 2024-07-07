package store.novabook.store.orders.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.entity.BookStatusEnum;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.request.BookIdAndQuantityDTO;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
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
import store.novabook.store.orders.repository.RedisOrderRepository;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.orders.service.OrdersService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

	// 보관용 재고
	private final OrdersRepository ordersRepository;
	private final DeliveryFeeRepository deliveryFeeRepository;
	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrdersStatusRepository ordersStatusRepository;
	private final MemberRepository memberRepository;
	private final RedisOrderRepository redisOrderRepository;
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;

	private RabbitTemplate rabbitTemplate;

	@Override
	public JSONObject create(TossPaymentRequest request) {
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();

		obj.put("orderId", request.orderId());
		obj.put("amount", request.amount());
		obj.put("paymentKey", request.paymentKey());

		log.info("[TossPayment] 전달 받은 파라미터 값 : {}, {} , {}",
			request.orderId()
			, request.amount()
			, request.paymentKey()
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
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		Orders orders = new Orders(member, deliveryFee, wrappingPaper, ordersStatus, request);
		ordersRepository.save(orders);
		return new CreateResponse(orders.getId());
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
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		return GetOrdersResponse.form(orders);
	}

	/**
	 * @author 2-say
	 * 가주문서 검증 비지니스 로직
	 * 제고 감소도 함께 일어남
	 */
	@Transactional
	@RabbitListener(queues = "nova.orders.formConfirm.queue")
	public void confirmOrderForm(Object paymentInfo) {
		try {
			// orderForm Fetch
			TossPaymentRequest toss = (TossPaymentRequest)paymentInfo;
			Optional<OrderTemporaryForm> orderForm = redisOrderRepository.findByOrderUUID(
				UUID.fromString(toss.orderId()));

			if (orderForm.isEmpty()) {
				throw new IllegalArgumentException("주문 정보가 없습니다.");
			}

			OrderTemporaryForm orderTemporaryForm = orderForm.get();
			List<BookIdAndQuantityDTO> books = orderTemporaryForm.books();

			long totalPrice = 0;

			// 조회를 한번만 하기 위해서 Cache 저장
			Map<Long, Book> bookCache = new HashMap<>();

			for (BookIdAndQuantityDTO bookDTO : books) {
				Book book = bookCache.computeIfAbsent(bookDTO.id(), id -> {
					Optional<Book> optionalBook = bookRepository.findById(id);
					if (optionalBook.isEmpty()) {
						throw new IllegalArgumentException("해당 도서가 존재하지 않습니다: " + id);
					}
					return optionalBook.get();
				});

				if (!book.getBookStatus().getName().equals(BookStatusEnum.FOR_SALE.getKoreanValue())) {
					throw new IllegalArgumentException("판매중인 도서가 아닙니다: " + book.getId());
				}

				totalPrice += book.getPrice() * bookDTO.quantity();
			}

			if (totalPrice != toss.amount()) {
				throw new IllegalArgumentException("가격 정보가 불일치 합니다.");
			}

			// 재고 감소는 총 가격 확인 후에 수행
			for (BookIdAndQuantityDTO bookDTO : books) {
				Book book = bookCache.get(bookDTO.id());
				book.decreaseInventory((int)bookDTO.quantity());

				// 판매 완료 시 상태 변경
				if(book.getInventory() <= 0) {
					Optional<BookStatus> statusOptional = bookStatusRepository.findById(BookStatusEnum.OUT_OF_STOCK.getValue());
					if(statusOptional.isEmpty()) {
						throw new IllegalArgumentException("책 상태를 찾을 수 없습니다.");
					}
					book.setBookStatus(statusOptional.get());
				}

				// 저장
				bookRepository.save(book);
			}
		} catch (Exception e) {
			// 예외가 발생하면 롤백 처리
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 실패 메시지 전송
			rabbitTemplate.convertAndSend("saga-exchange", "api1-producer-routing-key");
			// 예외 다시 던지기
			throw e;
		}
	}


	/**
	 * 상위 행위에서 에러 발생 시 보상 트랜잭션 행위
	 */
	@RabbitListener(queues = "nova.orders.compensate.formConfirm.queue")
	void compensateConfirmOrderForm() {
			// 재고 증가
			// 만약 도서 수량이 많으면 도서 상태 변경
			//
	}


	@Override
	public void update(Long id, UpdateOrdersRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		orders.update(member, deliveryFee, wrappingPaper, ordersStatus, request);
	}

}
