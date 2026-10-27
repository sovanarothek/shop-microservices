import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { demoFetch } from "./demoApi.js";

if (import.meta.env.VITE_DEMO) {
  window.fetch = demoFetch;
  const banner = document.createElement("p");
  banner.textContent =
    "Demo mode: data stays in your browser. The real app runs on Spring Boot + Kafka + PostgreSQL.";
  banner.style.cssText = "background:#fff3cd;padding:8px;margin:0;font:14px sans-serif";
  document.body.prepend(banner);
}

createRoot(document.getElementById("root")).render(<App />);
