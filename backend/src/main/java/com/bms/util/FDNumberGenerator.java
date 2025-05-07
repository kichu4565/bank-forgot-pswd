package com.bms.util;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class FDNumberGenerator {
    private static final String PREFIX = "FD";
    private static final int LENGTH = 8;
    private static final String CHARACTERS = "0123456789";
    private static final Random RANDOM = new Random();

    public String generateFDNumber() {
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
} 