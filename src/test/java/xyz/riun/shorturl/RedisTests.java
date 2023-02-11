package xyz.riun.shorturl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {
        System.out.println(stringRedisTemplate.opsForValue().increment("fahaoqi", 1));
    }

}
