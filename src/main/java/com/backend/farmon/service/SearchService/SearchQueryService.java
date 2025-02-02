package com.backend.farmon.service.SearchService;

import com.backend.farmon.dto.home.HomeResponse;

public interface SearchQueryService {
    // 사용자 최근 검색어 리스트 조회
    HomeResponse.RecentSearchListDTO findRecentSearchLogs(Long userId);
}
