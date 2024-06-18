package store.novabook.store.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.admin.dto.CreateAdminRequest;
import store.novabook.store.admin.dto.GetAdminResponse;
import store.novabook.store.admin.entity.Admin;
import store.novabook.store.admin.service.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

	private final AdminService adminService;

	@PostMapping
	public ResponseEntity<Void> createAdmin(@RequestBody CreateAdminRequest createAdminRequest) {
		Admin createdAdmin = adminService.createAdmin(createAdminRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<List<GetAdminResponse>> getAdminAll() {
		List<GetAdminResponse> adminAll = adminService.getAdminAll();
		return ResponseEntity.ok(adminAll);
	}

	@GetMapping("/{adminId}")
	public ResponseEntity<GetAdminResponse> getAdmin(@PathVariable Long adminId) {
		GetAdminResponse admin = adminService.getAdmin(adminId);
		return ResponseEntity.ok(admin);
	}

	@PutMapping("/{adminId}")
	public ResponseEntity<Void> updateAdmin(@PathVariable Long adminId, @RequestBody CreateAdminRequest createAdminRequest) {
		adminService.updateAdmin(adminId, createAdminRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{adminId}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
		adminService.deleteAdmin(adminId);
		return ResponseEntity.ok().build();
	}


}
