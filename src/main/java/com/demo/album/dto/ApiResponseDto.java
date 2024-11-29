package com.demo.album.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ApiResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto<T> {  // 제네릭으로 데이터 타입 유연성 제공
        private int code;    // 상태 코드
        private String msg;   // 메시지
        private T data;       // 추가 데이터 (선택적)

        public ResponseDto(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
