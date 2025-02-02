package com.backend.farmon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test")
    public String test(){
        return "Hello World!";
    }

    @GetMapping("/test-redis/set")
    public String testRedis(@RequestParam String key) {
        try {
            // Redis에 데이터 저장
            redisTemplate.opsForValue().set(key, "Hello, Redis!");

            // Redis에서 데이터 가져오기
            String value = redisTemplate.opsForValue().get(key);

            // 결과 반환
            return value != null ? "Redis 연결 성공! Value: " + value : "Redis 연결 실패!";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Redis 오류: " + e.getMessage();
        }
    }

    @GetMapping("/test-redis/exist")
    public String checkKey(@RequestParam String key) {
        try {
            // Redis에 해당 키가 존재하는지 확인
            Boolean exists = redisTemplate.hasKey(key);

            // 결과 반환
            if (exists != null && exists) {
                return "키가 Redis에 존재합니다: " + key;
            } else {
                return "키가 Redis에 존재하지 않습니다: " + key;
            }
        } catch (Exception e) {
            log.error("Redis 오류: ", e);
            return "Redis 오류: " + e.getMessage();
        }
    }

}