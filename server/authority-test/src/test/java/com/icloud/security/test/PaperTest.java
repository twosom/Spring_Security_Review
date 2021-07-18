package com.icloud.security.test;

import com.icloud.security.service.Paper;
import com.icloud.security.service.PaperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

public class PaperTest extends WebIntegrationTest {

    TestRestTemplate client;

    @Autowired
    PaperService paperService;

    Paper paper1 = createPaper(1L, "시험지1", "user1");
    Paper paper2 = createPaper(2L, "시험지1", "user2");

    private Paper createPaper(long paperId, String title, String user) {
        return Paper.builder()
                .paperId(paperId)
                .title(title)
                .tutorId("tutor1")
                .studentIds(List.of(user))
                .state(Paper.State.PREPARE)
                .build();
    }


    @DisplayName("1. user1 이 시험지 리스트 조회.")
    @Test
    void test_1() throws Exception {
        paperService.setPaper(paper1);

        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<List> response = client.getForEntity(uri("/paper/mypapers"), List.class);
        assertEquals(OK, response.getStatusCode());
        System.out.println("response.getBody() = " + response.getBody());
    }


    @DisplayName("2. user1이 user2의 시험지는 볼 수 없다.")
    @Test
    void test_2() throws Exception {
        paperService.setPaper(paper2);
        client = new TestRestTemplate("user2", "1111");
        ResponseEntity<Paper> response = client.getForEntity(uri("/paper/get/" + paper2.getPaperId()), Paper.class);

        assertEquals(FORBIDDEN, response.getStatusCode());

    }

}
