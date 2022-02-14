package src.argon.argon.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isNullOrEmpty_validString_isFalse() {
        // given
        String string = "A String With Some Content";
        // when
        boolean valid = StringUtils.isNullOrEmpty(string);
        // then
        assertFalse(valid);
    }

    @Test
    void isNullOrEmpty_whitespaces_isTrue() {
        // given
        String string = "    ";
        // when
        boolean valid = StringUtils.isNullOrEmpty(string);
        // then
        assertTrue(valid);
    }

    @Test
    void isNullOrEmpty_emptyString_isTrue() {
        // given
        String string = "";
        // when
        boolean valid = StringUtils.isNullOrEmpty(string);
        // then
        assertTrue(valid);
    }

    @Test
    void isNullOrEmpty_null_isTrue() {
        // given
        String string = null;
        // when
        boolean valid = StringUtils.isNullOrEmpty(string);
        // then
        assertTrue(valid);
    }

    @Test
    void validEmail_validEmails_isTrue() {
        // given
        List<String> validEmailAdresses = new ArrayList<>(Arrays.asList(
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "firstname-lastname@example.com"
        ));

        //when
        List<Boolean> results = validEmailAdresses.stream().map(email -> {
            return StringUtils.validEmail(email);
        }).collect(Collectors.toList());

        // then
        for (Boolean result : results) {
            assertTrue(result);
        }
    }

    @Test
    void validEmail_invalidEmails_isFalse() {
        // given
        List<String> invalidEmailAdresses = new ArrayList<>(Arrays.asList(
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                ".email@example.com",
                "email.@example.com",
                "email..email@example.com",
                "あいうえお@example.com",
                "email@example.com (Joe Smith)",
                "email@example",
                "email@-example.com",
                "email@111.222.333.44444",
                "email@example..com",
                "Abc..123@example.com",
                ""
        ));

        //when
        List<Boolean> results = invalidEmailAdresses.stream().map(email -> {
            return StringUtils.validEmail(email);
        }).collect(Collectors.toList());

        // then
        for (Boolean result : results) {
            assertFalse(result);
        }
    }

    @Test
    void validPassword_validPasswords_isTrue() {
        // given
        List<String> validPasswords = new ArrayList<>(Arrays.asList(
                "password1",
                "ComPlIcAtEdPassWord1",
                "simple_password23",
                "123password",
                "d5t6753785byu35byu",
                "AAAA111AAAA"
        ));

        //when
        List<Boolean> results = validPasswords.stream().map(password -> {
            System.out.println(password + ":" + StringUtils.validPassword(password));
            return StringUtils.validPassword(password);
        }).collect(Collectors.toList());

        // then
        for (Boolean result : results) {
            assertTrue(result);
        }
    }

    @Test
    void validPassword_invalidPasswords_isTrue() {
        // given
        List<String> invalidPasswords = new ArrayList<>(Arrays.asList(
                "short",
                "ComPlIcAtEdPassWord",
                "__________",
                "",
                "nodigitman"
        ));

        //when
        List<Boolean> results = invalidPasswords.stream().map(password -> {
            System.out.println(password + ":" + StringUtils.validPassword(password));
            return StringUtils.validPassword(password);
        }).collect(Collectors.toList());

        // then
        for (Boolean result : results) {
            assertFalse(result);
        }
    }
}