package com.example.redis.configuration;

import com.example.redis.bean.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
public class RedisKeyGenerator implements KeyGenerator {
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (Object object : objects) {
            if (ClassUtils.isPrimitiveOrWrapper(object.getClass())) {
                stringBuilder.append(object);
            } else {
                //若非基础类型，则转换为Id对象来获取主键
                try {
                    Id id = objectMapper.readValue(objectMapper.writeValueAsString(object), Id.class);
                    stringBuilder.append(id.getId());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
