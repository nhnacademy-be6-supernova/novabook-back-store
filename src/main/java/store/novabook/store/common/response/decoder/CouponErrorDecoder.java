package store.novabook.store.common.response.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;
import store.novabook.store.common.exception.FeignClientException;
import store.novabook.store.common.response.CouponErrorBody;
import store.novabook.store.common.response.ErrorResponse;

@Component
public class CouponErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		ErrorResponse<CouponErrorBody> errorResponse = null;

		if (response.body() != null) {
			try (InputStream bodyIs = response.body().asInputStream()) {
				String bodyString = new String(bodyIs.readAllBytes(), StandardCharsets.UTF_8);
				JsonNode rootNode = objectMapper.readTree(bodyString);
				JsonNode headerNode = rootNode.path("header");
				JsonNode bodyNode = rootNode.path("body");

				Map<String, Object> headerMap = objectMapper.convertValue(headerNode, Map.class);
				CouponErrorBody errorBody = objectMapper.treeToValue(bodyNode, CouponErrorBody.class);
				errorResponse = new ErrorResponse<>(headerMap, errorBody);
			} catch (IOException e) {
				errorResponse = new ErrorResponse<>(
					Map.of("result", "FAIL", "message", "An error occurred while decoding the error response"),
					new CouponErrorBody("UNKNOWN_ERROR", "An error occurred while decoding the error response", null));
			}
		}

		if (errorResponse == null) {
			errorResponse = new ErrorResponse<>(Map.of("result", "FAIL", "message", "Unknown error occurred"),
				new CouponErrorBody("UNKNOWN_ERROR", "Unknown error occurred", null));
		}

		return new FeignClientException(response.status(), errorResponse);
	}
}