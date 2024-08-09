package com.goorm.insideout.club.repository.custom;

import static com.goorm.insideout.club.entity.QClub.*;
import static com.goorm.insideout.user.domain.QUser.*;
import static org.springframework.util.StringUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.QClubBoardResponseDto;
import com.goorm.insideout.club.entity.Category;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubQueryDslRepositoryImpl implements ClubQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  /*
  일반 게시글 검색
   */
  @Override
  public List<ClubBoardResponseDto> findAllByCondition(SearchRequest condition) {

    return queryFactory
        .select(new QClubBoardResponseDto(club))
        .from(club)
        .leftJoin(club.owner, user).fetchJoin()
        .where(
            titleOrDescriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        )
        .orderBy(club.clubId.desc())
        .fetch();
  }

  /*
  정렬된 전체 모임 조회 + 일반 검색 및 정렬된 모임 검색 결과 조회
   */
  @Override
  public List<ClubBoardResponseDto> findByConditionAndSortType(SearchRequest condition) {
    JPAQuery<ClubBoardResponseDto> basicQuery = queryFactory
        .select(new QClubBoardResponseDto(club))
        .from(club)
        // User 엔티티를 구현하지 않았으므로 임시 주석 처리
        // .leftJoin(meeting.author, user).fetchJoin()
        .where(
            titleOrDescriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        );

    List<ClubBoardResponseDto> content = addSortingQuery(basicQuery, condition.getSortType());

    return content;
  }

  @Override
  public List<ClubBoardResponseDto> findBySortType(SearchRequest condition) {
    JPAQuery<ClubBoardResponseDto> basicQuery = queryFactory
        .select(new QClubBoardResponseDto(club))
        .from(club);

    List<ClubBoardResponseDto> content = addSortingQuery(basicQuery, condition.getSortType());

    return content;
  }

  /*
    pageSize를 구하는 Count 쿼리를 성능 최적화를 위해 따로 구현 + 가독성을 위해 메소드 추출
   */
  private JPAQuery<Long> getSearchResultCountQuery(SearchRequest condition) {

    return queryFactory
        .select(club.count())
        .from(club)
        // User 엔티티를 구현하지 않았으므로 임시 주석 처리
        // .leftJoin(meeting.author, user).fetchJoin()
        .where(
            titleOrDescriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        );
  }

  /**
   * 제목, 내용, 카테고리 포함 여부 조사 메소드
   */
  private BooleanExpression titleOrDescriptionContains(String query) {
    return hasText(query) ? titleContains(query).or(descriptionContains(query)) : null;
  }

  private BooleanExpression titleContains(String name) {
    return hasText(name) ? club.clubName.contains(name) : null;
  }

  private BooleanExpression descriptionContains(String description) {
    return hasText(description) ? club.content.contains(description) : null;
  }

  private BooleanExpression categoryEquals(String category) {
    Category findCategory = Category.findByName(category);

    return hasText(category) ? club.category.eq(findCategory) : null;
  }

  /*
    정렬 관련 쿼리를 추가하기 위한 메소드
   */
  private List<ClubBoardResponseDto> addSortingQuery(JPAQuery<ClubBoardResponseDto> basicQuery, String sortType) {
    List<ClubBoardResponseDto> content;
    switch (sortType) {
      case "like":
        content = basicQuery
            .orderBy(club.likes.size().desc())
                .fetch();
        break;

      case "rlike":
        content = basicQuery
            .orderBy(club.likes.size().asc())
            .fetch();
        break;

      case "date":
        content = basicQuery
            .orderBy(club.date.desc())
            .fetch();
        break;

      case "rdate":
        content = basicQuery
            .orderBy(club.date.asc())
            .fetch();
        break;

      // 거리순 정렬은 복잡한 로직으로 인해 일단 제외

      default:
        content = new ArrayList<>();
    }

    return content;
  }
}
