package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.lighthouse.scheduling.tests.SystemDefinitions.systemDefinition;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.sentinel.BasicTestClient;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.health.sentinel.TestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SchedulingControllerIT {

  @Test
  public void readAppointmentIdTest() {
    TestClient btc =
        BasicTestClient.builder()
            .service(systemDefinition().getScheduling())
            .contentType("application/json")
            .mapper(JacksonConfig::createMapper)
            .build();
    String apiPath = btc.service().urlWithApiPath();
    ExpectedResponse response = btc.get(apiPath + "scheduling/appointment/1");
    log.info(response.response().print());
  }
}
