package com.ctrial.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctrial.dto.StudentDTO;
import com.ctrial.dto.StudentRegistrationRequest;
import com.ctrial.dto.SubjectDTO;
import com.ctrial.entity.Student;
import com.ctrial.entity.Subject;
import com.ctrial.entity.User;
import com.ctrial.service.StudentService;
import com.ctrial.service.SubjectService;
import com.ctrial.service.UserService;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private UserService userService;

	// working
	@PostMapping("/register")
	public ResponseEntity<?> registerStudent(@RequestBody StudentRegistrationRequest request) {
		User user = userService.registerStudentUser(request.getUsername(), request.getPassword(), request.getName(),
				request.getAddress());
		return ResponseEntity.ok(user);
	}

	// working -> but returns infinite loop-like Json -> resolved using
	// @JsonIgnoreProperties({ "user" }) // Ignore lazy-loaded user property
	@PostMapping("/enroll/{subjectId}")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> enrollStudentToSubject(@PathVariable Long subjectId, Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(studentService.enrollStudentToSubject(username, subjectId));
	}

	@GetMapping("/students")
	public ResponseEntity<List<StudentDTO>> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		List<StudentDTO> studentDTOs = students.stream().map(this::convertToDTO).collect(Collectors.toList());
		return ResponseEntity.ok(studentDTOs);
	}

	// working
	@GetMapping("/subjects")
	public ResponseEntity<Set<Subject>> getStudentSubjects(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(studentService.getStudentSubjects(username));
	}

	// working
	@GetMapping("/all-subjects")
	public ResponseEntity<List<Subject>> getAllSubjects() {
		return ResponseEntity.ok(subjectService.getAllSubjects());
	}

	// working
	@GetMapping("/subjects/{id}")
	public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
		return ResponseEntity.ok(subjectService.getSubjectById(id));
	}

	// working
	@GetMapping("/subjects/search")
	public ResponseEntity<List<Subject>> searchSubjectsByName(@RequestParam String name) {
		return ResponseEntity.ok(subjectService.searchSubjectsByName(name));
	}

	// working
	@GetMapping("/subjects/student/{studentId}")
	public ResponseEntity<List<Subject>> getSubjectsByStudent(@PathVariable Long studentId) {
		return ResponseEntity.ok(subjectService.getSubjectsByStudent(studentId));
	}

	public StudentDTO convertToDTO(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setId(student.getId());
		dto.setName(student.getName());
		dto.setAddress(student.getAddress());
		dto.setSubjects(student.getSubjects().stream().map(subject -> {
			SubjectDTO subjectDTO = new SubjectDTO();
			subjectDTO.setId(subject.getId());
			subjectDTO.setName(subject.getName());
			return subjectDTO;
		}).collect(Collectors.toSet()));
		return dto;
	}
}