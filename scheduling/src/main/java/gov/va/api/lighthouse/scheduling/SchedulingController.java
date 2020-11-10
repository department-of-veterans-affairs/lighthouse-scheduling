package gov.va.api.lighthouse.scheduling;

import gov.va.api.health.r4.api.bundle.AbstractBundle;
import gov.va.api.health.r4.api.bundle.BundleLink;
import gov.va.api.health.r4.api.elements.Reference;
import gov.va.api.health.r4.api.resources.Appointment;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
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

  private final String basepath;

  SchedulingController(@Value("${scheduling.base-url:http://localhost:8060/}") String basepath) {
    this.basepath = basepath;
  }

  @SneakyThrows
  Appointment buildAppointment() {
    String appointmentID = "I2-5XYSWFRZ637QKNR6IIRKYHA5RY000109";
    String patientID = "1092387456V321456";
    String locationID = "I2-9QPSWFRZ530PLNR6IIRKYHA5RY031128";
    return Appointment.builder()
        .resourceType("Appointment")
        .id(appointmentID)
        .status(Appointment.AppointmentStatus.booked)
        .participant(
            List.of(
                Appointment.Participant.builder()
                    .actor(
                        Reference.builder()
                            .reference(basepath + "r4/Patient/" + patientID)
                            .display("JOHN Q VETERAN")
                            .build())
                    .status(Appointment.ParticipationStatus.tentative)
                    .build(),
                Appointment.Participant.builder()
                    .actor(
                        Reference.builder()
                            .reference(basepath + "r4/Location/" + locationID)
                            .display("ORLANDO VAMC")
                            .build())
                    .status(Appointment.ParticipationStatus.accepted)
                    .build()))
        .build();
  }

  /** Read appointment by ID. */
  @GetMapping(value = "/{id}")
  Appointment readAppointmentId(@PathVariable("id") String id) {
    return buildAppointment();
  }

  /** Get appointment by search parameters. */
  @GetMapping
  Appointment.Bundle readAppointmentSearchParameters(
      @RequestParam(value = "_id", required = false) String id,
      @RequestParam(value = "patient", required = false) String patient,
      @RequestParam(value = "identifier", required = false) String identifier) {
    Appointment appointment = buildAppointment();
    var queryString =
        StubbedQueryStringBuilder.builder()
            .id(Optional.ofNullable(id))
            .identifier(Optional.ofNullable(identifier))
            .pat(Optional.ofNullable(patient))
            .build()
            .toStubbedQueryString();
    return Appointment.Bundle.builder()
        .link(
            Arrays.asList(
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.first)
                    .url(basepath + "r4/Appointment?" + queryString + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.self)
                    .url(basepath + "r4/Appointment?" + queryString + "&page=1&count=1")
                    .build(),
                BundleLink.builder()
                    .relation(BundleLink.LinkRelation.last)
                    .url(basepath + "r4/Appointment?" + queryString + "&page=1&count=1")
                    .build()))
        .resourceType("Bundle")
        .type(AbstractBundle.BundleType.searchset)
        .total(1)
        .entry(
            List.of(
                Appointment.Entry.builder()
                    .fullUrl(basepath + "r4/Appointment/I2-5XYSWFRZ637QKNR6IIRKYHA5RY000109")
                    .resource(appointment)
                    .build()))
        .build();
  }

  @Builder
  public static class StubbedQueryStringBuilder {
    private Optional<String> id;
    private Optional<String> pat;
    private Optional<String> identifier;

    /** Builds a query string representation based on the query params provided. */
    public String toStubbedQueryString() {
      StringBuilder queryStringBuilder = new StringBuilder();
      id.ifPresent(s -> queryStringBuilder.append("_id=").append(s).append("&"));
      pat.ifPresent(s -> queryStringBuilder.append("patient=").append(s).append("&"));
      identifier.ifPresent(s -> queryStringBuilder.append("identifier=").append(s).append("&"));
      var queryString = queryStringBuilder.toString();
      if (queryString.length() > 1 && queryString.endsWith("&")) {
        return queryString.substring(0, queryString.length() - 1);
      }
      return queryString;
    }
  }
}
