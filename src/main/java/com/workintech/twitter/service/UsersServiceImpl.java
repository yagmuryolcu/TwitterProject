package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Roles;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.exception.UsersNotFoundException;
import com.workintech.twitter.mapper.UsersMapper;
import com.workintech.twitter.repository.RolesRepository;
import com.workintech.twitter.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final UsersMapper usersMapper;





    @Override
    public List<UsersResponseDto> getAll() {
        return usersRepository
                .findAll()
                .stream()
                .map(usersMapper::toResponseDto)
                .toList();
    }

    @Override
    public UsersResponseDto findById(Long id) {

        Optional<Users> optionalUser = usersRepository
                .findById(id);

        if(optionalUser.isPresent()){

            Users user = optionalUser.get();

            return usersMapper.toResponseDto(user);
        }

        throw new UsersNotFoundException(id + " id'li kullanıcı bulunamadı");
    }

    @Override
    public UsersResponseDto create(UsersRequestDto userRequestDto) {

        Users users = usersMapper.toEntity(userRequestDto);
        users= usersRepository.save(users);
        return usersMapper.toResponseDto(users);
    }

    @Override
    public UsersResponseDto replaceOrCreate(Long id, UsersRequestDto userRequestDto) {
        Optional<Users> optionalUser = usersRepository.findById(id);

        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            existingUser.setUsername(userRequestDto.username());
            existingUser.setEmail(userRequestDto.email());
            existingUser.setPassword(userRequestDto.password());
            existingUser.setFullName(userRequestDto.fullName());
            existingUser.setBio(userRequestDto.bio());

            usersRepository.save(existingUser);
            return usersMapper.toResponseDto(existingUser);
        } else {

            Users newUser = usersMapper.toEntity(userRequestDto);
            usersRepository.save(newUser);
            return usersMapper.toResponseDto(newUser);
        }
    }
    @Override
    public UsersResponseDto update(Long id, UsersPatchRequestDto userPatchRequestDto) {
        Users userToUpdate = usersRepository
                .findById(id)
                .orElseThrow(()-> new UsersNotFoundException(id + " id'li kullanici bulunamadi"));

        userToUpdate = usersMapper.updateEntity(userToUpdate, userPatchRequestDto);

        usersRepository.save(userToUpdate);

        return usersMapper.toResponseDto(userToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        usersRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Roles role = rolesRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        // Zaten bu role sahip mi kontrol et
        if (user.getRoles().contains(role)) {
            throw new RuntimeException("User already has this role!");
        }

        user.addRole(role);
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, String roleName) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Roles role = rolesRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        // En az bir rol kalmalı
        if (user.getRoles().size() <= 1) {
            throw new RuntimeException("User must have at least one role!");
        }

        user.removeRole(role);
        usersRepository.save(user);
    }


    @Override
    public UsersResponseDto getUserRoles(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return usersMapper.toResponseDto(user);
    }

}
