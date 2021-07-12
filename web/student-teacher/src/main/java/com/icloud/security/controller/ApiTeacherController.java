package com.icloud.security.controller;

import com.icloud.security.student.Student;
import com.icloud.security.student.StudentAuthenticationProvider;
import com.icloud.security.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class ApiTeacherController {

    private final StudentAuthenticationProvider authenticationProvider;


    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> students(@AuthenticationPrincipal Teacher teacher, Model model) {
        return authenticationProvider.myStudents(teacher.getId());
    }
}
