package com.ctrial.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctrial.dto.StudentDTO;
import com.ctrial.entity.Student;
import com.ctrial.entity.Subject;
import com.ctrial.repository.StudentRepository;
import com.ctrial.repository.SubjectRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private SubjectRepository subjectRepository;

	public Student addStudent(Student student) {
		return studentRepository.save(student);
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAllWithUserAndSubjects();
	}

	public Student getStudentById(Long id) {
		return studentRepository.findByIdWithSubjects(id)
				.orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
	}

	public Student getStudentByUsername(String username) {
		return studentRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Student not found with username: " + username));
	}

	public List<Student> searchStudentsByName(String name) {
		return studentRepository.findByNameContainingIgnoreCase(name);
	}

	public List<Student> getStudentsBySubject(Long subjectId) {
		return studentRepository.findAllBySubjectId(subjectId);
	}

	@Transactional
	public Student enrollStudentToSubject(String username, Long subjectId) {
		Student student = getStudentByUsername(username);
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new RuntimeException("Subject not found"));
		student.getSubjects().add(subject);
		return studentRepository.save(student);
	}

	public StudentDTO convertToDTO(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setId(student.getId());
		dto.setName(student.getName());
		dto.setAddress(student.getAddress());
//	    dto.setUsername(student.getUsername());
		// Map subjects if needed
//	    dto.setSubjects(student.getSubjects().stream()
//	        .map(subject -> {
//	            SubjectDTO subjectDTO = new SubjectDTO();
//	            subjectDTO.setId(subject.getId());
//	            subjectDTO.setName(subject.getName());
//	            return subjectDTO;
//	        })
//	        .collect(Collectors.toSet()));
		return dto;
	}

	public Set<Subject> getStudentSubjects(String username) {
		Student student = getStudentByUsername(username);
		return student.getSubjects();
	}
}