package works.weave.socks.cart;

// import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
// import io.micrometer.prometheus.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// @SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

// @EnablePrometheusEndpoint
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}
