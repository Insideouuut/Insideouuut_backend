package com.goorm.insideout.like.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubLike is a Querydsl query type for ClubLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubLike extends EntityPathBase<ClubLike> {

    private static final long serialVersionUID = -2015383435L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubLike clubLike = new QClubLike("clubLike");

    public final com.goorm.insideout.club.entity.QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.goorm.insideout.user.domain.QUser user;

    public QClubLike(String variable) {
        this(ClubLike.class, forVariable(variable), INITS);
    }

    public QClubLike(Path<? extends ClubLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubLike(PathMetadata metadata, PathInits inits) {
        this(ClubLike.class, metadata, inits);
    }

    public QClubLike(Class<? extends ClubLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.goorm.insideout.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.goorm.insideout.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

