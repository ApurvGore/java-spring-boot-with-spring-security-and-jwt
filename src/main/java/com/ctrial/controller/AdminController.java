package com.ctrial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctrial.dto.StudentRegistrationRequest;
import com.ctrial.entity.Student;
import com.ctrial.entity.Subject;
import com.ctrial.entity.User;
import com.ctrial.service.StudentService;
import com.ctrial.service.SubjectService;
import com.ctrial.service.UserService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private UserService userService;

	// working -> resolved infinite loop-like JSON using @JsonManagedReference and
	// @JsonBackReference
	@PostMapping("/students")
	public ResponseEntity<User> addStudent(@RequestBody StudentRegistrationRequest request) {
		User user = userService.registerStudentUser(request.getUsername(), request.getPassword(), request.getName(),
				request.getAddress());
		return ResponseEntity.ok(user);
	}

	// working
	@GetMapping("/students")
	public ResponseEntity<List<Student>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}

	// working
	@PostMapping("/subjects")
	public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
		return ResponseEntity.ok(subjectService.addSubject(subject));
	}

	// working
	@GetMapping("/subjects")
	public ResponseEntity<List<Subject>> getAllSubjects() {
		return ResponseEntity.ok(subjectService.getAllSubjects());
	}

	// working
	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
		return ResponseEntity.ok(studentService.getStudentById(id));
	}

	// working
	@GetMapping("/students/search")
	public ResponseEntity<List<Student>> searchStudentsByName(@RequestParam String name) {
		return ResponseEntity.ok(studentService.searchStudentsByName(name));
	}

	// working
	@GetMapping("/students/subject/{subjectId}")
	public ResponseEntity<List<Student>> getStudentsBySubject(@PathVariable Long subjectId) {
		return ResponseEntity.ok(studentService.getStudentsBySubject(subjectId));
	}

	// working
	@GetMapping("/subjects/students")
	public ResponseEntity<List<Subject>> getAllSubjectsWithStudents() {
		return ResponseEntity.ok(subjectService.getAllSubjectsWithStudents());
	}
}