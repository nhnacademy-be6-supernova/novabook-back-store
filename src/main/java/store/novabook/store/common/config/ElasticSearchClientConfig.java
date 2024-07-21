package store.novabook.store.common.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.client.RestTemplate;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableElasticsearchRepositories(basePackages = "store.novabook.store.search")
@RequiredArgsConstructor
public class ElasticSearchClientConfig {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	private final Environment environment;

	@Bean
	public ElasticsearchClient getRestClient() {
		//TODO 나중에 엘라스틱 서치 비번 설정하면 써야됨

		// RestTemplate restTemplate = new RestTemplate();
		// ElasticSearchConfigDto config = KeyManagerUtil.getElasticSearchConfig(environment);
		// BasicCredentialsProvider redsProv = new BasicCredentialsProvider();
		// redsProv.setCredentials(
		// 	AuthScope.ANY, new UsernamePasswordCredentials(config.id(), config.password())
		// );
		// RestClient restClient = RestClient.builder(HttpHost.create(config.uris())).build();
		RestClient restClient = RestClient.builder(HttpHost.create(host + ":" + port)).build();

		// Create the transport and the API client
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
		return new ElasticsearchClient(transport);
	}

}