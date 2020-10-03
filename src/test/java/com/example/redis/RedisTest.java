package com.example.redis;

import com.example.redis.bean.User;
import com.example.redis.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisTest {
    @Resource
    private UserMapper redisMapper;

    @Test
    public void testCacheable() {
        System.out.println("select user :\n" + redisMapper.selectById(1));
    }

    @Test
    public void testCachePut() {
        User user = new User();
        user.setId(1);
        user.setName("李四");
        System.out.println("update user :\n" + redisMapper.updateById(user));
    }

    @Test
    public void testCacheEvict() {
        System.out.println("delete user :\n" + redisMapper.deleteById(1));;
    }
}
