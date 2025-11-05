package com.workintech.twitter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    private UsersRequestDto userRequestDto;
    private UsersResponseDto userResponseDto;
    private final Long TEST_ID = 1L;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
        userRequestDto = new UsersRequestDto(
                "testuser",
                "test@example.com",
                "password123",
                "Test User Name",
                "Deneme"
        );

        userResponseDto = new UsersResponseDto(
                TEST_ID,
                "testuser",
                "test@example.com",
                "Test User Name",
                "Deneme",
                LocalDateTime.now(),
                Set.of("USER")
        );
    }

    @Test
    void getAll() throws Exception {
        when(usersService.getAll()).thenReturn(List.of(userResponseDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("testuser"));

        verify(usersService, times(1)).getAll();
    }

    @Test
    void findById() throws Exception {
        when(usersService.findById(TEST_ID)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/{id}", TEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usersService, times(1)).findById(TEST_ID);
    }

    @Test
    void create() throws Exception {
        when(usersService.create(any(UsersRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(usersService, times(1)).create(any(UsersRequestDto.class));
    }

    @Test
    void replaceOrCreate() throws Exception {
        when(usersService.replaceOrCreate(eq(TEST_ID), any(UsersRequestDto.class)))
                .thenReturn(userResponseDto);

        mockMvc.perform(put("/users/{id}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.fullName").value("Test User Name"));

        verify(usersService, times(1)).replaceOrCreate(eq(TEST_ID), any(UsersRequestDto.class));
    }

    @Test
    void update() throws Exception {
        when(usersService.update(eq(TEST_ID), any())).thenReturn(userResponseDto);

        mockMvc.perform(patch("/users/{id}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID));

        verify(usersService, times(1)).update(eq(TEST_ID), any());
    }

    @Test
    void deleteById() throws Exception {
        doNothing().when(usersService).deleteById(TEST_ID);

        mockMvc.perform(delete("/users/{id}", TEST_ID))
                .andExpect(status().isNoContent());

        verify(usersService, times(1)).deleteById(TEST_ID);
    }
}