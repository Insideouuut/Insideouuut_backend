package com.goorm.insideout.chatroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1021107575L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Long> associatedId = createNumber("associatedId", Long.class);

    public final com.goorm.insideout.club.entity.QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastMessageContent = createString("lastMessageContent");

    public final DateTimePath<java.time.LocalDateTime> lastMessageTime = createDateTime("lastMessageTime", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final EnumPath<ChatRoomType> type = createEnum("type", ChatRoomType.class);

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.goorm.insideout.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
    }

}

