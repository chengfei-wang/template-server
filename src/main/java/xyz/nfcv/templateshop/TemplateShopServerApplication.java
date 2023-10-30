package xyz.nfcv.templateshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class TemplateShopServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateShopServerApplication.class, args);
    }
}