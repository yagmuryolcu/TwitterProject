package com.workintech.twitter.service;

import com.workintech.twitter.dto.request.LoginRequestDto;
import com.workintech.twitter.dto.request.RegisterRequestDto;
import com.workintech.twitter.dto.response.LoginResponseDto;
import com.workintech.twitter.dto.response.RegisterResponseDto;
import com.workintech.twitter.entity.Roles;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.repository.RolesRepository;
import com.workintech.twitter.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {

        // Email kontrolü
        Optional<Users> existingUserByEmail = usersRepository.findByEmail(registerRequestDto.email());
        if (existingUserByEmail.isPresent()) {
            throw new RuntimeException("Email zaten kayıtlı");
        }

        // Username kontrolü
        Optional<Users> existingUserByUsername = usersRepository.findByUsername(registerRequestDto.username());
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("Kullanıcı adı çoktan alındı");
        }

        // Şifreyi encode et
        String encodedPassword = passwordEncoder.encode(registerRequestDto.password());

        // USER rolünü al
        Roles userRole = rolesRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER  rolü bulunamadı"));

        // Yeni kullanıcı oluştur
        Users user = new Users();
        user.setUsername(registerRequestDto.username());
        user.setEmail(registerRequestDto.email());
        user.setPassword(encodedPassword);
        user.setFullName(registerRequestDto.fullName());
        user.setBio(registerRequestDto.bio());
        user.addRole(userRole);

        Users savedUser = usersRepository.save(user);

        return new RegisterResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                "Kullanıcı başarıyla kaydedildi."
        );
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            // Authentication yap
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.usernameOrEmail(),
                            loginRequestDto.password()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return new LoginResponseDto(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    "Giriş işlemi başarılı."
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Geçersiz kullanıcı adı/ email veya şifre");
        }
    }
}
