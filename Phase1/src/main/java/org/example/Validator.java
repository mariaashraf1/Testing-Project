package org.example;

import java.util.*;
public class Validator {

    public static String validateMovieTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "ERROR: Movie Title " + title + " is wrong";
        }

        String[] words = title.split(" ");
        for (String word : words) {
            if (word.isEmpty()) continue;

            // Check if the first character is uppercase
            if (!Character.isUpperCase(word.charAt(0))) {
                // Allow words that start with numbers
                if (!Character.isDigit(word.charAt(0))) {
                    return "ERROR: Movie Title " + title + " is wrong";
                }
            }
        }

        return null; // No error
    }

    public static String validateMovieId(String id, String title, List<String> existingIds) {
        if (id == null || id.isEmpty()) {
            return "ERROR: Movie Id " + id + " is wrong";
        }

        //Check for duplicate IDs first
        if (existingIds != null && existingIds.contains(id)) {
            return "ERROR: Movie Id numbers " + id + " aren't unique";
        }

        // Extract first capital letter from each word in title
        StringBuilder capitalLetters = new StringBuilder();
        String[] words = title.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                char firstChar = word.charAt(0);
                if (Character.isUpperCase(firstChar)) {
                    capitalLetters.append(firstChar);
                } else if (Character.isDigit(firstChar)) {
                    // Skip words that start with digits for ID generation
                    continue;
                }
            }
        }

        // Check if movie ID starts with capital letters from title
        if (!id.startsWith(capitalLetters.toString())) {
            return "ERROR: Movie Id letters " + id + " are wrong";
        }

        // Check if the remaining part is 3 numbers
        String numbers = id.substring(capitalLetters.length());
        if (numbers.length() != 3) {
            return "ERROR: Movie Id numbers " + id + " are wrong";
        }

        // Check if all characters are digits
        for (char c : numbers.toCharArray()) {
            if (!Character.isDigit(c)) {
                return "ERROR: Movie Id numbers " + id + " are wrong";
            }
        }

        // Check if the number combination is unique across all movie IDs
        if (existingIds != null) {
            for (String existingId : existingIds) {
                if (existingId.equals(id)) {
                    continue; // Skip the current ID
                }

                // Extract the numbers from the existing ID
                StringBuilder existingCapitalLetters = new StringBuilder();
                for (int i = 0; i < existingId.length() && !Character.isDigit(existingId.charAt(i)); i++) {
                    existingCapitalLetters.append(existingId.charAt(i));
                }

                if (existingId.length() > existingCapitalLetters.length()) {
                    String existingNumbers = existingId.substring(existingCapitalLetters.length());
                    if (existingNumbers.equals(numbers)) {
                        return "ERROR: Movie Id numbers " + id + " aren't unique";
                    }
                }
            }
        }

        return null; // No error
    }


    public static String validateUserName(String name) {
        if (name == null || name.isEmpty() || name.startsWith(" ")) {
            return "ERROR: User Name " + name + " is wrong";
        }
        
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return "ERROR: User Name " + name + " is wrong";
            }
        }
        
        return null; // No error
    }
    
    public static String validateUserId(String id, Set<String> existingIds) {
        if (id == null || id.length() != 9) {
            return "ERROR: User Id " + id + " is wrong";
        }
        
        // Check if it starts with a number
        if (!Character.isDigit(id.charAt(0))) {
            return "ERROR: User Id " + id + " is wrong";
        }
        
        // Check if all characters are alphanumeric
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                return "ERROR: User Id " + id + " is wrong";
            }
        }
        
        // Check if it ends with at most one alphabetic character
        int letterCount = 0;
        for (int i = 0; i < id.length(); i++) {
            if (Character.isLetter(id.charAt(i))) {
                letterCount++;
            }
        }
        
        if (letterCount > 1 || (letterCount == 1 && !Character.isLetter(id.charAt(id.length() - 1)))) {
            return "ERROR: User Id " + id + " is wrong";
        }
        
        // Check for duplicate user IDs
        if (existingIds != null && existingIds.contains(id)) {
            return "ERROR: User Id " + id + " isn't unique";
        }
        
        return null; // No error
    }
    
    // Keep the original method for backward compatibility
}