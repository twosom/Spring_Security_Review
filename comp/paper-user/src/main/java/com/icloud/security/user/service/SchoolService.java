package com.icloud.security.user.service;

import com.icloud.security.user.domain.School;
import com.icloud.security.user.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public School save(School school) {
        return schoolRepository.save(school);
    }

    public Optional<School> updateName(Long schoolId, String name) {
        return schoolRepository.findById(schoolId)
                .map(school -> {
                    school.setName(name);
                    schoolRepository.save(school);
                    return school;
                });
    }

    public List<String> cities() {
        return schoolRepository.getCities();
    }


    public List<School> findAllByCity(String city) {
        return schoolRepository.findAllByCity(city);
    }
}
