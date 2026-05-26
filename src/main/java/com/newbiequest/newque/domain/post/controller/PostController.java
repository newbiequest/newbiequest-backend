package com.newbiequest.newque.domain.post.controller;

import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.service.MemberService;
import com.newbiequest.newque.domain.post.dto.request.PostDeleteRequest;
import com.newbiequest.newque.domain.post.dto.request.PostRequest;
import com.newbiequest.newque.domain.post.dto.request.PostUpdateRequest;
import com.newbiequest.newque.domain.post.dto.response.PostDeleteResponse;
import com.newbiequest.newque.domain.post.dto.response.PostResponse;
import com.newbiequest.newque.domain.post.entity.Post;
import com.newbiequest.newque.domain.post.repository.PostRepository;
import com.newbiequest.newque.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity<PostResponse> registerPost(@RequestBody PostRequest postRequest) {
        Member member = memberService.retrieveToken(postRequest.getAccessToken());
        Post post = postService.registerPost(postRequest, member);
        PostResponse postResponse = postService.toPostResponse(post);

        return ResponseEntity.ok(postResponse);
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponse> modifyPost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        Member member = memberService.retrieveToken(postUpdateRequest.getAccessToken());

        if (postService.isPostOwner(postId, member)) {
            postService.modifyPost(postId, postUpdateRequest);
        } else {
            throw new RuntimeException("post owner does not match with token");
        }

        PostResponse postResponse = postService.postIdToPostResponse(postId);

        return ResponseEntity.ok().body(postResponse);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostDeleteResponse> deletePost(
            @PathVariable Long postId,
            @RequestBody PostDeleteRequest postDeleteRequest
    ) {
        Member member = memberService.retrieveToken(postDeleteRequest.getAccessToken());

        if (postService.isPostOwner(postId, member)) {
            postService.postDelete(postId);
        } else {
            throw new RuntimeException("post owner does not match with token");
        }

        PostDeleteResponse postDeleteResponse = postService.getPostDeleteResponse();

        return ResponseEntity.ok().body(postDeleteResponse);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> getPostByPostId(@PathVariable Long postId) {
        PostResponse postResponse = postService.postIdToPostResponse(postId);

        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping("/post/member/{memberId}")
    public ResponseEntity<List<PostResponse>> getPostsByMemberId(@PathVariable Long memberId) {
        List<Post> posts = postService.getPostsByMemberId(memberId);
        List<PostResponse> postResponses = postService.postsToPostResponses(posts);

        return ResponseEntity.ok().body(postResponses);
    }

    @GetMapping("/post/{postId}/like/{ifLiked}")
    public ResponseEntity<PostResponse> updateLikes(
            @PathVariable Long postId,
            @PathVariable Boolean ifLiked,
            @RequestHeader("Authorization") Long accessToken
    ) {
        memberService.retrieveToken(accessToken);

        postService.updateLikes(postId, ifLiked);
        PostResponse postResponse = postService.postIdToPostResponse(postId);

        return ResponseEntity.ok().body(postResponse);
    }
}
