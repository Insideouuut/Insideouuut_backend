package com.goorm.insideout.image.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetingImage is a Querydsl query type for MeetingImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingImage extends EntityPathBase<MeetingImage> {

    private static final long serialVersionUID = 269881250L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetingImage meetingImage = new QMeetingImage("meetingImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QImage image;

    public final com.goorm.insideout.meeting.domain.QMeeting meeting;

    public QMeetingImage(String variable) {
        this(MeetingImage.class, forVariable(variable), INITS);
    }

    public QMeetingImage(Path<? extends MeetingImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetingImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetingImage(PathMetadata metadata, PathInits inits) {
        this(MeetingImage.class, metadata, inits);
    }

    public QMeetingImage(Class<? extends MeetingImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new QImage(forProperty("image")) : null;
        this.meeting = inits.isInitialized("meeting") ? new com.goorm.insideout.meeting.domain.QMeeting(forProperty("meeting"), inits.get("meeting")) : null;
    }

}

