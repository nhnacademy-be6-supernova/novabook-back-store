package store.novabook.store.book.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;
import store.novabook.store.book.service.BookService;


@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private CreateBookRequest createBookRequest;
	private CreateBookResponse createBookResponse;
	private UpdateBookRequest updateBookRequest;
	private GetBookResponse getBookResponse;
	private GetBookToMainResponseMap getBookToMainResponseMap;
	private Page<GetBookAllResponse> bookAllResponsePage;

	@BeforeEach
	void setUp() {
		createBookRequest = new CreateBookRequest(
			1L,  // bookStatusId
			"1234567890123",  // isbn
			"Example Book Title",  // title
			"Example Book Description",  // description
			"Detailed Description of Example Book",  // descriptionDetail
			"Author Name",  // author
			"Publisher Name",  // publisher
			LocalDateTime.now(),  // publicationDate
			100,  // inventory
			20000L,  // price
			15000L,  // discountPrice
			false,  // isPackaged
			"http://example.com/image.jpg",  // image
			Arrays.asList(1L, 2L, 3L),  // tags
			Arrays.asList(4L, 5L, 6L)  // categories
		);
		createBookResponse = new CreateBookResponse(1L);
		updateBookRequest = UpdateBookRequest.builder()
			.id(1L)  // 책 ID
			.bookStatusId(2L)  // 책 상태 ID
			.inventory(50)  // 재고 수량
			.price(20000L)  // 가격
			.discountPrice(15000L)  // 할인가격
			.isPackaged(false)  // 포장 여부
			.build();
		getBookResponse = GetBookResponse.builder()
			.id(1L)
			.isbn("1234567890")
			.title("Example Book Title")
			.description("This is an example description of the book.")
			.descriptionDetail("This is a detailed description of the example book.")
			.author("John Doe")
			.publisher("Example Publisher")
			.publicationDate(LocalDateTime.of(2023, 7, 1, 0, 0))
			.inventory(100)
			.price(20000L)
			.discountPrice(15000L)
			.isPackaged(false)
			.tags(Arrays.asList("Tag1", "Tag2"))
			.categories(Arrays.asList("Category1", "Category2"))
			.likes(50)
			.score(4.5)
			.image("http://example.com/image.jpg")
			.bookStatusId(1L)
			.build();

		Map<String, List<GetBookToMainResponse>> exampleData = new HashMap<>();

		exampleData.put("Bestsellers", Arrays.asList(
			GetBookToMainResponse.builder()
				.id(1L)
				.title("Bestseller Book 1")
				.price(10000)
				.discountPrice(100000)
				.image("http://example.com/bestseller1.jpg")
				.build(),
			GetBookToMainResponse.builder()
				.id(2L)
				.title("Bestseller Book 2")
				.price(10000)
				.discountPrice(100000)
				.image("http://example.com/bestseller2.jpg")
				.build()
		));

		exampleData.put("New Arrivals", Arrays.asList(
			GetBookToMainResponse.builder()
				.id(3L)
				.title("New Arrival Book 1")
				.price(10000)
				.discountPrice(100000)
				.image("http://example.com/newarrival1.jpg")
				.build(),
			GetBookToMainResponse.builder()
				.id(4L)
				.title("New Arrival Book 2")
				.price(10000)
				.discountPrice(100000)
				.image("http://example.com/newarrival2.jpg")
				.build()
		));
		GetBookAllResponse exampleBookResponse = new GetBookAllResponse(
			1L,
			1L,
			"Example Book",
			"1",
			15000,
			12000L,
			12000L,
			false
		);
		getBookToMainResponseMap = new GetBookToMainResponseMap(exampleData);
		bookAllResponsePage = new PageImpl<>(Collections.singletonList(exampleBookResponse));
	}

	@Test
	void getBook() throws Exception {
		when(bookService.getBook(1L)).thenReturn(getBookResponse);

		mockMvc.perform(get("/api/v1/store/books/{id}", 1L).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(getBookResponse.id()));
	}

	@Test
	void getBookToMainPage() throws Exception {
		when(bookService.getBookToMainPage()).thenReturn(getBookToMainResponseMap);

		mockMvc.perform(get("/api/v1/store/books/main").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.mainBookData.Bestsellers").isArray());
	}

	@Test
	void getBookAll() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		when(bookService.getBookAll(pageable)).thenReturn(bookAllResponsePage);

		mockMvc.perform(get("/api/v1/store/books")
				.with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray());
	}

	@Test
	void createBook() throws Exception {
		when(bookService.create(any(CreateBookRequest.class))).thenReturn(createBookResponse);

		mockMvc.perform(post("/api/v1/store/books")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createBookRequest)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(createBookResponse.id()));
	}

	@Test
	void updateBook() throws Exception {
		mockMvc.perform(put("/api/v1/store/books")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBookRequest)))
			.andExpect(status().isOk());
	}

	@Test
	void deleteBook() throws Exception {
		mockMvc.perform(delete("/api/v1/store/books/{id}", 1L)
				.with(csrf()))
			.andExpect(status().isOk());
	}
}