package com.calendar.app.util;

import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@UtilityClass
@Slf4j
public final class BasicAuthUtils {

    public String getUserName(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else return null;
    }

    public static Pair<String, String> getUserAndPasswordFromBasic(String basicCredentials) {
        String[] splitted =  splitCredentials(basicCredentials);
        log.info("basic auth {}", Arrays.toString(splitted));
        return Pair.of(splitted[0], splitted[1]);
    }

    private static String[] splitCredentials(String basicCredentials) {
        Objects.requireNonNull(basicCredentials, "Credentials for basic auth can't be null");
        String userAndPass = new String(Base64.getDecoder().decode(basicCredentials));
        return userAndPass.split(":");
    }
}
