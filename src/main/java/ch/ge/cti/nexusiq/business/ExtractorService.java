package ch.ge.cti.nexusiq.business;

import ch.ge.cti.nexusiq.configuration.WebClientProvider;
import ch.ge.cti.nexusiqdashboard.ApiException;
import ch.ge.cti.nexusiqdashboard.marshalling.ApiApplicationReportDTOV2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExtractorService {

    @Resource
    private WebClientProvider webClientProvider;

    /**
     * This is the main method of this application.
     * Generates the JSON file with all applications' vulnerabilities.
     */
    public void generateResultFile() throws ApiException {
        // get all reports
        var reports = getApplicationReports();
        log.info("Number of reports: {}", reports.length);

        // ...
    }

    private ApiApplicationReportDTOV2[] getApplicationReports() {
        try {
            var uri = "/api/v2/reports/applications";
            return webClientProvider.getWebClient()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ApiApplicationReportDTOV2[].class)
                    .block();
        } catch (RuntimeException e) {
            handleInvocationError(e);
            return new ApiApplicationReportDTOV2[] {};
        }
    }

    /**
     * Error during the call to NexusServices.
     * Handles cases where the error is neither a 4xx nor a 5xx. Examples: NexusServices is unavailable;
     * the URL of NexusServices is incorrect.
     * The best solution found is a try/catch block to handle this case,
     * see <a href="https://stackoverflow.com/questions/73989083/handling-server-unavailability-with-webclient">stackoverflow/handling-server-unavailability-with-webclient</a>.
     */
    private void handleInvocationError(RuntimeException exception) {
        log.error("Error during the call to Nexus IQ:", exception);
    }

}
