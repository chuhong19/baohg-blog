package vn.giabaoblog.giabaoblogserver.services;

import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.AccessException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.*;
import vn.giabaoblog.giabaoblogserver.data.dto.request.SearchPostRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.CreateOrUpdatePostDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.PostDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.PostReportRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.PostRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PostReportRepository postReportRepository;

    @Autowired
    public final UserService userService;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostReportRepository postReportRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postReportRepository = postReportRepository;
        this.userService = userService;
    }

    public List<PostDTO> getAllPosts() {

        List<PostDTO> allPosts = postRepository.findAll().stream()
                .filter(post -> !post.isBanned())
                .map(post -> {
                    String authorName = userRepository.findById(post.getAuthorId())
                            .map(User::getUsername)
                            .orElse(null);
                    return new PostDTO(post, authorName);
                })
                .collect(Collectors.toList());
        return allPosts;

    }

    public List<PostDTO> getAllMyPosts() {

        List<PostDTO> allPosts = getAllPosts();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        List<PostDTO> allMyPosts =
                allPosts.stream()
                        .filter(post -> userId.equals(post.getAuthorId()))
                        .filter(post -> !post.isBanned())
                        .collect(Collectors.toList());
        return allMyPosts;
    }

    public List<PostDTO> getAllMyFollowPosts() {
        List<PostDTO> allPosts = getAllPosts();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
//        Long userId = principal.getId();
        List<Long> allMyFollowUsers = userService.getFollow();
        List<PostDTO> allMyFollowPosts =
                allPosts.stream()
                        .filter(post -> allMyFollowUsers.contains(post.getAuthorId()))
                        .collect(Collectors.toList());
        return allMyFollowPosts;
    }

    public List<PostDTO> getAllAuthorPosts(Long userId) {
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Long> authorId = new ArrayList<>();
        authorId.add(userId);
        Page<Post> postPage = filterPost(new SearchPostRequest(0, 20, titles, authorId));
        List<PostDTO> postList = postPage.getContent().stream()
                .filter(post -> !post.isBanned())
                .map(post -> {
                    String authorName = userRepository.findById(post.getAuthorId())
                            .map(User::getUsername)
                            .orElse(null);
                    return new PostDTO(post, authorName);
                })
                .collect(Collectors.toList());
        return postList;
    }


    public PostDTO createPost(CreateOrUpdatePostDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorId(userId);
        postRepository.save(post);
        PostDTO postDTO = new PostDTO(post);
        return postDTO;
    }

    public PostDTO updatePost(CreateOrUpdatePostDTO request) {
        String title = request.getTitle();
        String content = request.getContent();
        Long postId = request.getPostId();
        if (postId == null) {
            throw new NotFoundException(String.format("Post not found"));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<Post> postOpt = postRepository.findById(postId);
        if (!postOpt.isPresent()) {
            throw new NotFoundException(String.format("Post not found with Id = %s", postId));
        }
        Post post = postOpt.get();
        if (!post.getAuthorId().equals(userId)) {
            throw new AccessException(String.format("You only can update your post"));
        }
        if (title != null) {
            post.setTitle(request.getTitle());
        }
        if (content != null) {
            post.setContent(request.getContent());
        }
        postRepository.save(post);
        PostDTO postDTO = new PostDTO(post);
        return postDTO;
    }

    public void deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<Post> postOpt = postRepository.findById(postId);
        if (!postOpt.isPresent()) {
            throw new NotFoundException(String.format("Post not found with Id = %s", postId));
        }
        Post post = postOpt.get();
        if (!post.getAuthorId().equals(userId)) {
            throw new AccessException(String.format("You only can delete your post"));
        }
        postRepository.delete(post);
    }


    public Page<Post> filterPost(SearchPostRequest request) {
        List<String> titles = request.getTitles();
        List<Long> authors = request.getAuthorId();
        Integer page = request.getPage();
        Integer limit = request.getLimit();

        Specification<Post> conditions = (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (titles != null && !titles.isEmpty()) {
                predicates.add(root.get(Post_.TITLE).in(titles));
            }

            if (authors != null && !authors.isEmpty()) {
                predicates.add(root.get(Post_.AUTHOR_ID).in(authors));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page, limit, Sort.by(User_.ID).descending());

        return postRepository.findAll(conditions, pageable);
    }

    public void reportPost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        Optional<Post> postOpt = postRepository.findById(postId);
        if (!postOpt.isPresent()) {
            throw new NotFoundException(String.format("Post not found with Id = %s", postId));
        }

        Post post = postOpt.get();
        Long authorId = post.getAuthorId();

        if (post.banned) {
            throw new NotFoundException("Post has already banned");
        }

        boolean hasReported = postReportRepository.existsByPostIdAndReportUserId(postId, userId);
        if (hasReported) {
            throw new IllegalStateException("You have already reported this post.");
        }

        PostReport postReport = new PostReport();
        postReport.setPostId(postId);
        postReport.setAuthorId(authorId);
        postReport.setReportUserId(userId);
        postReportRepository.save(postReport);
    }


}
