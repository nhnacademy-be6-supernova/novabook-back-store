package store.novabook.store.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorStatus;
import store.novabook.store.common.exception.ValidErrorResponse;

@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		if (body instanceof ErrorStatus errorStatus) {
			return new ErrorResponse<>(errorStatus.getMessage(), body);
		}

		if (body instanceof ProblemDetail problemDetail) {
			return new ErrorResponse<>(problemDetail.getDetail(), body);
		}

		if (body instanceof ValidErrorResponse validErrorResponse) {
			return new ErrorResponse<>("ServerError", validErrorResponse.result());
		}

		if (body instanceof Page<?> page) {
			Map<String, Object> pageBody = new HashMap<>();
			pageBody.put("pageNum", page.getNumber());
			pageBody.put("pageSize", page.getSize());
			pageBody.put("totalCount", page.getTotalElements());
			pageBody.put("data", page.getContent());
			return ApiResponse.success(pageBody);
		} else {
			return ApiResponse.success(body);
		}
	}
}