package com.newbiequest.newque.domain.member.service;

import com.newbiequest.newque.domain.member.dto.request.LogInRequest;
import com.newbiequest.newque.domain.member.dto.request.MemberSignUpRequest;
import com.newbiequest.newque.domain.member.dto.request.SignOutRequest;
import com.newbiequest.newque.domain.member.dto.response.LogInResponse;
import com.newbiequest.newque.domain.member.dto.response.MemberSignUpResponse;
import com.newbiequest.newque.domain.member.dto.response.SignOutResponse;
import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Member signUp(MemberSignUpRequest memberSignUpRequest) {
        if (memberSignUpRequest.getConsentToTerms()) {
            Member member = Member.builder()
                    .nickname(memberSignUpRequest.getNickname())
                    .password(memberSignUpRequest.getPassword())
                    .build();

            Member createMember = memberRepository.save(member);
            return createMember;
        }

        else {
            return null;
        }
    }

    public MemberSignUpResponse toMemberSignUpResponse(Member member) {
        MemberSignUpResponse memberSignUpResponse = MemberSignUpResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .accessToken(createAccessToken(member.getId()))
                .build();

        return memberSignUpResponse;
    }

    public Long createAccessToken(Long memberId) {
        return memberId * 10 + 5;
    }

    public Member retrieveToken(Long accessToken) {
        Long memberId = (accessToken - 5) / 10;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(
                () -> new RuntimeException("Member does not exists")
        );
        return member;
    }

    public Member signIn(LogInRequest logInRequest) {
        Member member = memberRepository.findByNickname(logInRequest.getNickname()).orElseThrow();
        if (member.getPassword().equals(logInRequest.getPassword()))
            return member;

        else {
            throw new RuntimeException("Password error");
        }
    }

    public LogInResponse getLogInResponse(Member member) {
        LogInResponse logInResponse = LogInResponse.builder()
                .nickname(member.getNickname())
                .accessToken(createAccessToken(member.getId()))
                .build();

        return logInResponse;
    }

    public void signOut(SignOutRequest signOutRequest) {
        Member member = memberRepository.findById(signOutRequest.getToDeleteId()).orElseThrow();
        memberRepository.delete(member);
    }

    public SignOutResponse getSignOutResponse() {
        SignOutResponse signOutResponse = SignOutResponse.builder()
                .message("Sign out completed")
                .build();

        return signOutResponse;
    }
}
