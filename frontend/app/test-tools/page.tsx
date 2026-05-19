"use client";

import { useState } from "react";
import { generateLeads, resetQuota } from "@/services/api";
import { StatusMessage } from "@/components/StatusMessage";

export default function TestToolsPage() {
  const [eventId, setEventId] = useState(() => `quota-reset-${Date.now()}`);
  const [loading, setLoading] = useState<string | null>(null);
  const [message, setMessage] = useState<{ kind: "success" | "error" | "info"; text: string } | null>(null);

  async function resetOnce() {
    setLoading("reset");
    try {
      const response = await resetQuota(eventId);
      setMessage({ kind: response.processed ? "success" : "info", text: response.message });
    } catch {
      setMessage({ kind: "error", text: "Quota reset webhook failed." });
    } finally {
      setLoading(null);
    }
  }

  async function callWebhookMultipleTimes() {
    setLoading("multi");
    try {
      const responses = await Promise.all([resetQuota(eventId), resetQuota(eventId), resetQuota(eventId)]);
      const processed = responses.filter((response) => response.processed).length;
      setMessage({ kind: "success", text: `Sent 3 webhook calls with the same event_id. Processed count: ${processed}.` });
    } catch {
      setMessage({ kind: "error", text: "Webhook idempotency test failed." });
    } finally {
      setLoading(null);
    }
  }

  async function generateTenLeads() {
    setLoading("generate");
    try {
      const response = await generateLeads();
      setMessage({ kind: "success", text: `Generated ${response.created}/${response.requested} leads.` });
    } catch {
      setMessage({ kind: "error", text: "Lead generation failed. Quota may be exhausted." });
    } finally {
      setLoading(null);
    }
  }

  return (
    <section className="max-w-2xl">
      <div className="mb-5">
        <h1 className="text-2xl font-semibold text-slate-950">Test Tools</h1>
        <p className="mt-1 text-sm text-slate-600">Use these controls to verify allocation, concurrency, and webhook behavior.</p>
      </div>

      <div className="space-y-4 rounded-md border border-slate-200 bg-white p-5">
        <label className="block text-sm font-medium text-slate-700">
          Webhook event_id
          <input
            className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
            value={eventId}
            onChange={(event) => setEventId(event.target.value)}
          />
        </label>

        <div className="flex flex-wrap gap-2">
          <button
            className="rounded-md bg-slate-950 px-4 py-2 text-sm font-medium text-white disabled:bg-slate-400"
            disabled={loading !== null}
            onClick={resetOnce}
          >
            {loading === "reset" ? "Resetting..." : "Reset provider quota"}
          </button>
          <button
            className="rounded-md border border-slate-300 px-4 py-2 text-sm font-medium hover:bg-slate-50 disabled:text-slate-400"
            disabled={loading !== null}
            onClick={callWebhookMultipleTimes}
          >
            {loading === "multi" ? "Calling..." : "Call webhook multiple times"}
          </button>
          <button
            className="rounded-md border border-slate-300 px-4 py-2 text-sm font-medium hover:bg-slate-50 disabled:text-slate-400"
            disabled={loading !== null}
            onClick={generateTenLeads}
          >
            {loading === "generate" ? "Generating..." : "Generate 10 leads instantly"}
          </button>
        </div>

        <button
          className="rounded-md border border-slate-300 px-3 py-2 text-sm hover:bg-slate-50"
          onClick={() => setEventId(`quota-reset-${Date.now()}`)}
        >
          New event_id
        </button>

        {message && <StatusMessage kind={message.kind} message={message.text} />}
      </div>
    </section>
  );
}
