package ch.ge.cti.nexusiq.business;

import lombok.Builder;
import lombok.Setter;

/**
 * The result ...
 */
@Setter
@Builder
public class Result {

    /**
     * Name of the Nexus IQ organization.
     * Example: "OPF".
     */
    String organizationName;

    /**
     * Name of the Nexus IQ application.
     * Example: "edem-webapp-solde".
     */
    String applicationName;

    /**
     * Name of the application's component that has security issues.
     * Example: "@angular/compiler : 14.3.0".
     */
    String componentDisplayName;

    /**
     * Security issue source.
     * Example: "cve".
     */
    String severityIssueSource;

    /**
     * Security issue reference.
     * Example: "CVE-2025-64756".
     */
    String severityIssueReference;

    /**
     * Severity issue severity.
     * Example: 7.7.
     */
    Float severityIssueSeverity;

    /**
     * Severity issue status.
     * Example: "Open".
     */
    String severityIssueStatus;

    /**
     * Severity issue URL.
     * Example: https://www.cve.org/CVERecord?id=CVE-2025-48976.
     */
    String severityIssueUrl;

    /**
     * Severity issue threat category.
     * Example: "critical".
     */
    String severityIssueThreatCategory;

    /**
     * Severity issue CWE.
     * Example: 770.
     */
    String severityIssueCwe;

}
