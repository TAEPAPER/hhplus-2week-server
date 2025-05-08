import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 10 },  // 기본 부하
        { duration: '20s', target: 50 }, // 고부하
        { duration: '10s', target: 10 },  // 부하 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% 요청이 500ms 미만이어야 함
        checks: ['rate>0.95'],            // 성공률 95% 이상
    }
};

export default function () {
    const res = http.get('http://localhost:8080/products/popular');
    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 500ms': (r) => r.timings.duration < 500,
    });
    sleep(1);
}

