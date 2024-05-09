package com.example.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 创建并配置一个RedisCacheManager实例。
     *
     * @param redisConnectionFactory 用于与Redis服务器建立连接的工厂。
     * @return 配置好的CacheManager实例，可用于管理缓存。
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 配置缓存的基本设置，包括默认的过期时间和序列化器
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置默认过期时间为5分钟
                .entryTtl(Duration.ofMinutes(60))
                //1. 键的序列化方式：使用StringRedisSerializer对键进行序列化，确保键总是字符串类型。
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //2. 值的序列化方式：使用GenericJackson2JsonRedisSerializer对值进行JSON格式的序列化，支持将Java对象序列化为JSON字符串，以及将JSON字符串反序列化为Java对象。
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 基于上述配置，构建并返回RedisCacheManager实例
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

}
