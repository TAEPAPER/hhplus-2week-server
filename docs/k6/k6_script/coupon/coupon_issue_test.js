import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 100, // 동시 사용자 수
  duration: '5s', // 테스트 시간
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% 요청이 500ms 미만
  },
};

export default function () {
  const url = 'http://localhost:8080/coupons/issue'; // 실제 서버 주소로 변경
  const payload = JSON.stringify({
    userId: Math.floor(Math.random() * 100000),
    couponId: 1 // 테스트용 쿠폰 ID
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, payload, params);

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1); // 사용자가 1초 간격으로 행동하는 시뮬레이션
}

