package site.theneta.api.apiMvc.v1.member.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "회원 API", description = "회원 API")
@RestController
@RequestMapping("v1/members")
@Validated
@RequiredArgsConstructor
public class MemberController {
}
