package store.novabook.store.admin.dto;

public record GetAdminResponse(
	Long id,
	String loginId,
	String name,
	String number,
	String email
) {
}
