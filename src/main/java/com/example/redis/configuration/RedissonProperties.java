package com.example.redis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(
        prefix = "spring.redisson"
)
public class RedissonProperties {
    private String password;
    private List<String> address;

    public String getPassword() {
        return password;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
