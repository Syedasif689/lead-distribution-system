import type { Provider } from "@/services/types";

const serviceLabels = {
  SERVICE_1: "Service 1",
  SERVICE_2: "Service 2",
  SERVICE_3: "Service 3"
};

export function ProviderCard({ provider }: { provider: Provider }) {
  return (
    <article className="rounded-md border border-slate-200 bg-white p-4">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h2 className="text-base font-semibold text-slate-950">{provider.name}</h2>
          <p className="mt-1 text-sm text-slate-600">{provider.leadsReceivedCount} assigned leads</p>
        </div>
        <div className="text-right text-sm">
          <p className="font-semibold text-slate-950">{provider.remainingQuota} remaining</p>
          <p className="text-slate-500">
            {provider.usedQuota}/{provider.monthlyQuota} used
          </p>
        </div>
      </div>

      <div className="mt-4 max-h-80 space-y-2 overflow-y-auto">
        {provider.assignedLeads.length === 0 ? (
          <p className="rounded-md bg-slate-50 px-3 py-2 text-sm text-slate-500">No leads assigned yet.</p>
        ) : (
          provider.assignedLeads.map((lead) => (
            <div key={lead.assignmentId} className="rounded-md border border-slate-200 p-3 text-sm">
              <div className="flex items-center justify-between gap-2">
                <p className="font-medium text-slate-950">{lead.customerName}</p>
                <span className="rounded bg-slate-100 px-2 py-1 text-xs text-slate-700">{serviceLabels[lead.serviceType]}</span>
              </div>
              <p className="mt-1 text-slate-600">
                {lead.city} · {lead.phone}
              </p>
              <p className="mt-2 line-clamp-2 text-slate-500">{lead.description}</p>
            </div>
          ))
        )}
      </div>
    </article>
  );
}
