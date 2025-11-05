package com.workintech.twitter.controller;

import com.workintech.twitter.dto.patchrequest.RolesPatchRequestDto;
import com.workintech.twitter.dto.request.RolesRequestDto;
import com.workintech.twitter.dto.response.RolesResponseDto;
import com.workintech.twitter.service.RolesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolesController {

    @Autowired
    private final RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RolesResponseDto>> getAllRoles() {
        return ResponseEntity.ok(rolesService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesResponseDto> getRoleById(@Positive @Min(1) @PathVariable Long id) {
        return ResponseEntity.ok(rolesService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RolesResponseDto> getRoleByName( @PathVariable String name) {
        return ResponseEntity.ok(rolesService.findByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolesResponseDto> createRole(@Validated @RequestBody RolesRequestDto rolesRequestDto) {
        RolesResponseDto createdRole = rolesService.create(rolesRequestDto);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolesResponseDto> replaceOrCreateRole(
            @Positive @PathVariable Long id,
            @Validated @RequestBody RolesRequestDto rolesRequestDto) {
        RolesResponseDto role = rolesService.replaceOrCreate(id, rolesRequestDto);
        return ResponseEntity.ok(role);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RolesResponseDto> updateRole(
            @Positive @PathVariable Long id,
            @Validated @RequestBody RolesPatchRequestDto rolesPatchRequestDto) {
        RolesResponseDto updatedRole = rolesService.update(id, rolesPatchRequestDto);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@Positive @PathVariable Long id) {
        rolesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> assignRoleToUser(
            @Positive @PathVariable Long roleId,
            @Positive @PathVariable Long userId) {
        rolesService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Void> removeRoleFromUser(
            @Positive @PathVariable Long roleId,
            @Positive @PathVariable Long userId) {
        rolesService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

}
