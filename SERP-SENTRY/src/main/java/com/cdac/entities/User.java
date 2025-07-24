package com.cdac.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
/*
 * User - class
id : Long
first name , last name, email ,password - String
dob - LocalDate
role : Enum 
image : byte[]
 */
/*
 * Mandatory annotation for hibernate to manage entity 
 * @Entity - class level 
 * @Id - for PK (unique id property - typically - ref type for easy null 
 * checking) - field level | getter level
 */
//JPA package
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity // mandatory
@Table(name = "users") // name of the table
@NoArgsConstructor
@Getter
@Setter
@ToString( callSuper = true,exclude = {"projects", "reports", "seoAnalyses"})
public class User extends Base implements UserDetails{

	    @Column(name = "user_name",nullable = false, unique = true, length = 20)
	    private String username;

	    @Column(length = 30,nullable = false, unique = true)
	    private String email;

	    @Column(length = 255,nullable = false)
	    private String password; // store hashed password

	    @Enumerated(EnumType.STRING)
	    @Column(name = "user_role" , nullable = false)
	    private UserRole role;

	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Project> projects = new ArrayList<>();;

	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Report> reports= new ArrayList<>();;

	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<SEOAnalysis> seoAnalyses= new ArrayList<>();;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
	
	
}
