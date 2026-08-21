import http from "k6/http";
import { check } from "k6";

// Dispara N VUs contra a MESMA account_id, para expor lost update (Fase 0)
// ou validar que o locking (Fase 1+) impede saldo negativo/incorreto.

export const options = {
  vus: 20,
  iterations: 20, // 1 débito por VU
};

const ACCOUNT_ID = __ENV.ACCOUNT_ID || "00000000-0000-0000-0000-000000000001";
const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export default function () {
  const res = http.post(
    `${BASE_URL}/accounts/${ACCOUNT_ID}/debit`,
    JSON.stringify({ amountCents: 1000 }),
    { headers: { "Content-Type": "application/json" } }
  );

  check(res, {
    "status é 200 ou 422 (saldo insuficiente, esperado no fim)": (r) =>
      r.status === 200 || r.status === 422,
  });
}
