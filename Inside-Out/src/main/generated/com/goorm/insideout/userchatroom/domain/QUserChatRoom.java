package com.goorm.insideout.userchatroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserChatRoom is a Querydsl query type for UserChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserChatRoom extends EntityPathBase<UserChatRoom> {

    private static final long serialVersionUID = 1565682477L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserChatRoom userChatRoom = new QUserChatRoom("userChatRoom");

    public final com.goorm.insideout.chatroom.domain.QChatRoom chatRoom;

    public final DateTimePath<java.time.LocalDateTime> configTime = createDateTime("configTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> invitationTime = createDateTime("invitationTime", java.time.LocalDateTime.class);

    public final com.goorm.insideout.user.domain.QUser user;

    public QUserChatRoom(String variable) {
        this(UserChatRoom.class, forVariable(variable), INITS);
    }

    public QUserChatRoom(Path<? extends UserChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserChatRoom(PathMetadata metadata, PathInits inits) {
        this(UserChatRoom.class, metadata, inits);
    }

    public QUserChatRoom(Class<? extends UserChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.goorm.insideout.chatroom.domain.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.user = inits.isInitialized("user") ? new com.goorm.insideout.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

