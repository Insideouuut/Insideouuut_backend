package com.goorm.insideout.meeting.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMeetingPlace is a Querydsl query type for MeetingPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetingPlace extends EntityPathBase<MeetingPlace> {

    private static final long serialVersionUID = -1098126994L;

    public static final QMeetingPlace meetingPlace = new QMeetingPlace("meetingPlace");

    public final StringPath addressName = createString("addressName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoMapId = createString("kakaoMapId");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final StringPath placeUrl = createString("placeUrl");

    public final StringPath roadAddressName = createString("roadAddressName");

    public QMeetingPlace(String variable) {
        super(MeetingPlace.class, forVariable(variable));
    }

    public QMeetingPlace(Path<? extends MeetingPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMeetingPlace(PathMetadata metadata) {
        super(MeetingPlace.class, metadata);
    }

}

