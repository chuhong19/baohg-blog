package vn.giabaoblog.giabaoblogserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PostCommentDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PostIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.UserIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.request.CommentPostRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.DeleteCommentRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.SearchPostRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.CreateOrUpdatePostDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.PostDTO;
import vn.giabaoblog.giabaoblogserver.services.PostCommentService;
import vn.giabaoblog.giabaoblogserver.services.PostLikeService;
import vn.giabaoblog.giabaoblogserver.services.PostService;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    public PostService postService;

    @Autowired
    public PostLikeService postLikeService;

    @Autowired
    public PostCommentService postCommentService;

    @GetMapping("/getAllPosts")
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/getAllMyPosts")
    public List<PostDTO> getAllMyPosts() {
        return postService.getAllMyPosts();
    }

    @GetMapping("/getAllMyFollowPosts")
    public List<PostDTO> getAllMyFollowPosts() {
        return postService.getAllMyFollowPosts();
    }

    @PostMapping("/getAllAuthorPosts")
    public List<PostDTO> getAllAuthorPosts(@RequestBody UserIdDTO userIdDTO) {
        Long userId = userIdDTO.getUserId();
        return postService.getAllAuthorPosts(userId);
    }

    @PostMapping("/create")
    public StandardResponse createPost(@RequestBody CreateOrUpdatePostDTO request) {
        PostDTO postDTO = postService.createPost(request);
        return StandardResponse.create("200", "Post created", postDTO);
    }

    @PostMapping("/update")
    public StandardResponse updatePost(@RequestBody CreateOrUpdatePostDTO request) {
        PostDTO postDTO = postService.updatePost(request);
        return StandardResponse.create("200", "Post updated", postDTO);
    }

    @PostMapping("/delete")
    public StandardResponse deletePost(@RequestBody PostIdDTO request) {
        Long postId = request.getPostId();
        postService.deletePost(postId);
        return StandardResponse.create("204", "Post deleted", postId);
    }

    @PostMapping("/filterPost")
    public Page<Post> filterPost(@RequestBody SearchPostRequest request) {
        return postService.filterPost(request);
    }

    @PostMapping("/reportPost")
    public StandardResponse reportPost(@RequestBody PostIdDTO request) {
        Long postId = request.getPostId();
        postService.reportPost(postId);
        return StandardResponse.create("204", "Post reported", postId);
    }

    @PostMapping("/likePost")
    public void likePost(@RequestBody PostIdDTO request) {
        postLikeService.likePost(request);
    }

    @PostMapping("/dislikePost")
    public void dislikePost(@RequestBody PostIdDTO request) {
        postLikeService.dislikePost(request);
    }

    @PostMapping("/unlikePost")
    public void unlikePost(@RequestBody PostIdDTO request) {
        postLikeService.unlikePost(request);
    }

    @PostMapping("/undislikePost")
    public void undislikePost(@RequestBody PostIdDTO request) {
        postLikeService.unDislikePost(request);
    }

    @PostMapping("/getLikeCount")
    public Integer getLikeCount(@RequestBody PostIdDTO request) {
        return postLikeService.getLikeCount(request);
    }

    @PostMapping("/getDislikeCount")
    public Integer getDislikeCount(@RequestBody PostIdDTO request) {
        return postLikeService.getDislikeCount(request);
    }

    @PostMapping("/checkStatus")
    public Integer checkStatus(@RequestBody PostIdDTO request) {
        return postLikeService.checkStatus(request);
    }

    @PostMapping("/commentPost")
    public void commentPost(@RequestBody CommentPostRequest request) {
        postCommentService.commentPost(request);
    }

    @PostMapping("/deleteCommentPost")
    public void deleteCommentPost(@RequestBody DeleteCommentRequest request) {
        postCommentService.deleteCommentPost(request);
    }

    @PostMapping("/getAllComments")
    public List<PostCommentDTO> getAllComments(@RequestBody PostIdDTO request) {
        return postCommentService.getAllComments(request);
    }

    @PostMapping("/getCommentCount")
    public Integer getCommentCount(@RequestBody PostIdDTO request) {
        return postCommentService.getCommentCount(request);
    }
}
