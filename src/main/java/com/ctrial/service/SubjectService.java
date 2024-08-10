package com.ctrial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctrial.entity.Subject;
import com.ctrial.repository.SubjectRepository;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	public Subject addSubject(Subject subject) {
		return subjectRepository.save(subject);
	}

	public List<Subject> getAllSubjects() {
		return subjectRepository.findAll();
	}

	public Subject getSubjectById(Long id) {
		return subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
	}

	public List<Subject> searchSubjectsByName(String name) {
		return subjectRepository.findByNameContainingIgnoreCase(name);
	}

	public List<Subject> getSubjectsByStudent(Long studentId) {
		return subjectRepository.findAllByStudentId(studentId);
	}

	public List<Subject> getAllSubjectsWithStudents() {
		return subjectRepository.findAllWithStudents();
	}
}