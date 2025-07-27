import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.DTO.UserDTO;
import com.cdac.service.AdminService;
import com.cdac.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    // ✅ Get all INDIVIDUAL users only
    @GetMapping("/users")
    public ResponseEntity<Collection<UserDTO>> getAllIndividualUsers() {
        Collection<UserDTO> users = userService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().name().equals("ROLE_INDIVIDUAL"))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // ✅ Delete user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
