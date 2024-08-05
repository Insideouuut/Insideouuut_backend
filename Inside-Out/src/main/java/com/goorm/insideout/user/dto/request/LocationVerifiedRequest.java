package com.goorm.insideout.user.dto.request;

import java.util.List;

import lombok.Getter;

@Getter
public class LocationVerifiedRequest {
	private List<String> locations;
	private Boolean isVerified;
}
