package vn.giabaoblog.giabaoblogserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.NotificationHandler;
import vn.giabaoblog.giabaoblogserver.config.exception.AccessException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.domains.PostComment;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PostCommentDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PostIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.request.CommentPostRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.DeleteCommentRequest;
import vn.giabaoblog.giabaoblogserver.data.repository.PostCommentRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.PostRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostCommentService {
    @Autowired
    public final PostCommentRepository postCommentRepository;

    @Autowired
    public final PostRepository postRepository;

    @Autowired
    public final UserRepository userRepository;

    @Autowired
    private NotificationHandler notificationHandler;

    public PostCommentService(PostCommentRepository postCommentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void commentPost(CommentPostRequest request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        PostComment postComment = new PostComment(postId, userId, request.getContent());
        postCommentRepository.save(postComment);

        Long authorId = existingPost.get().getAuthorId();
        if (Objects.equals(authorId, userId)) return;
        String notificationMessage = principal.getUsername() + " commented in your post!";
        try {
            System.out.println("Notification message: " + notificationMessage);
            notificationHandler.sendNotification(authorId, notificationMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCommentPost(DeleteCommentRequest request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Long commentId = request.getCommentId();
        Optional<PostComment> commentOpt = postCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new NotFoundException("Comment not found");
        }
        PostComment comment = commentOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        if (!comment.getUserId().equals(userId)) {
            throw new AccessException("You can only delete your comment");
        }
        postCommentRepository.delete(comment);
        // check post exist
        // check comment id exist
        // check comment belong to user
        // delete comment
    }

    public List<PostCommentDTO> getAllComments(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        return postCommentRepository.findAll().stream()
                .filter(comment -> Objects.equals(postId, comment.getPostId()))
                .map(comment -> {
                    String username = userRepository.findById(comment.getUserId())
                            .map(User::getUsername)
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    return new PostCommentDTO(comment, username);
                })
                .collect(Collectors.toList());
    }

    public Integer getCommentCount(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        return postCommentRepository.getCommentCountByPostId(postId);
    }
}
