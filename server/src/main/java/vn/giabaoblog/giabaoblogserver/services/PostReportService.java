package vn.giabaoblog.giabaoblogserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.domains.PostReport;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.PostDTO;
import vn.giabaoblog.giabaoblogserver.data.repository.PostReportRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostReportService {

    @Autowired
    public PostReportRepository postReportRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostService postService;

    public List<PostDTO> viewAllReportedPosts () {
        List<PostDTO> allPosts = postService.getAllPosts();
        List<Long> reportPostsId = postReportRepository.findAllPostIds();
        List<PostDTO> allReportedPosts =
                allPosts.stream()
                        .filter(post -> reportPostsId.contains(post.getId()))
                        .map(PostDTO::new)
                        .collect(Collectors.toList());
        return allReportedPosts;
    }

    public List<PostDTO> viewAllBannedPosts () {
        List<PostDTO> allPosts = postService.getAllPosts();
        List<PostDTO> allBannedPosts =
                allPosts.stream()
                        .filter(PostDTO::isBanned)
                        .collect(Collectors.toList());
        return allBannedPosts;
    }

    public PostReport banPost(Long postReportId) {
        Optional<PostReport> postReportOpt = postReportRepository.findById(postReportId);
        if (!postReportOpt.isPresent()) {
            throw new NotFoundException(String.format("Post report not found"));
        }
        PostReport postReport = postReportOpt.get();
        Long postId = postReport.getPostId();
        Optional<Post> postOpt = postRepository.findById(postId);
        if (!postOpt.isPresent()) {
            throw new NotFoundException(String.format("Post not found"));
        }
        Post post = postOpt.get();
        post.setBanned(true);
        postRepository.save(post);
        postReportRepository.delete(postReport);
        return postReport;
    }
}
