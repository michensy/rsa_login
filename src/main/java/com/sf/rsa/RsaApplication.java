package com.sf.rsa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RsaApplication implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("服务启动完成!");
    }
    public static void main(String[] args) {
        SpringApplication.run(RsaApplication.class, args);
    }
}
