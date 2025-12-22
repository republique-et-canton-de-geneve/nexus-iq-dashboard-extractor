package ch.ge.cti.nexusiq.business;

import ch.ge.cti.nexusiq.model.ApiApplicationDTO;
import ch.ge.cti.nexusiq.model.ApiApplicationReportDTOV2;
import ch.ge.cti.nexusiq.model.ApiOrganizationDTO;
import ch.ge.cti.nexusiq.model.ApiReportComponentDTOV2;
import ch.ge.cti.nexusiq.model.ApiSecurityIssueDTO;
import ch.ge.cti.nexusiqdashboard.ApiException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.Arrays;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class ExtractorService {

    @Resource
    private NexusIqAccessService nexusIqAccessService;

    /**
     * This is the main method of this application.
     * It generates a JSON file with all applications' vulnerabilities.
     */
    public void generateResultFile() throws ApiException {
        // get all application reports
        var reports = nexusIqAccessService.getApplicationReport();
        log.info("Number of reports: {}", reports.length);

        // for every application report, append the JSON to the
        StringBuilder sbFullJson = new StringBuilder();
        Arrays.stream(reports)
                .forEach(report -> {
                    var json = getJsonForApplication(report);
                    sbFullJson.append(json);
                });

        // dump the full JSON into a result file
    }

    private String getJsonForApplication(ApiApplicationReportDTOV2 report) {
        var applicationReport = nexusIqAccessService.getApplicationReport(report.getReportDataUrl());

        var application = nexusIqAccessService.getApplication(report.getApplicationId());
        log.debug("Application = {}", application.getName());

        var organization = nexusIqAccessService.getOrganization(application.getOrganizationId());
        log.debug("Organization = {}", organization.getName());

        applicationReport.getComponents()
                .forEach(comp -> getSecurityIssues(comp, application, organization));

        return null;
    }

    private ApiSecurityIssueDTO getSecurityIssues(
            ApiReportComponentDTOV2 component, ApiApplicationDTO application, ApiOrganizationDTO organization) {
        var securityData= component.getSecurityData();
        if (securityData != null) {
            var securityIssues = component.getSecurityData().getSecurityIssues();
            if (! isEmpty(securityIssues)) {
                log.info("Application {} (organization = {}) has component [{}] with security issues {}",
                        application.getName(), organization.getName(), component.getDisplayName(), securityIssues);
                if (securityIssues[0].getAnalysis() != null) {
                    log.info("Analysis = [{}]", securityIssues[0].getAnalysis());
                    System.exit(0);
                }
            }
        }
        return null;
    }

}
