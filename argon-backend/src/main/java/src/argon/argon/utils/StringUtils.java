package src.argon.argon.utils;

import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        if (string == null)
            return true;
        return string.isBlank();
    }

    public static boolean validEmail(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email)
                .matches();
    }

    public static boolean validPassword(String password) {
        return Pattern.compile("^(?=.*\\d)[0-9a-zA-Z!@#&()–_{}:;',?/*~$^+=<>]{8,}$")
                .matcher(password)
                .matches();
    }
}
