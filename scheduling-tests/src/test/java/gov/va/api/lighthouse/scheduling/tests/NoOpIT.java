package gov.va.api.lighthouse.scheduling.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class NoOpIT {
  @Test
  public void noOperation() {
    log.info("Integration Test Running: HELLO!");
  }
}
