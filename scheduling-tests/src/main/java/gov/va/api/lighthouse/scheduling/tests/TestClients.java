package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.lighthouse.scheduling.tests.SystemDefinitions.systemDefinition;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.sentinel.FhirTestClient;
import gov.va.api.health.sentinel.TestClient;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestClients {
  TestClient getFhirTestClient() {
    return FhirTestClient.builder()
        .service(systemDefinition().getScheduling())
        .contentTypes(List.of("application/json", "application/fhir+json"))
        .mapper(JacksonConfig::createMapper)
        .errorResponseEqualityCheck(
            new gov.va.api.lighthouse.scheduling.tests.OperationOutcomesAreFunctionallyEqual())
        .build();
  }
}
