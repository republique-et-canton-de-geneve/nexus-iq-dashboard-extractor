package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * An item in the "components" property of an application report.
 */
@Getter
public class ApiReportComponentDTOV2 {

    private String packageUrl;

    private String displayName;

    private ApiSecurityDataDTO securityData;

}
