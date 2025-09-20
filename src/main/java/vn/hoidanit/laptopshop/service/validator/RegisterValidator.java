package vn.hoidanit.laptopshop.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;

public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {

    @Override
    public boolean isValid(RegisterDTO user, ConstraintValidatorContext context) {
        boolean valid = true;

        // Check if password and confirm password are the same
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Password and Confirm Password must be the same")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Add validation here
        return valid;

    }

}
