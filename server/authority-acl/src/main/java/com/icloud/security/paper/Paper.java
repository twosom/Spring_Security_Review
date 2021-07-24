package com.icloud.security.paper;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Paper {

    @Id
    private Long id;
    private String title;
    private String tutorId;
//    private List<String> studentIds;
    private State state;


    @RequiredArgsConstructor
    public enum State {
        PREPARE("출제 중"),
        READY("시험 시작"),
        END("시험 종료");

        private final String description;
    }
}

