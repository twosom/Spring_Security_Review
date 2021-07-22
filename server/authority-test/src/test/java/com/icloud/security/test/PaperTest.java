package com.icloud.security.test;

import com.icloud.security.service.Paper;
import com.icloud.security.service.PaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.icloud.security.service.Paper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class PaperTest extends WebIntegrationTest {

    @Autowired
    private PaperService paperService;
    TestRestTemplate client;

    private final Paper paper1 = createPaper(1L, "시험지1", State.PREPARE, "user1");
    private final Paper paper2 = createPaper(2L, "시험지2", State.PREPARE, "user2");
    private final Paper paper3 = createPaper(3L, "시험지3", State.READY, "user1");

    private Paper createPaper(long paperId, String title, State state, String... studentIds) {
        return builder()
                .paperId(paperId)
                .title(title)
                .tutorId("tutor1")
                .studentIds(List.of(studentIds))
                .state(state)
                .build();
    }


    @DisplayName("1. user1이 시험지 리스트를 조회")
    @Test
    void test_1() {
        paperService.setPaper(paper1);
        paperService.setPaper(paper2);
        paperService.setPaper(paper3);

        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<List> response = client.getForEntity(uri("/paper/mypapers"), List.class);


        assertEquals(OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());


        List<Paper> body = response.getBody();
        System.out.println("body = " + body);
    }


    @DisplayName("2. user1이 user2의 시험지는 볼 수 없다.")
    @Test
    void test_2() throws Exception {
        paperService.setPaper(paper2);
        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<Paper> response = client.getForEntity(uri("/paper/get/2"), Paper.class);
        assertEquals(FORBIDDEN, response.getStatusCode());
    }

    @DisplayName("3. user2 라도 출제중인 시험지에는 접근할 수 없다.")
    @Test
    void test_3() throws Exception {
        paperService.setPaper(paper2);
        client = new TestRestTemplate("user2", "1111");
        ResponseEntity<Paper> response = client.getForEntity(uri("/paper/get/2"), Paper.class);
        assertEquals(FORBIDDEN, response.getStatusCode());
    }

    @DisplayName("5. 교장선생님은 모든 시험지를 볼 수 있다.")
    @Test
    void test_5() throws Exception {
        paperService.setPaper(paper1);
        paperService.setPaper(paper2);
        paperService.setPaper(paper3);
        client = new TestRestTemplate("primary", "1111");

        ResponseEntity<List> response = client.getForEntity(uri("/paper/getPapersByPrimary"), List.class);
        assertEquals(OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());

    }

}
