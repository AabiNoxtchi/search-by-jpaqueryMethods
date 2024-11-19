package com.example.queryMethods.service;


import com.example.queryMethods.models.entity.Student;
import com.example.queryMethods.models.filter.StudentFilter;
import com.example.queryMethods.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findAll(StudentFilter filter) {
        List<Student> results = new ArrayList<>(studentRepository.findAll());

        if (filter == null) {
            return results;
        }
        if (filter.getName() != null) {
            results.retainAll(studentRepository.findByNameContainingIgnoreCase(filter.getName()));
        }
        if (filter.getEmail() != null) {
            results.retainAll(studentRepository.findByEmailContainingIgnoreCase(filter.getEmail()));
        }
        if (filter.getAgeGreaterThan() != null) {
            results.retainAll(studentRepository.findByAgeGreaterThan(filter.getAgeGreaterThan()));
        }
        if (filter.getAgeLessThan() != null) {
            results.retainAll(studentRepository.findByAgeLessThan(filter.getAgeLessThan()));
        }
        if (filter.getCourseName() != null && filter.getCourseGrade() != null) {
            results.retainAll(studentRepository.findByEnrollmentsCourseNameAndGrade(filter.getCourseName(), filter.getCourseGrade()));
        } else if (filter.getCourseName() != null) {
            results.retainAll(studentRepository.findByEnrollmentsCourseNameContainingIgnoreCase(filter.getCourseName()));
        } else if (filter.getCourseGrade() != null) {
            results.retainAll(studentRepository.findByEnrollmentsGrade(filter.getCourseGrade()));
        }
        if (filter.getEnrollmentsCountGreaterThan() != null && filter.getEnrollmentsCountLessThan() != null) {
            results.retainAll(studentRepository.findByEnrollmentsSizeBetween(
                    filter.getEnrollmentsCountGreaterThan(),
                    filter.getEnrollmentsCountLessThan()
            ));
        }

        return results;
    }
}



