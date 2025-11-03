package com.lojaadocao.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", message);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message + " - " + throwable.getMessage());
        throwable.printStackTrace(); // Mantém o stack trace no console
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("[%s] %s - %s%n", timestamp, level, message);
    }
}