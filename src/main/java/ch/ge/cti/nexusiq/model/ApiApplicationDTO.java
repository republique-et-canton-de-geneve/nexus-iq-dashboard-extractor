package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The result of a call to api/v2/applications/{applicationId}
 */
@Getter
@SuppressWarnings("java:S1258")  // remove Sonar issue
public class ApiApplicationDTO {

    String id;

    String name;

    String organizationId;

}
