package exception;

import validator.ValidationResult;

public class ValidationException extends IllegalArgumentException {
    private final ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        super("Помилка валідації вхідних даних");
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
