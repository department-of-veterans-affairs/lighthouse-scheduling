package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.health.sentinel.SentinelProperties.magicAccessToken;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.SentinelProperties;
import gov.va.api.health.sentinel.ServiceDefinition;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
class SystemDefinitions {

  private static SystemDefinition lab() {
    String url = "https://sandbox-api.va.gov";
    return SystemDefinition.builder()
        .scheduling(
            serviceDefinition("scheduling", url, 443, magicAccessToken(), "/services/fhir/v0/r4/"))
        .testIds(testIds())
        .build();
  }

  private static SystemDefinition local() {
    String url = "http://localhost";
    return SystemDefinition.builder()
        .scheduling(serviceDefinition("scheduling", url, 8060, null, "/r4/"))
        .testIds(testIds())
        .build();
  }

  private static SystemDefinition qa() {
    String url = "https://blue.qa.lighthouse.va.gov";
    return SystemDefinition.builder()
        .scheduling(serviceDefinition("scheduling", url, 443, magicAccessToken(), "/fhir/v0/r4/"))
        .testIds(testIds())
        .build();
  }

  private static ServiceDefinition serviceDefinition(
      String name, String url, int port, String accessToken, String apiPath) {
    return SentinelProperties.forName(name)
        .defaultUrl(url)
        .defaultPort(port)
        .defaultApiPath(apiPath)
        .accessToken(() -> Optional.ofNullable(accessToken))
        .build()
        .serviceDefinition();
  }

  private static SystemDefinition stagingLab() {
    String url = "https://blue.staging-lab.lighthouse.va.gov";
    return SystemDefinition.builder()
        .scheduling(serviceDefinition("scheduling", url, 443, magicAccessToken(), "/fhir/v0/r4/"))
        .testIds(testIds())
        .build();
  }

  static SystemDefinition systemDefinition() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      case QA:
        return qa();
      case STAGING_LAB:
        return stagingLab();
      case LAB:
        return lab();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  private static TestIds testIds() {
    return TestIds.builder()
        .appointment("I2-5XYSWFRZ637QKNR6IIRKYHA5RY000109")
        .patient("1011537977V693883")
        // Frankenpatient
        .oauthPatient("1017283180V801730")
        .build();
  }
}
