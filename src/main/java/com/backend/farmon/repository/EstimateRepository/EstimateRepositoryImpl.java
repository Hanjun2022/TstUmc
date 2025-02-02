package com.backend.farmon.repository.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.QArea;
import com.backend.farmon.domain.QEstimate;
import com.backend.farmon.domain.QExpert;
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
public class EstimateRepositoryImpl implements EstimateRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QEstimate estimate = QEstimate.estimate;
    QExpert expert = QExpert.expert;
    QArea area = QArea.area;


    @Override
    public Page<Estimate> findFilteredEstimates(String estimateCategory, String budget, String areaName, String areaNameDetail, Pageable pageable) {

        //기본 쿼리 생성
        JPAQuery<Estimate> query = queryFactory.selectFrom(estimate);

        //BooleanBuilder 동적 필터링 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(estimateCategory != null && !estimateCategory.isEmpty()) {
            booleanBuilder.and(estimate.category.eq(estimateCategory));
        }
        if(budget != null && !budget.isEmpty()) {
            if(!budget.equals("1,000만원 이상")) {
                booleanBuilder.and(estimate.budget.eq(budget));
            } else if (budget.equals(("1,000만원 이상"))) {
                booleanBuilder.and(estimate.budget.notIn(
                        "10만원 ~ 50만원",
                        "50만원 ~ 100만원",
                        "100만원 ~ 500만원",
                        "500만원 ~ 1,000만원"
                ));
            }
        }
        if(areaName != null && !areaName.isEmpty() && !areaName.equals("전국")) {
            // "서울 전체 와 세부 지역 조건 추가
            if (areaNameDetail != null && !areaNameDetail.isEmpty()) {
                if(areaNameDetail.equals("서울 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("서울"));
                } else if(areaNameDetail.equals("경기 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경기"));
                } else if(areaNameDetail.equals("인천 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("인천"));
                } else if(areaNameDetail.equals("강원 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("강원"));
                } else if(areaNameDetail.equals("대전 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("대전"));
                } else if(areaNameDetail.equals("충남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("충남"));
                } else if(areaNameDetail.equals("충북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("충북"));
                } else if(areaNameDetail.equals("부산 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("부산"));
                } else if(areaNameDetail.equals("울산 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("울산"));
                } else if(areaNameDetail.equals("경남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경남"));
                } else if(areaNameDetail.equals("경북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경북"));
                } else if(areaNameDetail.equals("대구 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("대구"));
                } else if(areaNameDetail.equals("광주 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("광주"));
                } else if(areaNameDetail.equals("전남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("전남"));
                } else if(areaNameDetail.equals("전북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("전북"));
                } else if(areaNameDetail.equals("제주 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("제주"));
                } else {
                    booleanBuilder.and(estimate.area.areaName.eq(areaName)).and(estimate.area.areaNameDetail.eq(areaNameDetail));
                }
            } else {
                booleanBuilder.and(estimate.area.areaName.eq(areaName));
            }
        }

        //페이징 처리
        List<Estimate> estimates = queryFactory
                .selectFrom(estimate)
                .join(estimate.area, area)
                .where(booleanBuilder)
                .orderBy(estimate.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        // 전체 데이터 개수 조회
        Long total = queryFactory
                .select(estimate.count())
                .from(estimate)
                .join(estimate.area, area)
                .where(booleanBuilder)
                .fetchOne();


        return new PageImpl<>(estimates, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<Estimate> findFilteredEstimates2(Long expertId, String cropCategory, String cropName, String estimateCategory, String budget, String areaName, String areaNameDetail, Pageable pageable) {

        //BooleanBuilder 동적 필터링 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //expertId에 값이 있으면
        if (expertId != null) {
            booleanBuilder.and(estimate.crop.eq(
                    queryFactory
                            .select(expert.crop) // expert의 crop을 선택
                            .from(expert)
                            .where(expert.id.eq(expertId)) // expertId에 해당하는 expert만 선택
            ));
        }

        //cropCategory에 값이 있으면
        else if(cropCategory != null && !cropCategory.isEmpty()) {
            booleanBuilder.and(estimate.crop.category.eq(cropCategory));
        }

        //cropName에 값이 있으면
        else if(cropName != null && !cropName.isEmpty()) {
            booleanBuilder.and(estimate.crop.name.eq(cropName));
        };

        if(estimateCategory != null && !estimateCategory.isEmpty()) {
            booleanBuilder.and(estimate.category.eq(estimateCategory));
        }
        if(budget != null && !budget.isEmpty()) {
            if(!budget.equals("1,000만원 이상")) {
                booleanBuilder.and(estimate.budget.eq(budget));
            } else if (budget.equals(("1,000만원 이상"))) {
                booleanBuilder.and(estimate.budget.notIn(
                        "10만원 ~ 50만원",
                        "50만원 ~ 100만원",
                        "100만원 ~ 500만원",
                        "500만원 ~ 1,000만원"
                ));
            }
        }
        if(areaName != null && !areaName.isEmpty() && !areaName.equals("전국")) {
            // "서울 전체 와 세부 지역 조건 추가
            if (areaNameDetail != null && !areaNameDetail.isEmpty()) {
                if(areaNameDetail.equals("서울 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("서울"));
                } else if(areaNameDetail.equals("경기 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경기"));
                } else if(areaNameDetail.equals("인천 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("인천"));
                } else if(areaNameDetail.equals("강원 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("강원"));
                } else if(areaNameDetail.equals("대전 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("대전"));
                } else if(areaNameDetail.equals("충남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("충남"));
                } else if(areaNameDetail.equals("충북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("충북"));
                } else if(areaNameDetail.equals("부산 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("부산"));
                } else if(areaNameDetail.equals("울산 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("울산"));
                } else if(areaNameDetail.equals("경남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경남"));
                } else if(areaNameDetail.equals("경북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("경북"));
                } else if(areaNameDetail.equals("대구 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("대구"));
                } else if(areaNameDetail.equals("광주 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("광주"));
                } else if(areaNameDetail.equals("전남 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("전남"));
                } else if(areaNameDetail.equals("전북 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("전북"));
                } else if(areaNameDetail.equals("제주 전체")){
                    booleanBuilder.and(estimate.area.areaName.eq("제주"));
                } else {
                    booleanBuilder.and(estimate.area.areaName.eq(areaName)).and(estimate.area.areaNameDetail.eq(areaNameDetail));
                }
            } else {
                booleanBuilder.and(estimate.area.areaName.eq(areaName));
            }
        }

        //페이징 처리
        List<Estimate> estimates = queryFactory
                .selectFrom(estimate)
                .join(estimate.area, area)
                .where(booleanBuilder)
                .orderBy(estimate.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        // 전체 데이터 개수 조회
        Long total = queryFactory
                .select(estimate.count())
                .from(estimate)
                .join(estimate.area, area)
                .where(booleanBuilder)
                .fetchOne();


        return new PageImpl<>(estimates, pageable, total != null ? total : 0L);
    }

}
