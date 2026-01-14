package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The result of a call to api/v2/organizations/{organizationId}
 */
@SuppressWarnings("java:S1258")  // remove Sonar issue
@Getter
public class ApiOrganizationDTO {

    String id;

    String name;

    String componentDisplayName;


}
