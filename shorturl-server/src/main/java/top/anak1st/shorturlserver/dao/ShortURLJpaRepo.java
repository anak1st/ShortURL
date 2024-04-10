package top.anak1st.shorturlserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.anak1st.shorturlserver.model.ShortURLJpa;

public interface ShortURLJpaRepo extends JpaRepository<ShortURLJpa, String> {

    ShortURLJpa findByShortURL(String shortURL);

    void deleteByShortURL(String shortURL);
}