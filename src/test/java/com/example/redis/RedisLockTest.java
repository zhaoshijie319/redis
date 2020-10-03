package com.example.redis;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisLockTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisLock() {
        String lockName = "redlock_test";
        RLock redLock = redissonClient.getLock(lockName);
        boolean isLock;
        try {
            isLock = redLock.tryLock(500, 30000, TimeUnit.MILLISECONDS);
            System.out.println("isLock = " + isLock);
            if (isLock) {
                // lock success, do something;
                Thread.sleep(10000);
            }
        } catch (Exception e) {

        } finally {
            redLock.unlock();
            System.out.println("unlock success");
        }
    }

//    public static void main(String[] args) {
//        Config config1 = new Config();
//        config1.useSingleServer()
//                .setAddress("localhost:6379")
//                .setDatabase(0);
//        RedissonClient redissonClient1 = Redisson.create(config1);
//
//        Config config2 = new Config();
//        config2.useSingleServer()
//                .setAddress("10.192.77.202:6379")
//                .setPassword("Redisredis")
//                .setDatabase(1);
//        RedissonClient redissonClient2 = Redisson.create(config2);
//
//        String lockName = "redlock_test";
//        RLock lock1 = redissonClient1.getLock(lockName);
//        RLock lock2 = redissonClient2.getLock(lockName);
//
//        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2);
//        boolean isLock;
//        try {
//            isLock = redLock.tryLock(500, 30000, TimeUnit.MILLISECONDS);
//            System.out.println("isLock = " + isLock);
//            if (isLock) {
//                // lock success, do something;
//                Thread.sleep(10000);
//            }
//        } catch (Exception e) {
//
//        } finally {
//            redLock.unlock();
//            System.out.println("unlock success");
//        }
//    }
}
