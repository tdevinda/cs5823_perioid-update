package lk.cloud.noise.collect.repository;


import lk.cloud.noise.collect.dto.NoiseSample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SampleRepository {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    RedisTemplate<String, Float> redisTemplate;

    public void saveSample(NoiseSample sample) {
        logger.info("Saving sample {}", sample);
        redisTemplate.opsForList().leftPush("id:" + sample.getId(), sample.getLevel());
    }
}
