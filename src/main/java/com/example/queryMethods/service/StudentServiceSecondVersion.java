package com.example.queryMethods.service;

import com.example.queryMethods.models.entity.Student;
import com.example.queryMethods.models.filter.StudentFilter;
import com.example.queryMethods.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class StudentServiceSecondVersion {

    private final StudentRepository studentRepository;

    public List<Student> findAll(StudentFilter filter) {
        if (filter == null) {
            return studentRepository.findAll(); // Return all students if no filter
        }

        // Dynamically build queries
        if (isOnlyOneFilterProvided(filter)) {
            return querySingleCriteria(filter); // Optimize for single filter
        }

        return queryMultipleCriteria(filter);
    }

    private boolean isOnlyOneFilterProvided(StudentFilter filter) {
        long count = Stream.of(
                filter.getName(), filter.getEmail(), filter.getAgeGreaterThan(), filter.getAgeLessThan(),
                filter.getCourseName(), filter.getCourseGrade(), filter.getEnrollmentsCountGreaterThan(),
                filter.getEnrollmentsCountLessThan()
        ).filter(Objects::nonNull).count();
        return count == 1;
    }

    private List<Student> querySingleCriteria(StudentFilter filter) {
        if (filter.getName() != null) {
            return studentRepository.findByNameContainingIgnoreCase(filter.getName());
        }
        if (filter.getEmail() != null) {
            return studentRepository.findByEmailContainingIgnoreCase(filter.getEmail());
        }
        if (filter.getAgeGreaterThan() != null) {
            return studentRepository.findByAgeGreaterThan(filter.getAgeGreaterThan());
        }
        if (filter.getAgeLessThan() != null) {
            return studentRepository.findByAgeLessThan(filter.getAgeLessThan());
        }
        if (filter.getCourseName() != null && filter.getCourseGrade() != null) {
            return studentRepository.findByEnrollmentsCourseNameAndGrade(filter.getCourseName(), filter.getCourseGrade());
        }
        if (filter.getCourseName() != null) {
            return studentRepository.findByEnrollmentsCourseNameContainingIgnoreCase(filter.getCourseName());
        }
        if (filter.getCourseGrade() != null) {
            return studentRepository.findByEnrollmentsGrade(filter.getCourseGrade());
        }
        if (filter.getEnrollmentsCountGreaterThan() != null && filter.getEnrollmentsCountLessThan() != null) {
            return studentRepository.findByEnrollmentsSizeBetween(
                    filter.getEnrollmentsCountGreaterThan(),
                    filter.getEnrollmentsCountLessThan()
            );
        }

        return studentRepository.findAll(); // Default case
    }

    private List<Student> queryMultipleCriteria(StudentFilter filter) {
        List<Student> results = studentRepository.findByEnrollmentsNotNull();// fetch with all associations

        // Apply filters dynamically
        if (filter.getName() != null) {
            results = results.stream()
                    .filter(student -> student.getName().toLowerCase().contains(filter.getName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (filter.getEmail() != null) {
            results = results.stream()
                    .filter(student -> student.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (filter.getAgeGreaterThan() != null) {
            results = results.stream()
                    .filter(student -> student.getAge() > filter.getAgeGreaterThan())
                    .collect(Collectors.toList());
        }
        if (filter.getAgeLessThan() != null) {
            results = results.stream()
                    .filter(student -> student.getAge() < filter.getAgeLessThan())
                    .collect(Collectors.toList());
        }
        if (filter.getEnrollmentsCountGreaterThan() != null) {
            results = results.stream()
                    .filter(student -> student.getEnrollments().size() > filter.getEnrollmentsCountGreaterThan())
                    .collect(Collectors.toList());
        }
        if (filter.getEnrollmentsCountLessThan() != null) {
            results = results.stream()
                    .filter(student -> student.getEnrollments().size() < filter.getEnrollmentsCountLessThan())
                    .collect(Collectors.toList());
        }
        if (filter.getCourseName() != null && filter.getCourseGrade() != null) {
            results = results.stream()
                    .filter(student -> student.getEnrollments().stream()
                            .anyMatch(enrollment ->
                                    enrollment.getCourse().getName().toLowerCase()
                                            .contains(filter.getCourseName().toLowerCase())
                                    && filter.getCourseGrade().equals(enrollment.getGrade())))
                    .collect(Collectors.toList());
        } else if (filter.getCourseName() != null) {
            results = results.stream()
                    .filter(student -> student.getEnrollments().stream()
                            .anyMatch(enrollment -> enrollment.getCourse().getName().toLowerCase().contains(filter.getCourseName().toLowerCase())))
                    .collect(Collectors.toList());
        }
        else if (filter.getCourseGrade() != null) {
            results = results.stream()
                    .filter(student -> student.getEnrollments().stream()
                            .anyMatch(enrollment -> filter.getCourseGrade().equals(enrollment.getGrade())))
                    .collect(Collectors.toList());
        }

        return results;
    }
}
