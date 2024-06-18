package tn.faculte.facultebackend.Service;

import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Dto.CommentDto;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Dto.SharedPostDto;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.*;

import java.util.List;

public interface PostService {

    List<PostDto> getAllPostsIncludingFiles();

    Post createPost(UserDto userDto, String content, Long subjectId, List<MultipartFile> files);

    Post savePost(Post post);

    List<PostDto> getPostsBySubjectId(Long subjectId);
    List<PostDto> getPostsByUserSpecialty(String token);
    void deletePost(Long postId, Long loggedInUserId);

    Post updatePost(Long postId, UserDto userDto, String content, List<MultipartFile> files);

    List<PostDto> getPostsByUserId(Long userId);

    int likePost(Long postId, Long userId);

    Comment addComment(Long postId, Comment comment);
    void removeComment(Long postId, Long commentId);
    Comment updateComment(Long commentId, Long userId, String newContent);
    public List<CommentDto> getCommentsByPostId(Long postId);
    void sharePost(Long postId, Long userId, String additionalContent);
    List<SharedPostDto> getSharedPosts(Long userId);
    List<PostDto> searchPosts(String query);

    }
