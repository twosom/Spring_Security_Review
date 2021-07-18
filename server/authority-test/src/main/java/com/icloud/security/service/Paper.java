package com.icloud.security.service;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paper {

    private Long paperId;
    private String title;
    private String tutorId;
    private List<String> studentIds;
    private State state;


    @RequiredArgsConstructor
    public static enum State {
        PREPARE("출제 중"),
        READY("시험 시작"),
        END("시험 종료");

        private final String description;
    }

}
