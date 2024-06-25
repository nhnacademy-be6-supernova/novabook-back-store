// package store.novabook.store.user.member.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDateTime;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
// import store.novabook.store.user.member.dto.CreateMemberAddressResponse;
// import store.novabook.store.user.member.dto.UpdateMemberAddressRequest;
// import store.novabook.store.user.member.entity.Member;
// import store.novabook.store.user.member.entity.MemberAddress;
// import store.novabook.store.user.member.entity.StreetAddress;
// import store.novabook.store.user.member.repository.MemberAddressRepository;
// import store.novabook.store.user.member.repository.MemberRepository;
// import store.novabook.store.user.member.repository.StreetAddressRepository;
//
// @ExtendWith(MockitoExtension.class)
// class MemberAddressServiceTest {
//
// 	@Mock
// 	private MemberAddressRepository memberAddressRepository;
//
// 	@Mock
// 	private StreetAddressRepository streetAddressRepository;
//
// 	@Mock
// 	private MemberRepository memberRepository;
//
// 	@InjectMocks
// 	private MemberAddressService memberAddressService;
//
// 	private Member member;
// 	private StreetAddress streetAddress;
// 	private MemberAddress memberAddress;
// 	private CreateMemberAddressRequest createMemberAddressRequest;
// 	private UpdateMemberAddressRequest updateMemberAddressRequest;
//
// 	@BeforeEach
// 	void setUp() {
// 		member = Member.builder()
// 			.id(1L)
// 			.loginId("testLoginId")
// 			.loginPassword("testLoginPassword")
// 			.name("testName")
// 			.number("010-1234-5678")
// 			.email("test@example.com")
// 			.birth(LocalDateTime.of(2001, 2, 14, 0, 0))
// 			.build();
//
// 		streetAddress = StreetAddress.builder()
// 			.id(1L)
// 			.zipcode("12345")
// 			.streetAddress("test house")
// 			.build();
//
// 		memberAddress = MemberAddress.builder()
// 			.id(1L)
// 			.nickname("test")
// 			.memberAddressDetail("test detail")
// 			.streetAddress(streetAddress)
// 			.build();
//
// 		createMemberAddressRequest = new CreateMemberAddressRequest("11111", 1L, "testStreetAddress", "testNickname",
// 			"testDetail");
// 		updateMemberAddressRequest = new UpdateMemberAddressRequest("newNickname", "12345", "NewStreetAddress",
// 			"newDetail", 1L);
//
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 생성 - 성공")
// 	void createMemberAddress() {
// 		when(memberRepository.findById(createMemberAddressRequest.memberId())).thenReturn(Optional.of(member));
// 		when(streetAddressRepository.findByZipcodeAndStreetAddress(createMemberAddressRequest.zipcode(),
// 			createMemberAddressRequest.streetAddress())).thenReturn(streetAddress);
// 		when(memberAddressRepository.save(any(MemberAddress.class))).thenReturn(memberAddress);
//
// 		CreateMemberAddressResponse response = memberAddressService.createMemberAddress(createMemberAddressRequest);
//
// 		assertNotNull(response);
// 		assertEquals(memberAddress.getId(), response.id());
//
// 		verify(memberRepository, times(1)).findById(createMemberAddressRequest.memberId());
// 		verify(streetAddressRepository, times(1)).findByZipcodeAndStreetAddress(createMemberAddressRequest.zipcode(),
// 			createMemberAddressRequest.streetAddress());
// 		verify(memberAddressRepository, times(1)).save(any(MemberAddress.class));
// 	}
//
// 	/*@Test
// 	@DisplayName("회원 주소 생성 - 새로운 주소 생성")
// 	void createMemberAddressNewAddress() {
// 		when(memberRepository.findById(createMemberAddressRequest.memberId())).thenReturn(Optional.of(member));
//
// 	}*/
//
// 	@Test
// 	@DisplayName("회원 주소 생성 - 실패 - 회원 없음")
// 	void createMemberAddressFailNoMember() {
//
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 생성 - 실패 - 10개 이상 등록할 경우")
// 	void createMemberAddressFailCountOver() {
//
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 전체 조회 - 성공")
// 	void getMemberAddressAllSuccess() {
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 조회 - 성공")
// 	void getMemberAddressSuccess() {
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 조회 - 실패 - 회원 주소 없음")
// 	void getMemberAddressFailNoMemberAddress() {
//
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 수정 - 성공")
// 	void updateMemberAddressSuccess() {
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 수정 - 실패 - 회원 주소 없음")
// 	void updateMemberAddressFailNoMemberAddress() {
//
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 삭제 - 성공")
// 	void deleteMemberAddressSuccess() {
// 	}
//
// 	@Test
// 	@DisplayName("회원 주소 삭제 - 실패 - 회원 주소 없음")
// 	void deleteMemberAddressFailNoMember() {
//
// 	}
// }