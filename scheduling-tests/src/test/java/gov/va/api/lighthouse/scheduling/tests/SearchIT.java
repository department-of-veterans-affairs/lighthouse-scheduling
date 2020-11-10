package gov.va.api.lighthouse.scheduling.tests;

import static gov.va.api.lighthouse.scheduling.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.scheduling.tests.TestClients.r4Scheduling;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.r4.api.resources.Appointment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.health.sentinel.TestClient;
import org.junit.jupiter.api.Test;

public class SearchIT {

  @Test
  public void searchByAppointmentIdTest() {
    final String appointmentId = systemDefinition().testIds().appointment();
    TestClient btc = r4Scheduling();
    String apiPath = btc.service().urlWithApiPath();
    ExpectedResponse response = btc.get(apiPath + "Appointment/" + appointmentId);
    response.expect(200).expectValid(Appointment.class);
    assertThat(response.response()).isNotNull();
  }

  @Test
  public void searchByParametersTest() {
    final String appointmentId = systemDefinition().testIds().appointment();
    final String patientId = systemDefinition().testIds().patient();
    final String locationId = systemDefinition().testIds().location();
    TestClient btc = r4Scheduling();
    String apiPath = btc.service().urlWithApiPath();
    ExpectedResponse response =
        btc.get(apiPath + "Appointment?_id=" + appointmentId + " &patient=" + patientId);
    response.expect(200).expectValid(Appointment.Bundle.class);
    assertThat(response.response()).isNotNull();
    response =
        btc.get(apiPath + "Appointment?identifier=" + appointmentId + "&location=" + locationId);
    response.expect(200).expectValid(Appointment.Bundle.class);
    assertThat(response.response()).isNotNull();
  }
}
