package store.novabook.store.member.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberAddress;
import store.novabook.store.member.entity.StreetAddress;
import store.novabook.store.member.repository.MemberAddressRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.repository.StreetAddressRepository;

class MemberAddressServiceImplTest {

	@Mock
	private MemberAddressRepository memberAddressRepository;

	@Mock
	private StreetAddressRepository streetAddressRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private MemberAddressServiceImpl memberAddressService;

	private Member testMember;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testMember = createSampleMember();
	}

	@Test
	void createMemberAddress_ValidInput_ShouldCreateMemberAddress() {
		Long memberId = 1L;
		CreateMemberAddressRequest request = new CreateMemberAddressRequest("12345", "123 Main St", "Home", "Apt 101");
		StreetAddress streetAddress = StreetAddress.builder()
			.zipcode("12345")
			.streetAddress("123 Main St")
			.build();
		MemberAddress savedMemberAddress = MemberAddress.builder()
			.nickname("Home")
			.memberAddressDetail("Apt 101")
			.streetAddress(streetAddress)
			.member(testMember)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
		when(streetAddressRepository.findByZipcodeAndStreetAddress("12345", "123 Main St")).thenReturn(null);
		when(streetAddressRepository.save(any(StreetAddress.class))).thenReturn(streetAddress);
		when(memberAddressRepository.save(any(MemberAddress.class))).thenReturn(savedMemberAddress);

		CreateMemberAddressResponse response = memberAddressService.createMemberAddress(request, memberId);

		assertNotNull(response);
		assertEquals(savedMemberAddress.getId(), response.id());
	}

	@Test
	void createMemberAddress_InvalidMemberId_ShouldThrowNotFoundException() {
		Long memberId = 999L;
		CreateMemberAddressRequest request = new CreateMemberAddressRequest("12345", "123 Main St", "Home", "Apt 101");

		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> memberAddressService.createMemberAddress(request, memberId));
	}

	@Test
	void createMemberAddress_ExceedAddressLimit_ShouldThrowBadRequestException() {
		Long memberId = 1L;
		CreateMemberAddressRequest request = new CreateMemberAddressRequest("12345", "123 Main St", "Home", "Apt 101");

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
		when(memberAddressRepository.countByMember(testMember)).thenReturn(10);

		assertThrows(BadRequestException.class, () -> memberAddressService.createMemberAddress(request, memberId));
	}

	private Member createSampleMember() {
		return Member.builder()
			.loginId("testUser")
			.loginPassword("testPassword")
			.name("John Doe")
			.number("1234567890")
			.email("john.doe@example.com")
			.birth(LocalDateTime.of(1990, 1, 1, 0, 0))
			.latestLoginAt(LocalDateTime.now())
			.authentication(1)
			.role("ROLE_USER")
			.build();
	}

	@Test
	void getMemberAddressAll() {
		Long memberId = 1L;

		List<MemberAddress> memberAddresses = Arrays.asList(
			MemberAddress.builder()
				.nickname("Home")
				.memberAddressDetail("Apt 101")
				.streetAddress(StreetAddress.builder().zipcode("12345").streetAddress("123 Main St").build())
				.member(testMember)
				.build(),
			MemberAddress.builder()
				.nickname("Work")
				.memberAddressDetail("Office 202")
				.streetAddress(StreetAddress.builder().zipcode("54321").streetAddress("456 Broad St").build())
				.member(testMember)
				.build()
		);
		when(memberAddressRepository.findAllByMemberId(memberId)).thenReturn(memberAddresses);

		List<GetMemberAddressResponse> responses = memberAddressService.getMemberAddressAll(memberId);

		assertEquals(2, responses.size());
		assertEquals("Home", responses.get(0).nickname());
		assertEquals("Work", responses.get(1).nickname());
	}

	@Test
	void getMemberAddress() {
		Long addressId = 1L;
		Long streetAddressId = 1L;

		StreetAddress streetAddress = spy(new StreetAddress("12345", "123 Main St"));
		MemberAddress mockAddress = spy(new MemberAddress("Home", "Apt 101", streetAddress, testMember));

		doReturn(streetAddressId).when(streetAddress).getId();
		doReturn(addressId).when(mockAddress).getId();

		when(memberAddressRepository.findById(addressId)).thenReturn(Optional.of(mockAddress));

		GetMemberAddressResponse response = memberAddressService.getMemberAddress(addressId);

		assertEquals(addressId, response.id());
		assertEquals(streetAddressId, response.streetAddressId());
		assertEquals(testMember.getId(), response.memberId());
		assertEquals("12345", response.zipcode());
		assertEquals("123 Main St", response.streetAddress());
		assertEquals("Apt 101", response.memberAddressDetail());
		assertEquals("Home", response.nickname());
	}

	@Test
	void getMemberAddress_NotFound_ShouldThrowNotFoundException() {
		Long addressId = 1L;
		when(memberAddressRepository.findById(addressId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberAddressService.getMemberAddress(addressId);
		});

		assertEquals(ErrorCode.MEMBER_ADDRESS_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void updateMemberAddress() {
		Long addressId = 1L;
		UpdateMemberAddressRequest updateRequest = new UpdateMemberAddressRequest("Work", "54321", "456 Broad St",
			"Suite 303", 1L);

		StreetAddress oldStreetAddress = StreetAddress.builder()
			.zipcode("12345")
			.streetAddress("123 Main St")
			.build();

		MemberAddress mockAddress = MemberAddress.builder()
			.nickname("Home")
			.memberAddressDetail("Apt 101")
			.streetAddress(oldStreetAddress)
			.member(testMember)
			.build();

		StreetAddress newStreetAddress = StreetAddress.builder()
			.zipcode(updateRequest.zipcode())
			.streetAddress(updateRequest.streetAddress())
			.build();

		when(memberAddressRepository.findById(addressId)).thenReturn(Optional.of(mockAddress));
		when(streetAddressRepository.findByZipcodeAndStreetAddress(updateRequest.zipcode(),
			updateRequest.streetAddress())).thenReturn(null);
		when(streetAddressRepository.save(any(StreetAddress.class))).thenReturn(newStreetAddress);

		memberAddressService.updateMemberAddress(addressId, updateRequest);

		assertEquals("54321", mockAddress.getStreetAddress().getZipcode());
		assertEquals("456 Broad St", mockAddress.getStreetAddress().getStreetAddress());
		assertEquals("Work", mockAddress.getNickname());
		assertEquals("Suite 303", mockAddress.getMemberAddressDetail());
	}

	@Test
	void updateMemberAddress_NotFound_ShouldThrowNotFoundException() {
		Long addressId = 1L;
		UpdateMemberAddressRequest updateRequest = new UpdateMemberAddressRequest("54321", "456 Broad St", "Work",
			"Suite 303", 1L);

		when(memberAddressRepository.findById(addressId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberAddressService.updateMemberAddress(addressId, updateRequest);
		});

		assertEquals(ErrorCode.MEMBER_ADDRESS_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void deleteMemberAddress() {
		Long addressId = 1L;
		MemberAddress mockAddress = MemberAddress.builder()
			.nickname("Home")
			.memberAddressDetail("Apt 101")
			.streetAddress(StreetAddress.builder().zipcode("12345").streetAddress("123 Main St").build())
			.member(testMember)
			.build();

		when(memberAddressRepository.findById(addressId)).thenReturn(Optional.of(mockAddress));

		memberAddressService.deleteMemberAddress(addressId);

		verify(memberAddressRepository, times(1)).delete(mockAddress);
	}

	@Test
	void isExceed() {
		Long memberId = 1L;
		int currentAddressCount = 10;

		when(memberAddressRepository.countByMemberId(memberId)).thenReturn(currentAddressCount);

		ExceedResponse response = memberAddressService.isExceed(memberId);

		assertTrue(response.isExceed());
	}

}