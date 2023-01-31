package com.es.phoneshop.web.controller.pages.util;

import java.security.SecureRandom;

public class Generator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz ";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static SecureRandom random = new SecureRandom();

    private static final String NUMBER = "0123456789";

    public static final String PHONE_NUMBER_START_TEMPLATE = "+375";

    public static final int PHONE_NUMBER_LENGTH = 9;


    public static String generateRandomString(int length) {

        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        int rndUpperCharAt = random.nextInt(CHAR_UPPER.length());
        char rndUpperChar = CHAR_UPPER.charAt(rndUpperCharAt);
        sb.append(rndUpperChar);

        for (int i = 1; i < length; i++) {
            int rndLowerCharAt = random.nextInt(CHAR_LOWER.length());
            char rndLowerChar = CHAR_LOWER.charAt(rndLowerCharAt);
            sb.append(rndLowerChar);
        }

        return sb.toString();

    }

    public static String generateRandomPhone() {

        StringBuilder sb = new StringBuilder(PHONE_NUMBER_START_TEMPLATE.length() + PHONE_NUMBER_LENGTH);
        sb.append(PHONE_NUMBER_START_TEMPLATE);

        for (int i = 0; i < PHONE_NUMBER_LENGTH; i++) {
            int rndNumberCharAt = random.nextInt(NUMBER.length());
            char rndNumberChar = NUMBER.charAt(rndNumberCharAt);
            sb.append(rndNumberChar);
        }

        return sb.toString();

    }


}
