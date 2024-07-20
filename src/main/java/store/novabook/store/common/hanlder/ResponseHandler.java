package store.novabook.store.common.hanlder;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.common.response.ErrorResponse;
import store.novabook.store.common.response.PageResponse;
import store.novabook.store.common.response.ValidErrorResponse;

@Slf4j
@RestControllerAdvice(basePackages = {"store.novabook.store"})
public class ResponseHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(@Nullable MethodParameter returnType,
		@Nullable Class<? extends HttpMessageConverter<?>> converterType) {
		return converterType != null && MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, @Nullable MethodParameter returnType,
		@Nullable MediaType selectedContentType,
		@Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType, @Nullable ServerHttpRequest request,
		@Nullable ServerHttpResponse response) {

		if (body instanceof ValidErrorResponse validErrorResponse) {
			return ApiResponse.error(ErrorResponse.from(validErrorResponse));
		}

		if (body instanceof ErrorResponse errorResponse) {
			return ApiResponse.error(errorResponse);
		}

		if (body instanceof Page<?> page) {
			return PageResponse.success(page.getNumber(), page.getSize(), page.getTotalElements(), page.getContent());
		} else {
			return ApiResponse.success(body);
		}
	}
}