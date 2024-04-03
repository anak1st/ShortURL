package top.anak1st.shorturlserver.service;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import top.anak1st.shorturlserver.model.ShortURLJpa;
import top.anak1st.shorturlserver.utils.HashFunction;
import top.anak1st.shorturlserver.dao.ShortURLJpaRepo;


@Service
public class ShortURLService {

    @Autowired
    private ShortURLJpaRepo shortURLJpaRepo;

    private int counter = 0;

    private static String BASE_URL = "http://s.anak1st.top/";

    private static int MAX_SHORT_URL_NUM = 100;
    private int next() {
        counter = (counter + 1) % MAX_SHORT_URL_NUM;
        return counter;
    }

    private String nextHash() {
        return HashFunction.encode(next());
    }

    public String generateShortURL(String longURL) {
        ShortURLJpa shortURLJpa = shortURLJpaRepo.findByLongURL(longURL);
        if (shortURLJpa != null) {
            return BASE_URL + shortURLJpa.getShortURL();
        }

        String shortURL = nextHash();
        shortURLJpa = shortURLJpaRepo.findByShortURL(shortURL);
        if (shortURLJpa == null) {
            shortURLJpa = new ShortURLJpa();
            shortURLJpa.setShortURL(shortURL);
            shortURLJpaRepo.save(shortURLJpa);
        }
        shortURLJpa.setLongURL(longURL);
        shortURLJpa.setAccessCount(1);
        shortURLJpa.setCreatedTime(new Date());
        shortURLJpa.setLastAccessTime(new Date());
        shortURLJpaRepo.save(shortURLJpa);
        return BASE_URL + shortURL;
    }

    public String getLongURL(String shortURL) {
        ShortURLJpa shortURLJpa = shortURLJpaRepo.findByShortURL(shortURL);
        if (shortURLJpa == null) {
            return null;
        }
        shortURLJpa.setAccessCount(shortURLJpa.getAccessCount() + 1);
        shortURLJpa.setLastAccessTime(new Date());
        shortURLJpaRepo.save(shortURLJpa);
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
