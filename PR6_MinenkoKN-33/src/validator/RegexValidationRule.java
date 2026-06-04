package validator;

import java.util.regex.Pattern;

public class RegexValidationRule implements ValidationRule {
    private final Pattern pattern;
    private final String errorMessage;
    private final boolean shouldMatch;

    public RegexValidationRule(String regex, String errorMessage) {
        this(regex, errorMessage, true);
    }

    public RegexValidationRule(String regex, String errorMessage, boolean shouldMatch) {
        this.pattern = Pattern.compile(regex);
        this.errorMessage = errorMessage;
        this.shouldMatch = shouldMatch;
    }

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;
        boolean matches = pattern.matcher(value).matches();
        return shouldMatch ? matches : !matches;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
