package store.novabook.store.common.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.common.security.dto.FindMembersRequest;
import store.novabook.store.member.dto.FindMemberLoginResponse;

@FeignClient(name = "customUserDetailClient", url = "http://127.0.0.1:9777/api/v1/store/members")
public interface CustomMembersDetailClient {

	@PostMapping("/find")
	ApiResponse<FindMemberLoginResponse> find(@RequestBody FindMembersRequest findMembersRequest);

	@PostMapping("/find/admin")
	ApiResponse<FindMemberLoginResponse> findAdmin(@RequestBody FindMembersRequest findMembersRequest);
}
