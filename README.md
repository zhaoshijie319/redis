# 一、简介

Redis 是一个非关系型内存键值数据库、支持不同类型的值、可持久化到磁盘

# 二、说明

## 相比于Memcached

|  Redis  | Memcached |
| ------------ | ------------ |
| 支持多种数据类型  | 仅支持字符串 |
| RDB和AOF持久化  | 不支持持久化 |
| Redis Cluster  | 不支持分布式 |
| 不固定大小存储、存在碎片  | 固定大小存储、存在浪费 |

## 使用场景

- 计数器
- 缓存
- 查找表
- 消息队列
- 会话缓存
- 分布式锁实现

## 数据类型

### STRING

- 值：字符串、整数或者浮点数
- 用例：
    - 缓存HTML
- 操作：
    - 对整个字符串或者字符串的其中一部分执行操作
    - 对整数和浮点数执行自增或者自减操作

### LIST

- 值：列表
- 用例：
    - 维护最新记录
    - 发布/订阅
- 操作：
    - 从两端压入或者弹出元素
    - 对单个或者多个元素进行修剪
    - 只保留一个范围内的元素

### SET

- 值：无序集合
- 用例：
    - 共同好友
- 操作：
    - 添加、获取、移除单个元素
    - 检查一个元素是否存在于集合中
    - 计算交集、并集、差集
    - 从集合里面随机获取元素

### HASH

- 值：散列表
- 用例：
    - 存储对象
- 操作：
    - 添加、获取、移除单个元素
    - 获取所有键值对
    - 检查某个键是否存在
    
### ZSET

- 值：有序集合
- 用例：
    - 排行榜
- 操作：
    - 添加、获取、移除单个元素
    - 根据分值范围或者成员来获取元素
    - 计算一个键的排名
   
### BitMaps

- 值：位图
- 用例：
    - 活跃用户数
- 操作：
    - 单个元素设置0和1
    - 根据范围统计0或1的位数
    - 位运算
    - 0或1的第一位
    
### HyperLogLog  

- 值：布隆过滤器
- 用例：
    - 活跃用户数
- 操作：
    - 添加记录进行计数
    
## 数据结构

### 字典

dictht：是一个散列表结构，使用拉链法解决哈希冲突

### 跳跃表

- 跳跃表：是有序集合的底层实现之一，基于多指针有序链表实现的，可以看成多个有序链表
- 与红黑树等平衡树相比的优点：
    - 插入速度非常快速，因为不需要进行旋转等操作来维护平衡性；
    - 更容易实现；
    - 支持无锁操作
   
### 事件

Redis 服务器是一个事件驱动程序
- 文件事件：对套接字操作的抽象，基于 Reactor 模式的网络事件处理器
- 时间事件：对定时操作的抽象，可分为定时和周期性
- 运行过程
    - 1、初始化服务器
    - 2、一直处理事件，直到服务器关闭为止
    - 3、服务器关闭，执行清理操作

### 过期

- 过期精度：1毫秒
- 过期策略
    - 主动：访问失效
    - 被动：随机删除、按比例持续
- 缺点：
    - 设置过期会增加内存
    - 对于散列表只能为整个键设置过期时间（整个散列表），而不能为键里面的单个元素设置过期时间    
- 清除超时：使用删除和覆盖命令
- persist：永久保存
- expire：刷新过期时间

### 淘汰策略

- 淘汰过程：设置内存最大使用量，当内存使用量超出时，会施行数据淘汰策略
- 淘汰策略

|  策略  | 描述 |
| ------------ | ------------ |
| volatile-lfu  | 从已设置过期时间的数据集中挑选最少使用的数据淘汰 |
| volatile-lru | 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰 |
| volatile-ttl | 从已设置过期时间的数据集中挑选将要过期的数据淘汰 |
| volatile-random | 从已设置过期时间的数据集中任意选择数据淘汰 |
| allkeys-lfu | 从所有数据集中挑选最少使用的数据淘汰 |
| allkeys-lru | 从所有数据集中挑选最近最少使用的数据淘汰 |
| allkeys-random | 从所有数据集中任意选择数据进行淘汰 |
| noeviction | 禁止驱逐数据 |

- 使用场景
    - allkeys-lru：将内存最大使用量设置为热点数据占用的内存量，将最近最少使用的数据淘汰

### 持久化

- RDB：按指定的时间间隔执行数据集的时间点快照
    - 优点
        - 文件非常适合备份，在发生灾难时轻松还原数据集的不同版本
        - 最大限度地提高了Redis的性能，父实例只需创建一个分支，将永远不会执行磁盘I / O或类似操作
        - 允许大型数据集更快地重启
    - 缺点
        - 如果系统发生故障，将会丢失最后一次创建快照之后的数据
        - 如果数据量很大，Fork会很耗时，保存快照的时间会很长
    - 适用场景：注重数据，但可承受几分钟的数据丢失
    
- AOF：记录服务器接收的每个写入操作，这些操作将在服务器启动时再次执行，以重建原始数据集
    - 优点
        - 更加持久，可以使用不同的fsync策略：完全没有fsync，每秒fsync，每个查询fsync
        - 是仅追加的日志，如果断电则不会出现寻道或损坏问题。即使由于某种原因以半写命令结束日志使用redis-check-aof工具也可轻松修复
        - 在后台自动重写AOF是完全安全的，因为Redis继续追加到旧文件时，会生成一个全新的文件，其中包含创建当前数据集所需的最少操作集，完成切换即可
        - 以易于理解和解析的格式包含所有操作的日志
    - 缺点
        - 文件存储大于等效的RDB文件
        - 根据确切的fsync策略，AOF可能比RDB慢
    - 适用场景：确保数据安全性，AOF + RDB
    - 日志重写：
        随着服务器写请求的增多，AOF文件会越来越大。Redis提供了一种将AOF重写的特性，会编写最短的命令序列来重建内存中的当前数据集
    - 同步选项
    
