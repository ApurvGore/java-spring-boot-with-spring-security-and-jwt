package com.ctrial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctrial.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    List<Subject> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.students")
    List<Subject> findAllWithStudents();

    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.students WHERE s.id IN " +
           "(SELECT sub.id FROM Student st JOIN st.subjects sub WHERE st.id = :studentId)")
    List<Subject> findAllByStudentId(@Param("studentId") Long studentId);
}