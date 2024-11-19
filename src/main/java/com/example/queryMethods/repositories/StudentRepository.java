package com.example.queryMethods.repositories;

import com.example.queryMethods.models.entity.Grade;
import com.example.queryMethods.models.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    List<Student> findByEnrollmentsNotNull();

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByEmailContainingIgnoreCase(String email);

    List<Student> findByAgeGreaterThan(Integer age);

    List<Student> findByAgeLessThan(Integer age);

    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.name LIKE %:courseName%")
    List<Student> findByEnrollmentsCourseNameContainingIgnoreCase(@Param("courseName") String courseName);

    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.grade = :grade")
    List<Student> findByEnrollmentsGrade(@Param("grade") Grade grade);

    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.name LIKE %:courseName% AND e.grade = :grade")
    List<Student> findByEnrollmentsCourseNameAndGrade(@Param("courseName") String courseName, @Param("grade") Grade grade);

    @Query("SELECT s FROM Student s WHERE SIZE(s.enrollments) > :minEnrollments AND SIZE(s.enrollments) < :maxEnrollments")
    List<Student> findByEnrollmentsSizeBetween(@Param("minEnrollments") int minEnrollments, @Param("maxEnrollments") int maxEnrollments);
}

