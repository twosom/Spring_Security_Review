package com.icloud.security.controller;

import com.icloud.security.service.Paper;
import com.icloud.security.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paper")
public class PaperController {

    private final PaperService paperService;

    //    @PreAuthorize("isStudent()")
//    @PostFilter("notPrepareState(filterObject)")
    @GetMapping("/mypapers")
    public List<Paper> myPaper(@AuthenticationPrincipal User user) {
        return paperService.getMyPapers(user.getUsername());
    }



    @PostAuthorize("returnObject.studentIds.contains(#user.username)")
    @GetMapping("/get/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user, @PathVariable("paperId") Long paperId) {
        return paperService.getPaper(paperId);
    }


}
