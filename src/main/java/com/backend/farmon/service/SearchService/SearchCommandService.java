package com.backend.farmon.service.SearchService;

public interface SearchCommandService {

    // 사용자 최근 검색어 저장
    void saveRecentSearchLog(Long userId, String searchName);

    // 사용자 최근 검색어 삭제
    void deleteRecentSearchLog(Long userId, String searchName);

    // 사용자 최근 검색어 전체 삭제
    void deleteAllRecentSearchLog(Long userId);
}
