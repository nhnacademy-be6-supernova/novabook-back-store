package store.novabook.store.cart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.service.CartService;
import store.novabook.store.user.member.entity.Users;

@WebMvcTest(CartController.class)
@ContextConfiguration(classes = {CartService.class})
@EnableSpringDataWebSupport
public class CartControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CartService cartService;

	@BeforeEach
	void setup() {
		CartController controller = new CartController(cartService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
	}

	@Test
	void getCartByUserIDTest() throws Exception {
		Users usersMock = mock(Users.class);
		GetCartResponse getCartResponse = new GetCartResponse(usersMock, true);
		when(cartService.getCartByUserId(anyLong())).thenReturn(getCartResponse);

		mockMvc.perform(get("/api/v1/store/carts/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json("{\"users\":{},\"isExposed\":true}"));
	}

	@Test
	void createCartTest() throws Exception {
		Users user = new Users(1L,
			1,
			LocalDateTime.now()
			, LocalDateTime.now());

		CreateCartRequest createCartRequest = new CreateCartRequest(user.getId(), true);

		mockMvc.perform(post("/api/v1/store/carts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCartRequest)))
			.andExpect(status().isCreated());

		verify(cartService, times(1)).createCart(any(CreateCartRequest.class));
	}

}
