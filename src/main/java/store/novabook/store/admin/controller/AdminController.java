package store.novabook.store.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CheckRole;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/admin")
public class AdminController {

	@CheckRole("ROLE_ADMIN")
	@GetMapping()
	public ResponseEntity<Void> admin() {
		return ResponseEntity.ok().build();
	}
}
