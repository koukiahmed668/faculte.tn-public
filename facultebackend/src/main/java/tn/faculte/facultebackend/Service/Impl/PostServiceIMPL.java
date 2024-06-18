package tn.faculte.facultebackend.Service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Config.JwtTokenUtil;
import tn.faculte.facultebackend.Dto.*;
import tn.faculte.facultebackend.Entity.*;
import tn.faculte.facultebackend.Repo.*;
import tn.faculte.facultebackend.Service.FileService;
import tn.faculte.facultebackend.Service.PostService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceIMPL implements PostService {
    private PostRepo postRepo;
    @Autowired
    private FileService fileService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private SharedPostRepo sharedPostRepo;
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    public PostServiceIMPL(PostRepo postRepo, FileService fileService) {
        this.postRepo = postRepo;
        this.fileService = fileService;
    }

    @Override
    public List<PostDto> getAllPostsIncludingFiles() {
        List<Post> posts = postRepo.findAll();
        List<PostDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setContent(post.getContent());
            postDto.setAuthor(post.getAuthor());
            postDto.setCreatedAt(post.getCreatedAt());
            postDto.setLikeCount(post.getLikeCount());
            // Fetch comments for the post
            List<Comment> comments = commentRepo.findByPostId(post.getId());
            // Set comments in the DTO
            postDto.setComments(comments);
            // Fetch associated files for the post
            List<FileEntity> files = fileRepo.findByPost(post);
            // Map FileEntity objects to FileEntityDTO using ModelMapper
            List<FileEntityDTO> fileDtos = files.stream()
                    .map(file -> modelMapper.map(file, FileEntityDTO.class))
                    .collect(Collectors.toList());
            // Set files in the DTO
            postDto.setFiles(fileDtos);

            postDtos.add(postDto);
        }

        return postDtos;
    }



    @Autowired
    public void PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }


    @Override
    public Post createPost(UserDto userDto, String content, Long subjectId, List<MultipartFile> files) {
        User author = modelMapper.map(userDto, User.class);

        // Check if subject exists
        if (subjectId != null) {
            Optional<Subject> optionalSubject = subjectRepo.findById(subjectId);
            if (optionalSubject.isEmpty()) {
                throw new IllegalArgumentException("Subject not found");
            }
        }

        // Check if user is authorized to create post
        if (author != null && (author.getRoles().contains(Role.TEACHER) || author.getRoles().contains(Role.ADMIN))) {
            // Create the post
            Post post = new Post();
            post.setAuthor(author);
            post.setContent(content);
            if (subjectId != null) {
                Optional<Subject> optionalSubject = subjectRepo.findById(subjectId);
                optionalSubject.ifPresent(post::setSubject);
            }

            // Handle file uploads
            List<FileEntity> fileEntities = new ArrayList<>(); // Create a list to store file entities
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    FileEntity fileEntity = fileService.storeFile(file); // Store the file and get the returned FileEntity
                    fileEntity.setPost(post); // Associate the file with the post
                    fileEntities.add(fileEntity); // Add file entity to the list
                }
            }

            // Set the list of file entities to the post
            post.setFiles(fileEntities);

            // Save the post
            return postRepo.save(post);
        } else {
            throw new IllegalArgumentException("Only teachers and admins can create posts");
        }
    }





    @Override
    public Post savePost(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<PostDto> getPostsBySubjectId(Long subjectId) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        List<Post> posts = postRepo.findBySubject(subject);

        return posts.stream()
                .map(this::convertToDtoWithFiles) // Use a different converter method
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUserSpecialty(String token) {
        // Get the major ID from the JWT token
        Long specialtyId = jwtTokenUtil.getSpecialityIdFromToken(token.substring(7));
        if (specialtyId == null) {
            throw new IllegalArgumentException("Specialty ID not found in token");
        }

        // Retrieve all subjects related to the major
        List<Subject> subjects = subjectRepo.findBySpecialtyId(specialtyId);

        // Fetch posts associated with the fetched subjects
        List<Post> posts = postRepo.findBySubjectIn(subjects);

        // Convert Post entities to PostDto objects
        return posts.stream()
                .map(this::convertToDtoWithFiles)
                .collect(Collectors.toList());
    }


    // Helper method to convert Post entity to PostDto
    private PostDto convertToDtoWithFiles(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setAuthor(post.getAuthor());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setLikeCount(post.getLikeCount());

        // Convert files to DTOs and add them to the post DTO
        List<FileEntityDTO> fileDtos = post.getFiles().stream()
                .map(file -> modelMapper.map(file, FileEntityDTO.class))
                .collect(Collectors.toList());
        postDto.setFiles(fileDtos);

        // You can include other properties as needed

        return postDto;
    }




    @Override
    public void deletePost(Long postId, Long loggedInUserId) {
        // Fetch the post by its ID
        Optional<Post> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Check if the logged-in user ID matches the post author's ID
            if (post.getAuthor().getId().equals(loggedInUserId)) {
                // Delete the post
                postRepo.delete(post);
            } else {
                throw new IllegalArgumentException("You are not authorized to delete this post.");
            }
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }



    @Override
    public Post updatePost(Long postId, UserDto userDto, String content, List<MultipartFile> files) {
        // Convert UserDto to User entity
        User author = modelMapper.map(userDto, User.class);

        // Fetch the post by its ID
        Optional<Post> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Check if the user is the author of the post
            if (!post.getAuthor().equals(author)) {
                throw new IllegalArgumentException("Only the author can update the post");
            }
            // Update post content if provided
            if (content != null && !content.isEmpty()) {
                post.setContent(content);
            }
            // Add new files if provided
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    FileEntity fileEntity = fileService.storeFile(file); // Store the file
                    fileEntity.setPost(post); // Associate the file with the post
                    post.getFiles().add(fileEntity); // Add file entity to the post's files
                }
            }
            // Save and return the updated post
            return postRepo.save(post);
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }



    @Override
    public List<PostDto> getPostsByUserId(Long userId) {
        // Fetch all posts from the service along with associated files
        List<Post> posts = postRepo.findByAuthorId(userId);
        List<PostDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setContent(post.getContent());
            postDto.setAuthor(post.getAuthor());
            postDto.setCreatedAt(post.getCreatedAt());
            postDto.setLikeCount(post.getLikeCount());

            // Fetch associated files for the post
            List<FileEntity> files = fileRepo.findByPost(post);
            // Map FileEntity objects to FileEntityDTO using ModelMapper
            List<FileEntityDTO> fileDtos = files.stream()
                    .map(file -> modelMapper.map(file, FileEntityDTO.class))
                    .collect(Collectors.toList());
            // Set files in the DTO
            postDto.setFiles(fileDtos);

            postDtos.add(postDto);
        }

        return postDtos;
    }

    @Override
    public int likePost(Long postId, Long userId) {
        // Check if the user has already liked the post
        Optional<Like> existingLike = likeRepo.findByPostIdAndUserId(postId, userId);

        // Fetch the post
        Optional<Post> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            int likeCount = post.getLikeCount();

            if (existingLike.isPresent()) {
                // User has already liked the post, remove the like
                post.setLikeCount(likeCount - 1);
                likeRepo.delete(existingLike.get());
            } else {
                // User hasn't liked the post, add a new like
                post.setLikeCount(likeCount + 1);
                Like like = new Like();
                like.setUser(userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
                like.setPost(post);
                likeRepo.save(like);
            }

            // Save the updated post entity
            postRepo.save(post);

            // Return the updated like count
            return post.getLikeCount();
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }



    @Override
    public Comment addComment(Long postId, Comment comment) {
        // Fetch the post by its ID
        Optional<Post> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Associate the comment with the post
            comment.setPost(post);
            // Save the comment
            return commentRepo.save(comment);
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    @Override
    public void removeComment(Long postId, Long commentId) {
        // Fetch the post by its ID
        Optional<Post> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Fetch the comment by its ID
            Optional<Comment> optionalComment = commentRepo.findById(commentId);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                // Check if the comment belongs to the post
                if (comment.getPost().equals(post)) {
                    // Remove the comment from the post
                    commentRepo.delete(comment);
                } else {
                    throw new IllegalArgumentException("Comment does not belong to the specified post");
                }
            } else {
                throw new IllegalArgumentException("Comment not found");
            }
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    @Override
    public Comment updateComment(Long commentId, Long userId, String newContent) {
        // Fetch the comment by its ID
        Optional<Comment> optionalComment = commentRepo.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            // Check if the user making the request is the same as the one who created the comment
            if (comment.getUser().getId().equals(userId)) {
                // Update the content of the comment
                comment.setContent(newContent);
                // Save and return the updated comment
                return commentRepo.save(comment);
            } else {
                throw new IllegalArgumentException("You are not authorized to update this comment");
            }
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // Assuming you have a method in CommentRepo to fetch comments by postId
        List<Comment> comments = commentRepo.findByPostId(postId);

        // Convert Comment entities to CommentDto
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setContent(comment.getContent());

                    // Create and set the UserDto object
                    UserDto userDto = new UserDto();
                    userDto.setId(comment.getUser().getId());
                    userDto.setFirstName(comment.getUser().getFirstName());
                    userDto.setLastName(comment.getUser().getLastName());
                    commentDto.setUser(userDto);

                    return commentDto;
                })
                .collect(Collectors.toList());

        return commentDtos;
    }



    @Override
    public void sharePost(Long postId, Long userId, String additionalContent) {
        // Fetch the original post by its ID
        Optional<Post> optionalOriginalPost = postRepo.findById(postId);
        if (optionalOriginalPost.isPresent()) {
            Post originalPost = optionalOriginalPost.get();

            // Fetch the user who is sharing the post
            Optional<User> optionalSharingUser = userRepo.findById(userId);
            if (optionalSharingUser.isPresent()) {
                User sharingUser = optionalSharingUser.get();

                // Create a new instance of SharedPost
                SharedPost sharedPost = new SharedPost();
                sharedPost.setOriginalPost(originalPost);
                sharedPost.setSharingUser(sharingUser);
                sharedPost.setAdditionalContent(additionalContent);
                sharedPost.setCreatedAt(new Date());

                // Save the shared post
                sharedPostRepo.save(sharedPost);
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } else {
            throw new IllegalArgumentException("Original Post not found");
        }
    }

    @Override
    public List<SharedPostDto> getSharedPosts(Long userId) {

        // Fetch shared posts by sharing user ID from the repository
        List<SharedPost> sharedPosts = sharedPostRepo.findBySharingUserId(userId);
        List<SharedPostDto> sharedPostDtos = new ArrayList<>();

        for (SharedPost sharedPost : sharedPosts) {
            SharedPostDto sharedPostDto = new SharedPostDto();
            sharedPostDto.setId(sharedPost.getId());
            sharedPostDto.setAdditionalContent(sharedPost.getAdditionalContent());
            sharedPostDto.setCreatedAt(sharedPost.getCreatedAt());
            sharedPostDtos.add(sharedPostDto);
        }

        return sharedPostDtos;
    }
    @Override
    public List<PostDto> searchPosts(String query) {
        // Search posts by content containing the query string
        List<Post> postsByContent = postRepo.findByContentContaining(query);

        // Search posts by author's first name containing the query string
        List<Post> postsByFirstName = postRepo.findByAuthorFirstNameContaining(query);

        // Search posts by author's last name containing the query string
        List<Post> postsByLastName = postRepo.findByAuthorLastNameContaining(query);

        // Combine the results of all three queries
        List<Post> allPosts = new ArrayList<>();
        allPosts.addAll(postsByContent);
        allPosts.addAll(postsByFirstName);
        allPosts.addAll(postsByLastName);

        // Remove duplicate posts (if any)
        Set<Post> uniquePosts = new HashSet<>(allPosts);

        // Convert the unique set of posts to PostDto objects
        List<PostDto> postDtos = uniquePosts.stream()
                .map(this::convertToDtoWithFiles) // Use the existing converter method
                .collect(Collectors.toList());

        return postDtos;
    }




}

