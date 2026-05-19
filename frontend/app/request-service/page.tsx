"use client";

import { FormEvent, useState } from "react";
import { createLead } from "@/services/api";
import type { LeadRequest, ServiceType } from "@/services/types";
import { StatusMessage } from "@/components/StatusMessage";

const initialForm: LeadRequest = {
  name: "",
  phone: "",
  city: "",
  serviceType: "SERVICE_1",
  description: ""
};

export default function RequestServicePage() {
  const [form, setForm] = useState<LeadRequest>(initialForm);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ kind: "success" | "error"; text: string } | null>(null);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setLoading(true);
    setMessage(null);

    try {
      await createLead(form);
      setMessage({ kind: "success", text: "Lead created and allocated to providers." });
      setForm(initialForm);
    } catch (error: any) {
      setMessage({
        kind: "error",
        text: error?.response?.data?.message ?? "Unable to create lead."
      });
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="max-w-2xl">
      <div className="mb-5">
        <h1 className="text-2xl font-semibold text-slate-950">Request Service</h1>
        <p className="mt-1 text-sm text-slate-600">A submitted lead is persisted and allocated immediately.</p>
      </div>

      <form onSubmit={submit} className="space-y-4 rounded-md border border-slate-200 bg-white p-5">
        <label className="block text-sm font-medium text-slate-700">
          Name
          <input
            className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
            value={form.name}
            onChange={(event) => setForm({ ...form, name: event.target.value })}
            required
            maxLength={120}
          />
        </label>

        <label className="block text-sm font-medium text-slate-700">
          Phone Number
          <input
            className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
            value={form.phone}
            onChange={(event) => setForm({ ...form, phone: event.target.value })}
            required
            maxLength={32}
          />
        </label>

        <label className="block text-sm font-medium text-slate-700">
          City
          <input
            className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
            value={form.city}
            onChange={(event) => setForm({ ...form, city: event.target.value })}
            required
            maxLength={120}
          />
        </label>

        <label className="block text-sm font-medium text-slate-700">
          Service Type
          <select
            className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2"
            value={form.serviceType}
            onChange={(event) => setForm({ ...form, serviceType: event.target.value as ServiceType })}
          >
            <option value="SERVICE_1">Service 1</option>
            <option value="SERVICE_2">Service 2</option>
            <option value="SERVICE_3">Service 3</option>
          </select>
        </label>

        <label className="block text-sm font-medium text-slate-700">
          Description
          <textarea
            className="mt-1 min-h-28 w-full rounded-md border border-slate-300 px-3 py-2"
            value={form.description}
            onChange={(event) => setForm({ ...form, description: event.target.value })}
            required
            maxLength={2000}
          />
        </label>

        {message && <StatusMessage kind={message.kind} message={message.text} />}

        <button
          className="rounded-md bg-slate-950 px-4 py-2 text-sm font-medium text-white disabled:cursor-not-allowed disabled:bg-slate-400"
          disabled={loading}
          type="submit"
        >
          {loading ? "Submitting..." : "Submit Lead"}
        </button>
      </form>
    </section>
  );
}
