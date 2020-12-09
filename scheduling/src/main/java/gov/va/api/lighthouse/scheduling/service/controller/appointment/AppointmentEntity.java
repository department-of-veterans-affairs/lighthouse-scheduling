package gov.va.api.lighthouse.scheduling.service.controller.appointment;

import gov.va.api.health.dataquery.service.controller.datamart.DatamartEntity;
import java.time.Instant;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * Datamart Appointment representing the following table.
 *
 * <pre>
 * CREATE TABLE [App].[Appointment](
 *   [CDWId] [varchar](26) NOT NULL,
 *   [PatientFullICN] [varchar](50) NOT NULL,
 *   [DateUTC] [smalldatetime] NULL,
 *   [LastUpdated] [smalldatetime] DEFAULT getutcdate(),
 *   [Appointment]  varchar(max)
 * </pre>
 */
@Data
@Entity
@Builder
@Table(name = "Appointment", schema = "app")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentEntity implements DatamartEntity {
  @Id
  @Column(name = "CDWId")
  @EqualsAndHashCode.Include
  private String cdwId;

  @Column(name = "PatientFullICN")
  private String icn;

  @Column(name = "DateUTC")
  private Instant dateUtc;

  @Column(name = "LastUpdated")
  private Instant lastUpdated;

  @Lob
  @Basic(fetch = FetchType.EAGER)
  @Column(name = "Appointment")
  private String payload;

  static Sort naturalOrder() {
    return Sort.by("cdwId").ascending();
  }

  DatamartAppointment asDatamartAppointment() {
    return deserializeDatamart(payload, DatamartAppointment.class);
  }
}
