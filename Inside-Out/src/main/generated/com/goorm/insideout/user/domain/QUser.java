package com.goorm.insideout.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1958441223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.goorm.insideout.meeting.domain.Category, EnumPath<com.goorm.insideout.meeting.domain.Category>> interests = this.<com.goorm.insideout.meeting.domain.Category, EnumPath<com.goorm.insideout.meeting.domain.Category>>createSet("interests", com.goorm.insideout.meeting.domain.Category.class, EnumPath.class, PathInits.DIRECT2);

    public final BooleanPath isLocationVerified = createBoolean("isLocationVerified");

    public final SetPath<String, StringPath> locations = this.<String, StringPath>createSet("locations", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigDecimal> mannerTemp = createNumber("mannerTemp", java.math.BigDecimal.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final com.goorm.insideout.image.domain.QProfileImage profileImage;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profileImage = inits.isInitialized("profileImage") ? new com.goorm.insideout.image.domain.QProfileImage(forProperty("profileImage"), inits.get("profileImage")) : null;
    }

}

