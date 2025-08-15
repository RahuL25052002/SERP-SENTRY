package com.cdac.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
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
@ToString(callSuper = true, exclude = {"projects", "seoAnalyses"})
public class User extends Base implements UserDetails {

    @Column(name = "user_name", nullable = false, unique = true, length = 20)
    private String userName;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password; // store hashed password
    
    
    // for soft delete
    @Column(name = "is_deleted")
    private boolean deleted = false;
    
    
   

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SEOAnalysis> seoAnalyses = new ArrayList<>();

    // Explicitly define getUserName() method to ensure it works
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        // This is for Spring Security - returns email as username
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}