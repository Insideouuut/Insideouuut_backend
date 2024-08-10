package com.goorm.insideout.club.dto.responseDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.goorm.insideout.club.dto.responseDto.QClubBoardResponseDto is a Querydsl Projection type for ClubBoardResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QClubBoardResponseDto extends ConstructorExpression<ClubBoardResponseDto> {

    private static final long serialVersionUID = 1558807632L;

    public QClubBoardResponseDto(com.querydsl.core.types.Expression<? extends com.goorm.insideout.club.entity.Club> club) {
        super(ClubBoardResponseDto.class, new Class<?>[]{com.goorm.insideout.club.entity.Club.class}, club);
    }

}

