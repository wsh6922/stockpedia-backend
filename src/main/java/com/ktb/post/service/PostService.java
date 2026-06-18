package com.ktb.post.service;

import com.ktb.file.dto.UploadFile;
import com.ktb.global.utils.exception.BusinessException;
import com.ktb.global.utils.exception.ErrorCode;
import com.ktb.member.domain.Member;
import com.ktb.member.repository.MemberRepository;
import com.ktb.member.service.MemberService;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import com.ktb.post.dto.PostRequest;
import com.ktb.post.dto.PostResponse;
import com.ktb.postImage.domain.PostImage;
import com.ktb.postImage.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final PostImageRepository postImageRepository;

    @Transactional
    public Post createPost(Long currentMemberId, PostRequest.CreatePostRequest pc) {

        Member member = memberRepository.findMemberById(currentMemberId);

        if (member == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Post post = new Post(
                member,
                pc.getTitle(),
                pc.getContent(),
                0L,
                0L,
                0L
        );

        Post savedPost = postRepository.save(post);

        if (pc.getUploadFiles() != null) {

            for (UploadFile file : pc.getUploadFiles()) {
                PostImage postImage = new PostImage(
                        savedPost,
                        file.getOriginalName(),
                        file.getStoredPath(),
                        file.getS3Key());
                postImageRepository.save(postImage);
            }
        }

        return savedPost;
    }

    @Transactional
    public PostResponse.DetailPostResponse getPostDetail(Long postId, Long currentMemberId) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        postRepository.incrementViewCount(post.getId());

        PostResponse.DetailPostResponse response = postRepository.findPostDetailByPostId(post.getId(), currentMemberId);

        if (response == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public PostResponse.PostPageResponse getPosts(Long cursor, Integer limit) {

        if (limit == null) {
            limit = 10;
        }
        if (limit < 1 || limit > 30) {
            throw new BusinessException(ErrorCode.INVALID_PAGE_SIZE);
        }

        List<PostResponse.PostSummaryResult> posts =
                postRepository.findPostSummaryResultByCursor(cursor, limit + 1);

        // size보다 많이 왔으면 다음 페이지 있음
        boolean hasNext = posts.size() > limit;

        if (hasNext) {
            posts = posts.subList(0, limit);
        }

        // 다음 cursor = 마지막 글 id (없으면 null)
        Long nextCursor = hasNext ? posts.get(posts.size() - 1).getPostId() : null;

        return new PostResponse.PostPageResponse(posts, nextCursor, hasNext);
    }

    @Transactional
    public PostResponse.UpdatePostResponse updatePost(Long postId, Long currentMemberId, PostRequest.UpdatePostRequest pu) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        if (!post.isOwner(currentMemberId)) {
            throw new BusinessException(ErrorCode.POST_UPDATE_ACCESS_FORBIDDEN);
        }

        if (pu.getTitle() == null && pu.getContent() == null && pu.getUploadFiles() == null) {
            throw new BusinessException(ErrorCode.POST_UPDATE_EMPTY);
        }

        if (pu.getTitle() != null) {
            post.changeTitle(pu.getTitle());
        }

        if (pu.getContent() != null) {
            post.changeContent(pu.getContent());
        }

        List<String> imageUrls = null;

        if (pu.getUploadFiles() != null) {
            postImageRepository.deleteByPostId(post.getId());
            imageUrls = new ArrayList<>();
            for (UploadFile file : pu.getUploadFiles()) {
                PostImage postImage = new PostImage(
                        post,
                        file.getOriginalName(),
                        file.getStoredPath(),
                        file.getS3Key());
                postImageRepository.save(postImage);
                imageUrls.add(postImage.getStoredPath());
            }
        }

        return new PostResponse.UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                imageUrls
        );
    }

    @Transactional
    public void deletePost(Long postId, Long currentMemberId) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        if (!post.isOwner(currentMemberId)) {
            throw new BusinessException(ErrorCode.POST_UPDATE_ACCESS_FORBIDDEN);
        }

        postImageRepository.deleteByPostId(postId);

        postRepository.deleteById(postId);
    }
}
