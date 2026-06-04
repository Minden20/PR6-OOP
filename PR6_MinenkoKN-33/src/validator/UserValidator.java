package validator;

public class UserValidator {

    private static final FieldValidator nameValidator = new FieldValidator("name");
    private static final FieldValidator emailValidator = new FieldValidator("email");
    private static final FieldValidator passwordValidator = new FieldValidator("password");
    private static final FieldValidator phoneValidator = new FieldValidator("phone");

    static {
        nameValidator.addRule(new RegexValidationRule("^[a-zA-Zа-яА-ЯёЁіІїЇєЄґҐ\\s]+$",
                "ім'я може містити лише латинські або кириличні букви та пробіли"));
        nameValidator.addRule(new RegexValidationRule(".{2,}",
                "ім'я повинно містити щонайменше 2 символи"));

        emailValidator.addRule(new RegexValidationRule("^[a-zA-Z0-9+_.-@]*$",
                "email може містити лише латинські букви, цифри та символи '+', '_', '.', '-'"));
        emailValidator.addRule(new RegexValidationRule(".*@.*",
                "забули символ «@»"));
        emailValidator.addRule(new RegexValidationRule("^[^@]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                "відсутність домену поштового сервісу (наприклад, .com, .net, .org, .ua)"));

        passwordValidator.addRule(new RegexValidationRule(".{6,}",
                "пароль повинен містити щонайменше 6 символів"));
        passwordValidator.addRule(new RegexValidationRule(".*\\d.*",
                "пароль повинен містити хоча б одну цифру"));
        passwordValidator.addRule(new RegexValidationRule(".*[A-Z].*",
                "пароль повинен містити хоча б одну велику літеру"));
        passwordValidator.addRule(new RegexValidationRule(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*",
                "пароль повинен містити хоча б один спеціальний символ"));

        phoneValidator.addRule(new RegexValidationRule("^\\+.*",
                "номер телефону повинен починатися з символу '+'"));
        phoneValidator.addRule(new RegexValidationRule("^\\+[0-9]+$",
                "номер телефону після '+' може містити лише цифри"));
        phoneValidator.addRule(new RegexValidationRule("^\\+[0-9]{10,15}$",
                "номер телефону після '+' повинен містити від 10 до 15 цифр"));
    }

    public static ValidationResult validate(String name, String email, String password, String phone) {
        ValidationResult result = new ValidationResult();

        result.addErrors("name", nameValidator.validate(name));
        result.addErrors("email", emailValidator.validate(email));
        result.addErrors("password", passwordValidator.validate(password));
        result.addErrors("phone", phoneValidator.validate(phone));

        return result;
    }
}
