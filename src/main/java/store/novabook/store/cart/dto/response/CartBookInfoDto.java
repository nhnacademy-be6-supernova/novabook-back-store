package store.novabook.store.cart.dto.response;

public record CartBookInfoDto (
	Long id,
	Long discountPrice,
	Integer bookStatusId
){
}
