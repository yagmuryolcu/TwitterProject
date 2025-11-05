package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.RolesPatchRequestDto;
import com.workintech.twitter.dto.request.RolesRequestDto;
import com.workintech.twitter.dto.response.RolesResponseDto;
import com.workintech.twitter.entity.Roles;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.RolesNotFoundException;
import com.workintech.twitter.exception.UsersNotFoundException;
import com.workintech.twitter.mapper.RolesMapper;
import com.workintech.twitter.repository.RolesRepository;
import com.workintech.twitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService{

    @Autowired
    private final RolesRepository rolesRepository;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final RolesMapper rolesMapper;


    @Override
    public List<RolesResponseDto> getAll() {
        return rolesRepository.findAll()
                .stream()
                .map(rolesMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public RolesResponseDto findById(Long id) {
        Roles roles = rolesRepository.findById(id)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with id: " + id));
        return rolesMapper.toResponseDto(roles);
    }

    @Override
    public RolesResponseDto findByName(String name) {
        Roles roles = rolesRepository.findByName(name)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with name: " + name));
        return rolesMapper.toResponseDto(roles);
    }

    @Override
    @Transactional
    public RolesResponseDto create(RolesRequestDto rolesRequestDto) {
        Optional<Roles> existingRole = rolesRepository.findByName(rolesRequestDto.name());
        if (existingRole.isPresent()) {
            throw new RuntimeException("Role already exists with name: " + rolesRequestDto.name());
        }

        Roles roles = rolesMapper.toEntity(rolesRequestDto);
        Roles savedRoles = rolesRepository.save(roles);
        return rolesMapper.toResponseDto(savedRoles);
    }

    @Override
    @Transactional
    public RolesResponseDto replaceOrCreate(Long id, RolesRequestDto rolesRequestDto) {
        Optional<Roles> existingRole = rolesRepository.findById(id);

        if (existingRole.isPresent()) {
            // ID varsa, güncelle
            Roles roleToUpdate = existingRole.get();
            roleToUpdate.setName(rolesRequestDto.name());
            Roles updatedRole = rolesRepository.save(roleToUpdate);
            return rolesMapper.toResponseDto(updatedRole);
        } else {
            // yoksa yeni oluştur
            Roles roles = rolesMapper.toEntity(rolesRequestDto);
            Roles savedRoles = rolesRepository.save(roles);
            return rolesMapper.toResponseDto(savedRoles);
        }
    }

    @Override
    @Transactional
    public RolesResponseDto update(Long id, RolesPatchRequestDto rolesPatchRequestDto) {
        Roles existingRole = rolesRepository.findById(id)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with id: " + id));

        if (rolesPatchRequestDto.name() != null && !rolesPatchRequestDto.name().isBlank()) {
            // Aynı isimde başka rol var mı kontrol et
            Optional<Roles> roleWithSameName = rolesRepository.findByName(rolesPatchRequestDto.name());
            if (roleWithSameName.isPresent() && !roleWithSameName.get().getId().equals(id)) {
                throw new RolesNotFoundException("Role already exists with name: " + rolesPatchRequestDto.name());
            }
            existingRole.setName(rolesPatchRequestDto.name());
        }

        Roles updatedRole = rolesRepository.save(existingRole);
        return rolesMapper.toResponseDto(updatedRole);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Roles roles = rolesRepository.findById(id)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with id: " + id));
        rolesRepository.delete(roles);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersNotFoundException("User not found with id: " + userId));

        Roles role = rolesRepository.findById(roleId)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with id: " + roleId));

        user.addRole(role);
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersNotFoundException("User not found with id: " + userId));

        Roles role = rolesRepository.findById(roleId)
                .orElseThrow(() -> new RolesNotFoundException("Role not found with id: " + roleId));

        user.removeRole(role);
        usersRepository.save(user);
    }
}
