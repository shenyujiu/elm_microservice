package com.eleme.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.eleme.order.rule.RandomLoadBalancerConfig;
import com.eleme.order.rule.ThreeTimeLoadBalanceConfig;

@MapperScan("com.eleme.order.mapper")
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
public class OrderApplication9031 {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication9031.class, args);
    }
}
