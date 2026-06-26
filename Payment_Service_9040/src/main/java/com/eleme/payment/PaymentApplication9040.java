package com.eleme.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.eleme.payment.rule.RandomLoadBalancerConfig;
import com.eleme.payment.rule.ThreeTimeLoadBalanceConfig;

@MapperScan("com.eleme.payment.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.eleme.entity.feign")
@LoadBalancerClients({
        @LoadBalancerClient(name = "user-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "merchant-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "cart-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "order-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "payment-service", configuration = RandomLoadBalancerConfig.class)
})
@SpringBootApplication
public class PaymentApplication9040 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication9040.class, args);
    }
}
