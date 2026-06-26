package com.eleme.merchant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import com.eleme.merchant.rule.RandomLoadBalancerConfig;
import com.eleme.merchant.rule.ThreeTimeLoadBalanceConfig;

@MapperScan("com.eleme.merchant.mapper")
@EnableDiscoveryClient
@LoadBalancerClients({
        @LoadBalancerClient(name = "user-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "merchant-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "cart-service", configuration = RandomLoadBalancerConfig.class),
        @LoadBalancerClient(name = "order-service", configuration = ThreeTimeLoadBalanceConfig.class),
        @LoadBalancerClient(name = "payment-service", configuration = RandomLoadBalancerConfig.class)
})
@SpringBootApplication
public class MerchantApplication9010 {
    public static void main(String[] args) {
        SpringApplication.run(MerchantApplication9010.class, args);
    }
}
