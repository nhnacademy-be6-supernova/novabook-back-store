package store.novabook.store.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.service.GuestService;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestServiceImpl implements GuestService {

	private final OrdersBookRepository ordersBookRepository;

	@Override
	@Transactional(readOnly = true)
	public GetOrderDetailResponse getOrderGuest(GetGuestOrderHistoryRequest request) {
		List<OrdersBook> ordersBook = ordersBookRepository.findByOrdersCode(request.code());
		if (ordersBook.isEmpty()) {
			throw new NotFoundException(ErrorCode.ORDERS_NOT_FOUND);
		}
		return GetOrderDetailResponse.of(ordersBook);
	}
}
