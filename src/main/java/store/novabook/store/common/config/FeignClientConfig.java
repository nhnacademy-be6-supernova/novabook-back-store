package store.novabook.store.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;
import store.novabook.store.common.response.decoder.NovaErrorDecoder;

@Configuration
public class FeignClientConfig {

	@Bean
	public ErrorDecoder errorDecoder() {
		return new NovaErrorDecoder();
	}
}