|  选项  | 同步频率 | 效果 |  
| ------------ | ------------ | ------------ | 
| always  | 每个写命令都同步 | 非常慢，非常安全 | 
| everysec | 每秒同步一次 | 可保证系统崩溃时只会丢失一秒左右的数据 |
| no | 让操作系统来决定何时同步 | 更快，但更不安全，默认30秒 |   
    
### 分区

- 目标
    - 扩大内存量
    - 提升性能
- 方案
    - 范围分区
    - 哈希分区
- 实现方式
    - 客户端：客户端使用一致性哈希等算法决定键应当分布到哪个节点
    - 代理：将客户端请求发送到代理上，由代理转发请求到正确的节点上
    - 服务器：Redis Cluster，重定向
- 缺点
    - 不支持多个键操作
    - 复杂的数据处理
    - 分区粒度限制
    - 存储问题

### 主从复制

- 连接过程
    - 1、主服务器创建快照文件，发送给从服务器，并在发送期间使用缓冲区记录执行的写命令。快照文件发送完毕之后，开始向从服务器发送存储在缓冲区中的写命令
    - 2、从服务器丢弃所有旧数据，载入主服务器发来的快照文件，之后从服务器开始接受主服务器发来的写命令
    - 3、主服务器每执行一次写命令，就向从服务器发送相同的写命令
- 建议：
    - 主从数据库中启用持久性，若不能应关闭自动重启
    - 负载上升，可通过构建主从链进行处理

### 哨兵

- 功能
    - 监控主从实例
    - 通知
    - 自动故障转移
    - 配置提供程序
- 分布式
    - 多哨兵达成共识，执行故障检测，降低误报可能性
    - 克服单点故障
    
### 优化

- 内存优化
    - 少数据量的特殊编码
    - 位操作
    - 哈希存储对象
    - 删除延迟，驻留内存批量删除

- 性能优化
    - 缩短键值对的存储长度
    - 使用延迟删除特性
    - 设置键值的过期时间
    - 禁用长耗时的查询命令
    - 使用 slowlog 优化耗时命令
    - 使用 Pipeline 批量操作数据
    - 避免大量数据同时失效
    - 限制 Redis 内存大小
    - 使用物理机而非虚拟机安装 Redis
    - 使用分布式架构来增加读写速度

# 三、使用

## 缓存

这里是JAVA注解方式实现，也可通过RedisTemplate实现

### 1、添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 2、添加配置

```properties
spring.redis.host=localhost
spring.redis.port=6379
```

### 3、自定义配置

```java
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                //20分钟缓存失效
                .entryTtl(Duration.ofSeconds(60 * 20))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //默认形式改为JSON
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                //缓存前缀
                .prefixCacheNameWith("test-")
                //忽略空值
                .disableCachingNullValues();
        RedisCacheManager redisCacheManager = RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
        return redisCacheManager;
    }
}
```

### 4、定义数据模型

```java
@Getter
@Setter
public class Id {
    private Integer id;
}
```
```java
@Getter
@Setter
public class User extends Id {
    private String name;

    @Override
    public String toString() {
        return "id = " + this.getId() + "\n" + "name = " + this.getName();
    }
}
```

### 5、自定义key构建方式

```java
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
```

### 6、增删改查

```java
@Component
public class UserMapper {
    @Cacheable(value = "user", keyGenerator = "redisKeyGenerator", condition = "#id > 0", unless = "#result == null")
    public User selectById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("小仙女");
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
```

### 7、测试

```java
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
        user.setName("大怪兽");
        System.out.println("update user :\n" + redisMapper.updateById(user));
    }

    @Test
    public void testCacheEvict() {
        System.out.println("delete user :\n" + redisMapper.deleteById(1));;
    }
}
```

## Redlock

### 分布式锁

- 互斥：在任何给定时刻，只有一个客户端可以持有锁
- 无死锁：最终，即使锁定资源的客户端崩溃或分区，也始终可以获得锁定
- 容错：只要大多数节点都处于运行状态，客户端就可以获取和释放锁
    
### 实现方式

- 1、以毫秒为单位获取当前时间
- 2、尝试在所有N个实例中顺序使用所有实例中相同的键名和随机值来获取锁定
- 3、当前时间 - 步骤1的时间戳 = 获取锁所花费的时间。当且仅当在大多数实例获取到锁时则认为已获取锁
- 4、获取锁后将其有效时间视为初始有效时间减去经过的时间
- 5、如果客户端由于某种原因未能获得该锁，它将尝试解锁所有实例

### 使用

- 1、导入Redisson依赖

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.11.1</version>
</dependency>
```

- 2、构建RedissonProperties

配置文件
```properties
spring.redisson.password=Redisredis
spring.redisson.address[0]=redis://10.192.77.202:6379
spring.redisson.address[1]=redis://10.192.77.203:6379
spring.redisson.address[2]=redis://10.192.77.204:6379
```

自定义配置
```java
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
```

- 3、构建RedissonClient

```java
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)
                .addNodeAddress(redissonProperties.getAddress().toArray(new String[redissonProperties.getAddress().size()]))
                .setPassword(redissonProperties.getPassword());

        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
```

- 4、测试

```java
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
}
```

# 四、连接

[Redis官网](https://redis.io/ "Redis官网")

[spring-data-redis官网](https://spring.io/projects/spring-data-redis "spring-data-redis官网")

[Redisson官网](https://redisson.org/ "Redisson官网")
