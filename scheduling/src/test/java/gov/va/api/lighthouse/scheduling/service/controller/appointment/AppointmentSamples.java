package gov.va.api.lighthouse.scheduling.service.controller.appointment;

import gov.va.api.lighthouse.datamart.DatamartReference;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentSamples {

  @AllArgsConstructor(staticName = "create")
  public static class Datamart {
    public DatamartAppointment appointment() {
      return DatamartAppointment.builder()
          .cdwId("1600021515962")
          .status("PC")
          .cancelationReason(Optional.of("OTHER"))
          .serviceCategory("SURGERY")
          .serviceType("OTOLARYNGOLOGY/ENT")
          .specialty(null)
          .appointmentType(Optional.of("WALKIN"))
          .description("Walk-In Visit")
          .start(Instant.parse("2020-11-25T08:00:00Z"))
          .end(Instant.parse("2020-11-25T08:20:00Z"))
          .minutesDuration(20)
          .created("2020-11-24")
          .comment(
              Optional.of(
                  "LS 11/20/2020 PID 11/25/2020 5-DAY RTC PER DR STEELE "
                      + "F2F FOR 40-MIN OK TO SCHEDULE @0800 FOR 40-MIN PER DR STEELE "
                      + "TO SEE RESIDENT"))
          .basedOn(null)
          .participant(
              List.of(
                  DatamartAppointment.Participant.builder()
                      .actor(
                          DatamartReference.builder()
                              .type(Optional.of("Location"))
                              .reference(Optional.of("800157972"))
                              .display(Optional.of("SAC ENT RESIDENT 2"))
                              .build())
                      .required("required")
                      .status("accepted")
                      .build(),
                  DatamartAppointment.Participant.builder()
                      .actor(
                          DatamartReference.builder()
                              .type(Optional.of("Patient"))
                              .reference(Optional.of("802095909"))
                              .display(Optional.of("PATIENT,FHIRAPPTT JR"))
                              .build())
                      .required("required")
                      .status("accepted")
                      .build()))
          .build();
    }
  }
}
