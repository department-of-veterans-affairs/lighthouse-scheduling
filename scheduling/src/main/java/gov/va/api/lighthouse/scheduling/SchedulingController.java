package gov.va.api.lighthouse.scheduling;

import gov.va.api.health.r4.api.bundle.AbstractBundle;
import gov.va.api.health.r4.api.bundle.BundleLink;
import gov.va.api.health.r4.api.datatypes.Identifier;
import gov.va.api.health.r4.api.elements.Reference;
import gov.va.api.health.r4.api.resources.Appointment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/r4/Appointment",
    produces = {"application/json", "application/fhir+json"})
public class SchedulingController {

  @SneakyThrows
  Appointment buildAppointment(String appointmentID, String patientID, String locationID) {
    return Appointment.builder()
        .identifier(Collections.singletonList(Identifier.builder().id(appointmentID).build()))
        .id(appointmentID)
        .status(Appointment.AppointmentStatus.booked)
        .participant(
            List.of(
                Appointment.Participant.builder()
                    .actor(
                        Reference.builder()
                            .reference("Patient/" + patientID)
                            .display(patientID)
                            .build())
                    .status(Appointment.ParticipationStatus.tentative)
                    .build(),
                Appointment.Participant.builder()
                    .actor(
                        Reference.builder()
                            .reference("Location/" + locationID)
                            .display(locationID)
                            .build())
                    .status(Appointment.ParticipationStatus.accepted)
                    .build()))
        .build();
  }

  /** Read appointment by ID. */
  @GetMapping(value = "/{id}")
  Appointment readAppointmentId(@PathVariable("id") String id) {
    return buildAppointment("I2-APP", "I2-PAT", "I2-LOC");
  }

  /** Get appointment by search parameters. */
  @GetMapping(value = "")
  Appointment.Bundle readAppointmentSearchParameters(
      @RequestParam(value = "_id", defaultValue = "I2-APP", required = false) String id,
      @RequestParam(value = "patient", defaultValue = "I2-PAT", required = false) String patient,
      @RequestParam(value = "location", defaultValue = "I2-LOC", required = false) String location,
      @RequestParam(value = "identifier", required = false) String identifier) {
    Appointment appointment = buildAppointment("I2-APP", "I2-PAT", "I2-LOC");
    return Appointment.Bundle.builder()
        .link(
            Arrays.asList(
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.first)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + "I2-PAT"
                            + "&location="
                            + "I2-LOC"
                            + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.self)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + "I2-PAT"
                            + "&location="
                            + "I2-LOC"
                            + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.last)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + "I2-PAT"
                            + "&location="
                            + "I2-LOC"
                            + "&page=1&count=1")
                    .build()))
        .resourceType("Bundle")
        .type(AbstractBundle.BundleType.searchset)
        .total(1)
        .entry(List.of(Appointment.Entry.builder().resource(appointment).build()))
        .id("93")
        .build();
  }
}
