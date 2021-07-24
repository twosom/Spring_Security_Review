package com.icloud.security;

import com.icloud.security.paper.Paper;
import com.icloud.security.paper.PaperRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CacheTest {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CacheManager cacheManager;

    Optional<Paper> getPaper(Long id) {
        return Optional.ofNullable(cacheManager.getCache("papers")
                .get(id, Paper.class));
    }

    @DisplayName("1. 조회한 Paper 는 캐시에 등록된다.")
    @Test
    void test_1() {
        Paper paper1 = createPaper(1L, "paper1");
        paperRepository.save(paper1);

        assertEquals(Optional.empty(), getPaper(1L));
        paperRepository.findById(1L);

        assertTrue(getPaper(1L).isPresent());
    }

    private Paper createPaper(long id, String title) {
        return Paper.builder()
                .id(id)
                .title(title)
                .build();
    }
}
