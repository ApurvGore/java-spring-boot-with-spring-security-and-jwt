package com.ctrial.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ctrial.entity.Student;
import com.ctrial.entity.User;
import com.ctrial.repository.StudentRepository;
import com.ctrial.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final StudentRepository studentRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			StudentRepository studentRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.studentRepository = studentRepository;
	}

	@Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = getUserByUsername(username);
		return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
				.password(user.getPassword()).roles(user.getRole().name()).build();
	}

	public Long getStudentIdFromUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		return studentRepository.findById(user.getId()).map(student -> student.getId())
				.orElseThrow(() -> new RuntimeException("Student not found"));
	}

	@Transactional
    public User registerStudentUser(String username, String password, String name, String address) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.STUDENT);

        Student student = new Student();
        student.setName(name);
        student.setAddress(address);
        student.setUser(user);
//        student.setUsername(username);

        user.setStudent(student);

        return userRepository.save(user);
    }
}