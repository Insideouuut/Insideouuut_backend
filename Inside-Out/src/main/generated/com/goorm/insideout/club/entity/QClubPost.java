package com.goorm.insideout.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubPost is a Querydsl query type for ClubPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubPost extends EntityPathBase<ClubPost> {

    private static final long serialVersionUID = -1476332450L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubPost clubPost = new QClubPost("clubPost");

    public final StringPath category = createString("category");

    public final QClub club;

    public final QClubUser clubUser;

    public final ListPath<ClubComment, QClubComment> comments = this.<ClubComment, QClubComment>createList("comments", ClubComment.class, QClubComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final ListPath<com.goorm.insideout.image.domain.ClubPostImage, com.goorm.insideout.image.domain.QClubPostImage> images = this.<com.goorm.insideout.image.domain.ClubPostImage, com.goorm.insideout.image.domain.QClubPostImage>createList("images", com.goorm.insideout.image.domain.ClubPostImage.class, com.goorm.insideout.image.domain.QClubPostImage.class, PathInits.DIRECT2);

    public final StringPath postContent = createString("postContent");

    public final StringPath postTitle = createString("postTitle");

    public final StringPath writer = createString("writer");

    public QClubPost(String variable) {
        this(ClubPost.class, forVariable(variable), INITS);
    }

    public QClubPost(Path<? extends ClubPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubPost(PathMetadata metadata, PathInits inits) {
        this(ClubPost.class, metadata, inits);
    }

    public QClubPost(Class<? extends ClubPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.clubUser = inits.isInitialized("clubUser") ? new QClubUser(forProperty("clubUser"), inits.get("clubUser")) : null;
    }

}

