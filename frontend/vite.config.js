import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api/orders": "http://localhost:8081",
      "/api/notifications": "http://localhost:8082",
    },
  },
});
