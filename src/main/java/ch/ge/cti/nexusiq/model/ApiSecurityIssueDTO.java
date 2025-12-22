package ch.ge.cti.nexusiq.model;

import lombok.Getter;
import lombok.ToString;

/**
 * An item in a "securityIssues" property within a "securityData".
 */
@Getter
@ToString
public class ApiSecurityIssueDTO {

    private String source;

    private String reference;

    private float severity;

    private String status;

    private String url;

    private String threatCategory;

    private String cwe;

    private String cvssVector;

    private String cvssVectorSource;

    private ApiSecurityIssueAnalysisDTO analysis;

}
