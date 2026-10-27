const KEY = "demo-state";
const load = () => JSON.parse(localStorage.getItem(KEY) || '{"orders":[],"notes":[]}');
const save = (s) => localStorage.setItem(KEY, JSON.stringify(s));
const res = (data, status = 200) =>
  new Response(JSON.stringify(data), { status, headers: { "Content-Type": "application/json" } });

export async function demoFetch(url, opts = {}) {
  const method = (opts.method || "GET").toUpperCase();
  const s = load();

  if (url === "/api/orders" && method === "GET") return res(s.orders);
  if (url === "/api/notifications" && method === "GET") return res(s.notes);

  if (url === "/api/orders" && method === "POST") {
    const body = JSON.parse(opts.body);
    const id = crypto.randomUUID();
    const total = body.items.reduce((t, i) => t + i.quantity * i.unitPrice, 0);
    const order = {
      id,
      customerName: body.customerName,
      customerEmail: body.customerEmail,
      totalAmount: total.toFixed(2),
      status: "NEW",
      items: body.items,
    };
    s.orders.unshift(order);
    save(s);
    setTimeout(() => {
      const t = load();
      t.notes.unshift({
        id: crypto.randomUUID(),
        orderId: id,
        type: "ORDER_CREATED",
        message: `Hi ${order.customerName}, your order total is $${order.totalAmount}`,
      });
      save(t);
    }, 1500);
    return res(order, 201);
  }

  const m = url.match(/^\/api\/orders\/([^/]+)\/status$/);
  if (m && method === "PATCH") {
    const order = s.orders.find((o) => o.id === m[1]);
    if (!order) return res({ error: "Not found" }, 404);
    order.status = JSON.parse(opts.body).status;
    save(s);
    return res(order);
  }

  return res({ error: "Not found" }, 404);
}
