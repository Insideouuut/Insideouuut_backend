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

    public final NumberPath<Integer> ageLimit = createNumber("ageLimit", Integer.class);

    public final StringPath category = createString("category");

    public final NumberPath<Long> clubId = createNumber("clubId", Long.class);

    public final StringPath clubImg = createString("clubImg");

    public final StringPath clubName = createString("clubName");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath date = createString("date");

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final NumberPath<Integer> memberLimit = createNumber("memberLimit", Integer.class);

    public final ListPath<ClubUser, QClubUser> members = this.<ClubUser, QClubUser>createList("members", ClubUser.class, QClubUser.class, PathInits.DIRECT2);

    public final com.goorm.insideout.user.domain.QUser owner;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath question = createString("question");

    public final StringPath region = createString("region");

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
        this.owner = inits.isInitialized("owner") ? new com.goorm.insideout.user.domain.QUser(forProperty("owner")) : null;
    }

}

