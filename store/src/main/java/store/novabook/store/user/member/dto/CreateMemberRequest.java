package store.novabook.store.user.member.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(

	@NotBlank
	String loginId,

	@NotBlank
	String loginPassword,

	@NotBlank
	String name,

	@NotBlank
	String number,

	@NotBlank
	@Email
	String email,

	@NotBlank
	LocalDateTime birth) {


}
