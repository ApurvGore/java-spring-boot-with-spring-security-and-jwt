package com.ctrial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctrial.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.subjects WHERE s.id = :id")
	Optional<Student> findByIdWithSubjects(@Param("id") Long id);

	List<Student> findByNameContainingIgnoreCase(String name);

	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.subjects")
	List<Student> findAllWithSubjects();

	@Query("SELECT DISTINCT s FROM Student s " 
			+ "LEFT JOIN FETCH s.user u " 
			+ "LEFT JOIN FETCH s.subjects sub")
	List<Student> findAllWithUserAndSubjects();

	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.subjects WHERE s.id IN "
			+ "(SELECT st.id FROM Subject sub JOIN sub.students st WHERE sub.id = :subjectId)")
	List<Student> findAllBySubjectId(@Param("subjectId") Long subjectId);

	Optional<Student> findByUsername(String username);
}