package com.backend.farmon.repository.ExpertReposiotry;

import com.backend.farmon.domain.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpertRepositoryImpl implements ExpertRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QExpert expert = QExpert.expert;

    @Override
    public Page<Expert> findFilteredExperts(String crop, String area, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // Crop 필터링
        if (crop != null && !crop.isEmpty()) {  // crop 값이 null이 아니고 빈 문자열이 아닌 경우에만 필터링
            crop = crop.replace(" ", "");  // 공백 제거

            // "분야전체"이면 필터링을 하지 않음
            if ("분야전체".equals(crop)) {
                // 필터링을 하지 않음 (모든 분야를 조회)
            } else if (crop.endsWith("전체")) {  // crop 값이 "전체"로 끝나는지 확인
                String category = crop.replace("전체", "");  // "전체"를 제거하여 카테고리 이름만 추출 (ex. "곡물전체" -> "곡물")
                builder.and(expert.crop.category.eq(category));  // 해당 카테고리에 해당하는 전문가들만 필터링
            } else {
                builder.and(expert.crop.name.eq(crop));
            }
        }

        // Area 필터링
        if (area != null && !area.isEmpty()) {
            area = area.replace(" ", "");

            // "전국전체"이면 필터링을 하지 않음
            if ("전국전체".equals(area)) {
            } else if (area.endsWith("전체")) { // ex) "서울전체"
                String category = area.replace("전체", "");
                builder.and(expert.area.areaName.eq(category));
            } else {
                builder.and(expert.area.areaNameDetail.eq(area));
            }
        }

        // Query 실행
        JPAQuery<Expert> query = queryFactory.selectFrom(expert)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Expert> expertList = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(expertList, pageable, total);
    }
}