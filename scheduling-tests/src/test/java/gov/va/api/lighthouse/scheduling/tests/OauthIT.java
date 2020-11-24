package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static org.junit.jupiter.api.Assertions.fail;

import gov.va.api.health.r4.api.resources.Appointment;
import gov.va.api.health.r4.api.resources.OperationOutcome;
import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.OauthRobotProperties;
import gov.va.api.health.sentinel.SystemOauthRobot;
import gov.va.api.health.sentinel.TestClient;
import gov.va.api.health.sentinel.TokenExchange;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class OauthIT {
  TestClient client = TestClients.r4Scheduling();

  TestIds ids = SystemDefinitions.systemDefinition().testIds();

  private String exchangeToken() {
    SystemOauthRobot.Configuration config =
        OauthRobotProperties.usingSystemProperties()
            .forSystemOauth()
            // Loading ClientId, ClientSecret, Audience, and TokenUrl will be done via properties
            .defaultScopes(List.of("system/Appointment.read"))
            .build()
            .systemOauthConfig();
    TokenExchange token = SystemOauthRobot.builder().config(config).build().token();
    if (token.isError()) {
      fail("Failed to get token result from oauth robot.");
    }
    return token.accessToken();
  }

  @Test
  void oauthFlow() {
    assumeEnvironmentIn(Environment.STAGING_LAB, Environment.LAB);
    String token = exchangeToken();
    // Valid Token
    test(
        200,
        Appointment.Bundle.class,
        token,
        client.service().urlWithApiPath() + "Appointment?patient={icn}",
        ids.oauthPatient());
    // Invalid Token
    test(
        401,
        OperationOutcome.class,
        "NOPE",
        client.service().urlWithApiPath() + "Appointment?patient={icn}",
        ids.oauthPatient());
    // Invalid Resource for Scopes
    test(
        200,
        Appointment.Bundle.class,
        token,
        client.service().urlWithApiPath() + "Observation?patient={icn}",
        ids.oauthPatient());
  }

  private void test(int status, Class<?> expected, String token, String path, String... params) {
    log.info(
        "Oauth: Expect {} ({}) for {} {}",
        expected.getSimpleName(),
        status,
        path,
        Arrays.toString(params));
    client
        .get(Map.of("Authorization", "Bearer " + token), path, params)
        .expect(status)
        .expectValid(expected);
  }
}
