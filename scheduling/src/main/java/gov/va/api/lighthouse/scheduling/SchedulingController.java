package gov.va.api.lighthouse.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.health.r4.api.datatypes.Identifier;
import gov.va.api.health.r4.api.elements.Reference;
import gov.va.api.health.r4.api.resources.Appointment;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/scheduling")
public class SchedulingController {

  @SneakyThrows
  String buildJson(String appointmentID, String patientID, String locationID) {
    Appointment appointment =
        Appointment.builder()
            .identifier(Collections.singletonList(Identifier.builder().id(appointmentID).build()))
            .status(Appointment.AppointmentStatus.booked)
            .participant(
                List.of(
                    Appointment.Participant.builder()
                        .actor(Reference.builder().id(patientID).type("Patient").build())
                        .status(Appointment.ParticipationStatus.tentative)
                        .build(),
                    Appointment.Participant.builder()
                        .actor(Reference.builder().id(locationID).type("Location").build())
                        .status(Appointment.ParticipationStatus.accepted)
                        .build()))
            .build();
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    return mapper.writeValueAsString(appointment);
  }

  /** Read appointment by ID. */
  @GetMapping(value = "/appointment/{id}", produces = "application/json")
  String readAppointmentId(@PathVariable("id") String id) {
    return buildJson(id, "109", "1010");
  }

  /** Get appointment by search parameters. */
  @GetMapping(value = "/appointment", produces = "application/json")
  String readAppointmentSearchParameters(
      @RequestParam(value = "patient", required = false) String patient,
      @RequestParam(value = "location", required = false) String location) {
    if (patient != null) {
      return buildJson("93", patient, "103");
    } else if (location != null) {
      return buildJson("62", "25", location);
    }
    return "{\"ERROR\":\"Please provide a search parameter\"";
  }
}
