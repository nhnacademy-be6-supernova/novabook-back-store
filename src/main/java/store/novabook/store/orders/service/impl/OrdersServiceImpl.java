package store.novabook.store.orders.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
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
}
