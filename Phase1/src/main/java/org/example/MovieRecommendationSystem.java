package org.example;

import java.io.*;
import java.util.*;

public class MovieRecommendationSystem {
    private List<Movie> movies;
    private List<User> users;
    private String firstError = null;
    
    public MovieRecommendationSystem() {
        movies = new ArrayList<>();
        users = new ArrayList<>();
    }
    
    public void loadMovies(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        List<String> existingMovieIds = new ArrayList<>();
        
        while ((line = reader.readLine()) != null && firstError == null) {
            String[] parts = line.split(",");
            if (parts.length != 2) {
                continue; // Skip invalid lines
            }
            
            String title = parts[0].trim();
            String id = parts[1].trim();
            
            // Validate movie title
            String titleError = Validator.validateMovieTitle(title);
            if (titleError != null) {
                firstError = titleError;
                reader.close();
                return;
            }
            
            // Validate movie ID with existing IDs
            String idError = Validator.validateMovieId(id, title, existingMovieIds);
            if (idError != null) {
                firstError = idError;
                reader.close();
                return;
            }
            
            existingMovieIds.add(id);
            
            // Read genres
            line = reader.readLine();
            if (line == null) {
                break;
            }
            
            String[] genres = line.split(",");
            for (int i = 0; i < genres.length; i++) {
                genres[i] = genres[i].trim();
            }
            
            movies.add(new Movie(title, id, genres));
        }
        
        reader.close();
    }
    
    public void loadUsers(String filePath) throws IOException {
        // If we already found an error in movies, don't process users
        if (firstError != null) {
            return;
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Set<String> userIdSet = new HashSet<>();
        
        while ((line = reader.readLine()) != null && firstError == null) {
            String[] parts = line.split(",");
            if (parts.length != 2) {
                continue; // Skip invalid lines
            }
            
            // Don't trim the name to preserve leading spaces for validation
            String name = parts[0];
            String id = parts[1].trim();
            
            // Validate user name and ID
            String nameError = Validator.validateUserName(name);
            if (nameError != null) {
                firstError = nameError;
                reader.close();
                return;
            }
            
            // Validate user ID including uniqueness check
            String idError = Validator.validateUserId(id, userIdSet);
            if (idError != null) {
                firstError = idError;
                reader.close();
                return;
            }
            
            userIdSet.add(id);
            User user = new User(name, id);
            
            // Read liked movie IDs
            line = reader.readLine();
            if (line == null) {
                break;
            }
            
            String[] likedMovieIds = line.split(",");
            for (String movieId : likedMovieIds) {
                user.addLikedMovieId(movieId.trim());
            }
            
            users.add(user);
        }
        
        reader.close();
    }
    
    public void generateRecommendations(String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        
        // If we found an error earlier, just output that error
        if (firstError != null) {
            writer.write(firstError);
            writer.newLine();
            writer.close();
            return;
        }
        
        for (User user : users) {
            writer.write(user.getName() + "," + user.getId());
            writer.newLine();
            
            Set<String> recommendedMovies = new HashSet<>();
            Set<String> likedGenres = new HashSet<>();
            
            // Find genres of liked movies
            for (String likedMovieId : user.getLikedMovieIds()) {
                for (Movie movie : movies) {
                    if (movie.getId().equals(likedMovieId)) {
                        for (String genre : movie.getGenres()) {
                            likedGenres.add(genre);
                        }
                        break;
                    }
                }
            }
            
            // Find movies with the same genres
            for (Movie movie : movies) {
                boolean isLiked = false;
                for (String likedMovieId : user.getLikedMovieIds()) {
                    if (movie.getId().equals(likedMovieId)) {
                        isLiked = true;
                        break;
                    }
                }
                
                if (!isLiked) {
                    for (String genre : movie.getGenres()) {
                        if (likedGenres.contains(genre)) {
                            recommendedMovies.add(movie.getTitle());
                            break;
                        }
                    }
                }
            }
            
            // Write recommendations
            if (recommendedMovies.isEmpty()) {
                writer.write("No recommendations");
            } else {
                writer.write(String.join(",", recommendedMovies));
            }
            writer.newLine();
        }
        
        writer.close();
    }
}