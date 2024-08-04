package com.goorm.insideout.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// user
	USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보가 존재하지 않습니다"),
	INCORRECT_AUTH_INFO(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다"),
	DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다"),
	DUPLICATE_USER_LOGIN_ID(HttpStatus.CONFLICT, "중복된 로그인 아이디입니다"),
	DUPLICATE_USER_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다"),
	DUPLICATE_USER_PHONE_NUMBER(HttpStatus.CONFLICT, "중복된 전화번호입니다"),

	// auth
	NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"쿠키에 refresh token 을 찾아올 수 없습니다"),
	EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 로그인 토큰입니다."),
	INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 로그인 토큰입니다."),
	NOT_BEARER_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "Bearer 타입의 토큰이 아닙니다."),
	NEED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
	INCORRECT_PASSWORD_OR_ACCOUNT(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸거나, 해당 계정이 없습니다."),
	ACCOUNT_USERNAME_EXIST(HttpStatus.UNAUTHORIZED, "해당 계정이 존재합니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "존재하지 않은 리프래쉬 토큰으로 재발급 요청을 했습니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프래쉬 토큰입니다."),
	OAUTH2_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth2 제공자 서버에 문제가 발생했습니다."),
	OAUTH2_INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 OAuth2 에러가 발생했습니다."),
	OPEN_ID_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OpenID 제공자 서버에 문제가 발생했습니다."),

	// club
	CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 동아리입니다."),
	CLUB_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 동아리입니다."),
	CLUB_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "동아리에 가입되어 있지 않습니다."),
	CLUB_ALREADY_JOINED(HttpStatus.CONFLICT, "이미 가입된 동아리입니다."),
	CLUB_NOT_OWNER(HttpStatus.FORBIDDEN, "동아리의 호스트가 아닙니다."),
	CLUB_NOT_MEMBER(HttpStatus.FORBIDDEN, "동아리의 멤버가 아닙니다."),

	// meeting
	MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),
	MEETING_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 모임입니다."),
	MEETING_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "모임에 가입되어 있지 않습니다."),
	MEETING_ALREADY_JOINED(HttpStatus.CONFLICT, "이미 가입된 모임입니다."),
	MEETING_NOT_HOST(HttpStatus.FORBIDDEN, "모임의 호스트가 아닙니다."),
	MEETING_NOT_MEMBER(HttpStatus.FORBIDDEN, "모임의 멤버가 아닙니다."),
	MEETING_GENDER_RATIO_INVALID(HttpStatus.BAD_REQUEST, "성별 비율이 올바르지 않습니다."),

	// chat
	CHAT_NOT_EMPTY(HttpStatus.NOT_FOUND, "빈값을 보낼 수 없습니다."),
	CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
	CHAT_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 채팅방입니다."),
	CHAT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "채팅방에 가입되어 있지 않습니다."),
	CHAT_ALREADY_JOINED(HttpStatus.CONFLICT, "이미 가입된 채팅방입니다."),
	CHATROOM_NOT_ALLOWED(HttpStatus.FORBIDDEN, "채팅방에 접근할 수 없습니다."),
	CHAT_NOT_AVAILABLE(HttpStatus.FORBIDDEN, "메시지를 보낼 수 없습니다."),
	NOT_ALLOWED_TO_DELETE_CHATROOM(HttpStatus.FORBIDDEN, "채팅방을 삭제할 수 없습니다."),

	// stomp
	INVALID_STOMP_MESSAGE_HEADER(HttpStatus.NOT_FOUND,"유효한 헤더가 아닙니다"),

	// others
	REQUEST_OK(HttpStatus.OK, "올바른 요청입니다."),

	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다."),
	NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),
	INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
	FOR_TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트용 에러입니다.");

	private final HttpStatus status;
	private final String message;
}