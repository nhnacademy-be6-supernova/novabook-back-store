package store.novabook.store.common.security.aop;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.UnauthorizedException;
import store.novabook.store.common.security.dto.CustomUserDetails;

@Component
@Slf4j
public class CurrentMembersArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(CurrentMembers.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		@NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		CurrentMembers currentMembers = parameter.getParameterAnnotation(CurrentMembers.class);
		if (currentMembers == null) {
			log.info("CurrentMembersArgumentResolver resolveArgument currentMembers is null");
			return null;
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("CurrentMembersArgumentResolver resolveArgument authentication: {}", authentication);

		if (authentication == null || "anonymousUser".equals(authentication.getName())) {
			handleUnauthenticatedUser(currentMembers.required());
			log.info("CurrentMembersArgumentResolver resolveArgument anonymousUser");
			return null;
		}
		CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
		log.info("CurrentMembersArgumentResolver resolveArgument principal: {}", principal);
		return principal.getMembersId();
	}

	private void handleUnauthenticatedUser(boolean required) {
		if (required) {
			log.error("CurrentMembersArgumentResolver anonymousUser, 로그인을 하고 @CurrentMembers를 사용해주세요.");
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}
	}
}
