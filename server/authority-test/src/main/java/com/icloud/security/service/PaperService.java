package com.icloud.security.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PaperService {

    private Map<Long, Paper> paperDB = new HashMap<>();


    public void setPaper(Paper paper) {
        paperDB.put(paper.getPaperId(), paper);
    }


    public List<Paper> getMyPapers(String username) {
        return paperDB
                .values()
                .stream()
                .filter(isContainsUsername(username))
                .collect(Collectors.toList());
    }

    public Paper getPaper(Long paperId) {
        return paperDB.get(paperId);
    }

    private Predicate<Paper> isContainsUsername(String username) {
        return paper -> paper.getStudentIds().contains(username);
    }
}
