# dohko-common-starter
>将与业务无关的常用功能(如：日志、缓存、分布式锁等横切功能点)封装成spring-boot-starter


<br>
<br>

## dohko-log-spring-boot-starter
方法调用日志starter，记录方法调用的请求参数、响应数据以及方法调用耗时

### 使用说明
* 使用 `mvn install` 将项目安装到本地仓库

* 在项目中引入dohko-log-spring-boot-starter
```
<dependency>
     <groupId>com.dohko</groupId>
     <artifactId>dohko-log-spring-boot-starter</artifactId>
     <version>0.0.1-SNAPSHOT</version>
 </dependency>
 ```
 
* 在springboot启动类上启用注解 @EnableCallLog
```
 @EnableCallLog
 @SpringBootApplication
 public class Application {
 
     public static void main(String[] args) {
         SpringApplication.run(RecApplication.class, args);
     }
 
 }
 ```
 
* 在方法上启用注解 @CallLog
```
 @GetMapping("/test")
 @CallLog
 public User user(HttpServletRequest request){

     String userId = request.getParameter("userId");

     log.info("---模拟业务处理---");

     User user = new User();
     user.setUserId(userId);
     user.setUsername("楚留香");

     return user;
 }
 ```
 
 * 效果展示： /test?userId=123456
 ```
   调用方法: TestController.user, 请求来源: 192.168.251.44, 请求URI: /test
   调用方法: TestController.user, 方法参数: {request={userId=123456}}
   ---模拟业务处理---
   调用方法:TestController.user  耗时: 7 ms
   调用方法: TestController.user, 方法响应: User(userId=123456, username=楚留香)
```
 

<br>
<br>

## dohko-cache-spring-boot-starter
缓存starter，目前只实现了redis, 支持spring-cache，默认缓存时间30分钟。 后续会逐步实现memcache

### 使用说明
* 使用 `mvn install` 将项目安装到本地仓库

* 在项目中引入dohko-cache-spring-boot-starter
```
<dependency>
     <groupId>com.dohko</groupId>
     <artifactId>dohko-cache-spring-boot-starter</artifactId>
     <version>0.0.1-SNAPSHOT</version>
 </dependency>
 ```
 
* 启动类增加starter项目的包扫描 `com.dohko`
```
@SpringBootApplication
@ComponentScan(basePackages={"com.example", "com.dohko"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

* application.properties配置redis
```
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=123456

spring.redis.lettuce.pool.max-active=10
spring.redis.lettuce.pool.max-wait=10000
```

* 代码中可以使用spring-cache风格
```
@Cacheable(cacheNames = "hello", key = "#name")
public String hello(String name) {
    System.out.println("模拟业务处理");
    return name;
}
```

* 直接获取RedisTemplate，灵活进行redis操作
```
@Autowired
private CacheProvider cacheProvider;


 RedisTemplate<String, String> redisTemplate = cacheProvider.getRedisTemplate();
         
 String key = "test";
 redisTemplate.opsForValue().set(key, name);
 String value =  redisTemplate.opsForValue().get(key);
 
```


<br>
<br>

## dohko-lock-redisson-spring-boot-starter
分布式锁starter，基于redission

### 使用说明
* 使用 `mvn install` 将项目安装到本地仓库

* 在项目中引入dohko-lock-redisson-spring-boot-starter
```
<dependency>
    <groupId>com.dohko</groupId>
    <artifactId>dohko-lock-reddisson-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
 ```
 
* 启动类启用分布式锁
```
@EnableLock
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

* application.properties配置redis
```
# 单个redis服务器
dohko.redisson.cluster-type=single
dohko.redisson.db=0
dohko.redisson.password=
dohko.redisson.address=redis://127.0.0.1:6379
dohko.redisson.connect-timeout=10000
dohko.redisson.connection-pool-size=64


# redis-集群-哨兵模式
#dohko.redisson.cluster-type=sentinal
#dohko.redisson.sentinel-master-name=your-sentinel-name
#dohko.redisson.db=0
dohko.redisson.password=
#dohko.redisson.sentinel-nodes=redis://127.0.0.1:6379,redis://127.0.0.1:6389,redis://127.0.0.1:6399
#dohko.redisson.connect-timeout=10000
#dohko.redisson.connection-pool-size=64

# redis-集群-cluster模式
#dohko.redisson.cluster-type=cluster
dohko.redisson.password=
#dohko.redisson.cluster-nodes=redis://127.0.0.1:6349,redis://127.0.0.1:6359,redis://127.0.0.1:6369,redis://127.0.0.1:6379,redis://127.0.0.1:6389,redis://127.0.0.1:6399
#dohko.redisson.connect-timeout=10000
#dohko.redisson.connection-pool-size=64
```

* 方式一：使用注解实现分布式锁
> 分布式锁的key为: module + key, 如orderNo=10086, 则组合后的key为 order-10086
```
 @DistributedLock(module = "order-", key="#{#orderNo}")
 public void testLock2(String orderNo) {
     try {
         log.info("模拟业务");
         Thread.sleep(5000L);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
 }
```

* 方式二：获取DistributedLockService，灵活操作分布式锁
```
 @Autowired
 private DistributedLockService distributedLockService;


  public void testLock1(OrderDTO dto) {
     log.info("进入方法了");
     RLock lock = distributedLockService.getLock(dto.getOrderNo());
     try {
         boolean isLock = lock.tryLock(0, 10L, TimeUnit.SECONDS);
         if (isLock) {
             // 获取到锁

         } else {
             // 没有获取到锁
         }
     } catch (Exception e) {
         e.printStackTrace();
     } finally {
         if (Objects.nonNull(lock) && lock.isHeldByCurrentThread()) {
             lock.unlock();
         }
     }
 }
 
```