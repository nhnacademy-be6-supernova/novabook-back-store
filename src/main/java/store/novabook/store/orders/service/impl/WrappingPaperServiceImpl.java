package store.novabook.store.orders.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.orders.service.WrappingPaperService;

@Service
@RequiredArgsConstructor
@Transactional
public class WrappingPaperServiceImpl implements WrappingPaperService {
	private final WrappingPaperRepository wrappingPaperRepository;

	@Override
	public CreateResponse createWrappingPaper(CreateWrappingPaperRequest request) {
		WrappingPaper wrappingPaper = new WrappingPaper(request);
		wrappingPaperRepository.save(wrappingPaper);
		return new CreateResponse(wrappingPaper.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetWrappingPaperResponse> getWrappingPaperAll(Pageable pageable) {
		List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();
		List<GetWrappingPaperResponse> wrappingPaperResponses = new ArrayList<>();
		wrappingPapers.forEach(
			wrappingPaper -> wrappingPaperResponses.add(GetWrappingPaperResponse.from(wrappingPaper)));
		return new PageImpl<>(wrappingPaperResponses, pageable, wrappingPapers.size());
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetWrappingPaperResponse> getWrappingPaperAllList() {

		List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();
		List<GetWrappingPaperResponse> wrappingPaperResponses = new ArrayList<>();
		wrappingPapers.forEach(
			wrappingPaper -> wrappingPaperResponses.add(GetWrappingPaperResponse.from(wrappingPaper)));
		return wrappingPaperResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public GetWrappingPaperResponse getWrappingPaperById(Long id) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		return GetWrappingPaperResponse.from(wrappingPaper);
	}

	@Override
	public void updateWrappingPaper(Long id, UpdateWrappingPaperRequest request) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		wrappingPaper.updated(request);
	}
}
