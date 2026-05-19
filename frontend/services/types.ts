export type ServiceType = "SERVICE_1" | "SERVICE_2" | "SERVICE_3";

export type LeadRequest = {
  name: string;
  phone: string;
  city: string;
  serviceType: ServiceType;
  description: string;
};

export type Lead = LeadRequest & {
  id: number;
  createdAt: string;
};

export type AssignedLead = {
  assignmentId: number;
  leadId: number;
  customerName: string;
  phone: string;
  city: string;
  serviceType: ServiceType;
  description: string;
  assignedAt: string;
};

export type Provider = {
  id: number;
  name: string;
  monthlyQuota: number;
  usedQuota: number;
  remainingQuota: number;
  leadsReceivedCount: number;
  assignedLeads: AssignedLead[];
};

export type WebhookResponse = {
  eventId: string;
  processed: boolean;
  message: string;
};
