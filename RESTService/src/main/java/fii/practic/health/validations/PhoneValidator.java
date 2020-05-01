package fii.practic.health.validations;

import fii.practic.health.boundry.dto.PhoneNumberDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, PhoneNumberDTO> {

    @Override
    public boolean isValid(PhoneNumberDTO phoneNumberDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumberDTO == null || phoneNumberDTO.getPhoneNumber() == null) {
            return false;
        }
        String phoneNumber = phoneNumberDTO.getPhoneNumber();
        return phoneNumber.matches("\\+04\\d{10}");
    }
}
