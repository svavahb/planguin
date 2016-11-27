package project.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import project.persistence.entities.User;
import project.service.LoginService;

/**
 * Created by Svava on 16.11.16.
 */
public class UserValidator implements Validator {

    private LoginService loginService = new LoginService();

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        // Check whether username is empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Username should not be empty!");

        // Check whether username is too short/long
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Username should be between 4 and 32 characters!");
        }

        // Check whether username already exists
        if (loginService.usernameExists(user.getUsername())) {
            errors.rejectValue("username", "This username already exists!");
        }

        // Check whether password is empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Password should not be empty!");

        // Check whether password is too short/long
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            errors.rejectValue("password", "Password should be between 6 and 20 characters!");
        }

        // Check whether passwords match
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Passwords do not match!");
        }
    }
}
