package org.frank.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use Jackson2JsonRedisSerializer to serialize and deserialize the value of redis
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = createJackson2JsonRedisSerializer();

        // String serialization
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key uses String serialization
        template.setKeySerializer(stringRedisSerializer);
        // hash's key also uses String serialization
        template.setHashKeySerializer(stringRedisSerializer);
        // value uses jackson serialization
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash's value also uses jackson serialization
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    private GenericJackson2JsonRedisSerializer createJackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Solve the problem of jackson2 converting LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        // Set visibility
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // Activate default typing
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
