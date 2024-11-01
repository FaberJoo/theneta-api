-- 1. Member 테이블
CREATE TABLE member (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'GUEST',  -- GUEST, USER, ADMIN 역할 추가
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. AuthLocal 테이블
CREATE TABLE auth_local (
    id UUID PRIMARY KEY,
    member_id UUID REFERENCES member(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. AuthSocial 테이블
CREATE TABLE auth_social (
    id UUID PRIMARY KEY,
    member_id UUID REFERENCES member(id) ON DELETE CASCADE,
    type VARCHAR(64) NOT NULL,
    social_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Profile 테이블
CREATE TABLE profile (
    id UUID PRIMARY KEY,
    member_id UUID REFERENCES member(id) ON DELETE CASCADE,
    name VARCHAR(32) NOT NULL,
    info TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Follow 테이블
CREATE TABLE follow (
    id UUID PRIMARY KEY,
    follower UUID REFERENCES member(id) ON DELETE CASCADE,
    following UUID REFERENCES member(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Post 테이블
CREATE TABLE post (
    id UUID PRIMARY KEY,
    member_id UUID REFERENCES member(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    origin_post UUID REFERENCES post(id) ON DELETE SET NULL, -- Optional
    reactions INT DEFAULT 0, -- 반응 수
    replies INT DEFAULT 0, -- 답글 수
    mentions INT DEFAULT 0, -- 멘션된 사용자 수
    shares INT DEFAULT 0, -- 재게시 및 인용된 수
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 타임존 포함된 수정일
    deleted_at TIMESTAMP -- Optional, 논리적 삭제
);

-- 7. Tag 테이블
CREATE TABLE tag (
    id UUID PRIMARY KEY,
    tag_name VARCHAR(32) NOT NULL
);

-- 8. PostTag 테이블
CREATE TABLE post_tag (
    id UUID PRIMARY KEY,
    post_id UUID REFERENCES post(id) ON DELETE CASCADE,
    tag_id UUID REFERENCES tag(id) ON DELETE CASCADE
);

-- 9. ReactionType 테이블
CREATE TABLE reaction_type (
    id UUID PRIMARY KEY,
    type_name VARCHAR(32) NOT NULL
);

-- 10. Reaction 테이블
CREATE TABLE reaction (
    id UUID PRIMARY KEY,
    post_id UUID REFERENCES post(id) ON DELETE CASCADE,
    member_id UUID REFERENCES member(id) ON DELETE CASCADE,
    reaction_type_id UUID REFERENCES reaction_type(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);
