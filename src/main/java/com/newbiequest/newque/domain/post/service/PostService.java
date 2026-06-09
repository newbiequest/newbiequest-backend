package com.newbiequest.newque.domain.post.service;

import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.repository.MemberRepository;
import com.newbiequest.newque.domain.post.dto.request.PostRequest;
import com.newbiequest.newque.domain.post.dto.request.PostUpdateRequest;
import com.newbiequest.newque.domain.post.dto.response.PostDeleteResponse;
import com.newbiequest.newque.domain.post.dto.response.PostResponse;
import com.newbiequest.newque.domain.post.entity.Post;
import com.newbiequest.newque.domain.post.repository.PostRepository;
import com.newbiequest.newque.global.client.openai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Post registerPost(PostRequest postRequest, Member member) {
        Post post = Post.builder()
                .member(member)
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .build();

        Post createPost = postRepository.save(post);

        return createPost;
    }

    public PostResponse toPostResponse(Post post) {
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .member(post.getMember())
                .title(post.getTitle())
                .body(post.getBody())
                .likes(post.getLikes())
                .createAt(post.getCreateAt())
                .build();

        return postResponse;
    }

    @Transactional
    public Boolean isPostOwner(Long postId, Member member) {
        return postRepository.findById(postId).orElseThrow()
                .getMember().equals(member);
    }

    @Transactional
    public void postDelete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        postRepository.delete(post);
    }

    public PostDeleteResponse getPostDeleteResponse() {
        PostDeleteResponse postDeleteResponse = PostDeleteResponse.builder()
                .message("Post delete completed")
                .build();

        return postDeleteResponse;
    }

    @Transactional
    public void modifyPost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId).orElseThrow();

        if (!post.getTitle().equals(postUpdateRequest.getTitle())) {
            post.updateTitle(postUpdateRequest.getTitle());
        }

        if (!post.getBody().equals(postUpdateRequest.getBody())) {
            post.updateBody(postUpdateRequest.getBody());
        }

        System.out.println();
    }

    @Transactional
    public PostResponse postIdToPostResponse(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.orElseThrow();
        PostResponse postResponse = toPostResponse(post);

        return postResponse;
    }

    @Transactional
    public List<Post> getPostsByMemberId(Long memberId) {
        List<Post> posts = postRepository.findByMemberId(memberId);

        return posts;
    }

    public List<PostResponse> postsToPostResponses(List<Post> posts) {

        List<PostResponse> responseList = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .member(post.getMember())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .likes(post.getLikes())
                    .createAt(post.getCreateAt())
                    .build();

            responseList.add(response);
        }

        return  responseList;
    }

    @Transactional
    public void updateLikes(Long postId, Boolean ifLiked) {
        Post post = postRepository.findById(postId).orElseThrow();

        if (ifLiked) {
            post.pressLikes();
        }

        else if (ifLiked == false) {
            post.unpressLikes();
        }
    }
}
