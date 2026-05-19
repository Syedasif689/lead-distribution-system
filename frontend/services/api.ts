import axios from "axios";
import type { Lead, LeadRequest, Provider, WebhookResponse } from "./types";

export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    "Content-Type": "application/json"
  }
});

export async function createLead(payload: LeadRequest) {
  const { data } = await api.post<Lead>("/leads", payload);
  return data;
}

export async function getLeads() {
  const { data } = await api.get<Lead[]>("/leads");
  return data;
}

export async function getProviders() {
  const { data } = await api.get<Provider[]>("/providers");
  return data;
}

export async function resetQuota(eventId: string) {
  const { data } = await api.post<WebhookResponse>("/webhook/reset-quota", { event_id: eventId });
  return data;
}

export async function generateLeads() {
  const { data } = await api.post<{ requested: number; created: number; leads: Lead[] }>("/test/generate-leads");
  return data;
}
