package com.cdac.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.entities.User;
import com.cdac.entities.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
    
//    @Query("SELECT new com.cdac.DTO.RecentUserProjectDTO(u.id, u.userName, p.name, p.updatedOn) " +
//            "FROM User u JOIN u.projects p " +
//            "WHERE p.updatedOn = (SELECT MAX(p2.updatedOn) FROM Project p2 WHERE p2.user = u) " +
//            "AND p.updatedOn >= :date")
//     List<RecentUserProjectDTO> findUsersWithMostRecentProject(@Param("date") LocalDate date);
} 