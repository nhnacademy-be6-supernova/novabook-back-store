package store.novabook.store.orders.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.orders.service.impl.WrappingPaperServiceImpl;

class WrappingPaperServiceImplTest {

	@Mock
	private WrappingPaperRepository wrappingPaperRepository;

	@InjectMocks
	private WrappingPaperServiceImpl wrappingPaperServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateWrappingPaper() {
		CreateWrappingPaperRequest request = CreateWrappingPaperRequest.builder()
			.name("Holiday Wrap")
			.price(10L)
			.status("Available")
			.build();
		WrappingPaper wrappingPaper = new WrappingPaper(request);
		when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenReturn(wrappingPaper);

		CreateResponse response = wrappingPaperServiceImpl.createWrappingPaper(request);

		assertNotNull(response);
		assertEquals(wrappingPaper.getId(), response.id());
		verify(wrappingPaperRepository, times(1)).save(any(WrappingPaper.class));
	}

	@Test
	void testGetWrappingPaperAll() {
		WrappingPaper paper1 = new WrappingPaper(CreateWrappingPaperRequest.builder()
			.name("Wrap 1")
			.price(5L)
			.status("Available")
			.build());
		WrappingPaper paper2 = new WrappingPaper(CreateWrappingPaperRequest.builder()
			.name("Wrap 2")
			.price(15L)
			.status("Unavailable")
			.build());
		List<WrappingPaper> wrappingPapers = Arrays.asList(paper1, paper2);
		when(wrappingPaperRepository.findAll()).thenReturn(wrappingPapers);

		Pageable pageable = PageRequest.of(0, 10);
		Page<GetWrappingPaperResponse> result = wrappingPaperServiceImpl.getWrappingPaperAll(pageable);

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals("Wrap 1", result.getContent().get(0).name());
		assertEquals("Wrap 2", result.getContent().get(1).name());
		verify(wrappingPaperRepository, times(1)).findAll();
	}

	@Test
	void testGetWrappingPaperById() {
		Long id = 1L;
		WrappingPaper wrappingPaper = new WrappingPaper(CreateWrappingPaperRequest.builder()
			.name("Wrap 1")
			.price(5L)
			.status("Available")
			.build());
		when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(wrappingPaper));

		GetWrappingPaperResponse response = wrappingPaperServiceImpl.getWrappingPaperById(id);

		assertNotNull(response);
		assertEquals(wrappingPaper.getName(), response.name());
		assertEquals(wrappingPaper.getStatus(), response.status());
		verify(wrappingPaperRepository, times(1)).findById(id);
	}

	@Test
	void testGetWrappingPaperByIdNotFound() {
		Long id = 1L;
		when(wrappingPaperRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			wrappingPaperServiceImpl.getWrappingPaperById(id);
		});

		verify(wrappingPaperRepository, times(1)).findById(id);
	}

	@Test
	void testUpdateWrappingPaper() {
		Long id = 1L;
		WrappingPaper wrappingPaper = new WrappingPaper(CreateWrappingPaperRequest.builder()
			.name("Wrap 1")
			.price(5L)
			.status("Available")
			.build());
		UpdateWrappingPaperRequest updateRequest = UpdateWrappingPaperRequest.builder()
			.price(10L)
			.status("Unavailable")
			.build();
		when(wrappingPaperRepository.findById(id)).thenReturn(Optional.of(wrappingPaper));

		wrappingPaperServiceImpl.updateWrappingPaper(id, updateRequest);

		assertEquals(10L, wrappingPaper.getPrice());
		assertEquals("Unavailable", wrappingPaper.getStatus());
		assertNotNull(wrappingPaper.getUpdatedAt());
		verify(wrappingPaperRepository, times(1)).findById(id);
	}

	@Test
	void testUpdateWrappingPaperNotFound() {
		Long id = 1L;
		UpdateWrappingPaperRequest updateRequest = UpdateWrappingPaperRequest.builder()
			.price(10L)
			.status("Unavailable")
			.build();
		when(wrappingPaperRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			wrappingPaperServiceImpl.updateWrappingPaper(id, updateRequest);
		});

		verify(wrappingPaperRepository, times(1)).findById(id);
	}
}

