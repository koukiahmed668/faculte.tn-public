package tn.faculte.facultebackend.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Dto.CommentDto;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Dto.SharedPostDto;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.*;
import tn.faculte.facultebackend.Repo.CommentRepo;
import tn.faculte.facultebackend.Repo.SubjectRepo;
import tn.faculte.facultebackend.Service.PostService;
import tn.faculte.facultebackend.Service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @GetMapping("/allposts")
    public ResponseEntity<?> getAllPosts() {
        try {
            // Fetch all posts from the service along with associated files
            List<PostDto> postDtos = postService.getAllPostsIncludingFiles();
            return new ResponseEntity<>(postDtos, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching posts");
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestParam("userId") Long userId,
                                        @RequestParam(name = "content", required = true) String content,
                                        @RequestParam(name = "subjectId", required = false) Long subjectId,
                                        @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        // Retrieve the user using the userId
        Optional<UserDto> userOptional = userService.getUserById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            UserDto userDto = userOptional.get(); // Retrieve the userDto from the optional
            try {
                // Create the post for the specified subject
                Post createdPost = postService.createPost(userDto, content, subjectId, files);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    @GetMapping("/posts/{subjectId}")
    public List<PostDto> getPostsBySubjectId(@PathVariable Long subjectId) {
        return postService.getPostsBySubjectId(subjectId);
    }

    @GetMapping("/getbyspecialty")
    public ResponseEntity<List<PostDto>> getPostsByUserMajor(@RequestHeader("Authorization") String token) {
        List<PostDto> posts = postService.getPostsByUserSpecialty(token);
        return ResponseEntity.ok(posts);
    }


    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestParam Long loggedInUserId) {
        try {
            // Delete the post by its ID
            postService.deletePost(postId, loggedInUserId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post");
        }
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("content") String content,
                                        @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        // Retrieve the user using the userId
        Optional<UserDto> userOptional = userService.getUserById(userId);

        // Check if the user exists
        if (userOptional.isPresent()) {
            UserDto userDto = userOptional.get(); // Retrieve the userDto from the optional
            try {
                // Update the post
                Post updatedPost = postService.updatePost(postId, userDto, content, files);
                return ResponseEntity.status(HttpStatus.OK).body(updatedPost);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    @GetMapping("/postsbyuser")
    public ResponseEntity<?> getPostsByUserId(@RequestParam Long userId) {
        try {
            // Fetch posts created by the user identified by userId
            List<PostDto> postDtos = postService.getPostsByUserId(userId);
            return new ResponseEntity<>(postDtos, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching posts");
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Integer> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        int likeCount = postService.likePost(postId, userId);
        return ResponseEntity.ok().body(likeCount);
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestParam("userId") Long userId, @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setContent(content);

        // Fetch the user by userId
        Optional<UserDto> userOptional = userService.getUserById(userId);

        if (userOptional.isPresent()) {
            UserDto userDto = userOptional.get();
            User user = modelMapper.map(userDto, User.class);


            comment.setUser(user);

            // Add the comment to the post
            Comment savedComment = postService.addComment(postId, comment);
            return ResponseEntity.ok().body(savedComment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> removeCommentFromPost(@PathVariable Long postId,
                                                   @PathVariable Long commentId,
                                                   @RequestParam Long userId) {
        try {
            // Retrieve the comment by its ID
            Optional<Comment> commentOptional = commentRepo.findById(commentId);

            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                // Check if the user making the request is the same as the one who created the comment
                if (comment.getUser().getId().equals(userId)) {
                    // If so, remove the comment
                    postService.removeComment(postId, commentId);
                    return ResponseEntity.ok().build();
                } else {
                    // If not, return a forbidden response
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this comment");
                }
            } else {
                // Comment not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment");
        }
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("newContent") String newContent) {
        try {
            // Update the comment
            Comment updatedComment = postService.updateComment(commentId, userId, newContent);
            return ResponseEntity.ok(updatedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update comment");
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        try {
            // Fetch comments for the specified post
            List<CommentDto> commentDtos = postService.getCommentsByPostId(postId);
            return ResponseEntity.ok(commentDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/share")
    public ResponseEntity<String> sharePost(
            @RequestParam Long postId,
            @RequestParam Long userId,
            @RequestParam(name = "additionalContent", required = false) String additionalContent) {

        try {
            postService.sharePost(postId, userId, additionalContent);
            return ResponseEntity.ok("Post shared successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/shared")
    public ResponseEntity<List<SharedPostDto>> getSharedPosts(@RequestParam Long userId) {
        try {
            List<SharedPostDto> sharedPosts = postService.getSharedPosts(userId);
            return ResponseEntity.ok(sharedPosts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String query) {
        List<PostDto> posts = postService.searchPosts(query);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/recommend")
    public ResponseEntity<String> recommendPosts(@RequestParam("userId") Long userId) {
        try {
            // Define the URL of the Flask endpoint
            String flaskEndpointUrl = "http://127.0.0.1:5000/recommend?user_id=" + userId;

            // Make a GET request to the Flask endpoint to get recommended posts
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskEndpointUrl,
                    HttpMethod.GET,
                    null,
                    String.class // Define the type of response as String
            );

            // Return the JSON response from the Flask endpoint as it is
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Handle any exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}


