package com.icloud.security.paper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paper")
@RestController
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;


    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @GetMapping("/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user,
                          @PathVariable("paperId") Long paperId) {

        return paperService.getPaper(paperId).get();
    }

}
