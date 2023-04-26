package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Basic
    @Column(name = "public_id")
    private String publicId;
    @Basic
    @Column
    private Date date;
    @OneToOne
    @JoinColumn(name="userSkillId",nullable = false)
    private UserSkill userSkill;

    public Certificate() {
    }

    public Certificate(UserSkill userSkill) {
        UUID uuid = UUID.randomUUID();
        this.publicId = uuid.toString();
        this.date = new Date();
        this.userSkill = userSkill;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    @JsonIgnore
    public UserSkill getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(UserSkill userSkill) {
        this.userSkill = userSkill;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
