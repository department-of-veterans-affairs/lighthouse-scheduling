package gov.va.api.lighthouse.scheduling.service.controller.appointment;

import gov.va.api.lighthouse.datamart.DatamartCoding;
import gov.va.api.lighthouse.datamart.DatamartReference;
import gov.va.api.lighthouse.datamart.HasReplaceableId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatamartAppointment implements HasReplaceableId {
  @Builder.Default private String objectType = "Appointment";

  @Builder.Default private String objectVersion = "1";

  private String cdwId;

  private String status;

  private Optional<String> cancelationReason;

  private String serviceCategory;

  private String serviceType;

  private Optional<DatamartCoding> specialty;

  private Optional<String> appointmentType;

  private String description;

  private Instant start;

  private Instant end;

  private Integer minutesDuration;

  private String created;

  private Optional<String> comment;

  private Optional<DatamartReference> basedOn;

  private List<Participant> participant;

  public Optional<String> appointmentType() {
    return lazyGetter(appointmentType);
  }

  public Optional<DatamartReference> basedOn() {
    return lazyGetter(basedOn);
  }

  public Optional<String> cancelationReason() {
    return lazyGetter(cancelationReason);
  }

  public Optional<String> comment() {
    return lazyGetter(comment);
  }

  private <T> Optional<T> lazyGetter(Optional<T> value) {
    if (value == null) {
      return Optional.empty();
    }
    return value;
  }

  public Optional<DatamartCoding> specialty() {
    return lazyGetter(specialty);
  }

  @Data
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Participant {
    private DatamartReference actor;

    private String required;

    private String status;
  }
}
