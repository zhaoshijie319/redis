//package com.example.redis.lock;
//
//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//
//import java.util.concurrent.TimeUnit;
//
//public class MyRedisLock {
//    RedissonClient.get
//    private static final String LOCK_TITLE = "redisLock_";
//    //加锁
//    public static boolean acquire(String lockName){
//        String key = LOCK_TITLE + lockName;
//        RLock mylock = redisson.getLock(key);
//        mylock.lock(2, TimeUnit.MINUTES);
//        System.out.println("======lock======"+Thread.currentThread().getName());
//        return  true;
//    }
//    //释放锁
//    public static void release(String lockName){
//        String key = LOCK_TITLE + lockName;
//        RLock mylock = redisson.getLock(key);
//        mylock.unlock();
//        System.out.println("======unlock======"+Thread.currentThread().getName());
//    }
//}
