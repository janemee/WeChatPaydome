package service;

import com.xiaoleilu.hutool.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 任务锁
 */
@Slf4j
@Service
public class LockService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String prefix = "LOCK:";
    private static final String prefix_timeout = "LOCK:TIMEOUT:";

    /**
     * 根据key判断当前key是否已被线程占用<br/><br/>
     * <p>
     * 在redis根据不同的key创建一个自增值,返回结果大于2则说明已被占用,
     * key值设置30秒过期,创建一个守护线程每过20秒续期30秒,设置30秒过期则是为了防止系统关闭后无法解锁
     *
     * @param key
     * @return true已上锁/false未上锁
     */
    public boolean lock(final String key) {
        String k = prefix + key;
        boolean result = stringRedisTemplate.boundValueOps(k).increment(1) > 1;//val做+1操作
        stringRedisTemplate.expire(k, 30, TimeUnit.SECONDS);// 默认30秒过期时间
        if (!result) {
            ThreadUtil.execute(() -> {
//                RedisService redisService = SpringContextUtils.getBean(RedisService.class);
//                String tk = prefix_timeout + key;
//                Long now = System.currentTimeMillis();
//                redisService.put(tk, now, 30);
//                while (true) {
//                    Long timeout = redisService.get(tk, Long.class);
//                    if (redisService.hasKey(k) && timeout != null && timeout.equals(now)) {
//                        redisService.expire(k,30);
//                        redisService.expire(tk,30);
//                    } else break;
//                    ThreadUtil.sleep(20000);
//
//                }
            });
        }
        return result;
    }

    /**
     * 解锁
     *
     * @param key
     */
    public void unlock(final String key) {
        stringRedisTemplate.delete(prefix + key);
        stringRedisTemplate.delete(prefix_timeout + key);
    }

    /**
     * 根据key锁定任务
     *
     * @param key
     * @param runnable
     */
    public void lockConsumerForKey(final String key, final Runnable runnable) {
//        ThreadUtil.execute(() -> {
//            LockService lockService = SpringContextUtils.getBean(LockService.class);
//            while (true) {
//                if (lockService.lock(key)) {
//                    log.info("线程被锁");
//                    ThreadUtil.sleep(1000);
//                } else {
//                    try {
//                        runnable.run();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        // 解锁动作 必须解锁
//                        lockService.unlock(key);
//                    }
//                    // 完成后跳出循环
//                    break;
//                }
//            }
//        });

    }

}
