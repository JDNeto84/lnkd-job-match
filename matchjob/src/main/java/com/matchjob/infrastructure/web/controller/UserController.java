package com.matchjob.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchjob.infrastructure.web.dto.user.ChangePasswordRequest;
import com.matchjob.infrastructure.web.dto.user.UpdatePreferencesRequest;
import com.matchjob.infrastructure.web.dto.auth.MeResponse;
import com.matchjob.application.port.incoming.user.ChangePasswordUseCase;
import com.matchjob.application.port.incoming.user.GetCurrentUserUseCase;
import com.matchjob.application.port.incoming.user.UpdateUserPreferencesUseCase;
import com.matchjob.application.user.dto.ChangePasswordCommand;
import com.matchjob.application.user.dto.GetCurrentUserQuery;
import com.matchjob.application.user.dto.UpdateUserPreferencesCommand;
import com.matchjob.domain.user.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final GetCurrentUserUseCase getCurrentUser;
    private final UpdateUserPreferencesUseCase updateUserPreferences;
    private final ChangePasswordUseCase changePassword;

    public UserController(GetCurrentUserUseCase getCurrentUser,
                          UpdateUserPreferencesUseCase updateUserPreferences,
                          ChangePasswordUseCase changePassword) {
        this.getCurrentUser = getCurrentUser;
        this.updateUserPreferences = updateUserPreferences;
        this.changePassword = changePassword;
    }

    @PatchMapping("/change-preferences")
    public ResponseEntity<?> updatePreferences(@AuthenticationPrincipal UserDetails principal,
                                               @RequestBody UpdatePreferencesRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        var userOpt = getCurrentUser.execute(new GetCurrentUserQuery(principal.getUsername()));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        String location = request.location();
        location = (location == null || location.isBlank()) ? "Brasil" : location;
        User updated = updateUserPreferences.execute(new UpdateUserPreferencesCommand(
                userOpt.get().getId(),
                request.keyword(),
                location,
                request.remotePreferred()
        ));
        String role = "ROLE_" + updated.getRole().name();
        MeResponse response = new MeResponse(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                role,
                updated.getPlan().name()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails principal,
                                            @RequestBody ChangePasswordRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        var userOpt = getCurrentUser.execute(new GetCurrentUserQuery(principal.getUsername()));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        User updated = changePassword.execute(new ChangePasswordCommand(
                userOpt.get().getId(),
                request.currentPassword(),
                request.newPassword()
        ));
        return ResponseEntity.ok().build();
    }
}
