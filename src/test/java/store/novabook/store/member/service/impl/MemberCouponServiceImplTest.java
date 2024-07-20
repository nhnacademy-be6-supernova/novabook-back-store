package store.novabook.store.member.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.adatper.CouponAdapter;
import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetCouponResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.messaging.CouponSender;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.common.response.PageResponse;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;
import store.novabook.store.member.dto.request.DownloadCouponRequest;
import store.novabook.store.member.dto.request.RegisterCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberCoupon;
import store.novabook.store.member.entity.MemberStatus;
import store.novabook.store.member.repository.MemberCouponRepository;
import store.novabook.store.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberCouponServiceImplTest {

	@Mock
	private MemberCouponRepository memberCouponRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private CouponAdapter couponAdapter;

	@Mock
	private CouponSender couponSender;

	@InjectMocks
	private MemberCouponServiceImpl memberCouponService;

	private Member member;
	private MemberCoupon memberCoupon;
	private CreateMemberCouponRequest createMemberCouponRequest;
	private DownloadCouponRequest downloadCouponRequest;
	private RegisterCouponRequest registerCouponRequest;
	private RegisterCouponMessage registerCouponMessage;

	@BeforeEach
	void setUp() {
		member = Member.builder()
			.loginId("testLoginId")
			.loginPassword("encodedPassword")
			.name("OldName")
			.number("OldNumber")
			.email("testEmail")
			.birth(LocalDateTime.of(1990, 1, 1, 0, 0))
			.latestLoginAt(LocalDateTime.now())
			.memberStatus(new MemberStatus("활동"))
			.role("ROLE_MEMBERS")
			.build();

		memberCoupon = MemberCoupon.builder().member(member).couponId(1L).build();

		createMemberCouponRequest = new CreateMemberCouponRequest(1L);
		downloadCouponRequest = new DownloadCouponRequest(1L);
		registerCouponRequest = new RegisterCouponRequest(1L);
		registerCouponMessage = new RegisterCouponMessage(1L, 1L);
	}

	@Test
	void testCreateMemberCoupon_success() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		CreateCouponResponse couponResponse = CreateCouponResponse.builder().id(1L).build();
		ApiResponse<CreateCouponResponse> apiResponse = ApiResponse.success(couponResponse);

		when(couponAdapter.createCoupon(any())).thenReturn(apiResponse);
		when(memberCouponRepository.save(any())).thenReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.createMemberCoupon(1L, createMemberCouponRequest);

		assertThat(response).isNotNull();
		assertThat(response.couponId()).isEqualTo(1L);
	}

	@Test
	void testCreateMemberCoupon_memberNotFound() {
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.createMemberCoupon(1L, createMemberCouponRequest));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	void testDownloadCoupon_success() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		CreateCouponResponse couponResponse = CreateCouponResponse.builder().id(1L).build();
		ApiResponse<CreateCouponResponse> apiResponse = ApiResponse.success(couponResponse);

		when(couponAdapter.createCoupon(any())).thenReturn(apiResponse);
		when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.downloadCoupon(1L, downloadCouponRequest);

		assertThat(response).isNotNull();
		assertThat(response.couponId()).isEqualTo(1L);
	}

	@Test
	void testDownloadCoupon_memberNotFound() {
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.downloadCoupon(1L, downloadCouponRequest));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	void testRegisterMemberCoupon_success() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.registerMemberCoupon(1L, registerCouponRequest);

		assertThat(response).isNotNull();
		assertThat(response.couponId()).isEqualTo(1L);
	}

	@Test
	void testRegisterMemberCoupon_memberNotFound() {
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.registerMemberCoupon(1L, registerCouponRequest));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	void testCreateMemberCouponByMessage_success() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

		CreateMemberCouponResponse response = memberCouponService.createMemberCouponByMessage(registerCouponMessage);

		assertThat(response).isNotNull();
		assertThat(response.couponId()).isEqualTo(1L);
	}

	@Test
	void testCreateMemberCouponByMessage_memberNotFound() {
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberCouponService.createMemberCouponByMessage(registerCouponMessage));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
	}

	@Test
	void testGetValidAllByMemberId() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(List.of(memberCoupon));
		GetCouponAllResponse couponAllResponse = GetCouponAllResponse.builder().build();
		ApiResponse<GetCouponAllResponse> apiResponse = ApiResponse.success(couponAllResponse);
		when(couponAdapter.getCouponValidAll(anyList())).thenReturn(apiResponse);

		GetCouponAllResponse response = memberCouponService.getValidAllByMemberId(1L);

		assertThat(response).isNotNull();
	}

	@Test
	void testGetValidAllByMemberId_noCoupons() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(Collections.emptyList());

		GetCouponAllResponse response = memberCouponService.getValidAllByMemberId(1L);

		assertThat(response).isNotNull();
	}

	@Test
	void testGetMemberCouponHistory() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(List.of(memberCoupon));
		Page<GetCouponResponse> pageResponse = new PageImpl<>(List.of(GetCouponResponse.builder().build()));
		PageResponse<GetCouponResponse> apiPageResponse = new PageResponse<>(0, 10, 1, pageResponse.getContent());
		when(couponAdapter.getCouponAll(anyList(), any(Pageable.class))).thenReturn(apiPageResponse);

		Page<GetCouponHistoryResponse> response = memberCouponService.getMemberCouponHistory(1L, Pageable.unpaged());

		assertThat(response).isNotNull();
	}

	@Test
	void testGetMemberCouponHistory_noCoupons() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(Collections.emptyList());

		Page<GetCouponHistoryResponse> response = memberCouponService.getMemberCouponHistory(1L, Pageable.unpaged());

		assertThat(response).isNotNull();
		assertThat(response.getContent()).isEmpty();
	}

	@Test
	void testGetMemberUsedCouponHistory() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(List.of(memberCoupon));
		Page<GetCouponResponse> pageResponse = new PageImpl<>(List.of(GetCouponResponse.builder().build()));
		PageResponse<GetCouponResponse> apiPageResponse = new PageResponse<>(0, 10, 1, pageResponse.getContent());
		when(couponAdapter.getCouponByStatus(anyList(), eq(CouponStatus.USED), any(Pageable.class))).thenReturn(
			apiPageResponse);

		Page<GetUsedCouponHistoryResponse> response = memberCouponService.getMemberUsedCouponHistory(1L,
			Pageable.unpaged());

		assertThat(response).isNotNull();
	}

	@Test
	void testGetMemberUsedCouponHistory_noCoupons() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(Collections.emptyList());

		Page<GetUsedCouponHistoryResponse> response = memberCouponService.getMemberUsedCouponHistory(1L,
			Pageable.unpaged());

		assertThat(response).isNotNull();
		assertThat(response.getContent()).isEmpty();
	}

	@Test
	void testGetMemberCoupon() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(List.of(memberCoupon));

		GetCouponIdsResponse response = memberCouponService.getMemberCoupon(1L);

		assertThat(response).isNotNull();
		assertThat(response.couponIds()).contains(1L);
	}

	@Test
	void testDownloadLimitedCoupon() {
		when(memberCouponRepository.findByMemberId(1L)).thenReturn(List.of(memberCoupon));
		DownloadCouponMessageRequest request = new DownloadCouponMessageRequest("123", CouponType.LIMITED, 3L);

		memberCouponService.downloadLimitedCoupon("token", "refresh", 1L, request);

		verify(couponSender).sendToNotifyQueue(eq("token"), eq("refresh"), argThat(
			argument -> argument.memberId().equals(1L) && argument.couponIdList().contains(1L)));
	}

}