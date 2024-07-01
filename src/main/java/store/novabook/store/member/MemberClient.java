package store.novabook.store.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.member.dto.GetMembersUUIDRequest;
import store.novabook.store.member.dto.GetMembersUUIDResponse;

@FeignClient(name = "memberClient", url = "http://localhost:9777/auth/uuid")
public interface MemberClient {
	@PostMapping
	GetMembersUUIDResponse getMembersId(@RequestBody GetMembersUUIDRequest getMembersUUIDRequest);

}