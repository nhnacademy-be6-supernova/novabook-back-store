package store.novabook.store.orders.service;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;

import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.TossPaymentRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

public interface OrdersService {

	Page<GetOrdersResponse> getOrdersResponsesAll();

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersRequest request);
}
