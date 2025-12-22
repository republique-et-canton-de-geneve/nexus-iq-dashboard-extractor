package ch.ge.cti.nexusiq.model;

import lombok.Getter;
import lombok.ToString;

/**
 * The "analysis" property within an item in "securityIssues".
 * Maybe useless: always empty at Etat de Geneve.
 */
@Getter
@ToString
public class ApiSecurityIssueAnalysisDTO {

    private String state;

    private String justification;

    private String response;

    private String detail;

}
