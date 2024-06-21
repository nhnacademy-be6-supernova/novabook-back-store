package store.novabook.store.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.GetWrappingPaperResponse;
import store.novabook.store.orders.dto.UpdateWrappingPaperRequest;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class WrappingPaperService {
	private final WrappingPaperRepository wrappingPaperRepository;

	//생성
	public CreateResponse createWrappingPaper(CreateWrappingPaperRequest request) {
		WrappingPaper wrappingPaper = new WrappingPaper(request);
		wrappingPaperRepository.save(wrappingPaper);
		return new CreateResponse(wrappingPaper.getId());
	}

	//전체 조회
	@Transactional(readOnly = true)
	public Page<GetWrappingPaperResponse> getWrappingPaperAll() {
		List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();
		List<GetWrappingPaperResponse> wrappingPaperResponses = new ArrayList<>();
		wrappingPapers.forEach(
			wrappingPaper -> wrappingPaperResponses.add(GetWrappingPaperResponse.from(wrappingPaper)));
		return new PageImpl<>(wrappingPaperResponses);
	}

	//단건 조회
	@Transactional(readOnly = true)
	public GetWrappingPaperResponse getWrappingPaperById(Long id) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(WrappingPaper.class, id));
		return GetWrappingPaperResponse.from(wrappingPaper);
	}

	//수정
	public void updateWrappingPaper(Long id, UpdateWrappingPaperRequest request) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(WrappingPaper.class, id));
		wrappingPaper.updated(request);
	}
}
