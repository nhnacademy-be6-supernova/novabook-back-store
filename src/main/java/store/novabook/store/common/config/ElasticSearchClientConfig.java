package store.novabook.store.common.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories(basePackages = "store.novabook.store.search")
public class ElasticSearchClientConfig
{
	@Value("${spring.elasticsearch.uris}")
	private String host;

	@Value("${spring.elasticsearch.api-key}")
	private String apiKey;

	@Bean
	public ElasticsearchClient getRestClient() {

		// Create the low-level client
		RestClient restClient = RestClient
			.builder(HttpHost.create(host))
			.setDefaultHeaders(new Header[]{
				new BasicHeader("Authorization", "ApiKey " + apiKey)
			})
			.build();

		// Create the transport with a Jackson mapper
		ElasticsearchTransport transport = new RestClientTransport(
			restClient, new JacksonJsonpMapper());

		// And create the API client
		return new ElasticsearchClient(transport);
	}

}