package crocker.golf.bestball.core.service.user;

import com.google.common.base.Strings;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.user.UserCredentials;
import crocker.golf.bestball.domain.user.UserCredentialsDto;
import crocker.golf.bestball.domain.exceptions.user.*;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserRegistrationValidator {

    private UserRepository userRepository;

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+" +
            "(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*" +
            "(.[A-Za-z]{2,})$";

    public UserRegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateNewUser(UserCredentialsDto userCredentialsDto) throws RegistrationException {
        Map<String, Exception> exceptions = new HashMap<>();
        validateEmail(userCredentialsDto, exceptions);
        validateUsername(userCredentialsDto, exceptions);
        validateName(userCredentialsDto, exceptions);
        validatePassword(userCredentialsDto, exceptions);

        if(!exceptions.isEmpty()) {
            throw new RegistrationException("Registration error", exceptions);
        }
    }

    private void validateEmail(UserCredentialsDto userCredentialsDto, Map<String, Exception> exceptions) {
        if(isNullEmptyOrWhitespace(userCredentialsDto.getEmail())) {
            exceptions.put("email", new ValueNotProvidedException("An email is required"));
            return;
        }

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(userCredentialsDto.getEmail());
        if(!matcher.matches()) {
            exceptions.put("email", new EmailNotValidException("Please provide a valid email"));
        }

        UserCredentials userCredentials = userRepository.findByEmail(userCredentialsDto.getEmail());
        if (userCredentials != null) {
            exceptions.put("email", new EmailNotUniqueException("That email is already used by a different user"));
        }
    }

    private void validateUsername(UserCredentialsDto userCredentialsDto, Map<String, Exception> exceptions) {
        if(isNullEmptyOrWhitespace(userCredentialsDto.getUserName())) {
            exceptions.put("userName", new ValueNotProvidedException("A username is required"));
        }

        UserCredentials userCredentials = userRepository.findByUsername(userCredentialsDto.getUserName());
        if (userCredentials != null) {
            exceptions.put("userName", new UsernameNotUniqueException("That username is already used by a different user"));
        }
    }

    private void validateName(UserCredentialsDto userCredentialsDto, Map<String, Exception> exceptions) {
        if(isNullEmptyOrWhitespace(userCredentialsDto.getFirstName())) {
            exceptions.put("firstName", new ValueNotProvidedException("A first name is required"));
        }
        if(isNullEmptyOrWhitespace(userCredentialsDto.getLastName())) {
            exceptions.put("lastName", new ValueNotProvidedException("A last name is required"));
        }
    }

    private void validatePassword(UserCredentialsDto userCredentialsDto, Map<String, Exception> exceptions) {
        if(isNullEmptyOrWhitespace(userCredentialsDto.getPassword())) {
            exceptions.put("password", new ValueNotProvidedException("A password is required"));
        }
        if(isNullEmptyOrWhitespace(userCredentialsDto.getConfirmPassword())) {
            exceptions.put("confirmPassword", new ValueNotProvidedException("Please confirm your password"));
            return;
        }

        String password = userCredentialsDto.getPassword();

        if(password.length() < 1 || password.length() > 64) {
            exceptions.put("password", new PasswordNotValidException("Password must be between 8 to 64 characters long"));
        }

        if(!password.equals(userCredentialsDto.getConfirmPassword())) {
            exceptions.put("confirmPassword", new PasswordNotMatchException("The two passwords do not match"));
        }
    }

    private boolean isNullEmptyOrWhitespace(String value) {
        return Strings.isNullOrEmpty(value) || StringUtils.containsWhitespace(value);
    }

}
