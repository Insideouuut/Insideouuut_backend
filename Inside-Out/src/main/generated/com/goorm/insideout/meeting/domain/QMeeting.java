package com.goorm.insideout.meeting.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeeting is a Querydsl query type for Meeting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeeting extends EntityPathBase<Meeting> {

    private static final long serialVersionUID = -1697413351L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeeting meeting = new QMeeting("meeting");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final StringPath categoryDetail = createString("categoryDetail");

    public final com.goorm.insideout.chatroom.domain.QChatRoom chatRoom;

    public final StringPath description = createString("description");

    public final EnumPath<GenderRatio> genderRatio = createEnum("genderRatio", GenderRatio.class);

    public final BooleanPath hasMembershipFee = createBoolean("hasMembershipFee");

    public final com.goorm.insideout.user.domain.QUser host;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.goorm.insideout.image.domain.MeetingImage, com.goorm.insideout.image.domain.QMeetingImage> images = this.<com.goorm.insideout.image.domain.MeetingImage, com.goorm.insideout.image.domain.QMeetingImage>createList("images", com.goorm.insideout.image.domain.MeetingImage.class, com.goorm.insideout.image.domain.QMeetingImage.class, PathInits.DIRECT2);

    public final SetPath<String, StringPath> joinQuestions = this.<String, StringPath>createSet("joinQuestions", String.class, StringPath.class, PathInits.DIRECT2);

    public final EnumPath<Level> level = createEnum("level", Level.class);

    public final ListPath<com.goorm.insideout.like.domain.MeetingLike, com.goorm.insideout.like.domain.QMeetingLike> likes = this.<com.goorm.insideout.like.domain.MeetingLike, com.goorm.insideout.like.domain.QMeetingLike>createList("likes", com.goorm.insideout.like.domain.MeetingLike.class, com.goorm.insideout.like.domain.QMeetingLike.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maximumAge = createNumber("maximumAge", Integer.class);

    public final QMeetingPlace meetingPlace;

    public final ListPath<MeetingUser, QMeetingUser> meetingUsers = this.<MeetingUser, QMeetingUser>createList("meetingUsers", MeetingUser.class, QMeetingUser.class, PathInits.DIRECT2);

    public final NumberPath<Integer> membershipFee = createNumber("membershipFee", Integer.class);

    public final NumberPath<Integer> minimumAge = createNumber("minimumAge", Integer.class);

    public final NumberPath<Integer> participantLimit = createNumber("participantLimit", Integer.class);

    public final NumberPath<Integer> participantsNumber = createNumber("participantsNumber", Integer.class);

    public final EnumPath<Progress> progress = createEnum("progress", Progress.class);

    public final SetPath<String, StringPath> rules = this.<String, StringPath>createSet("rules", String.class, StringPath.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> schedule = createDateTime("schedule", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

    public QMeeting(String variable) {
        this(Meeting.class, forVariable(variable), INITS);
    }

    public QMeeting(Path<? extends Meeting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeeting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeeting(PathMetadata metadata, PathInits inits) {
        this(Meeting.class, metadata, inits);
    }

    public QMeeting(Class<? extends Meeting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.goorm.insideout.chatroom.domain.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.host = inits.isInitialized("host") ? new com.goorm.insideout.user.domain.QUser(forProperty("host"), inits.get("host")) : null;
        this.meetingPlace = inits.isInitialized("meetingPlace") ? new QMeetingPlace(forProperty("meetingPlace")) : null;
    }

}

