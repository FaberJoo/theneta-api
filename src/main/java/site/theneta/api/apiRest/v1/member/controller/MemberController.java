package site.theneta.api.apiRest.v1.member.controller;

import java.net.URI;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.theneta.api.apiRest.v1.member.controller.request.SignupRequest;
import site.theneta.api.apiRest.v1.member.service.MemberService;

@Slf4j
@Tag(name = "Post API", description = "게시글 API")
@RestController
@RequestMapping("v1/members")
@Validated
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(
            @RequestBody @Valid SignupRequest req) {
        memberService.signup(req);

        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/{value}")
    public String hello(
            @PathVariable("value") @Min(5) Long value
    ) {
        return value.toString();
    }
}
