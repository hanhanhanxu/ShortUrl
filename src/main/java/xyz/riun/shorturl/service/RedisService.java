package xyz.riun.shorturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.riun.shorturl.entity.constants.RedisConstant;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成自增id,从1000000开始
     * @return
     */
    public Long getIncrementId() {
        try {
            Long redisIncrementId = stringRedisTemplate.opsForValue().increment(RedisConstant.INCREMENT_KEY, 1);
            return 1000000L + redisIncrementId;
        } catch (Exception e) {
            throw new RuntimeException("redis获取自增id失败");
        }
    }

}
