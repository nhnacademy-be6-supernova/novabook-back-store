package store.novabook.store.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(

	@NotBlank
	String loginId,

	@NotBlank
	String loginPassword,

	@NotBlank
	String name,

	@NotBlank
	String number,

	@NotBlank
	String email

) {
}
