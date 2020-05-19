package wizut.tpsi.ogloszenia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"wizut.tpsi.ogloszenia"})
public class OgloszeniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OgloszeniaApplication.class, args);
	}

}
