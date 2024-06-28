package store.novabook.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		System.out.println("젠킨스 12-3012-03091-20491-049-02394-01249-02349-012394-01294-0=1293-012943-01394-1094-01249-02349-02394-23942-30492-304923-40923-049");
		SpringApplication.run(StoreApplication.class, args);
	}

}
