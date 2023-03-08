package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory9QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory9CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;
  @Autowired private MQGenericCohortQueries mQGenericCohortQueries;

  @DocumentedDefinition(value = "findPatientsWhoArePregnantDuringInclusionPeriod")
  public CohortDefinition findPatientsWhoArePregnantDuringInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY.findPatientsWhoArePregnantDuringInclusionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingDuringInclusionPeriod")
  public CohortDefinition findPatientsWhoAreBreastfeedingDuringInclusionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingDuringInclusionPeriod");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory9QueriesInterface.QUERY.findPatientsWhoAreBreastfeedingDuringInclusionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAdultInARTWhoReceivedCD4ResultIn33DaysAfterFirstClinicalConsultaionCategory9_91_Numerator")
  public CohortDefinition
      findPatientsInARTWhoReceivedCD4ResultIn33DaysAfterFirstClinicalConsultaionCategory9_91_Numerator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findAdultInARTWhoReceivedCD4ResultIn33DaysAfterFirstClinicalConsultaionCategory9_91_Numerator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "START-ART-DENOMINATOR",
        EptsReportUtils.map(
            this.mQGenericCohortQueries
                .findPatientOnARTdExcludingPregantAndBreastfeedingAndTransferredInTransferredOut(),
            mappings));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation(),
            mappings));

    definition.setCompositionString("START-ART-DENOMINATOR AND CD4");

    return definition;
  }
}
