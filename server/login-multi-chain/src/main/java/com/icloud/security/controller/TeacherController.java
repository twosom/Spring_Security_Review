package com.icloud.security.controller;

import com.icloud.security.student.Student;
import com.icloud.security.student.StudentAuthenticationProvider;
import com.icloud.security.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final StudentAuthenticationProvider studentAuthenticationProvider;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("studentList", studentAuthenticationProvider.myStudentList(teacher.getId()));
        return "TeacherMain";
    }
}
