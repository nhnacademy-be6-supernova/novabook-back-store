package store.novabook.store.book.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberStatus;

class CreateLikesResponseTest {

	@Test
	void testFromLikes() {
		// Arrange
		Book book = Book.builder()
			.bookStatus(BookStatus.builder().id(1L).build()) // BookStatus를 적절히 초기화하세요
			.isbn("978-3-16-148410-0")
			.title("Test Book")
			.description("A test book description")
			.descriptionDetail("Detailed description of the test book")
			.author("Author Name")
			.publisher("Publisher Name")
			.publicationDate(LocalDateTime.now())
			.inventory(10)
			.price(1000L)
			.discountPrice(800L)
			.isPackaged(false)
			.build();

		Member member = Member.builder()
			.loginId("testUser")
			.loginPassword("password123")
			.name("Test User")
			.number("123456789")
			.email("test@example.com")
			.birth(LocalDateTime.now().minusYears(30))
			.latestLoginAt(LocalDateTime.now())
			.authentication(1)
			.memberStatus(MemberStatus.builder().build()) // MemberStatus를 적절히 초기화하세요
			.role("USER")
			.build();

		Likes likes = Likes.builder()
			.book(book)
			.member(member)
			.build();

		// Act
		CreateLikesResponse response = CreateLikesResponse.from(likes);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(likes.getId());
	}

	@Test
	void testBuilder() {
		// Act
		CreateLikesResponse response = CreateLikesResponse.builder()
			.id(1L)
			.build();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(1L);
	}
}
