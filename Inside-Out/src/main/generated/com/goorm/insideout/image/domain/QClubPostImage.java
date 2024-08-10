package com.goorm.insideout.image.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubPostImage is a Querydsl query type for ClubPostImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubPostImage extends EntityPathBase<ClubPostImage> {

    private static final long serialVersionUID = -1477316349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubPostImage clubPostImage = new QClubPostImage("clubPostImage");

    public final com.goorm.insideout.club.entity.QClubPost clubPost;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QImage image;

    public QClubPostImage(String variable) {
        this(ClubPostImage.class, forVariable(variable), INITS);
    }

    public QClubPostImage(Path<? extends ClubPostImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubPostImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubPostImage(PathMetadata metadata, PathInits inits) {
        this(ClubPostImage.class, metadata, inits);
    }

    public QClubPostImage(Class<? extends ClubPostImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubPost = inits.isInitialized("clubPost") ? new com.goorm.insideout.club.entity.QClubPost(forProperty("clubPost"), inits.get("clubPost")) : null;
        this.image = inits.isInitialized("image") ? new QImage(forProperty("image")) : null;
    }

}

