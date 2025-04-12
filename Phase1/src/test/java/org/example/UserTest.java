package org.example;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class UserTest {

    private User user;
    private String name;
    private String id;

    @Before
    public void setUp() {
        name = "John Doe";
        id = "123456789";
        user = new User(name, id);
    }

    @Test
    public void testUserConstructorAndGetters() {
        // Assert
        assertEquals(name, user.getName());
        assertEquals(id, user.getId());
        assertNotNull(user.getLikedMovieIds());
        assertTrue(user.getLikedMovieIds().isEmpty());
    }

    @Test
    public void testAddLikedMovieId() {
        // Arrange
        String movieId1 = "TM123";
        String movieId2 = "AV456";

        // Act
        user.addLikedMovieId(movieId1);
        user.addLikedMovieId(movieId2);

        // Assert
        List<String> likedMovies = user.getLikedMovieIds();
        assertEquals(2, likedMovies.size());
        assertTrue(likedMovies.contains(movieId1));
        assertTrue(likedMovies.contains(movieId2));
    }

    @Test
    public void testAddDuplicateLikedMovieId() {
        // Arrange
        String movieId = "TM123";

        // Act
        user.addLikedMovieId(movieId);
        user.addLikedMovieId(movieId); // Add the same ID again

        // Assert
        List<String> likedMovies = user.getLikedMovieIds();
        assertEquals(2, likedMovies.size()); // List allows duplicates
        assertEquals(movieId, likedMovies.get(0));
        assertEquals(movieId, likedMovies.get(1));
    }

    @Test
    public void testSetLikedMovieIds() {
        // Arrange
        List<String> newLikedMovies = new ArrayList<>();
        newLikedMovies.add("TM123");
        newLikedMovies.add("AV456");

        // Act
        user.setLikedMovieIds(newLikedMovies);

        // Assert
        assertEquals(newLikedMovies, user.getLikedMovieIds());
        assertEquals(2, user.getLikedMovieIds().size());
    }

    @Test
    public void testSetEmptyLikedMovieIds() {
        // Arrange
        user.addLikedMovieId("TM123"); // Add a movie first
        List<String> emptyList = new ArrayList<>();

        // Act
        user.setLikedMovieIds(emptyList);

        // Assert
        assertTrue(user.getLikedMovieIds().isEmpty());
    }

    @Test
    public void testLikedMoviesListModification() {
        // Arrange
        user.addLikedMovieId("TM123");
        List<String> likedMovies = user.getLikedMovieIds();

        // Act
        likedMovies.add("AV456"); // Modify the returned list

        // Assert
        assertEquals(2, user.getLikedMovieIds().size()); // Changes to the returned list affect the internal list
        assertTrue(user.getLikedMovieIds().contains("AV456"));
    }
}