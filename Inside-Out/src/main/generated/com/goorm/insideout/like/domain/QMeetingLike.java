package com.goorm.insideout.like.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingLike is a Querydsl query type for MeetingLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingLike extends EntityPathBase<MeetingLike> {

    private static final long serialVersionUID = -87201686L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingLike meetingLike = new QMeetingLike("meetingLike");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.goorm.insideout.meeting.domain.QMeeting meeting;

    public QMeetingLike(String variable) {
        this(MeetingLike.class, forVariable(variable), INITS);
    }

    public QMeetingLike(Path<? extends MeetingLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingLike(PathMetadata metadata, PathInits inits) {
        this(MeetingLike.class, metadata, inits);
    }

    public QMeetingLike(Class<? extends MeetingLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meeting = inits.isInitialized("meeting") ? new com.goorm.insideout.meeting.domain.QMeeting(forProperty("meeting")) : null;
    }

}

