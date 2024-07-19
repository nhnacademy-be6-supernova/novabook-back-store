package store.novabook.store.common.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static store.novabook.store.common.exception.ErrorCode.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import store.novabook.store.common.exception.KeyManagerException;
import store.novabook.store.common.util.dto.DatabaseConfigDto;
import store.novabook.store.common.util.dto.ElasticSearchConfigDto;
import store.novabook.store.common.util.dto.ImageManagerDto;
import store.novabook.store.common.util.dto.RabbitMQConfigDto;
import store.novabook.store.common.util.dto.RedisConfigDto;

@ExtendWith(MockitoExtension.class)
class KeyManagerUtilTest {

	@Mock
	private Environment environment;

	@Mock
	private RestTemplate restTemplate;

	private static final String APPKEY = "testAppkey";
	private static final String USER_ACCESS_KEY = "testUserAccessKey";
	private static final String SECRET_ACCESS_KEY = "testSecretAccessKey";

	@BeforeEach
	void setUp() {
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn(APPKEY);
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn(USER_ACCESS_KEY);
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn(SECRET_ACCESS_KEY);
	}

	@Test
	void getRedisConfig_returnsRedisConfig() {
		// given
		String redisKey = "testRedisKey";
		given(environment.getProperty("nhn.cloud.keyManager.redisKey")).willReturn(redisKey);

		// 가짜 응답 데이터 설정
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body",
			Map.of("secret", "{\"host\":\"localhost\",\"database\":0,\"password\":\"password\",\"port\":6379}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when
		RedisConfigDto redisConfig = KeyManagerUtil.getRedisConfig(environment, restTemplate);

		// then
		assertNotNull(redisConfig);
		assertEquals("localhost", redisConfig.host());
		assertEquals(0, redisConfig.database());
		assertEquals("password", redisConfig.password());
		assertEquals(6379, redisConfig.port());
	}

	@Test
	void getElasticSearchConfig_returnsElasticSearchConfig() {
		// given
		String elasticSearchKey = "testElasticSearchKey";
		given(environment.getProperty("nhn.cloud.keyManager.elasticSearchUser")).willReturn(elasticSearchKey);

		// 가짜 응답 데이터 설정
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body",
			Map.of("secret", "{\"uris\":\"http://localhost:9200\",\"id\":\"elastic\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when
		ElasticSearchConfigDto elasticSearchConfig = KeyManagerUtil.getElasticSearchConfig(environment, restTemplate);

		// then
		assertNotNull(elasticSearchConfig);
		assertEquals("http://localhost:9200", elasticSearchConfig.uris());
		assertEquals("elastic", elasticSearchConfig.id());
		assertEquals("password", elasticSearchConfig.password());
	}

	@Test
	void getImageManager_returnsImageManagerConfig() {
		// given
		String imageManagerKey = "testImageManagerKey";
		given(environment.getProperty("nhn.cloud.keyManager.imageManagerKey")).willReturn(imageManagerKey);

		// 가짜 응답 데이터 설정
		// 가짜 응답 데이터 설정
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret",
			"{\"endpointUrl\":\"http://localhost:9000\",\"accessKey\":\"access\",\"secretKey\":\"secret\",\"bucketName\":\"bucket\",\"localStorage\":\"/tmp/storage\"}"));

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when
		ImageManagerDto imageManagerConfig = KeyManagerUtil.getImageManager(environment, restTemplate);

		// then
		assertNotNull(imageManagerConfig);
		assertEquals("http://localhost:9000", imageManagerConfig.endpointUrl());
		assertEquals("access", imageManagerConfig.accessKey());
		assertEquals("secret", imageManagerConfig.secretKey());
		assertEquals("bucket", imageManagerConfig.bucketName());
		assertEquals("/tmp/storage", imageManagerConfig.localStorage());
	}

	@Test
	void getRabbitMQConfig_returnsRabbitMQConfig() {
		// given
		String rabbitMQKey = "testRabbitMQKey";
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn(rabbitMQKey);

		// 가짜 응답 데이터 설정
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret",
			"{\"host\":\"localhost\",\"port\":5672,\"username\":\"user\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when
		RabbitMQConfigDto rabbitMQConfig = KeyManagerUtil.getRabbitMQConfig(environment, restTemplate);

		// then
		assertNotNull(rabbitMQConfig);
		assertEquals("localhost", rabbitMQConfig.host());
		assertEquals(5672, rabbitMQConfig.port());
		assertEquals("user", rabbitMQConfig.username());
		assertEquals("password", rabbitMQConfig.password());
	}

