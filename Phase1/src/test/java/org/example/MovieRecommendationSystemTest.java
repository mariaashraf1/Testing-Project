package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class MovieRecommendationSystemTest {
    private MovieRecommendationSystem recommendationSystem;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        recommendationSystem = new MovieRecommendationSystem();
    }

    // Helper method to create a temporary file with content
    private File createTempFile(String filename, String... lines) throws IOException {
        Path filePath = tempDir.resolve(filename);
        Files.write(filePath, Arrays.asList(lines));
        return filePath.toFile();
    }

    @Test
    public void testConstructor() {
        // Verify that the constructor initializes the lists
        assertNotNull(recommendationSystem);
    }

    @Test
    public void testLoadMoviesWithValidData() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM123",
                "Action,Sci-Fi",
                "Inception,I456",
                "Sci-Fi,Thriller");

        // Mock the validator to always return null (no errors)
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(anyString(), anyString(), anyList())).thenReturn(null);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());

            // Assert - we can't directly access the movies list, so we'll test indirectly
            // by generating recommendations and checking the output
            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            // The output should be empty since we haven't loaded any users
            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertTrue(outputLines.isEmpty());
        }
    }

    @Test
    public void testLoadMoviesWithInvalidTitle() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "the matrix,TM123", // Invalid title (lowercase)
                "Action,Sci-Fi");

        String expectedError = "ERROR: Movie Title the matrix is wrong";

        // Mock the validator to return an error for the title
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle("the matrix")).thenReturn(expectedError);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());

            // Assert - check that the error is captured
            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(1, outputLines.size());
            assertEquals(expectedError, outputLines.get(0));
        }
    }

    @Test
    public void testLoadMoviesWithInvalidId() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM12", // Invalid ID (only 2 digits)
                "Action,Sci-Fi");

        String expectedError = "ERROR: Movie Id numbers TM12 are wrong";

        // Mock the validator
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(eq("TM12"), eq("The Matrix"), anyList()))
                    .thenReturn(expectedError);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());

            // Assert
            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(1, outputLines.size());
            assertEquals(expectedError, outputLines.get(0));
        }
    }

    @Test
    public void testLoadUsersWithValidData() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM123",
                "Action,Sci-Fi");

        File usersFile = createTempFile("users.txt",
                "John Doe,123456789",
                "TM123");

        // Mock the validator
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(anyString(), anyString(), anyList())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserName(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserId(anyString(), anySet())).thenReturn(null);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());
            recommendationSystem.loadUsers(usersFile.getAbsolutePath());

            // Assert - test indirectly through generateRecommendations
            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(2, outputLines.size());
            assertEquals("John Doe,123456789", outputLines.get(0));
            // Since the user likes the only movie, there should be no recommendations
            assertEquals("No recommendations", outputLines.get(1));
        }
    }

    @Test
    public void testLoadUsersWithInvalidName() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM123",
                "Action,Sci-Fi");

        File usersFile = createTempFile("users.txt",
                " John Doe,123456789", // Invalid name (leading space)
                "TM123");

        String expectedError = "ERROR: User Name  John Doe is wrong";

        // Mock the validator
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(anyString(), anyString(), anyList())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserName(" John Doe")).thenReturn(expectedError);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());
            recommendationSystem.loadUsers(usersFile.getAbsolutePath());

            // Assert
            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(1, outputLines.size());
            assertEquals(expectedError, outputLines.get(0));
        }
    }

    @Test
    public void testGenerateRecommendationsWithMultipleUsers() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM123",
                "Action,Sci-Fi",
                "Inception,I456",
                "Sci-Fi,Thriller",
                "Titanic,T789",
                "Romance,Drama");

        File usersFile = createTempFile("users.txt",
                "John Doe,123456789",
                "TM123",
                "Jane Smith,987654321",
                "I456,T789");

        // Mock the validator
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(anyString(), anyString(), anyList())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserName(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserId(anyString(), anySet())).thenReturn(null);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());
            recommendationSystem.loadUsers(usersFile.getAbsolutePath());

            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            // Assert
            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(4, outputLines.size());

            // John should get Inception as a recommendation (shares Sci-Fi genre with The
            // Matrix)
            assertEquals("John Doe,123456789", outputLines.get(0));
            assertEquals("Inception", outputLines.get(1));

            // Jane should get The Matrix as a recommendation (shares Sci-Fi genre with
            // Inception)
            assertEquals("Jane Smith,987654321", outputLines.get(2));
            assertEquals("The Matrix", outputLines.get(3));
        }
    }

    @Test
    public void testGenerateRecommendationsWithNoMatchingGenres() throws IOException {
        // Arrange
        File moviesFile = createTempFile("movies.txt",
                "The Matrix,TM123",
                "Action,Sci-Fi",
                "Titanic,T789",
                "Romance,Drama");

        File usersFile = createTempFile("users.txt",
                "John Doe,123456789",
                "TM123");

        // Mock the validator
        try (MockedStatic<Validator> validatorMock = Mockito.mockStatic(Validator.class)) {
            validatorMock.when(() -> Validator.validateMovieTitle(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateMovieId(anyString(), anyString(), anyList())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserName(anyString())).thenReturn(null);
            validatorMock.when(() -> Validator.validateUserId(anyString(), anySet())).thenReturn(null);

            // Act
            recommendationSystem.loadMovies(moviesFile.getAbsolutePath());
            recommendationSystem.loadUsers(usersFile.getAbsolutePath());

            File outputFile = createTempFile("output.txt");
            recommendationSystem.generateRecommendations(outputFile.getAbsolutePath());

            // Assert
            List<String> outputLines = Files.readAllLines(outputFile.toPath());
            assertEquals(2, outputLines.size());
            assertEquals("John Doe,123456789", outputLines.get(0));
            assertEquals("No recommendations", outputLines.get(1));
        }
    }

    @Test
    public void testLoadMoviesWithFileNotFound() {
        // Act & Assert
        assertThrows(IOException.class, () -> {
            recommendationSystem.loadMovies("non_existent_file.txt");
        });
    }

    @Test
    public void testLoadUsersWithFileNotFound() {
        // Act & Assert
        assertThrows(IOException.class, () -> {
            recommendationSystem.loadUsers("non_existent_file.txt");
        });
    }

}