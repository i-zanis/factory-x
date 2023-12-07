package com.izanis.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {


  //TODO(i-zanis): add configuration for all services (local profile), Posgreqsl
  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }
}
