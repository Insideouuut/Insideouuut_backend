package com.goorm.insideout.image.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubImage is a Querydsl query type for ClubImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubImage extends EntityPathBase<ClubImage> {

    private static final long serialVersionUID = 1283033539L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubImage clubImage = new QClubImage("clubImage");

    public final com.goorm.insideout.club.entity.QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QImage image;

    public QClubImage(String variable) {
        this(ClubImage.class, forVariable(variable), INITS);
    }

    public QClubImage(Path<? extends ClubImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubImage(PathMetadata metadata, PathInits inits) {
        this(ClubImage.class, metadata, inits);
    }

    public QClubImage(Class<? extends ClubImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.goorm.insideout.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.image = inits.isInitialized("image") ? new QImage(forProperty("image")) : null;
    }

}

