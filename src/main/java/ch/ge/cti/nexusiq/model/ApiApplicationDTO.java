package ch.ge.cti.nexusiq.model;

import lombok.Getter;

/**
 * The result of a call to api/v2/applications/{applicationId}
 */
@Getter
public class ApiApplicationDTO {

    String id;

    String name;

    String organizationId;

}
