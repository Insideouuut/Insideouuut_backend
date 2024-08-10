package com.goorm.insideout.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubComment is a Querydsl query type for ClubComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubComment extends EntityPathBase<ClubComment> {

    private static final long serialVersionUID = 386798049L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubComment clubComment = new QClubComment("clubComment");

    public final QClubPost clubPost;

    public final QClubUser clubUser;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> dateTime = createDateTime("dateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath writer = createString("writer");

    public QClubComment(String variable) {
        this(ClubComment.class, forVariable(variable), INITS);
    }

    public QClubComment(Path<? extends ClubComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubComment(PathMetadata metadata, PathInits inits) {
        this(ClubComment.class, metadata, inits);
    }

    public QClubComment(Class<? extends ClubComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubPost = inits.isInitialized("clubPost") ? new QClubPost(forProperty("clubPost"), inits.get("clubPost")) : null;
        this.clubUser = inits.isInitialized("clubUser") ? new QClubUser(forProperty("clubUser"), inits.get("clubUser")) : null;
    }

}

