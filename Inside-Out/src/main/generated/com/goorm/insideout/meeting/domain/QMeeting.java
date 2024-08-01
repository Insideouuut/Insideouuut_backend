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

    public static final QMeeting meeting = new QMeeting("meeting");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> femaleRatio = createNumber("femaleRatio", Integer.class);

    public final StringPath genderCondition = createString("genderCondition");

    public final BooleanPath hasMembershipFee = createBoolean("hasMembershipFee");

    public final StringPath hobby = createString("hobby");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.goorm.insideout.image.domain.Image, com.goorm.insideout.image.domain.QImage> images = this.<com.goorm.insideout.image.domain.Image, com.goorm.insideout.image.domain.QImage>createList("images", com.goorm.insideout.image.domain.Image.class, com.goorm.insideout.image.domain.QImage.class, PathInits.DIRECT2);

    public final StringPath joinQuestion = createString("joinQuestion");

    public final EnumPath<Level> level = createEnum("level", Level.class);

    public final ListPath<com.goorm.insideout.like.domain.MeetingLike, com.goorm.insideout.like.domain.QMeetingLike> likes = this.<com.goorm.insideout.like.domain.MeetingLike, com.goorm.insideout.like.domain.QMeetingLike>createList("likes", com.goorm.insideout.like.domain.MeetingLike.class, com.goorm.insideout.like.domain.QMeetingLike.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    public final NumberPath<Integer> maleRatio = createNumber("maleRatio", Integer.class);

    public final NumberPath<Integer> maximumAge = createNumber("maximumAge", Integer.class);

    public final NumberPath<Integer> membershipFee = createNumber("membershipFee", Integer.class);

    public final NumberPath<Integer> minimumAge = createNumber("minimumAge", Integer.class);

    public final NumberPath<Integer> participantLimit = createNumber("participantLimit", Integer.class);

    public final NumberPath<Integer> participantsNumber = createNumber("participantsNumber", Integer.class);

    public final StringPath rule = createString("rule");

    public final DateTimePath<java.time.LocalDateTime> schedule = createDateTime("schedule", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> view = createNumber("view", Integer.class);

    public QMeeting(String variable) {
        super(Meeting.class, forVariable(variable));
    }

    public QMeeting(Path<? extends Meeting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMeeting(PathMetadata metadata) {
        super(Meeting.class, metadata);
    }

}

