package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            MovieRecommendationSystem system = new MovieRecommendationSystem();
            system.loadMovies("C:\\Users\\Maria\\OneDrive\\Desktop\\Phase1\\Phase1\\src\\main\\java\\org\\example\\movies.txt");
            system.loadUsers("C:\\Users\\Maria\\OneDrive\\Desktop\\Phase1\\Phase1\\src\\main\\java\\org\\example\\users.txt");
            system.generateRecommendations("C:\\Users\\Maria\\OneDrive\\Desktop\\Phase1\\Phase1\\src\\main\\java\\org\\example\\recommendations.txt");
            System.out.println("Recommendations generated successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}