package store.novabook.store.member.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;

@FeignClient(name = "memberClient", url = "http://localhost:9777/auth/members/uuid")
public interface MemberClient {
	@PostMapping
	GetMembersUUIDResponse getMembersId(@RequestBody GetMembersUUIDRequest getMembersUUIDRequest);

}