package store.novabook.store.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.order.dto.CreateOrdersRequest;
import store.novabook.store.order.dto.CreateResponse;
import store.novabook.store.order.dto.GetOrdersResponse;
import store.novabook.store.order.dto.UpdateOrdersRequest;
import store.novabook.store.order.entity.DeliveryFee;
import store.novabook.store.order.entity.Orders;
import store.novabook.store.order.entity.OrdersStatus;
import store.novabook.store.order.entity.ReturnPolicy;
import store.novabook.store.order.entity.WrappingPaper;
import store.novabook.store.order.repository.DeliveryFeeRepository;
import store.novabook.store.order.repository.OrdersRepository;
import store.novabook.store.order.repository.OrdersStatusRepository;
import store.novabook.store.order.repository.ReturnPolicyRepository;
import store.novabook.store.order.repository.WrappingPaperRepository;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.repository.UsersRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersService {
	private final OrdersRepository ordersRepository;
	private final DeliveryFeeRepository deliveryFeeRepository;
	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrdersStatusRepository ordersStatusRepository;
	private final ReturnPolicyRepository returnPolicyRepository;
	private final UsersRepository usersRepository;

	//생성
	public CreateResponse create(CreateOrdersRequest request){
		Users users = usersRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(Users.class, request.userId()));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(DeliveryFee.class, request.deliveryFeeId()));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(WrappingPaper.class, request.wrappingPaperId()));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(OrdersStatus.class, request.ordersStatusId()));
		ReturnPolicy returnPolicy = returnPolicyRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(ReturnPolicy.class, request.returnPolicyId()));
		Orders orders = new Orders(users, deliveryFee, wrappingPaper,ordersStatus,returnPolicy,request);
		ordersRepository.save(orders);
		return new CreateResponse(orders.getId());
	}

	public Page<GetOrdersResponse> getOrdersResponsesAll(){
		List<Orders> orders = ordersRepository.findAll();
		List<GetOrdersResponse> responses = new ArrayList<>();
		for(Orders order : orders){
			responses.add(GetOrdersResponse.form(order));
		}
		return new PageImpl<>(responses);
	}

	public GetOrdersResponse getOrdersById(Long id){
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, id));
		return GetOrdersResponse.form(orders);
	}

	public void update(Long id, UpdateOrdersRequest request){
		Users users = usersRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(Users.class, request.userId()));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(DeliveryFee.class, request.deliveryFeeId()));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(WrappingPaper.class, request.wrappingPaperId()));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(OrdersStatus.class, request.ordersStatusId()));
		ReturnPolicy returnPolicy = returnPolicyRepository.findById(request.userId())
			.orElseThrow(() -> new EntityNotFoundException(ReturnPolicy.class, request.returnPolicyId()));
		Orders orders = ordersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Orders.class, id));
		orders.update(users,deliveryFee,wrappingPaper,ordersStatus,returnPolicy,request);
	}

}
