package store.novabook.store.cart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.service.CartBookService;

@WebMvcTest(CartController.class)
@ContextConfiguration(classes = {CartBookService.class})
@EnableSpringDataWebSupport
class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartBookService cartBookService;

	@Autowired
	private ObjectMapper objectMapper;

	CartBookIdDTO cartBookIdDTO;

	CartBookDTO cartBookDTO;

	@BeforeEach
	void setup() {
		CartController controller = new CartController(cartBookService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
		Map<Long, Integer> cartBookIdDTOMap = new HashMap();
		cartBookIdDTO = new CartBookIdDTO(cartBookIdDTOMap);
		cartBookDTO = new CartBookDTO(1L, "Test Book", "image.jpg", 1000L, 900L, 1, false, 1L);

	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void getCartBookAllByMemberId_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/api/v1/store/carts/member")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void getCartBookAllByGuest_shouldReturnOk() throws Exception {
		mockMvc.perform(post("/api/v1/store/carts/guest")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartBookIdDTO)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void addCartBook_shouldReturnOk() throws Exception {

		when(cartBookService.createCartBook(any(Long.class), any(CartBookDTO.class)))
			.thenReturn(new CreateCartBookResponse(1L));

		mockMvc.perform(post("/api/v1/store/carts/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartBookDTO)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void addCartBooks_shouldReturnOk() throws Exception {
		CartBookListDTO request = new CartBookListDTO(List.of(cartBookDTO));

		when(cartBookService.createCartBooks(any(Long.class), any(CartBookListDTO.class)))
			.thenReturn(new CreateCartBookListResponse(List.of(1L)));

		mockMvc.perform(post("/api/v1/store/carts/adds")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void updateCartBook_shouldReturnOk() throws Exception {
		UpdateCartBookQuantityRequest request = new UpdateCartBookQuantityRequest(1L, 1);

		doNothing().when(cartBookService)
			.updateCartBookQuantity(any(Long.class), any(UpdateCartBookQuantityRequest.class));

		mockMvc.perform(put("/api/v1/store/carts/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void deleteCartBook_shouldReturnOk() throws Exception {
		doNothing().when(cartBookService).deleteCartBook(any(Long.class), eq(1L));

		mockMvc.perform(delete("/api/v1/store/carts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN", "MEMBERS"})
	void deleteCartBooks_shouldReturnOk() throws Exception {
		DeleteCartBookListRequest request = new DeleteCartBookListRequest(List.of(1L));

		doNothing().when(cartBookService).deleteCartBooks(any(Long.class), any(DeleteCartBookListRequest.class));

		mockMvc.perform(delete("/api/v1/store/carts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
