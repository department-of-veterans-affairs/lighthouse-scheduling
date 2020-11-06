package gov.va.api.lighthouse.scheduling.tests;

import gov.va.api.health.sentinel.ServiceDefinition;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public final class SystemDefinition {
  @NonNull ServiceDefinition scheduling;
}
