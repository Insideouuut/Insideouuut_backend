package com.goorm.insideout.meeting.repository.custom;

import static com.goorm.insideout.meeting.domain.QMeeting.*;
import static org.springframework.util.StringUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.meeting.dto.request.MeetingSearchRequest;
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
  일반 게시글 검색
   */
  @Override
  public Page<MeetingResponse> findAllByCondition(MeetingSearchRequest condition, Pageable pageable) {

    List<MeetingResponse> content = queryFactory
        .select(new QMeetingResponse(meeting))
        .from(meeting)
        // User 엔티티를 구현하지 않았으므로 임시 주석 처리
        // .leftJoin(meeting.author, user).fetchJoin()
        .where(
            titleContains(condition.getQuery()),
            descriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(meeting.id.desc())
        .fetch();

    // 성능 최적화를 위해 pageSize를 구하는 쿼리를 따로 빼놓았음.
    JPAQuery<Long> countQuery = getSearchResultCountQuery(condition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
  }

  /*
  정렬된 전체 모임 조회 + 일반 검색 및 정렬된 모임 검색 결과 조회
   */
  @Override
  public Page<MeetingResponse> findAllBySortType(MeetingSearchRequest condition, Pageable pageable) {
    JPAQuery<MeetingResponse> basicQuery = queryFactory
        .select(new QMeetingResponse(meeting))
        .from(meeting)
        // User 엔티티를 구현하지 않았으므로 임시 주석 처리
        // .leftJoin(meeting.author, user).fetchJoin()
        .where(
            titleContains(condition.getQuery()),
            descriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    List<MeetingResponse> content = addSortingQuery(basicQuery, condition.getSortType());

    // 성능 최적화를 위해 pageSize를 구하는 쿼리를 따로 빼놓았음.
    JPAQuery<Long> countQuery = getSearchResultCountQuery(condition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
  }

  /*
    pageSize를 구하는 Count 쿼리를 성능 최적화를 위해 따로 구현 + 가독성을 위해 메소드 추출
   */
  private JPAQuery<Long> getSearchResultCountQuery(MeetingSearchRequest condition) {

    return queryFactory
        .select(meeting.count())
        .from(meeting)
        // User 엔티티를 구현하지 않았으므로 임시 주석 처리
        // .leftJoin(meeting.author, user).fetchJoin()
        .where(
            titleContains(condition.getQuery()),
            descriptionContains(condition.getQuery()),
            categoryEquals(condition.getCategory())
        );
  }

  /**
   * 제목, 내용, 카테고리 포함 여부 조사 메소드
   */
  private BooleanExpression titleContains(String name) {
    return hasText(name) ? meeting.title.contains(name) : null;
  }

  private BooleanExpression descriptionContains(String description) {
    return hasText(description) ? meeting.description.contains(description) : null;
  }

  private BooleanExpression categoryEquals(String category) {
    return hasText(category) ? meeting.category.stringValue().eq(category) : null;
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
