package ch.ge.cti.nexusiq.business;

import ch.ge.cti.nexusiq.configuration.WebClientProvider;
import ch.ge.cti.nexusiq.model.ApiApplicationDTO;
import ch.ge.cti.nexusiq.model.ApiApplicationReportDTOV2;
import ch.ge.cti.nexusiq.model.ApiOrganizationDTO;
import ch.ge.cti.nexusiq.model.ApiReportRawDataDTOV2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * An object to perform the raw REST calls to the Nexus IQ API.
 */
@Service
@Slf4j
public class NexusIqAccessService {

    @Resource
    private WebClientProvider webClientProvider;

    /**
     * Retrieves from Nexus IQ the report of every application.
     */
    public ApiApplicationReportDTOV2[] getApplicationReport() {
        try {
            var uri = "/api/v2/reports/applications";
            log.info("URI = {}", uri);
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
     * Retrieves from Nexus IQ the report for a specific application.
     * @param reportDataUrl Nexus IQ API URL to be invoked to retrieve the report.
     *                      Example: api/v2/applications/{applicationId}/reports/{scanId}/raw.
     */
    public ApiReportRawDataDTOV2 getApplicationReport(String reportDataUrl) {
        try {
            var uri = reportDataUrl;
            log.info("URI = {}", uri);
            return webClientProvider.getWebClient()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ApiReportRawDataDTOV2.class)
                    .block();
        } catch (RuntimeException e) {
            handleInvocationError(e);
            return null;
        }
    }

    /**
     * Retrieves from Nexus IQ the basic data of an application.
     */
    public ApiApplicationDTO getApplication(String applicationId) {
        try {
            var uri = "/api/v2/applications/" + applicationId;
            return webClientProvider.getWebClient()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ApiApplicationDTO.class)
                    .block();
        } catch (RuntimeException e) {
            handleInvocationError(e);
            return null;
        }
    }

    /**
     * Retrieves from Nexus IQ the basic data of an organization.
     */
    public ApiOrganizationDTO getOrganization(String organizationId) {
        try {
            var uri = "/api/v2/organizations/" + organizationId;
            return webClientProvider.getWebClient()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ApiOrganizationDTO.class)
                    .block();
        } catch (RuntimeException e) {
            handleInvocationError(e);
            return null;
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
