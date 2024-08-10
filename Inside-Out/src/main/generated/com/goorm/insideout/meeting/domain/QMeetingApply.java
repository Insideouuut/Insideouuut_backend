package com.goorm.insideout.meeting.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingApply is a Querydsl query type for MeetingApply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingApply extends EntityPathBase<MeetingApply> {

    private static final long serialVersionUID = -1111845931L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingApply meetingApply = new QMeetingApply("meetingApply");

    public final StringPath answer = createString("answer");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMeeting meeting;

    public final com.goorm.insideout.user.domain.QUser user;

    public QMeetingApply(String variable) {
        this(MeetingApply.class, forVariable(variable), INITS);
    }

    public QMeetingApply(Path<? extends MeetingApply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingApply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingApply(PathMetadata metadata, PathInits inits) {
        this(MeetingApply.class, metadata, inits);
    }

    public QMeetingApply(Class<? extends MeetingApply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meeting = inits.isInitialized("meeting") ? new QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
        this.user = inits.isInitialized("user") ? new com.goorm.insideout.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

