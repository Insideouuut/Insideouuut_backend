package com.goorm.insideout.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClubApply is a Querydsl query type for ClubApply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubApply extends EntityPathBase<ClubApply> {

    private static final long serialVersionUID = 1464508272L;

    public static final QClubApply clubApply = new QClubApply("clubApply");

    public final StringPath answer = createString("answer");

    public final NumberPath<Long> applyId = createNumber("applyId", Long.class);

    public final NumberPath<Long> clubId = createNumber("clubId", Long.class);

    public final NumberPath<java.math.BigDecimal> mannerTemp = createNumber("mannerTemp", java.math.BigDecimal.class);

    public final StringPath profileImgUrl = createString("profileImgUrl");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QClubApply(String variable) {
        super(ClubApply.class, forVariable(variable));
    }

    public QClubApply(Path<? extends ClubApply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClubApply(PathMetadata metadata) {
        super(ClubApply.class, metadata);
    }

}

