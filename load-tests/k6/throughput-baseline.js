import http from "k6/http";
import { check } from "k6";

// Mede throughput/latência (p50/p99) sob contas DIFERENTES (baixa contenção),
// para servir de baseline comparável entre fases.

export const options = {
  scenarios: {
    throughput: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "30s", target: 50 },
        { duration: "1m", target: 50 },
        { duration: "30s", target: 0 },
      ],
    },
  },
  thresholds: {
    http_req_duration: ["p(95)<300", "p(99)<800"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export default function () {
  const accountId = `00000000-0000-0000-0000-${String(
    Math.floor(Math.random() * 100000)
  ).padStart(12, "0")}`;

  const res = http.post(
    `${BASE_URL}/accounts/${accountId}/debit`,
    JSON.stringify({ amountCents: 100 }),
    { headers: { "Content-Type": "application/json" } }
  );

  check(res, { "status é 200 ou 404 (conta de teste inexistente)": (r) => [200, 404].includes(r.status) });
}
