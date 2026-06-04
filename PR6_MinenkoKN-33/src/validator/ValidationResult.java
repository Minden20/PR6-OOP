package validator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {
    private final Map<String, List<String>> errors = new LinkedHashMap<>();

    public void addErrors(String fieldName, List<String> fieldErrors) {
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).addAll(fieldErrors);
        }
    }

    public void addError(String fieldName, String errorMessage) {
        errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
