package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Entity.Bookmark;
import tn.faculte.facultebackend.Entity.Post;

import java.util.List;

public interface BookmarkService {
    void addBookmark(Long userId, Long postId);
    void removeBookmark(Long userId, Long postId);
    public List<PostDto> getBookmarkedPostsDto(Long userId);
    public List<Post> getBookmarkedPosts(Long userId);

    }
