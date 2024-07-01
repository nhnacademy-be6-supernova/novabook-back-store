package store.novabook.store.orders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;

public interface WrappingPaperService {
	CreateResponse createWrappingPaper(CreateWrappingPaperRequest request);

	@Transactional(readOnly = true)
	Page<GetWrappingPaperResponse> getWrappingPaperAll(Pageable pageable);

	@Transactional(readOnly = true)
	List<GetWrappingPaperResponse> getWrappingPaperAllList();

	@Transactional(readOnly = true)
	GetWrappingPaperResponse getWrappingPaperById(Long id);

	void updateWrappingPaper(Long id, UpdateWrappingPaperRequest request);
}
