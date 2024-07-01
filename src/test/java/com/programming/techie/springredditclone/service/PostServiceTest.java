package com.programming.techie.springredditclone.service;

import com.programming.techie.springredditclone.dto.PostRequest;
import com.programming.techie.springredditclone.dto.PostResponse;
import com.programming.techie.springredditclone.mapper.PostMapper;
import com.programming.techie.springredditclone.model.Post;
import com.programming.techie.springredditclone.model.Subreddit;
import com.programming.techie.springredditclone.model.User;
import com.programming.techie.springredditclone.repository.PostRepository;
import com.programming.techie.springredditclone.repository.SubredditRepository;
import com.programming.techie.springredditclone.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.emptyList;

// @ExtendWith is initializing mocks, it is a must annotation whenever we like to use mockito at Junit5
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    SubredditRepository subredditRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthService authService;

    @Mock
    PostMapper postMapper;

    // We are using @Captor to create argument captor at field level
    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    /*
    When unit testing full-stack applications with Mockito, creating both entity and DTO objects in your tests is a good practice. It ensures that:
            - The data transformation logic between entities and DTOs is properly tested.
            - Your tests are clear and maintainable.
            - You can effectively mock dependencies to isolate the unit of work being tested.
     */

    @Test
    @DisplayName("It should work base on the dummy Id by using findById()")
    public void getPostTesting() {
        // Given: Test Init

        PostService postService = new PostService(postRepository
                , subredditRepository
                , userRepository
                , authService
                , postMapper);

        // Since the User creates the post, it will generate postId
        // Post object represents the entity as it is store at database
        // Entity: Represents the data structure as it is stored in the database.
        // We need to create a dummy Post object to be able to test
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);

        // Since we need DTO to let the data sent to the client, we better to test DTO as well
        // DTO: Represents the data structure transferred over the network or API
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subreddit", 0, 0, "1 Hour Ago", false, false);

        /*

        Full-stack applications often have services that depend on repositories (for database operations)
        and mappers (for entity-to-DTO conversion).

        So we need to use postRepository to test model as an entity,
        and we need to user postMapper to test DTO by using entity

         */

        // Using Optional in unit tests since it is a good practice to avoid NullPointerExceptions
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(post)).thenReturn(expectedPostResponse);

        // When: operation , call the method we like to test
        // Testing the getPost method by using postID inject by post-object at line 69
        // Whenever we like to ensure operation correctly transforms an entity to a DTO ,
        // we need to create response object in your test
        PostResponse actualPostResponse = postService.getPost(123L);

        // Then: result
        // Check is the dummy object is equals to or not
        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("It should work")
    public void saveTest() {
        // When
        PostService postService = new PostService(postRepository ,
                subredditRepository ,
                userRepository ,
                authService ,
                postMapper);

        // We need to init dummy user object to create Subreddit object
        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);

        //Represents the request DTO for creating a post
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

        //We need to mock the init object. Since there have 4 , you need to mock all 4 of it

        //Mock with subreddit object
        Mockito.when(subredditRepository.findByName("First Subreddit")).thenReturn(Optional.of(subreddit));

        Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);

        Mockito.when(postMapper.map(postRequest, subreddit, currentUser)).thenReturn(post);

        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        // When - operation
        postService.save(postRequest);


        // Then - result
        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);

        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
    }
}
