package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * An item in the "components" property of an application report.
 */
@Getter
@SuppressWarnings("java:S1258")  // remove Sonar issue
public class ApiReportComponentDTOV2 {

    private String packageUrl;

    private String displayName;

    private ApiComponentIdentifierDTOV2 componentIdentifier;

    private ApiSecurityDataDTO securityData;

}
