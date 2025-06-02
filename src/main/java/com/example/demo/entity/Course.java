package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("course_id")
    private Long course_id;

    @JsonProperty("course_name")
    private String course_name;

    @JsonProperty("price")
    private double price;

    @JsonProperty("instructor")
    private String instructor;

    @Lob
    private String description;

    private String p_link;  // No @JsonProperty here

    @JsonProperty("y_link")
    private String y_link;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Questions> questions;

    public Long getId() {
        return course_id;
    }

    public void setId(Long id) {
        this.course_id = id;
    }

    public String getCourseName() {
        return course_name;
    }

    public void setCourseName(String courseName) {
        this.course_name = courseName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTutor() {
        return instructor;
    }

    public void setTutor(String tutor) {
        this.instructor = tutor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("p_link")
    public String getPhoto() {
        return "http://localhost:8080/uploads/" + p_link;
    }

    public void setPhoto(String photo) {
        this.p_link = photo;
    }

    public String getVideo() {
        return y_link;
    }

    public void setVideo(String video) {
        this.y_link = video;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
