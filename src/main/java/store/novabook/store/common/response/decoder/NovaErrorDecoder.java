package store.novabook.store.common.response.decoder;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import store.novabook.store.common.exception.FeignClientException;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.common.response.ErrorResponse;

public class NovaErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder defaultErrorDecoder = new Default();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		try (InputStream bodyIs = response.body().asInputStream()) {
			ApiResponse<ErrorResponse> apiResponse = objectMapper.readValue(bodyIs,
				objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, ErrorResponse.class));
			ErrorResponse errorResponse = apiResponse.getBody();
			return new FeignClientException(response.status(), errorResponse.errorCode());
		} catch (IOException e) {
			return defaultErrorDecoder.decode(methodKey, response);
		}
	}
}
