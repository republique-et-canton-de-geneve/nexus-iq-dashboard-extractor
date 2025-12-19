package ch.ge.cti.nexusiq;

import ch.ge.cti.nexusiq.business.ExtractorService;
import ch.ge.cti.nexusiqdashboard.ApiException;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "ch.ge.cti.nexusiq" })
public class Application implements CommandLineRunner {

    @Resource
    ExtractorService extractorService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException, ApiException {
        extractorService.generateResultFile();
    }

}
