package store.novabook.store.common.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.util.KeyManagerUtil;
import store.novabook.store.common.util.dto.ElasticSearchConfigDto;

@Configuration
@EnableElasticsearchRepositories(basePackages = "store.novabook.store.search")
@RequiredArgsConstructor
public class ElasticSearchClientConfig {
	private final Environment environment;

	@Bean
	public ElasticsearchClient getRestClient() {
		ElasticSearchConfigDto config = KeyManagerUtil.getElasticSearchConfig(environment);

		// Create the low-level client
		RestClient restClient = RestClient.builder(HttpHost.create(config.uris()))
			.setDefaultHeaders(new Header[] {new BasicHeader("Authorization", "ApiKey " + config.apiKey())})
			.build();

		// Create the transport with a Jackson mapper
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// And create the API client
		return new ElasticsearchClient(transport);
	}

}