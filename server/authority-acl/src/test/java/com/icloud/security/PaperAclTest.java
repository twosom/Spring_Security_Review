package com.icloud.security;

import com.icloud.security.paper.Paper;
import com.icloud.security.paper.PaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaperAclTest {

    @LocalServerPort
    private int port;

    @Autowired
    PaperRepository paperRepository;

    public String url(Long paperId) {
        return String.format("http://localhost:%d/paper/%d", port, paperId);
    }

    @BeforeEach
    void beforeEach() {
        paperRepository.deleteAll();
        Paper paper1 = createPaper(1L, "paper1", "tutor1", Paper.State.PREPARE);
        paperRepository.save(paper1);
    }

    private Paper createPaper(long id, String title, String toturId, Paper.State state) {
        return Paper.builder()
                .id(id)
                .title(title)
                .tutorId(toturId)
                .state(state)
                .build();
    }

    @DisplayName("1. student1이 1L 시험지를 가져온다.")
    @Test
    void test_1() {
        TestRestTemplate client = new TestRestTemplate("student1", "1111");
        ResponseEntity<Paper> response = client.getForEntity(url(1L), Paper.class);

        System.out.println("response.getBody() = " + response.getBody());
    }


}