	@Test
	void getDatabaseConfig_returnsDatabaseConfig() {
		// given
		String databaseKey = "testDatabaseKey";
		given(environment.getProperty("nhn.cloud.keyManager.storeKey")).willReturn(databaseKey);

		// 가짜 응답 데이터 설정
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret",
			"{\"url\":\"jdbc:mysql://localhost:3306/test\",\"username\":\"root\",\"password\":\"password\"}"));
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when
		DatabaseConfigDto databaseConfig = KeyManagerUtil.getDatabaseConfig(environment, restTemplate);

		// then
		assertNotNull(databaseConfig);
		assertEquals("jdbc:mysql://localhost:3306/test", databaseConfig.url());
		assertEquals("root", databaseConfig.username());
		assertEquals("password", databaseConfig.password());
	}

	@Test
	void getDataSource_responseEntityNull_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn("testAppkey");
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn("testUserAccessKey");
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn("testSecretAccessKey");

		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(null);

		// when, then
		assertThrows(
			KeyManagerException.class, () -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getDataSource_bodyObjNull_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn("testAppkey");
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn("testUserAccessKey");
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn("testSecretAccessKey");

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class,
			() -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getDataSource_bodyObjNotMap_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn("testAppkey");
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn("testUserAccessKey");
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn("testSecretAccessKey");

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", "invalid");

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class,
			() -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getDataSource_secretKeyMissing_throwsException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn("testAppkey");
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn("testUserAccessKey");
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn("testSecretAccessKey");

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("invalidKey", "invalidValue"));

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class,
			() -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
	}

	@Test
	void getRedisConfig_jsonProcessingException_throwsKeyManagerException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.redisKey")).willReturn("testRedisKey");
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(new ResponseEntity<>(getMockResponseBody(), HttpStatus.OK));

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRedisConfig(environment, restTemplate));
	}

	@Test
	void getElasticSearchConfig_jsonProcessingException_throwsKeyManagerException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.elasticSearchUser")).willReturn("testElasticSearchKey");
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(new ResponseEntity<>(getMockResponseBody(), HttpStatus.OK));

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getElasticSearchConfig(environment, restTemplate));
	}

	@Test
	void getImageManager_jsonProcessingException_throwsKeyManagerException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.imageManagerKey")).willReturn("testImageManagerKey");
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(new ResponseEntity<>(getMockResponseBody(), HttpStatus.OK));

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getImageManager(environment, restTemplate));
	}

	@Test
	void getRabbitMQConfig_jsonProcessingException_throwsKeyManagerException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.rabbitMQKey")).willReturn("testRabbitMQKey");
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(new ResponseEntity<>(getMockResponseBody(), HttpStatus.OK));

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getRabbitMQConfig(environment, restTemplate));
	}

	@Test
	void getDatabaseConfig_jsonProcessingException_throwsKeyManagerException() {
		// given
		given(environment.getProperty("nhn.cloud.keyManager.storeKey")).willReturn("testDatabaseKey");
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(new ResponseEntity<>(getMockResponseBody(), HttpStatus.OK));

		// when, then
		assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getDatabaseConfig(environment, restTemplate));
	}

	@Test
	void getStringObjectMap_resultEmpty_throwsKeyManagerException() {

		given(environment.getProperty("nhn.cloud.keyManager.appkey")).willReturn("testAppkey");
		given(environment.getProperty("nhn.cloud.keyManager.userAccessKey")).willReturn("testUserAccessKey");
		given(environment.getProperty("nhn.cloud.keyManager.secretAccessKey")).willReturn("testSecretAccessKey");

		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", ""));

		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(bodyMap, HttpStatus.OK);
		given(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
			eq(new ParameterizedTypeReference<Map<String, Object>>() {
			})))
			.willReturn(responseEntity);

		// when, then
		assertThrows(KeyManagerException.class,
			() -> KeyManagerUtil.getDataSource(environment, "testKeyId", restTemplate));
		// give

		// when, then
		KeyManagerException exception = assertThrows(KeyManagerException.class, () -> KeyManagerUtil.getStringObjectMap(responseEntity));
		assertEquals(MISSING_BODY_KEY, exception.getErrorCode());
	}

	private Map<String, Object> getMockResponseBody() {
		Map<String, Object> bodyMap = new HashMap<>();
		bodyMap.put("body", Map.of("secret", "{\"invalid\":\"invalid\"}"));
		return bodyMap;
	}
}
