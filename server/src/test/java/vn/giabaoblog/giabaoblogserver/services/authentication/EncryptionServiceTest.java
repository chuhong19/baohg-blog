package vn.giabaoblog.giabaoblogserver.services.authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vn.giabaoblog.giabaoblogserver.config.exception.InvalidConfigurationException;

public class EncryptionServiceTest {

    @Test
    void generateSalt_ShouldReturnCorrectLength() {
        // Given
        int length = 16;

        // When
        String salt = EncryptionService.generateSalt(length);

        // Then
        assertNotNull(salt);
        assertEquals(length, salt.length());
    }

    @Test
    void generateSalt_ShouldReturnDifferentValuesForDifferentCalls() {
        // Given
        int length = 16;

        // When
        String salt1 = EncryptionService.generateSalt(length);
        String salt2 = EncryptionService.generateSalt(length);

        // Then
        assertNotEquals(salt1, salt2);
    }

    @Test
    void hash_ShouldReturnNonNullValue() {
        // Given
        String password = "password123";
        byte[] salt = EncryptionService.generateSalt(16).getBytes();

        // When
        byte[] hashedPassword = EncryptionService.hash(password.toCharArray(), salt);

        // Then
        assertNotNull(hashedPassword);
    }

    @Test
    void hash_ShouldReturnDifferentHashesForDifferentSalts() {
        // Given
        String password = "password123";
        byte[] salt1 = EncryptionService.generateSalt(16).getBytes();
        byte[] salt2 = EncryptionService.generateSalt(16).getBytes();

        // When
        byte[] hash1 = EncryptionService.hash(password.toCharArray(), salt1);
        byte[] hash2 = EncryptionService.hash(password.toCharArray(), salt2);

        // Then
        assertNotEquals(hash1, hash2);
    }

    @Test
    void encrypt_ShouldReturnNonNullValue() {
        // Given
        String password = "password123";
        String salt = EncryptionService.generateSalt(16);

        // When
        String encryptedPassword = EncryptionService.encrypt(password, salt);

        // Then
        assertNotNull(encryptedPassword);
    }

    @Test
    void encrypt_ShouldThrowExceptionWhenSaltIsNull() {
        // Given
        String password = "password123";

        // When & Then
        assertThrows(InvalidConfigurationException.class, () ->
                EncryptionService.encrypt(password, null)
        );
    }

    @Test
    void encrypt_ShouldThrowExceptionWhenSaltIsBlank() {
        // Given
        String password = "password123";
        String salt = "";

        // When & Then
        assertThrows(InvalidConfigurationException.class, () ->
                EncryptionService.encrypt(password, salt)
        );
    }

    @Test
    void isPasswordValid_ShouldReturnTrueForMatchingPasswords() {
        // Given
        String password = "password123";
        String salt = EncryptionService.generateSalt(16);
        String encryptedPassword = EncryptionService.encrypt(password, salt);

        // When
        boolean isValid = EncryptionService.isPasswordValid(password, encryptedPassword, salt);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isPasswordValid_ShouldReturnFalseForNonMatchingPasswords() {
        // Given
        String password = "password123";
        String wrongPassword = "wrongPassword";
        String salt = EncryptionService.generateSalt(16);
        String encryptedPassword = EncryptionService.encrypt(password, salt);

        // When
        boolean isValid = EncryptionService.isPasswordValid(wrongPassword, encryptedPassword, salt);

        // Then
        assertFalse(isValid);
    }


}
