package store.novabook.store.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SagaMessage {
	private String status;
	private Long orderId;

}