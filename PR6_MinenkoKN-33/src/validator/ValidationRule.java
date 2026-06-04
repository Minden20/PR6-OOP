package validator;

public interface ValidationRule {
    boolean isValid(String value);
    String getErrorMessage();
}
