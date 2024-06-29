package store.novabook.store.common.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.common.security.dto.GetNewTokenRequest;
import store.novabook.store.common.security.dto.GetNewTokenResponse;

@FeignClient(name = "newTokenClient", url = "http://localhost:9777/auth/refresh")
public interface NewTokenClient {
	@PostMapping
	GetNewTokenResponse getNewToken(@RequestBody GetNewTokenRequest getNewTokenRequest);
}