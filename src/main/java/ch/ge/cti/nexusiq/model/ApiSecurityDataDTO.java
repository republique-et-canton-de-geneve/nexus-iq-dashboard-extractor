package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The "securityData" property within a "component".
 */
@SuppressWarnings("java:S1258")  // remove Sonar issue
@Getter
public class ApiSecurityDataDTO {

    private ApiSecurityIssueDTO[] securityIssues;

}
