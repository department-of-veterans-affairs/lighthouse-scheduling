package gov.va.api.lighthouse.scheduling.tests;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.SentinelProperties;
import gov.va.api.health.sentinel.ServiceDefinition;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
class SystemDefinitions {

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
        .scheduling(serviceDefinition("scheduling", url, 443, null, "/scheduling/r4/"))
        .testIds(testIds())
        .build();
  }

  private static ServiceDefinition serviceDefinition(
      String name, String url, int port, String accessToken, String apiPath) {
    return ServiceDefinition.builder()
        .url(SentinelProperties.optionUrl(name, url))
        .port(port)
        .accessToken(() -> Optional.ofNullable(accessToken))
        .apiPath(apiPath)
        .build();
  }

  static SystemDefinition systemDefinition() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      case QA:
        return qa();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  private static TestIds testIds() {
    return TestIds.builder()
        .appointment("I2-5XYSWFRZ637QKNR6IIRKYHA5RY000109")
        .patient("1092387456V321456")
        .location("I2-9QPSWFRZ530PLNR6IIRKYHA5RY031128")
        .build();
  }
}
