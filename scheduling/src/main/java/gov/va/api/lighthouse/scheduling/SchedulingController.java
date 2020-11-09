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
  }

  /** Read appointment by ID. */
  @GetMapping(value = "/{id}")
  Appointment readAppointmentId(@PathVariable("id") String id) {
    return buildAppointment(id, "109", "1010");
  }

  /** Get appointment by search parameters. */
  @GetMapping(params = {"patient", "location"})
  Appointment.Bundle readAppointmentSearchParameters(
      @RequestParam(value = "patient", required = false) String patient,
      @RequestParam(value = "location", required = false) String location) {
    Appointment appointment = buildAppointment("93", patient, location);
    return Appointment.Bundle.builder()
        .link(
            Arrays.asList(
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.first)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + patient
                            + "&location="
                            + location
                            + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.self)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + patient
                            + "&location="
                            + location
                            + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.last)
                    .url(
                        "http://localhost:8060/r4/Appointment?patient="
                            + patient
                            + "&location="
                            + location
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
