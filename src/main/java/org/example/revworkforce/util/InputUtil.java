package org.example.revworkforce.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);

    // Read a string
    public static String getString(String message) {
        System.out.print(message + ": ");
        return sc.nextLine();
    }

    // Read an integer
    public static int getInt(String message) {
        System.out.print(message + ": ");
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    // Read a date in format "yyyy-MM-dd"
    public static Date getDate(String message) {
        System.out.print(message + " (yyyy-MM-dd): ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // strict parsing
        while (true) {
            try {
                String input = sc.nextLine();
                return sdf.parse(input);
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please enter again (yyyy-MM-dd): ");
            }
        }
    }

    public static LocalDate getLocalDate(String prompt) {
        while (true) {
            try {
                String input = getString(prompt);        // ask user for input
                return LocalDate.parse(input);           // parse yyyy-MM-dd
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }


}


