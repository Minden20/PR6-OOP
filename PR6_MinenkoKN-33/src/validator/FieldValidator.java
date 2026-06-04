package validator;

import java.util.ArrayList;
import java.util.List;

public class FieldValidator {
    private final String fieldName;
    private final List<ValidationRule> rules = new ArrayList<>();

    public FieldValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValidator addRule(ValidationRule rule) {
        this.rules.add(rule);
        return this;
    }

    public List<String> validate(String value) {
        List<String> errors = new ArrayList<>();
        for (ValidationRule rule : rules) {
            if (!rule.isValid(value)) {
                errors.add(rule.getErrorMessage());
            }
        }
        return errors;
    }

    public String getFieldName() {
        return fieldName;
    }
}
