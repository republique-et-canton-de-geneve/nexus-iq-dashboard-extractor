package ch.ge.cti.nexusiq.business;

import ch.ge.cti.nexusiq.model.ApiApplicationDTO;
import ch.ge.cti.nexusiq.model.ApiApplicationReportDTOV2;
import ch.ge.cti.nexusiq.model.ApiOrganizationDTO;
import ch.ge.cti.nexusiq.model.ApiReportComponentDTOV2;
import ch.ge.cti.nexusiqdashboard.ApiException;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ExtractorService {

    @Resource
    private NexusIqAccessService nexusIqAccessService;

    /**
     * This is the main method of the application.
     * It generates a JSON file with all applications' vulnerabilities.
     */
    public void generateResultFile() throws ApiException {
        // get all application reports
        var reports = nexusIqAccessService.getApplicationReport();
        log.info("Number of reports: {}", reports.length);

        // for every application, for every component, for every security issue, create a Result
        AtomicInteger counter = new AtomicInteger(0);
        var allApplicationsResults = new ArrayList<Result>();
        Arrays.stream(reports)
                .forEach(report -> {
                    List<Result> applicationResults = getApplicationResults(report);
                    allApplicationsResults.addAll(applicationResults);
                    if (counter.incrementAndGet() % 25 == 0) {
                        log.info("Already processed {} reports", counter);
                    }
                });

        // serialize the results into JSON format
        Gson gson = new Gson();
        String jsonString = gson.toJson(allApplicationsResults);

        // dump the full JSON into a result file
        dumpOutput(jsonString);
    }

    /**
     * Gets every security issues report of every component of the specified application.
     */
    private List<Result> getApplicationResults(ApiApplicationReportDTOV2 report) {
        var applicationReport = nexusIqAccessService.getApplicationReport(report.getReportDataUrl());
        var applicationResults = new ArrayList<Result>();

        if (applicationReport != null) {     // this happens sometimes
            var application = nexusIqAccessService.getApplication(report.getApplicationId());
            log.debug("Application = {}", application.getName());

            var organization = nexusIqAccessService.getOrganization(application.getOrganizationId());
            log.debug("Organization = {}", organization.getName());

            applicationReport.getComponents()
                    .forEach(comp -> {
                        List<Result> componentResults = getComponentResults(comp, application, organization);
                        applicationResults.addAll(componentResults);
                    });
        }

        return applicationResults;
    }

    /**
     * Gets every security issues report of the specified component.
     */
    private List<Result> getComponentResults(
            ApiReportComponentDTOV2 component, ApiApplicationDTO application, ApiOrganizationDTO organization) {
        var componentResults = new ArrayList<Result>();
        if (component.getSecurityData() != null) {
            var securityIssues = component.getSecurityData().getSecurityIssues();
            log.debug("Application {} (organization = {}) has component [{}] with security issues {}",
                        application.getName(), organization.getName(), component.getDisplayName(), securityIssues);
            Arrays.stream(securityIssues)
                    .forEach(securityIssue -> {
                        var result = Result.builder()
                                .organizationName(organization.getName())
                                .applicationName(application.getName())
                                .componentDisplayName(component.getDisplayName())
                                .severityIssueSource(securityIssue.getSource())
                                .severityIssueReference(securityIssue.getReference())
                                .severityIssueSeverity(securityIssue.getSeverity())
                                .severityIssueStatus(securityIssue.getStatus())
                                .severityIssueUrl(securityIssue.getUrl())
                                .severityIssueThreatCategory(securityIssue.getThreatCategory())
                                .severityIssueCwe(securityIssue.getCwe())
                                .build();
                        componentResults.add(result);
                        if (securityIssue.getAnalysis() != null) {
                            // a trace, just to know that an analysis is not empty
                            log.warn("Analysis = [{}]", securityIssue.getAnalysis());
                        }
                    });
        }
        return componentResults;
    }

    private void dumpOutput(String jsonString) {
        var formattedDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        var path = Path.of("./output/result_" + formattedDate + ".json");
        try {
            Files.writeString(path, jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing JSON output into file " + path, e);
        }
    }

}
