package org.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class MovieTest {
    @Test
    public void testMovieConstructorAndGetters() {
        // Arrange
        String title = "The Matrix";
        String id = "TM123";
        String[] genres = { "Action", "Sci-Fi" };

        // Act
        Movie movie = new Movie(title, id, genres);

        // Assert
        assertEquals(title, movie.getTitle());
        assertEquals(id, movie.getId());
        assertArrayEquals(genres, movie.getGenres());
    }

    @Test
    public void testHasGenreWithMatchingGenre() {
        // Arrange
        Movie movie = new Movie("Inception", "I123", new String[] { "Sci-Fi", "Action", "Thriller" });

        // Act & Assert
        assertTrue(movie.hasGenre("Sci-Fi"));
        assertTrue(movie.hasGenre("Action"));
        assertTrue(movie.hasGenre("Thriller"));
    }

    @Test
    public void testHasGenreWithNonMatchingGenre() {
        // Arrange
        Movie movie = new Movie("Inception", "I123", new String[] { "Sci-Fi", "Action", "Thriller" });

        // Act & Assert
        assertFalse(movie.hasGenre("Comedy"));
        assertFalse(movie.hasGenre("Horror"));
        assertFalse(movie.hasGenre("Drama"));
    }

    @Test
    public void testHasGenreWithCaseInsensitivity() {
        // Arrange
        Movie movie = new Movie("Inception", "I123", new String[] { "Sci-Fi", "Action", "Thriller" });

        // Act & Assert
        assertTrue(movie.hasGenre("sci-fi"));
        assertTrue(movie.hasGenre("ACTION"));
        assertTrue(movie.hasGenre("ThRiLlEr"));
    }

    @Test
    public void testHasGenreWithExtraSpaces() {
        // Arrange
        Movie movie = new Movie("Inception", "I123", new String[] { "Sci-Fi ", " Action", "Thriller" });

        // Act & Assert
        assertTrue(movie.hasGenre("Sci-Fi"));
        assertTrue(movie.hasGenre("Action"));
        assertTrue(movie.hasGenre(" Thriller "));
    }
}

