import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,           // 동시 사용자 수 (virtual users)
  duration: '30s',   // 테스트 지속 시간
};

export default function () {
  const res = http.get('http://localhost:8080/products/1');
  check(res, {
    'is status 200': (r) => r.status === 200,
  });
  sleep(1); // 사용자가 1초 쉬고 다시 요청
}

