package com.icloud.security.controller;

import com.icloud.security.student.StudentAuthenticationProvider;
import com.icloud.security.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final StudentAuthenticationProvider authenticationProvider;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("studentList", authenticationProvider.myStudents(teacher.getId()));
        return "TeacherMain";
    }
}
