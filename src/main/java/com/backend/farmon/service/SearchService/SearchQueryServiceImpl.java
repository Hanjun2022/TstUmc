package com.backend.farmon.service.SearchService;

import com.backend.farmon.dto.home.HomeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchQueryServiceImpl implements SearchQueryService{
    private final RedisTemplate<String, String> recentSearchLogRedisTemplate;

    // 사용자 최근 검색어 리스트 조회
    @Override
    public HomeResponse.RecentSearchListDTO findRecentSearchLogs(Long userId) {
        return HomeResponse.RecentSearchListDTO.builder()
                .recentSearchList( recentSearchLogRedisTemplate.opsForList().range("RecentSearchLog"+userId, 0, 10))
                .build();
    }
}
