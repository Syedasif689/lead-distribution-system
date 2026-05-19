@Service
public class TestService {

    private final LeadService leadService;

    public TestService(LeadService leadService) {
        this.leadService = leadService;
    }

    public GenerateLeadsResponse generateLeads() {

        String batch = UUID.randomUUID().toString().substring(0, 8);

        List<LeadResponse> leads = java.util.stream.IntStream.rangeClosed(1, 10)
                .mapToObj(i -> leadService.createLead(new CreateLeadRequest(
                        "Generated Customer " + i,
                        "90000" + batch.substring(0, 4) + i,
                        "Mumbai",
                        ServiceType.values()[i % ServiceType.values().length],
                        "Generated test lead " + i + " for concurrency verification"
                )))
                .toList();

        return new GenerateLeadsResponse(10, leads.size(), leads);
    }
}