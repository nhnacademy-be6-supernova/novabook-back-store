package store.novabook.store.cart.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookResponse;
import store.novabook.store.cart.service.impl.CartBookServiceImpl;

@WebMvcTest(CartBookControllerTest.class)
@ContextConfiguration(classes = {CartBookServiceImpl.class})
@EnableSpringDataWebSupport
public class CartBookControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	private CartBookServiceImpl cartBookServiceImpl;

	@BeforeEach
	void setup() {
		CartBookController controller = new CartBookController(cartBookServiceImpl);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
	}

	@Test
	void getAllCartBookTest() throws Exception {
		GetCartBookResponse mock = mock(GetCartBookResponse.class);

		List<GetCartBookResponse> getCartBookResponseList = Collections.singletonList(mock);
		Page<GetCartBookResponse> page = new PageImpl<>(getCartBookResponseList, PageRequest.of(0, 10),
			getCartBookResponseList.size());

		when(cartBookServiceImpl.getCartBookListByCartId(anyLong(), any())).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/cart/books/1"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void createCartBookTest() throws Exception {
		CreateCartBookRequest createCartBookRequest = new CreateCartBookRequest(1L, 1L, 1);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(createCartBookRequest);

		mockMvc.perform(post("/api/v1/store/cart/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isCreated());

		verify(cartBookServiceImpl, times(1)).createCartBook(any(CreateCartBookRequest.class));
	}

	@Test
	void deleteCartBookTest() throws Exception {
		DeleteCartBookRequest deleteCartBookRequest = new DeleteCartBookRequest(1L, 1L);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(deleteCartBookRequest);

		mockMvc.perform(delete("/api/v1/store/cart/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());

		verify(cartBookServiceImpl, times(1)).deleteCartBookAndBook(any(DeleteCartBookRequest.class));
	}
}
