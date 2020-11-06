package gov.va.api.lighthouse.scheduling.tests;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ServiceDefinition;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

public class SystemDefinitions {

  private static SystemDefinition local() {
    String url = "http://localhost";
    return SystemDefinition.builder().scheduling(serviceDefinition(url, 8060, null, "/")).build();
  }

  private static ServiceDefinition serviceDefinition(
      String url, int port, String accessToken, String apiPath) {
    return ServiceDefinition.builder()
        .url(url)
        .port(port)
        .accessToken(() -> Optional.ofNullable(accessToken))
        .apiPath(apiPath)
        .build();
  }

  static SystemDefinition systemDefinition() {
    switch (Environment.get()) {
      case LOCAL:
        return local();
      default:
        throw new IllegalArgumentException(
            "Unsupported sentinel environment: " + Environment.get());
    }
  }

  @Value
  @Builder
  static final class SystemDefinition {
    @NonNull ServiceDefinition scheduling;
  }
}
