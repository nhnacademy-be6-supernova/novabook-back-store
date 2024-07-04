package store.novabook.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		System.out.println("젠킨스 강하경 7월 4일 11:28");
		SpringApplication.run(StoreApplication.class, args);
	}

}
