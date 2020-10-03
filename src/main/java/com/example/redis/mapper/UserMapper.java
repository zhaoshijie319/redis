package com.example.redis.mapper;

import com.example.redis.bean.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Cacheable(value = "user", keyGenerator = "redisKeyGenerator", condition = "#id > 0", unless = "#result == null")
    public User selectById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("张三");
        return user;
    }

    @CachePut(value = "user", keyGenerator = "redisKeyGenerator")
    public User updateById(User user) {
        return user;
    }

    @CacheEvict(value = "user", keyGenerator = "redisKeyGenerator")
    public Integer deleteById(Integer id) {
        return id;
    }
}
