package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Entity.Bookmark;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Service.BookmarkService;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("/add")
    public ResponseEntity<String> addBookmark(@RequestParam Long userId, @RequestParam Long postId) {
        bookmarkService.addBookmark(userId, postId);
        return ResponseEntity.ok("Bookmark added successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeBookmark(@RequestParam Long userId, @RequestParam Long postId) {
        bookmarkService.removeBookmark(userId, postId);
        return ResponseEntity.ok("Bookmark removed successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostDto>> getBookmarksByUserId(@PathVariable Long userId) {
        List<PostDto> bookmarkedPosts = bookmarkService.getBookmarkedPostsDto(userId);
        return ResponseEntity.ok(bookmarkedPosts);
    }


}
