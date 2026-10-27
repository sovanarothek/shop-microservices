import { useEffect, useState } from "react";

const empty = { customerName: "", customerEmail: "", productName: "", quantity: 1, unitPrice: "" };
const json = { "Content-Type": "application/json" };

export default function App() {
  const [form, setForm] = useState(empty);
  const [orders, setOrders] = useState([]);
  const [notes, setNotes] = useState([]);
  const [error, setError] = useState("");

  const load = async () => {
    try {
      const [o, n] = await Promise.all([fetch("/api/orders"), fetch("/api/notifications")]);
      setOrders(await o.json());
      setNotes(await n.json());
      setError("");
    } catch {
      setError("Cannot reach the API");
    }
  };

  useEffect(() => {
    load();
    const t = setInterval(load, 5000);
    return () => clearInterval(t);
  }, []);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    const res = await fetch("/api/orders", {
      method: "POST",
      headers: json,
      body: JSON.stringify({
        customerName: form.customerName,
        customerEmail: form.customerEmail,
        items: [{ productName: form.productName, quantity: Number(form.quantity), unitPrice: Number(form.unitPrice) }],
      }),
    });
    if (!res.ok) return setError("Order rejected, check the fields");
    setForm(empty);
    load();
  };

  const setStatus = async (id, status) => {
    await fetch(`/api/orders/${id}/status`, { method: "PATCH", headers: json, body: JSON.stringify({ status }) });
    load();
  };

  return (
    <div>
      <h1>Shop Orders</h1>
      {error && <p className="err">{error}</p>}

      <form onSubmit={submit}>
        <input placeholder="Customer name" value={form.customerName} onChange={set("customerName")} required />
        <input placeholder="Email" type="email" value={form.customerEmail} onChange={set("customerEmail")} required />
        <input placeholder="Product" value={form.productName} onChange={set("productName")} required />
        <input placeholder="Quantity" type="number" min="1" value={form.quantity} onChange={set("quantity")} required />
        <input placeholder="Unit price" type="number" step="0.01" min="0.01" value={form.unitPrice} onChange={set("unitPrice")} required />
        <button type="submit">Create order</button>
      </form>

      <h2>Orders</h2>
      <ul>
        {orders.map((o) => (
          <li key={o.id}>
            {o.customerName}: ${o.totalAmount} [{o.status}]
            {o.status === "NEW" && <button onClick={() => setStatus(o.id, "PAID")}>Mark paid</button>}
            {o.status === "PAID" && <button onClick={() => setStatus(o.id, "SHIPPED")}>Mark shipped</button>}
          </li>
        ))}
      </ul>

      <h2>Notifications</h2>
      <ul>
        {notes.map((n) => (
          <li key={n.id}>{n.type}: {n.message}</li>
        ))}
      </ul>
    </div>
  );
}
