package com.qqspeed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate配置类：解决Spring Boot 3.x 自动创建的RedisTemplate序列化问题，同时显式创建Bean
 */
@Configuration // ① 标记这是配置类，Spring启动时会执行这里的代码
public class RedisTemplateConfig {

    @Bean // ② 把方法返回的RedisTemplate对象注册成Spring Bean，供其他类注入
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 1. 创建RedisTemplate对象————造一个「遥控器」（RedisTemplate对象）
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 2. 设置连接工厂————给遥控器装「信号发射器」（连接工厂）：建立RedisTemplate和Redis服务器的连接，RedisConnectionFactory是Spring自动创建的，包含你的Redis地址/端口/密码等配置
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 3. 设置Key序列化器（String）————给遥控器设置「Key的编码格式」（String序列化）：保证Key是明文（比如"qqspeed:car:detail:雷诺"），不是乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer); // 普通Key的序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer); // Hash类型Key的序列化
        // 4. 设置Value序列化器（JSON，支持对象）————给遥控器设置「Value的编码格式」（JSON序列化）：把Java对象（比如CarVO）转成JSON字符串存到Redis，取的时候再转回对象
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonRedisSerializer); // 普通Value的序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer); // Hash类型Value的序列化
        // 5. 初始化RedisTemplate————激活配置：让上面的序列化、连接工厂配置生效
        redisTemplate.afterPropertiesSet();
        // 6. 把定制好的「遥控器」交给Spring容器
        return redisTemplate;
    }
}