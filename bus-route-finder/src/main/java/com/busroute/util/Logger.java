package com.busroute.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/search_history.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        try {
            new File(LOG_DIR).mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                pw.println("[" + LocalDateTime.now().format(FORMATTER) + "] " + message);
            }
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}
