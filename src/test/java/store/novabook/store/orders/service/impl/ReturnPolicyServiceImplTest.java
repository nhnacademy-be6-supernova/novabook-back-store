package store.novabook.store.orders.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;
import store.novabook.store.orders.entity.ReturnPolicy;
import store.novabook.store.orders.repository.ReturnPolicyRepository;

class ReturnPolicyServiceImplTest {
	@Mock
	private ReturnPolicyRepository returnPolicyRepository;

	@InjectMocks
	private ReturnPolicyServiceImpl returnPolicyServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave() {
		CreateReturnPolicyRequest request = CreateReturnPolicyRequest.builder()
			.content("Sample Return Policy Content")
			.build();
		ReturnPolicy returnPolicy = new ReturnPolicy(request);
		when(returnPolicyRepository.save(any(ReturnPolicy.class))).thenReturn(returnPolicy);

		CreateResponse response = returnPolicyServiceImpl.save(request);

		assertNotNull(response);
		assertEquals(returnPolicy.getId(), response.id());
		verify(returnPolicyRepository, times(1)).save(any(ReturnPolicy.class));
	}

	@Test
	void testLatestReturnPolicyContent() {
		String expectedContent = "Latest Return Policy Content";
		when(returnPolicyRepository.findContentByOrderByIdDesc()).thenReturn(expectedContent);

		String actualContent = returnPolicyServiceImpl.latestReturnPolicyContent();

		assertEquals(expectedContent, actualContent);
		verify(returnPolicyRepository, times(1)).findContentByOrderByIdDesc();
	}

	@Test
	void testGetReturnPolicies() {
		ReturnPolicy policy1 = new ReturnPolicy(CreateReturnPolicyRequest.builder().content("Policy 1").build());
		ReturnPolicy policy2 = new ReturnPolicy(CreateReturnPolicyRequest.builder().content("Policy 2").build());
		List<ReturnPolicy> returnPolicies = Arrays.asList(policy1, policy2);
		when(returnPolicyRepository.findAll()).thenReturn(returnPolicies);

		Page<GetReturnPolicyResponse> result = returnPolicyServiceImpl.getReturnPolicies();

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals("Policy 1", result.getContent().get(0).content());
		assertEquals("Policy 2", result.getContent().get(1).content());
		verify(returnPolicyRepository, times(1)).findAll();
	}

	@Test
	void testGetReturnPolicyById() {
		Long id = 1L;
		ReturnPolicy returnPolicy = new ReturnPolicy(
			CreateReturnPolicyRequest.builder().content("Policy Content").build());
		when(returnPolicyRepository.findById(id)).thenReturn(Optional.of(returnPolicy));

		GetReturnPolicyResponse response = returnPolicyServiceImpl.getReturnPolicyById(id);

		assertNotNull(response);
		assertEquals(returnPolicy.getContent(), response.content());
		verify(returnPolicyRepository, times(1)).findById(id);
	}

	@Test
	void testGetReturnPolicyByIdNotFound() {
		Long id = 1L;
		when(returnPolicyRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			returnPolicyServiceImpl.getReturnPolicyById(id);
		});

		verify(returnPolicyRepository, times(1)).findById(id);
	}

}
