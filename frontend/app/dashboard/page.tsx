"use client";

import { useCallback, useEffect, useState } from "react";
import { ProviderCard } from "@/components/ProviderCard";
import { StatusMessage } from "@/components/StatusMessage";
import { useAssignmentsSocket } from "@/hooks/useAssignmentsSocket";
import { getProviders } from "@/services/api";
import type { Provider } from "@/services/types";

export default function DashboardPage() {
  const [providers, setProviders] = useState<Provider[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadProviders = useCallback(async () => {
    try {
      const data = await getProviders();
      setProviders(data);
      setError(null);
    } catch {
      setError("Unable to load providers from the backend.");
    } finally {
      setLoading(false);
    }
  }, []);

  const { connected } = useAssignmentsSocket(loadProviders);

  useEffect(() => {
    void loadProviders();
  }, [loadProviders]);

  return (
    <section>
      <div className="mb-5 flex flex-wrap items-end justify-between gap-3">
        <div>
          <h1 className="text-2xl font-semibold text-slate-950">Provider Dashboard</h1>
          <p className="mt-1 text-sm text-slate-600">Real database quota and assignment data.</p>
        </div>
        <div className="flex items-center gap-3">
          <span className={`h-2.5 w-2.5 rounded-full ${connected ? "bg-emerald-500" : "bg-slate-300"}`} />
          <span className="text-sm text-slate-600">{connected ? "Live updates connected" : "Live updates reconnecting"}</span>
          <button className="rounded-md border border-slate-300 px-3 py-2 text-sm hover:bg-slate-50" onClick={() => void loadProviders()}>
            Refresh
          </button>
        </div>
      </div>

      {error && <div className="mb-4"><StatusMessage kind="error" message={error} /></div>}
      {loading ? (
        <StatusMessage kind="info" message="Loading provider data..." />
      ) : (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {providers.map((provider) => (
            <ProviderCard key={provider.id} provider={provider} />
          ))}
        </div>
      )}
    </section>
  );
}
