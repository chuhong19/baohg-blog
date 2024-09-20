package vn.giabaoblog.giabaoblogserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.NotificationHandler;
import vn.giabaoblog.giabaoblogserver.config.exception.DuplicateException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.domains.PostLike;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.PostIdDTO;
import vn.giabaoblog.giabaoblogserver.data.enums.LikeStatus;
import vn.giabaoblog.giabaoblogserver.data.repository.PostLikeRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.PostRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class PostLikeService {

    @Autowired
    public final PostLikeRepository postLikeRepository;

    @Autowired
    public final PostRepository postRepository;

    @Autowired
    private NotificationHandler notificationHandler;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    public void likePost(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.LIKE);
        Optional<PostLike> existingDislike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.DISLIKE);

        if (existingLike.isPresent()) {
            throw new DuplicateException("You have already liked this post");
        } else {
            if (existingDislike.isPresent()) {
                PostLike postDislike = existingDislike.get();
                postLikeRepository.delete(postDislike);
            }
            PostLike postLike = new PostLike(postId, userId, LikeStatus.LIKE);
            postLikeRepository.save(postLike);

            Long authorId = existingPost.get().getAuthorId();
            String notificationMessage = "User " + principal.getUsername() + " liked your post!";
            try {
                System.out.println("Notification message: " + notificationMessage);
                notificationHandler.sendNotification(authorId, notificationMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dislikePost(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.LIKE);
        Optional<PostLike> existingDislike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.DISLIKE);

        if (existingDislike.isPresent()) {
            throw new DuplicateException("You have already disliked this post");
        } else {
            if (existingLike.isPresent()) {
                PostLike postLike = existingLike.get();
                postLikeRepository.delete(postLike);
            }
            PostLike postDislike = new PostLike(postId, userId, LikeStatus.DISLIKE);
            postLikeRepository.save(postDislike);
        }
    }

    public void unlikePost(PostIdDTO request) {
        Long postId = request.getPostId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.LIKE);

        if (existingLike.isPresent()) {
            PostLike postLike = existingLike.get();
            postLikeRepository.delete(postLike);

        } else {
            throw new NotFoundException("You haven't already liked this post");
        }
    }

    public void unDislikePost(PostIdDTO request) {
        Long postId = request.getPostId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<PostLike> existingDislike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.DISLIKE);

        if (existingDislike.isPresent()) {
            PostLike postDislike = existingDislike.get();
            postLikeRepository.delete(postDislike);

        } else {
            throw new NotFoundException("You haven't already disliked this post");
        }
    }

    public Integer getLikeCount(PostIdDTO request) {
        Long postId = request.getPostId();
        Integer likeCount = postLikeRepository.getLikeCountByPostId(postId);
        return likeCount != null ? likeCount : 0;
    }

    public Integer getDislikeCount(PostIdDTO request) {
        Long postId = request.getPostId();
        Integer dislikeCount = postLikeRepository.getDislikeCountByPostId(postId);
        return dislikeCount != null ? dislikeCount : 0;
    }

    public Integer checkStatus(PostIdDTO request) {
        Long postId = request.getPostId();
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.LIKE);
        if (existingLike.isPresent()) {
            return 1;
        }
        Optional<PostLike> existingDislike = postLikeRepository.findByPostIdAndUserIdAndLikeStatus(postId, userId, LikeStatus.DISLIKE);
        if (existingDislike.isPresent()) {
            return -1;
        }
        return 0;
    }
}
