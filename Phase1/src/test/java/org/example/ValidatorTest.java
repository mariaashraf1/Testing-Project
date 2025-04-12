package org.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ValidatorTest {
    @Test
    public void testValidateMovieTitleWithValidTitle() {
        assertNull(Validator.validateMovieTitle("The Matrix"));
        assertNull(Validator.validateMovieTitle("Inception"));
        assertNull(Validator.validateMovieTitle("The Lord Of The Rings"));
    }

    @Test
    public void testValidateMovieTitleWithInvalidTitle() {
        String errorMsg = "ERROR: Movie Title the matrix is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle("the matrix"));

        errorMsg = "ERROR: Movie Title inception is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle("inception"));

        errorMsg = "ERROR: Movie Title The lord of the rings is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle("The lord of the rings"));
    }

    @Test
    public void testValidateMovieTitleWithNullOrEmptyTitle() {
        String errorMsg = "ERROR: Movie Title null is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle(null));

        errorMsg = "ERROR: Movie Title  is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle(""));
    }

    @Test
    public void testValidateMovieTitleWithMultipleSpaces() {
        assertNull(Validator.validateMovieTitle("The  Matrix")); // Double space is allowed
        assertNull(Validator.validateMovieTitle("The   Lord   Of   The   Rings")); // Multiple spaces are allowed
    }

    @Test
    public void testValidateMovieTitleWithSpecialCharacters() {
        String errorMsg = "ERROR: Movie Title @The Matrix is wrong";
        assertEquals(errorMsg, Validator.validateMovieTitle("@The Matrix"));
    }

    @Test
    public void testValidateMovieTitleWithNumbers() {
        assertNull(Validator.validateMovieTitle("The Matrix 2"));
    }


    @Test
    public void testValidateMovieIdWithTitleHavingNoCapitals() {
        String errorMsg = "ERROR: Movie Id numbers tm123 are wrong";
        assertEquals(errorMsg, Validator.validateMovieId("tm123", "the matrix", null));
    }
    // Movie ID Validation Tests
    @Test
    public void testValidateMovieIdWithValidId() {
        List<String> existingIds = new ArrayList<>();
        assertNull(Validator.validateMovieId("TM123", "The Matrix", existingIds));

        existingIds.add("TM123");
        assertNull(Validator.validateMovieId("I456", "Inception", existingIds));
    }

    @Test
    public void testValidateMovieIdWithInvalidIdFormat() {
        String errorMsg = "ERROR: Movie Id letters ABC123 are wrong";
        assertEquals(errorMsg, Validator.validateMovieId("ABC123", "The Matrix", null));

        errorMsg = "ERROR: Movie Id numbers TM12 are wrong";
        assertEquals(errorMsg, Validator.validateMovieId("TM12", "The Matrix", null));

        errorMsg = "ERROR: Movie Id numbers TM1234 are wrong";
        assertEquals(errorMsg, Validator.validateMovieId("TM1234", "The Matrix", null));
    }

    @Test
    public void testValidateMovieIdWithNonNumericSuffix() {
        String errorMsg = "ERROR: Movie Id numbers TMABC are wrong";
        assertEquals(errorMsg, Validator.validateMovieId("TMABC", "The Matrix", null));
    }

    @Test
    public void testValidateMovieIdWithDuplicateId() {
        List<String> existingIds = new ArrayList<>();
        existingIds.add("TM123");

        String errorMsg = "ERROR: Movie Id numbers TM123 aren't unique";
        assertEquals(errorMsg, Validator.validateMovieId("TM123", "The Matrix", existingIds));
    }

    @Test
    public void testValidateMovieIdWithDuplicateNumbers() {
        List<String> existingIds = new ArrayList<>();
        existingIds.add("TM123");

        String errorMsg = "ERROR: Movie Id numbers I123 aren't unique";
        assertEquals(errorMsg, Validator.validateMovieId("I123", "Inception", existingIds));
    }

    // User Name Validation Tests
    @Test
    public void testValidateUserNameWithValidName() {
        assertNull(Validator.validateUserName("John Doe"));
        assertNull(Validator.validateUserName("Mary Jane"));
        assertNull(Validator.validateUserName("Robert"));
    }

    @Test
    public void testValidateUserNameWithInvalidName() {
        String errorMsg = "ERROR: User Name John123 is wrong";
        assertEquals(errorMsg, Validator.validateUserName("John123"));

        errorMsg = "ERROR: User Name Mary@Jane is wrong";
        assertEquals(errorMsg, Validator.validateUserName("Mary@Jane"));

        errorMsg = "ERROR: User Name     is wrong";
        assertEquals(errorMsg, Validator.validateUserName("   "));
    }

    @Test
    public void testValidateUserNameWithLeadingSpace() {
        String errorMsg = "ERROR: User Name  John is wrong";
        assertEquals(errorMsg, Validator.validateUserName(" John"));
    }

    @Test
    public void testValidateUserNameWithNullOrEmptyName() {
        String errorMsg = "ERROR: User Name null is wrong";
        assertEquals(errorMsg, Validator.validateUserName(null));

        errorMsg = "ERROR: User Name  is wrong";
        assertEquals(errorMsg, Validator.validateUserName(""));
    }

    // User ID Validation Tests
    @Test
    public void testValidateUserIdWithValidId() {
        Set<String> existingIds = new HashSet<>();
        assertNull(Validator.validateUserId("123456789", existingIds));
        assertNull(Validator.validateUserId("12345678a", existingIds));
    }

    @Test
    public void testValidateUserIdWithInvalidLength() {
        String errorMsg = "ERROR: User Id 12345678 is wrong";
        assertEquals(errorMsg, Validator.validateUserId("12345678", null));

        errorMsg = "ERROR: User Id 1234567890 is wrong";
        assertEquals(errorMsg, Validator.validateUserId("1234567890", null));

        errorMsg="ERROR: User Id abcderfg is wrong";
        assertEquals(errorMsg, Validator.validateUserId("abcderfg", null));

        errorMsg="ERROR: User Id 12345678@ is wrong";
        assertEquals(errorMsg, Validator.validateUserId("12345678@", null));
    }

    @Test
    public void testValidateUserIdWithInvalidStartCharacter() {
        String errorMsg = "ERROR: User Id a12345678 is wrong";
        assertEquals(errorMsg, Validator.validateUserId("a12345678", null));
    }

    @Test
    public void testValidateUserIdWithInvalidCharacters() {
        String errorMsg = "ERROR: User Id 12345@678 is wrong";
        assertEquals(errorMsg, Validator.validateUserId("12345@678", null));
    }

    @Test
    public void testValidateUserIdWithLetterNotAtEnd() {
        String errorMsg = "ERROR: User Id 1234a5678 is wrong";
        assertEquals(errorMsg, Validator.validateUserId("1234a5678", null));
    }

    @Test
    public void testValidateUserIdWithDuplicateId() {
        Set<String> existingIds = new HashSet<>();
        existingIds.add("123456789");

        String errorMsg = "ERROR: User Id 123456789 isn't unique";
        assertEquals(errorMsg, Validator.validateUserId("123456789", existingIds));
    }


}