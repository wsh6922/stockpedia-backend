package com.ktb.post.service;

import com.ktb.global.exception.BusinessException;
import com.ktb.global.exception.ErrorCode;
import com.ktb.member.service.MemberService;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import com.ktb.post.dto.PostRequest;
import com.ktb.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;

    @Transactional
    public Post createPost(Long currentMemberId, PostRequest.CreatePostRequest pc) {

        Post post = new Post(
                null,
                currentMemberId,
                pc.getTitle(),
                pc.getContent(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        return postRepository.save(post);
    }

    @Transactional
    public PostResponse.DetailPostResponse getPostDetail(Long postId, Long currentMemberId) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        postRepository.incrementViewCount(postId);

        PostResponse.DetailPostResponse response = postRepository.findPostDetailByPostId(postId, currentMemberId);

        if (response == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        return response;
    }

    @Transactional
    public PostResponse.UpdatePostResponse updatePost(Long postId, Long currentMemberId, String title, String content) {


        if (postRepository.findByPostId(postId) == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Long id = postRepository.findAuthorIdByPostId(postId);

        if (!id.equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.POST_UPDATE_ACCESS_FORBIDDEN);
        }

        postRepository.updateById(postId, title, content);

        // updated_at이 필요한가?
        // 필요하면 재조회
        // 필요하지 않다면 재조회 없이 응답 디티오에 그냥 담아 컨트롤러로 보낼것

        Post post = postRepository.findByPostId(postId);

        return new PostResponse.UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUpdatedAt()
        );
    }

    @Transactional
    public void deletePost(Long postId, Long currentMemberId) {

        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        Long id = postRepository.findAuthorIdByPostId(postId);

        if (!id.equals(currentMemberId)) {
            throw new BusinessException(ErrorCode.POST_DELETE_ACCESS_FORBIDDEN);
        }

        postRepository.deleteById(postId);
    }
}
