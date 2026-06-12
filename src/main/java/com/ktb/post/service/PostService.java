package com.ktb.post.service;

import com.ktb.global.exception.BusinessException;
import com.ktb.global.exception.ErrorCode;
import com.ktb.member.domain.Member;
import com.ktb.member.repository.MemberRepository;
import com.ktb.member.service.MemberService;
import com.ktb.post.domain.Post;
import com.ktb.post.repository.PostRepository;
import com.ktb.post.dto.PostRequest;
import com.ktb.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final MemberService memberService;

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

        return postRepository.save(post);
    }

    @Transactional
    public PostResponse.DetailPostResponse getPostDetail(Long postId, Long currentMemberId) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        post.addView();

        PostResponse.DetailPostResponse response = postRepository.findPostDetailByPostId(post.getId(), currentMemberId);

        if (response == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        return response;
    }

    @Transactional
    public PostResponse.UpdatePostResponse updatePost(Long postId, Long currentMemberId, String title, String content) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        if (!post.isOwner(currentMemberId)) {
            throw new BusinessException(ErrorCode.POST_UPDATE_ACCESS_FORBIDDEN);
        }

        post.update(title, content);

        return new PostResponse.UpdatePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent()
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

        postRepository.deleteById(postId);
    }
}
