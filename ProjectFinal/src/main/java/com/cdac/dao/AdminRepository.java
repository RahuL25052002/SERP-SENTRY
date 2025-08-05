package com.cdac.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.entities.User;
import com.cdac.entities.UserRole;

public interface AdminRepository extends JpaRepository<User, Long> {
    
	List<User> findByRoleIn(List<UserRole> roles);
   
    Optional<User> findByEmail(String email);
    
    @Query("""
            SELECT new com.cdac.DTO.RecentUserProjectDTO(
                u.id, u.userName, u.email, p.name, p.domainUrl, p.updatedOn
            )
            FROM User u
            JOIN u.projects p
            WHERE p.updatedOn = (
                SELECT MAX(p2.updatedOn)
                FROM Project p2
                WHERE p2.user = u
            )
            ORDER BY p.updatedOn DESC
            """)
        List<RecentUserProjectDTO> findAllUsersWithRecentProjects();
} 