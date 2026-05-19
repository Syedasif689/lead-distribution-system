package com.project.dto;

import java.util.List;

public record GenerateLeadsResponse(int requested, int created, List<LeadResponse> leads) {
}
