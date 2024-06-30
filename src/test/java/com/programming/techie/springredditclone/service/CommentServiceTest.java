package com.programming.techie.springredditclone.service;


import com.programming.techie.springredditclone.exceptions.SpringRedditException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


class CommentServiceTest {

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        // Given: test init
        commentService = new CommentService(null , null , null ,
                null , null , null, null
                );
    }

    @Test
    @DisplayName("This test should pass since there is no swear word")

    public void notContainsSwearWords() {
        // When: operation
        boolean message = commentService.containsSwearWords("Hi , it is CK");

        // Then: result
        assertFalse(message);
    }

    @Test
    @DisplayName("This test should pass since there has swear word")
    public void didContainsSwearWords() {

        // When
        SpringRedditException exception = assertThrows(SpringRedditException.class , () ->
    commentService.containsSwearWords("shit"));

        // Then
        assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));
    }
}


