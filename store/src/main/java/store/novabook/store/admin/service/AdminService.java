package store.novabook.store.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.admin.dto.CreateAdminRequest;
import store.novabook.store.admin.dto.GetAdminResponse;
import store.novabook.store.admin.entity.Admin;
import store.novabook.store.admin.exception.AdminAlreadyExistsException;
import store.novabook.store.admin.exception.AdminNotFoundException;
import store.novabook.store.admin.repository.AdminRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {

	private final AdminRepository adminRepository;

	public Admin createAdmin(CreateAdminRequest createAdminRequest) {
		Admin admin = Admin.builder()
			.loginId(createAdminRequest.loginId())
			.loginPassword(createAdminRequest.loginPassword())
			.name(createAdminRequest.name())
			.number(createAdminRequest.number())
			.email(createAdminRequest.email())
			.build();

		validateId(admin.getId());
		Admin savedAdmin = adminRepository.save(admin);
		return savedAdmin;
	}

	public List<GetAdminResponse> getAdminAll() {
		return adminRepository.findAll().stream()
			.map(admin -> new GetAdminResponse(
				admin.getId(),
				admin.getLoginId(),
				admin.getName(),
				admin.getNumber(),
				admin.getEmail()
			))
			.collect(Collectors.toList());
	}

	public GetAdminResponse getAdmin(Long adminId) {
		Admin admin = adminRepository.findById(adminId).orElse(null);
		if (admin != null) {
			return new GetAdminResponse(
				admin.getId(),
				admin.getLoginId(),
				admin.getName(),
				admin.getNumber(),
				admin.getEmail()
			);
		}
		throw new AdminNotFoundException();
	}

	public void updateAdmin(Long adminId, CreateAdminRequest createAdminRequest) {
		Admin admin = adminRepository.findById(adminId).orElse(null);
		if (admin != null) {
			admin.update(
				createAdminRequest.loginId(),
				createAdminRequest.loginPassword(),
				createAdminRequest.name(),
				createAdminRequest.number(),
				createAdminRequest.email()
			);

			adminRepository.save(admin);
		}
		throw new AdminNotFoundException();
	}

	public void deleteAdmin(Long adminId) {
		Admin admin = adminRepository.findById(adminId).orElse(null);
		if (admin != null) {
			adminRepository.delete(admin);
		}
		throw new AdminNotFoundException();
	}

	private void validateId(Long id) {
		if (adminRepository.existsById(id)) {
			throw new AdminAlreadyExistsException(id);
		}
	}
}

