package com.cmq.cloudnestbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
//启用 Spring AOP 的自动代理功能
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CloudNestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNestBackendApplication.class, args);
    }

}
