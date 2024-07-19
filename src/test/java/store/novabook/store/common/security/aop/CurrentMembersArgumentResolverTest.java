package store.novabook.store.common.security.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.UnauthorizedException;
import store.novabook.store.common.security.dto.CustomUserDetails;

class CurrentMembersArgumentResolverTest {

	private CurrentMembersArgumentResolver resolver;

	@Mock
	private MethodParameter methodParameter;

	@Mock
	private ModelAndViewContainer mavContainer;

	@Mock
	private NativeWebRequest webRequest;

	@Mock
	private WebDataBinderFactory binderFactory;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private CustomUserDetails customUserDetails;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		resolver = new CurrentMembersArgumentResolver();
	}

	@Test
	void supportsParameter_ShouldReturnTrue_WhenParameterIsAnnotated() {
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(mock(CurrentMembers.class));
		assertTrue(resolver.supportsParameter(methodParameter));
	}

	@Test
	void supportsParameter_ShouldReturnFalse_WhenParameterIsNotAnnotated() {
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(null);
		assertFalse(resolver.supportsParameter(methodParameter));
	}

	@Test
	void resolveArgument_ShouldReturnMembersId_WhenUserIsAuthenticated() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getPrincipal()).thenReturn(customUserDetails);
		when(customUserDetails.getMembersId()).thenReturn(1L);

		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(mock(CurrentMembers.class));

		Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
		assertEquals(1L, result);
	}

	@Test
	void resolveArgument_ShouldReturnNull_WhenAuthenticationIsNull() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(securityContext);

		CurrentMembers annotation = mock(CurrentMembers.class);
		when(annotation.required()).thenReturn(false);
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(annotation);

		Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
		assertNull(result);
	}

	@Test
	void resolveArgument_ShouldThrowUnauthorizedException_WhenAuthenticationIsNullAndRequired() {
		when(securityContext.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(securityContext);

		CurrentMembers annotation = mock(CurrentMembers.class);
		when(annotation.required()).thenReturn(true);
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(annotation);

		UnauthorizedException exception = assertThrows(UnauthorizedException.class,
			() -> resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory));
		assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
	}

	@Test
	void resolveArgument_ShouldReturnNull_WhenPrincipalIsNotCustomUserDetails() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getPrincipal()).thenReturn("notCustomUserDetails");

		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(mock(CurrentMembers.class));

		Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
		assertNull(result);
	}

	@Test
	void resolveArgument_ShouldHandleUnauthenticatedUser_WhenAnonymousUserAndNotRequired() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getName()).thenReturn("anonymousUser");

		CurrentMembers annotation = mock(CurrentMembers.class);
		when(annotation.required()).thenReturn(false);
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(annotation);

		Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
		assertNull(result);
	}

	@Test
	void resolveArgument_ShouldThrowUnauthorizedException_WhenAnonymousUserAndRequired() {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getName()).thenReturn("anonymousUser");

		CurrentMembers annotation = mock(CurrentMembers.class);
		when(annotation.required()).thenReturn(true);
		when(methodParameter.getParameterAnnotation(CurrentMembers.class)).thenReturn(annotation);

		UnauthorizedException exception = assertThrows(UnauthorizedException.class,
			() -> resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory));
		assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
	}
}
