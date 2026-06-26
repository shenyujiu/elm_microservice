package com.eleme.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import com.eleme.cart.rule.RandomLoadBalancerConfig;
import com.eleme.cart.rule.ThreeTimeLoadBalanceConfig;

@MapperScan("com.eleme.cart.mapper")
@EnableDiscoveryClient
@LoadBalancerClients({
        @LoadBalancerClient(name = "user-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "merchant-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "cart-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "order-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "payment-service", configuration = RandomLoadBalancerConfig.class)
})
@SpringBootApplication
public class CartApplication9020 {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication9020.class, args);
    }
}
