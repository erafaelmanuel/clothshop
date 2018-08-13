package com.clothshop.catalog.util;

import java.util.Calendar;

public class UUIDGenerator {

    public static String randomUUID() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final String base = "999999";
        final StringBuilder builder = new StringBuilder();

        for (char c : base.toCharArray()) {
            int temp = (int) ((Integer.parseInt(String.valueOf(c))) * Math.random());
            temp += (int) (2 * Math.random());
            builder.append(temp);
        }
        return String.valueOf(Long.parseLong(year + builder.toString()));
    }
}
