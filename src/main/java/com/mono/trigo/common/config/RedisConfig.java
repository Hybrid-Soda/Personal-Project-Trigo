package com.mono.trigo.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;


@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("#{environment['spring.redis.host']}")
    private String host;

    @Value("#{environment['spring.redis.port']}")
    private Integer port;

    // jackson LocalDateTime mapper
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper
                // 날짜를 타임스탬프로 쓰지 않도록 비활성화
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 날짜/시간 및 Optional 등 JDK8 호환 모듈 등록 (InvalidDefinitionException 방지)
                .registerModules(new JavaTimeModule(), new Jdk8Module())
                // 다형성 타입 정보 포함 활성화 (ClassCastException 방지)
                .activateDefaultTyping(
                        mapper.getPolymorphicTypeValidator(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                );
        return mapper;
    }

    // outer redis connection
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    // serialize byte value
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // key
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        // value
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper());
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        return template;
    }
}
