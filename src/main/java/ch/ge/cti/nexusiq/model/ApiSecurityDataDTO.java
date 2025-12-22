package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The "securityData" property within a "component".
 */
@Getter
public class ApiSecurityDataDTO {

    private ApiSecurityIssueDTO[] securityIssues;

}
