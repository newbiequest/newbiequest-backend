package com.newbiequest.newque.domain.member.controller;

import com.newbiequest.newque.domain.member.dto.request.LogInRequest;
import com.newbiequest.newque.domain.member.dto.request.MemberSignUpRequest;
import com.newbiequest.newque.domain.member.dto.request.SignOutRequest;
import com.newbiequest.newque.domain.member.dto.response.LogInResponse;
import com.newbiequest.newque.domain.member.dto.response.MemberSignUpResponse;
import com.newbiequest.newque.domain.member.dto.response.SignOutResponse;
import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/healthcheck")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<MemberSignUpResponse> signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        Member member = memberService.signUp(memberSignUpRequest);
        MemberSignUpResponse memberSignUpResponse = memberService.toMemberSignUpResponse(member);
        log.info(memberSignUpResponse.getId().toString());

        return ResponseEntity.ok().body(memberSignUpResponse);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LogInRequest logInRequest) {
        Member member = memberService.signIn(logInRequest);
        LogInResponse logInResponse = memberService.getLogInResponse(member);

        return ResponseEntity.ok().body(logInResponse);
    }

    @PostMapping("/auth/signout")
    public ResponseEntity<SignOutResponse> signOut(@RequestBody SignOutRequest signOutRequest) {
        memberService.signOut(signOutRequest);
        SignOutResponse signOutResponse = memberService.getSignOutResponse();

        return ResponseEntity.ok().body(signOutResponse);
    }
}
