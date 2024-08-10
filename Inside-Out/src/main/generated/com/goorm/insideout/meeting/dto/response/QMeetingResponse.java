package com.goorm.insideout.meeting.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.goorm.insideout.meeting.dto.response.QMeetingResponse is a Querydsl Projection type for MeetingResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMeetingResponse extends ConstructorExpression<MeetingResponse> {

    private static final long serialVersionUID = 1064507174L;

    public QMeetingResponse(com.querydsl.core.types.Expression<? extends com.goorm.insideout.meeting.domain.Meeting> meeting) {
        super(MeetingResponse.class, new Class<?>[]{com.goorm.insideout.meeting.domain.Meeting.class}, meeting);
    }

}

