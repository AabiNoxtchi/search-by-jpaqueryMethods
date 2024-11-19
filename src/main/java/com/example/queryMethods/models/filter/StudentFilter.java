package com.example.queryMethods.models.filter;

import com.example.queryMethods.models.entity.Grade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFilter {

    private Long id;

    private String name;
    private String email;

    private Integer ageGreaterThan;
    private Integer ageLessThan;

    private Integer enrollmentsCountGreaterThan;
    private Integer enrollmentsCountLessThan;

    private String courseName;
    private Grade courseGrade;

}
