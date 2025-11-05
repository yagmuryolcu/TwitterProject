package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Users Repository Testleri")
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private Users user;

    @BeforeEach
    void init() {
        usersRepository.deleteAll();

        user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setBio("Deneme amaçlı");

        usersRepository.save(user);
    }

    @AfterEach
    void cleanup() {
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("Kullanıcı adıyla bul - mevcutsa döndür")
    void findByUsername_exists() {
        Optional<Users> found = usersRepository.findByUsername("testuser");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Kullanıcı adıyla bul - yoksa boş döndür")
    void findByUsername_notExists() {
        Optional<Users> found = usersRepository.findByUsername("yok");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Email ile bul - varsa döndür")
    void findByEmail_exists() {
        Optional<Users> found = usersRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Email ile bul - yoksa boş döndür")
    void findByEmail_notExists() {
        Optional<Users> found = usersRepository.findByEmail("yok@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Kullanıcı adı veya email ile bul - kullanıcı adı doğruysa")
    void findByUsernameOrEmail_usernameCorrect() {
        Optional<Users> found = usersRepository.findByUsernameOrEmail("testuser", "yanlis@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Kullanıcı adı veya email ile bul - email doğruysa")
    void findByUsernameOrEmail_emailCorrect() {
        Optional<Users> found = usersRepository.findByUsernameOrEmail("yanlisuser", "test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Yeni kullanıcı kaydet")
    void saveUser() {
        Users newUser = new Users();
        newUser.setUsername("yeniuser");
        newUser.setEmail("yeni@example.com");
        newUser.setPassword("sifre123");
        newUser.setFullName("Yeni Kullanıcı");
        newUser.setBio("Test");

        Users saved = usersRepository.save(newUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("yeniuser");

        Optional<Users> fromDb = usersRepository.findById(saved.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getUsername()).isEqualTo("yeniuser");
    }

    @Test
    @DisplayName("Kullanıcı sil")
    void deleteUser() {
        usersRepository.delete(user);
        Optional<Users> found = usersRepository.findByUsername("testuser");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Kullanıcı güncelle")
    void updateUser() {
        user.setEmail("guncellendi@example.com");

        Users updated = usersRepository.save(user);

        assertThat(updated.getEmail()).isEqualTo("guncellendi@example.com");

        Optional<Users> fromDb = usersRepository.findByUsername("testuser");
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getEmail()).isEqualTo("guncellendi@example.com");
    }
}
