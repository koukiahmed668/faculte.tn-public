package tn.faculte.facultebackend.Service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.FileEntityDTO;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Entity.Bookmark;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.BookmarkRepo;
import tn.faculte.facultebackend.Repo.PostRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.BookmarkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceIMPL implements BookmarkService {
    @Autowired
    private BookmarkRepo bookmarkRepository;
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PostRepo postRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addBookmark(Long userId, Long postId) {
        // Fetch user and post entities
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();

        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            throw new IllegalArgumentException("Post not found");
        }
        Post post = postOptional.get();

        // Check if the bookmark already exists
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);
        if (existingBookmark.isPresent()) {
            throw new IllegalStateException("Bookmark already exists");
        }

        // Create and save the new bookmark
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setPost(post);
        bookmarkRepository.save(bookmark);
    }
    @Override
    public void removeBookmark(Long userId, Long postId) {
        // Find and delete the bookmark
        Optional<Bookmark> bookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);
        bookmark.ifPresent(bookmarkRepository::delete);
    }
    @Override
    public List<PostDto> getBookmarkedPostsDto(Long userId) {
        List<Post> bookmarkedPosts = getBookmarkedPosts(userId);
        return bookmarkedPosts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        // Populate PostDto fields from Post object
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
        return postDto;
    }

    @Override
    public List<Post> getBookmarkedPosts(Long userId) {
        return bookmarkRepository.findBookmarkedPostsByUserId(userId);
    }


}
