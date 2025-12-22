package ch.ge.cti.nexusiq.model;

import lombok.Getter;

import java.util.List;

/**
 * The result of a call to api/v2/applications/<{applicationId}/reports/{scanId}/raw.
 */
@Getter
public class ApiReportRawDataDTOV2 {

    private List<ApiReportComponentDTOV2> components;

}
