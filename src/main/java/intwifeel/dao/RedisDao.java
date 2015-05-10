package intwifeel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String readValue(String key) {
        if (redisTemplate.hasKey(key)) {
            return redisTemplate.boundValueOps(key).get();
        }

        return null;
    }
}
