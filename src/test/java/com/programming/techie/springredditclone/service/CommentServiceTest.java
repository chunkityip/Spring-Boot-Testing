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
        //Given - init
        commentService = new CommentService(null ,
                null , null , null ,
                null, null , null);
    }


    @Test
    @DisplayName("This test should pass since it didn't contains any swear word")
    public void containsSwearWordsHappyTest() {
        //When - operation
        boolean message = commentService.containsSwearWords("Hi , it is Ck");

        assertFalse(message);
    }

    @Test
    @DisplayName("This test should pass since it will throw the exception that I want")
    public void containsSwearWordsUnHappyTest() {
        //When - operation

        SpringRedditException springRedditException = assertThrows(SpringRedditException.class ,
                () -> commentService.containsSwearWords("shit"));

        assertTrue(springRedditException.getMessage().contains("unacceptable"));
    }
}


