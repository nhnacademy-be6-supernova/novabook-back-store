package store.novabook.store.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.util.KeyManagerUtil;
import store.novabook.store.common.util.dto.RedisConfigDto;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {
	private final Environment environment;

	private final RestTemplate restTemplate;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		RedisConfigDto redisConfig = KeyManagerUtil.getRedisConfig(environment, restTemplate);

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisConfig.host());
		redisStandaloneConfiguration.setPort(redisConfig.port());
		redisStandaloneConfiguration.setPassword(redisConfig.password());
		redisStandaloneConfiguration.setDatabase(redisConfig.database());
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic("notificationTopic");
	}
}
