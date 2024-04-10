package top.anak1st.shorturlserver.service;


import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import top.anak1st.shorturlserver.model.ShortURLJpa;
import top.anak1st.shorturlserver.dao.ShortURLJpaRepo;
import top.anak1st.shorturlserver.utils.HashUtil;

@Service
public class ShortURLService {

    @Autowired
    private ShortURLJpaRepo shortURLJpaRepo;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // private static String BASE_URL = "https://anak1st.top/s/";
    private static final String BASE_URL = "https://localhost:8080/s/";

    // 缓存过期时间
    private static final int TIMEOUT = 5;

    // 自定义长链接防重复字符串
    private static final String DUPLICATE_SUFFIX = "*";

    // private static final BloomFilter<String> bloomFilter = BloomFilter.create(1000000, 0.01);

    public String saveLongURL(String shortURL, String hashVal, String longURL) {
        // 从redis中查询是否有缓存
        String redisLongURL = redisTemplate.opsForValue().get(shortURL);
        if (redisLongURL != null) {
            if (redisLongURL.equals(longURL)) {
                // redis中有缓存，且长链接相同
                redisTemplate.expire(shortURL, TIMEOUT, TimeUnit.MINUTES);
                return shortURL;
            } else {
                // 加入自定义防重复字符串
                hashVal = hashVal + DUPLICATE_SUFFIX;
                return saveLongURL(HashUtil.hashToBase62(hashVal), hashVal, longURL);
            }
        }
        // redis中没有缓存，查询数据库
        ShortURLJpa shortURLJpa = shortURLJpaRepo.findByShortURL(shortURL);
        if (shortURLJpa != null) {
            if (shortURLJpa.getLongURL().equals(longURL)) {
                // 数据库中有记录，且长链接相同
                // 保存到redis
                redisTemplate.opsForValue().set(shortURL, longURL, TIMEOUT, TimeUnit.MINUTES);
                return shortURL;
            } else {
                // 数据库中有记录，但长链接不同
                // 加入自定义防重复字符串
                hashVal = hashVal + DUPLICATE_SUFFIX;
                return saveLongURL(HashUtil.hashToBase62(hashVal), hashVal, longURL);
            }
        } else {
            // 数据库中没有记录，保存到数据库
            shortURLJpa = new ShortURLJpa();
            shortURLJpa.setShortURL(shortURL);
            shortURLJpa.setLongURL(longURL);
            shortURLJpa.setCreatedTime(new Date());
            shortURLJpaRepo.save(shortURLJpa);
            // 保存到redis
            redisTemplate.opsForValue().set(shortURL, longURL, TIMEOUT, TimeUnit.MINUTES);
            return shortURL;
        }
    }

    public String getLongURL(String shortURL) {
        // 查询redis中是否有缓存
        String longURL = redisTemplate.opsForValue().get(shortURL);
        if (longURL != null) {
            // 有缓存，延迟过期时间
            redisTemplate.expire(shortURL, TIMEOUT, TimeUnit.MINUTES);
            return longURL;
        }
        // 没有缓存，查询数据库
        ShortURLJpa shortURLJpa = shortURLJpaRepo.findByShortURL(shortURL);
        if (shortURLJpa == null) {
            // 数据库中没有，返回null
            return null;
        }
        // 数据库中有，保存到redis
        redisTemplate.opsForValue().set(shortURL, shortURLJpa.getLongURL(), TIMEOUT, TimeUnit.MINUTES);
        return shortURLJpa.getLongURL();
    }

    public List<ShortURLJpa> getAll() {
        return shortURLJpaRepo.findAll();
    }

    public ShortURLJpa getByShortURL(String shortURL) {
        return shortURLJpaRepo.findByShortURL(shortURL);
    }

    public void deleteAll() {
        shortURLJpaRepo.deleteAll();
    }

    public void deleteByShortURL(String shortURL) {
        shortURLJpaRepo.deleteByShortURL(shortURL);
    }
}
