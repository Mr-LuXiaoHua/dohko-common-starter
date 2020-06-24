# dohko-common-starter
**将与业务无关的常用功能封装成spring-boot-starter**




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
   LOG-ID: 60258630 -> 调用方法: TestController.user, 请求来源: 192.168.251.44, 请求URI: /test
   LOG-ID: 60258630 -> 调用方法: TestController.user, 方法参数: {request={userId=123456}}
   ---模拟业务处理---
   LOG-ID: 60258630 -> 调用方法:TestController.user  耗时: 7 ms
   LOG-ID: 60258630 -> 调用方法: TestController.user, 方法响应: User(userId=123456, username=楚留香)
```
 



