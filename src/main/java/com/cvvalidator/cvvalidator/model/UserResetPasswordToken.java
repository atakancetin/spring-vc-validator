package com.cvvalidator.cvvalidator.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity

@Table(name = "user_reset_password_token")
public class UserResetPasswordToken {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
   
    @Column(nullable = false)
    private String token;
 
    private Date expiration;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public UserResetPasswordToken(String token, Date expiration, User user) 
    {
        this.token = token;
        this.expiration = expiration;
        this.user =  user;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}