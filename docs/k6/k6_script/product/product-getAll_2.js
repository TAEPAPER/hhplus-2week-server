import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 50 }, // 30초 동안 VU를 50까지 증가
    { duration: '1m', target: 50 }, // 1분 동안 VU 유지
    { duration: '30s', target: 0 }, // 30초 동안 VU를 0으로 감소
  ],
};

const BASE_URL = 'http://localhost:8080/products'; // 서버의 기본 URL

export default function () {
  // 전체 상품 조회 테스트
  let res = http.get(BASE_URL);
  check(res, {
    'GET /products - status is 200': (r) => r.status === 200,
    'GET /products - response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1); // 각 요청 사이에 1초 대기
}