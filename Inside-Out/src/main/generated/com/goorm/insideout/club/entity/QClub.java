package com.goorm.insideout.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = -1622448482L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final StringPath categoryDetail = createString("categoryDetail");

    public final NumberPath<Long> chat_room_id = createNumber("chat_room_id", Long.class);

    public final com.goorm.insideout.chatroom.domain.QChatRoom chatRoom;

    public final NumberPath<Long> clubId = createNumber("clubId", Long.class);

    public final StringPath clubName = createString("clubName");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath date = createString("date");

    public final EnumPath<GenderRatio> genderRatio = createEnum("genderRatio", GenderRatio.class);

    public final BooleanPath hasMembershipFee = createBoolean("hasMembershipFee");

    public final ListPath<com.goorm.insideout.image.domain.ClubImage, com.goorm.insideout.image.domain.QClubImage> images = this.<com.goorm.insideout.image.domain.ClubImage, com.goorm.insideout.image.domain.QClubImage>createList("images", com.goorm.insideout.image.domain.ClubImage.class, com.goorm.insideout.image.domain.QClubImage.class, PathInits.DIRECT2);

    public final SetPath<String, StringPath> joinQuestions = this.<String, StringPath>createSet("joinQuestions", String.class, StringPath.class, PathInits.DIRECT2);

    public final EnumPath<Level> level = createEnum("level", Level.class);

    public final ListPath<com.goorm.insideout.like.domain.ClubLike, com.goorm.insideout.like.domain.QClubLike> likes = this.<com.goorm.insideout.like.domain.ClubLike, com.goorm.insideout.like.domain.QClubLike>createList("likes", com.goorm.insideout.like.domain.ClubLike.class, com.goorm.insideout.like.domain.QClubLike.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maxAge = createNumber("maxAge", Integer.class);

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final NumberPath<Integer> memberLimit = createNumber("memberLimit", Integer.class);

    public final ListPath<ClubUser, QClubUser> members = this.<ClubUser, QClubUser>createList("members", ClubUser.class, QClubUser.class, PathInits.DIRECT2);

    public final NumberPath<Integer> minAge = createNumber("minAge", Integer.class);

    public final com.goorm.insideout.user.domain.QUser owner;

    public final ListPath<ClubPost, QClubPost> posts = this.<ClubPost, QClubPost>createList("posts", ClubPost.class, QClubPost.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath region = createString("region");

    public final SetPath<String, StringPath> rules = this.<String, StringPath>createSet("rules", String.class, StringPath.class, PathInits.DIRECT2);

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.goorm.insideout.chatroom.domain.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.owner = inits.isInitialized("owner") ? new com.goorm.insideout.user.domain.QUser(forProperty("owner"), inits.get("owner")) : null;
    }

}

