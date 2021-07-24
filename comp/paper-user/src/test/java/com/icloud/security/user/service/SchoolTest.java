package com.icloud.security.user.service;

import com.icloud.security.user.domain.School;
import com.icloud.security.user.repository.SchoolRepository;
import com.icloud.security.user.service.helper.SchoolTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class SchoolTest {

    @Autowired
    private SchoolRepository schoolRepository;

    private SchoolService schoolService;

    private SchoolTestHelper schoolTestHelper;

    private School school;


    @BeforeEach
    void before() {
        this.schoolRepository.deleteAll();
        this.schoolService = new SchoolService(schoolRepository);
        this.schoolTestHelper = new SchoolTestHelper(this.schoolService);
        this.school = this.schoolTestHelper.createSchool("테스트 학교", "서울");

    }

    @DisplayName("1. 학교를 생성한다.")
    @Test
    void test_1() throws Exception {
        SchoolTestHelper.assertSchool(school, "테스트 학교", "서울");
    }

    @DisplayName("2. 학교 이름을 수정한다.")
    @Test
    void test_2() throws Exception {
        schoolService.updateName(school.getSchoolId(), "테스트2 학교");
        School updatedSchool = schoolRepository.findAll().get(0);
        SchoolTestHelper.assertSchool(updatedSchool, "테스트2 학교", "서울");
        assertNotEquals(updatedSchool.getCreated(), updatedSchool.getUpdated());
    }

    @DisplayName("3. 지역 목록을 가져온다.")
    @Test
    void test_3() throws Exception {
        List<String> cities = schoolService.cities();
        assertEquals(1, cities.size());
        assertEquals("서울", cities.get(0));

        schoolTestHelper.createSchool("부산 학교", "부산");
        cities = schoolService.cities();
        assertEquals(2, cities.size());
    }

    @DisplayName("4. 지역으로 학교 목록을 가져온다.")
    @Test
    void test_4() throws Exception {
        List<School> list = schoolService.findAllByCity("서울");
        assertEquals(1, list.size());

        schoolTestHelper.createSchool("서울2 학교", "서울");
        list = schoolService.findAllByCity("서울");
        assertEquals(2, list.size());
        
    }

}
