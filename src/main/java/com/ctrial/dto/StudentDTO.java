package com.ctrial.dto;

import java.util.Set;

public class StudentDTO {
    private Long id;
    private String name;
    private String address;
    private String username;
    private Set<SubjectDTO> subjects; // You can exclude subjects if not needed.

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Set<SubjectDTO> getSubjects() { return subjects; }
    public void setSubjects(Set<SubjectDTO> subjects) { this.subjects = subjects; }
}
