package com.spacedrop.user.mcp;

import com.spacedrop.user.model.User;
import com.spacedrop.user.service.UserService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMcpTools {

    private final UserService userService;

    public UserMcpTools(UserService userService) {
        this.userService = userService;
    }

    @Tool(description = "Get user details by their unique ID.")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfo getUserById(
            @ToolParam(description = "The unique user ID") Long userId) {
        return userService.getUserById(userId)
                .map(this::toInfo)
                .orElse(null);
    }

    @Tool(description = "Get user details by their email address.")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfo getUserByEmail(
            @ToolParam(description = "The user's email address") String email) {
        return userService.getUserByEmail(email)
                .map(this::toInfo)
                .orElse(null);
    }

    @Tool(description = "Get all users in the system. Use with caution as this may return many results.")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfo> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Get all users with a specific role (USER, OWNER, or ADMIN).")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfo> getUsersByRole(
            @ToolParam(description = "The role to filter by: USER, OWNER, or ADMIN") String role) {
        return userService.getAllUsers().stream()
                .filter(user -> user.getRole().name().equalsIgnoreCase(role))
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Check if a user exists by their email address.")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean userExistsByEmail(
            @ToolParam(description = "The email address to check") String email) {
        return userService.getUserByEmail(email).isPresent();
    }

    private UserInfo toInfo(User user) {
        return new UserInfo(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public record UserInfo(
            Long id,
            String firstName,
            String lastName,
            String email,
            String role
    ) {}
}
