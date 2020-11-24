package gov.va.api.lighthouse.scheduling.tests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public final class TestIds {
  @NonNull String appointment;
  @NonNull String patient;
  @NonNull String oauthPatient;
}
