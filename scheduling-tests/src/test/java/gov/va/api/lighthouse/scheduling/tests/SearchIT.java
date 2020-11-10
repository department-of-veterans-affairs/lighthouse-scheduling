package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.lighthouse.scheduling.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.scheduling.tests.TestClients.r4Scheduling;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.r4.api.resources.Appointment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.health.sentinel.TestClient;
import gov.va.api.lighthouse.scheduling.SchedulingController;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class SearchIT {

  public void checkResponse(String endpoint, Class expectedClass) {
    TestClient btc = r4Scheduling();
    String apiPath = btc.service().urlWithApiPath();
    ExpectedResponse response = btc.get(apiPath + endpoint);
    response.expect(200).expectValid(expectedClass);
    assertThat(response.response()).isNotNull();
  }

  @Test
  public void searchByAppointmentIdTest() {
    final String appointmentId = systemDefinition().testIds().appointment();
    checkResponse("Appointment/" + appointmentId, Appointment.class);
  }

  @Test
  public void searchByParametersTest() {
    final String appointmentId = systemDefinition().testIds().appointment();
    final String patientId = systemDefinition().testIds().patient();
    checkResponse(
        SchedulingController.StubbedQueryStringBuilder.builder()
            .id(Optional.ofNullable(appointmentId))
            .build()
            .toStubbedQueryString(),
        Appointment.Bundle.class);
    checkResponse(
        SchedulingController.StubbedQueryStringBuilder.builder()
            .identifier(Optional.ofNullable(appointmentId))
            .build()
            .toStubbedQueryString(),
        Appointment.Bundle.class);
    checkResponse(
        SchedulingController.StubbedQueryStringBuilder.builder()
            .pat(Optional.ofNullable(patientId))
            .build()
            .toStubbedQueryString(),
        Appointment.Bundle.class);
  }
}
