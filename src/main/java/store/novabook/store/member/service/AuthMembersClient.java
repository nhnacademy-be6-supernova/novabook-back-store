package store.novabook.store.member.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.member.dto.request.GetDormantMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetDormantMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;

@FeignClient(name = "authMembersClient", url = "http://localhost:9777/auth/members/uuid")
public interface AuthMembersClient {
	@PostMapping
	ApiResponse<GetMembersUUIDResponse> getMembersId(@Valid @RequestBody GetMembersUUIDRequest getMembersUUIDRequest);

	@PostMapping("/dormant")
	ApiResponse<GetDormantMembersUUIDResponse> getDormantMembersId(
		@Valid @RequestBody GetDormantMembersUUIDRequest getDormantMembersUUIDRequest);

}