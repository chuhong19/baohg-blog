package vn.giabaoblog.giabaoblogserver.services.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.InvalidUserDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PasswordValidatorService {
    private static final int MAX_PASSWORD_LENGTH = 60;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$";

    private Pattern pattern;

    public PasswordValidatorService() {
        pattern = Pattern.compile(PASSWORD_REGEX);
    }

    public void checkPassword(String password) {
            if (password == null || Strings.isEmpty(password)) {
                throw new InvalidUserDataException("Password cannot be null or empty");
            }

            if (password.length() > MAX_PASSWORD_LENGTH) {
                throw new InvalidUserDataException(String.format("Password is too long: max number of chars is %s",
                        MAX_PASSWORD_LENGTH));
            }

            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                throw new InvalidUserDataException("Password must to be at least 8 chars, 1 number, 1 upper case," +
                        " 1 lower case letter, 1 special char, no spaces");
            }
    }

}
