package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Roles;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.mapper.UsersMapper;
import com.workintech.twitter.repository.RolesRepository;
import com.workintech.twitter.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private UsersServiceImpl usersService;

    private Users testUser;
    private UsersRequestDto requestDto;
    private UsersResponseDto responseDto;
    private UsersPatchRequestDto patchRequestDto;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setUsername("turanata");
        testUser.setEmail("turan@hotmail.com");
        testUser.setPassword("encodedPassword123");
        testUser.setFullName("Turan Ata");
        testUser.setBio("Test bio");
        testUser.setCreatedAt(LocalDateTime.now());

        Roles userRole = new Roles();
        userRole.setId(1L);
        userRole.setName("USER");
        testUser.setRoles(new HashSet<>(Set.of(userRole)));

        requestDto = new UsersRequestDto(
                "turanata",
                "turan@hotmail.com",
                "1234",
                "Turan Ata",
                "Test bio"
        );

        responseDto = new UsersResponseDto(
                1L,
                "turanata",
                "turan@hotmail.com",
                "Turan Ata",
                "Test bio",
                LocalDateTime.now(),
                Set.of("USER")
        );

        patchRequestDto = new UsersPatchRequestDto(
                "newusername",
                "newemail@test.com",
                "newpass",
                "New Full Name",
                "New bio"
        );
    }

    @Test
    void getAll() {
        // Given
        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("Asya");

        List<Users> users = Arrays.asList(testUser, user2);

        UsersResponseDto responseDto2 = new UsersResponseDto(
                2L, "Asya", "asya@test.com", "Asya Asan", "Asya Bio", LocalDateTime.now(), Set.of("USER")
        );

        when(usersRepository.findAll()).thenReturn(users);
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);
        when(usersMapper.toResponseDto(user2)).thenReturn(responseDto2);

        // When
        List<UsersResponseDto> result = usersService.getAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(responseDto, responseDto2);
        verify(usersRepository, times(1)).findAll();
        verify(usersMapper, times(2)).toResponseDto(any(Users.class));
    }

    @Test
    void findById() {
        // Given
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);

        // When
        UsersResponseDto result = usersService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("turanata");
        verify(usersRepository, times(1)).findById(1L);
        verify(usersMapper, times(1)).toResponseDto(testUser);
    }

    @Test
    void create() {
        // Given
        when(usersMapper.toEntity(requestDto)).thenReturn(testUser);
        when(usersRepository.save(testUser)).thenReturn(testUser);
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);

        // When
        UsersResponseDto result = usersService.create(requestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("turanata");
        verify(usersMapper, times(1)).toEntity(requestDto);
        verify(usersRepository, times(1)).save(testUser);
        verify(usersMapper, times(1)).toResponseDto(testUser);
    }

    @Test
    void replaceOrCreate() {
        UsersRequestDto updateDto = new UsersRequestDto(
                "updateduser",
                "updated@test.com",
                "newpass",
                "Updated Name",
                "Updated bio"
        );

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usersRepository.save(testUser)).thenReturn(testUser);
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);

        UsersResponseDto result = usersService.replaceOrCreate(1L, updateDto);

        assertThat(result).isNotNull();
        verify(usersRepository, times(1)).findById(1L);
        verify(usersRepository, times(1)).save(testUser);
        verify(usersMapper, times(1)).toResponseDto(testUser);
        verify(usersMapper, never()).toEntity(any());

        // Test 2: Kullanıcı yok, yeni oluşturmalı
        reset(usersRepository, usersMapper);

        when(usersRepository.findById(999L)).thenReturn(Optional.empty());
        when(usersMapper.toEntity(requestDto)).thenReturn(testUser);
        when(usersRepository.save(testUser)).thenReturn(testUser);
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);

        UsersResponseDto result2 = usersService.replaceOrCreate(999L, requestDto);

        assertThat(result2).isNotNull();
        verify(usersRepository, times(1)).findById(999L);
        verify(usersMapper, times(1)).toEntity(requestDto);
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    void update() {

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usersMapper.updateEntity(testUser, patchRequestDto)).thenReturn(testUser);
        when(usersRepository.save(testUser)).thenReturn(testUser);
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);
        UsersResponseDto result = usersService.update(1L, patchRequestDto);

        assertThat(result).isNotNull();
        verify(usersRepository, times(1)).findById(1L);
        verify(usersMapper, times(1)).updateEntity(testUser, patchRequestDto);
        verify(usersRepository, times(1)).save(testUser);
        verify(usersMapper, times(1)).toResponseDto(testUser);
    }

    @Test
    void deleteById() {

        doNothing().when(usersRepository).deleteById(1L);
        usersService.deleteById(1L);
        verify(usersRepository, times(1)).deleteById(1L);
    }

    @Test
    void assignRoleToUser() {

        Roles adminRole = new Roles();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");
        adminRole.setUsers(new HashSet<>());

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(rolesRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(usersRepository.save(testUser)).thenReturn(testUser);

        usersService.assignRoleToUser(1L, "ADMIN");

        verify(usersRepository, times(1)).findById(1L);
        verify(rolesRepository, times(1)).findByName("ADMIN");
        verify(usersRepository, times(1)).save(testUser);

        // Test 2: Aynı rol varsa exception fırlatmalı
        reset(usersRepository, rolesRepository);

        Roles userRole = new Roles();
        userRole.setId(1L);
        userRole.setName("USER");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(rolesRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        assertThatThrownBy(() -> usersService.assignRoleToUser(1L, "USER"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User already has this role!");

        verify(usersRepository, never()).save(any());
    }

    @Test
    void removeRoleFromUser() {
        // Test 1: Rol kaldırma başarılı olmalı (birden fazla rol varken)
        Roles userRole = new Roles();
        userRole.setId(1L);
        userRole.setName("USER");
        userRole.setUsers(new HashSet<>(Set.of(testUser)));

        Roles adminRole = new Roles();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");

        testUser.setRoles(new HashSet<>(Set.of(userRole, adminRole)));

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(rolesRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(usersRepository.save(testUser)).thenReturn(testUser);

        usersService.removeRoleFromUser(1L, "ADMIN");

        verify(usersRepository, times(1)).findById(1L);
        verify(rolesRepository, times(1)).findByName("ADMIN");
        verify(usersRepository, times(1)).save(testUser);

        // Test 2: Tek rol varsa exception fırlatmalı
        reset(usersRepository, rolesRepository);

        testUser.setRoles(new HashSet<>(Set.of(userRole)));

        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(rolesRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        assertThatThrownBy(() -> usersService.removeRoleFromUser(1L, "USER"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User must have at least one role!");

        verify(usersRepository, never()).save(any());
    }

    @Test
    void getUserRoles() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usersMapper.toResponseDto(testUser)).thenReturn(responseDto);

        UsersResponseDto result = usersService.getUserRoles(1L);

        assertThat(result).isNotNull();
        assertThat(result.roles()).containsExactly("USER");
        verify(usersRepository, times(1)).findById(1L);
        verify(usersMapper, times(1)).toResponseDto(testUser);
    }
}