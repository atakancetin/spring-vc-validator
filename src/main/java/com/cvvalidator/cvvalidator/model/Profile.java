package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthDate")
    private Date birthDate;
    @Column(name = "country")
    private String  country;
    @Column(name = "city")
    private String  city;
    @Lob
    @Column(name = "about")
    private String  about;
    @Column(name = "linkedinUrl")
    private String  linkedinUrl;
    @Column(name = "githubUrl")
    private String  githubUrl;
    @Column(name="image",columnDefinition="LONGBLOB")
    @Lob
    private byte[] image;
    @JsonIgnore
    @OneToOne(mappedBy = "profile")
    private User user;
    @OneToMany(mappedBy = "profile")
    @JsonIgnore
    private List<University> universityList;
    @OneToMany(mappedBy = "profile")
    @JsonIgnore
    private List<Experience> experienceList;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<University> getUniversityList() {
        return universityList;
    }

    public void setUniversityList(List<University> universityList) {
        this.universityList = universityList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
}
