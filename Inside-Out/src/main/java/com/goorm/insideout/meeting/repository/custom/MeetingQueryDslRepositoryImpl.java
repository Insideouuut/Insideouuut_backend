package com.goorm.insideout.meeting.repository.custom;

import static com.goorm.insideout.meeting.domain.QMeeting.*;
import static com.goorm.insideout.user.domain.QUser.*;
import static org.springframework.util.StringUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.meeting.domain.Category;
import com.goorm.insideout.meeting.dto.request.SearchRequest;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.goorm.insideout.meeting.dto.response.QMeetingResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryDslRepositoryImpl implements MeetingQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  /*
  일반 모임 검색
   */
  @Override
  public List<MeetingResponse> findAllByCondition(SearchRequest condition) {

    return queryFactory
        .select(new QMeetingResponse(meeting))
        .from(meeting)
        .leftJoin(meeting.host, user).fetchJoin()
        .where(
            titleOrDescriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        )
        .orderBy(meeting.id.desc())
        .fetch();
  }

  /*
  키워드에 따른 모임 검색 후 정렬
   */
  @Override
  public List<MeetingResponse> findByConditionAndSortType(SearchRequest condition) {
    JPAQuery<MeetingResponse> basicQuery = queryFactory
        .select(new QMeetingResponse(meeting))
        .from(meeting)
        .leftJoin(meeting.host, user).fetchJoin()
        .where(
            titleOrDescriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        );

    List<MeetingResponse> content = addSortingQuery(basicQuery, condition.getSortType());

    return content;
  }

  /*
  전체 모임 조회 후 정렬
   */
  @Override
  public List<MeetingResponse> findBySortType(SearchRequest condition) {
    JPAQuery<MeetingResponse> basicQuery = queryFactory
        .select(new QMeetingResponse(meeting))
        .from(meeting);

    List<MeetingResponse> content = addSortingQuery(basicQuery, condition.getSortType());

    return content;
  }

  /*
    pageSize를 구하는 Count 쿼리를 성능 최적화를 위해 따로 구현 + 가독성을 위해 메소드 추출
   */
  private JPAQuery<Long> getSearchResultCountQuery(SearchRequest condition) {

    return queryFactory
        .select(meeting.count())
        .from(meeting)
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
    return hasText(name) ? meeting.title.contains(name) : null;
  }

  private BooleanExpression descriptionContains(String description) {
    return hasText(description) ? meeting.description.contains(description) : null;
  }

  private BooleanExpression categoryEquals(String category) {
    Category findCategory = Category.findByName(category);

    return hasText(category) ? meeting.category.eq(findCategory) : null;
  }

  /*
    정렬 관련 쿼리를 추가하기 위한 메소드
   */
  private List<MeetingResponse> addSortingQuery(JPAQuery<MeetingResponse> basicQuery, String sortType) {
    List<MeetingResponse> content;
    switch (sortType) {
      case "like":
        content = basicQuery
            .orderBy(meeting.likes.size().desc())
                .fetch();
        break;

      case "rlike":
        content = basicQuery
            .orderBy(meeting.likes.size().asc())
            .fetch();
        break;

      case "date":
        content = basicQuery
            .orderBy(meeting.schedule.desc())
            .fetch();
        break;

      case "rdate":
        content = basicQuery
            .orderBy(meeting.schedule.asc())
            .fetch();
        break;

      // 거리순 정렬은 복잡한 로직으로 인해 일단 제외

      default:
        content = new ArrayList<>();
    }

    return content;
  }
}
