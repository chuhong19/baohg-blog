package vn.giabaoblog.giabaoblogserver.services.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.giabaoblog.giabaoblogserver.config.exception.InvalidUserDataException;
import vn.giabaoblog.giabaoblogserver.services.validation.PasswordValidatorService;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorServiceTest {

    private PasswordValidatorService passwordValidatorService;

    @BeforeEach
    void setUp() {
        passwordValidatorService = new PasswordValidatorService();
    }

    @Test
    void checkPassword_ShouldThrowException_WhenPasswordIsNull() {
        assertThrows(InvalidUserDataException.class, () -> passwordValidatorService.checkPassword(null),
                "Password cannot be null or empty");
    }

    @Test
    void checkPassword_ShouldThrowException_WhenPasswordIsEmpty() {
        assertThrows(InvalidUserDataException.class, () -> passwordValidatorService.checkPassword(""),
                "Password cannot be null or empty");
    }

    @Test
    void checkPassword_ShouldThrowException_WhenPasswordIsTooLong() {
        String longPassword = "aA1!" + "a".repeat(60);
        assertThrows(InvalidUserDataException.class, () -> passwordValidatorService.checkPassword(longPassword),
                String.format("Password is too long: max number of chars is %s", 60));
    }

    @Test
    void checkPassword_ShouldThrowException_WhenPasswordDoesNotMatchPattern() {
        String invalidPassword = "password";
        assertThrows(InvalidUserDataException.class, () -> passwordValidatorService.checkPassword(invalidPassword),
                "Password must to be at least 8 chars, 1 number, 1 upper case, 1 lower case letter, 1 special char, no spaces");
    }

    @Test
    void checkPassword_ShouldNotThrowException_WhenPasswordIsValid() {
        String validPassword = "Validpass1!";
        passwordValidatorService.checkPassword(validPassword); // no exception should be thrown
    }
}
