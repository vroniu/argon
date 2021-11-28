package src.argon.argon.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        if (string == null)
            return true;
        return string.strip().isEmpty();
    }
}
