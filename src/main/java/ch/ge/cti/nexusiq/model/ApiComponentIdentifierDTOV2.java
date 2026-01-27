package ch.ge.cti.nexusiq.model;

import lombok.Getter;

import java.util.Map;

/**
 * The "componentIdentifier" within a "components" of an application report.
 */
@Getter
@SuppressWarnings("java:S1258")  // remove Sonar issue
public class ApiComponentIdentifierDTOV2 {

    private String format;

    private Map<String, String> coordinates;

}
