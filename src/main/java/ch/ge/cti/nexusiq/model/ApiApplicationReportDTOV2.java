package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The result of a call to api/v2/reports/applications.
 *
 * Quote from the Nexus IQ OpenAPI:
 * "The response JSON contains URLs to view the report data in html and pdf format,
 * for each application to which you have access.
 * The response field stage indicates the stage at which the policy evaluation was
 * executed, such as 'develop', 'build' and 'release' The response field latestReportHtmlUrl
 * is a relative link to view the most recent report. Response fields reportPdfUrl and
 * reportHtmlUrl are links to view the pdf version of the report.The response field
 * reportDataUrl is a link to view the most recent report data."
 */
@Getter
public class ApiApplicationReportDTOV2 {

    private String applicationId;

    private String reportDataUrl;

}
