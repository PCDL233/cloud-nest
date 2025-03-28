package com.cmq.cloudnestbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.cmq.cloudnestbackend.mapper")
//启用 Spring AOP 的自动代理功能
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CloudNestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNestBackendApplication.class, args);
    }

}
