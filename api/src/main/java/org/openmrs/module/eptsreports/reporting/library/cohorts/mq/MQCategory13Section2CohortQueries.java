package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory13Section2QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13Section2CohortQueries {

  @Autowired private MQCategory13Section1CohortQueries mQCategory13Section1CohortQueries;

  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreInSecondLineDenominatorB2")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreInSecondLineDenominatorB2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section2QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreInSecondLineDenominatorB2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithLastClinicalConsultationwhoAreNotInSecondLineDenominatorB2E")
  public CohortDefinition
      findPatientsWithLastClinicalConsultationwhoAreNotInSecondLineDenominatorB2E() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory13Section2QueriesInterface.QUERY
            .findPatientsWithLastClinicalConsultationwhoAreNotInSecondLineDenominatorB2E;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorCategory13SectionIIB")
  public CohortDefinition findDenominatorCategory13SectionIIB(boolean excludeTbActiveDiagnostic) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";
    String compositionString = "";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationDenominatorB1(),
            mappings));

    definition.addSearch(
        "B2ENEW",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW(),
            mappings));

    definition.addSearch(
        "B2NEWII",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII(),
            mappings));

    definition.addSearch(
        "B5E",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWithRequestCVDenominatorB5E(), mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWhoArePregnantCAT13Part1(), mappings));

    definition.addSearch(
        "DROPPEDOUT",
        EptsReportUtils.map(
            mQCohortQueries
                .findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    if (excludeTbActiveDiagnostic) {
      compositionString =
          "(B1 AND (B2NEWII NOT (B2ENEW OR DROPPEDOUT)))  NOT (B5E OR C OR TB-ACTIVA)";
    } else {
      compositionString = "(B1 AND (B2NEWII NOT (B2ENEW OR DROPPEDOUT)))  NOT (B5E OR C)";
    }

    definition.setCompositionString(compositionString);

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorCategory13SectionIIBChildrens")
  public CohortDefinition findDenominatorCategory13SectionIIBChildrens() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationDenominatorB1(),
            mappings));

    definition.addSearch(
        "B2ENEW",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW(),
            mappings));

    definition.addSearch(
        "B2NEWII",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII(),
            mappings));

    definition.addSearch(
        "B5E",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWithRequestCVDenominatorB5E(), mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWhoArePregnantCAT13Part1(), mappings));

    definition.addSearch(
        "D",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWhoAreBreastfeedingCAT13Part1(),
            mappings));

    definition.addSearch(
        "DROPPEDOUT",
        EptsReportUtils.map(
            mQCohortQueries
                .findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation(),
            mappings));

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    definition.setCompositionString(
        "(B1 AND (B2NEWII NOT (B2ENEW OR DROPPEDOUT)))  NOT (B5E OR C OR D OR TB-ACTIVA)");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionIIC")
  public CohortDefinition findFinalNumeratorCategory13SectionIIC(
      boolean excludeTbActiveDiagnostic) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findFinalNumeratorCategory13SectionIIC");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B",
        EptsReportUtils.map(
            this.findDenominatorCategory13SectionIIB(excludeTbActiveDiagnostic), mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findNumeratorCategory13Section1C(), mappings));

    definition.setCompositionString("B AND C");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionIICChildrens")
  public CohortDefinition findFinalNumeratorCategory13SectionIICChildrens() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findFinalNumeratorCategory13SectionIIC");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "B", EptsReportUtils.map(this.findDenominatorCategory13SectionIIBChildrens(), mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findNumeratorCategory13Section1C(), mappings));

    definition.setCompositionString("B AND C");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorTBAndHIVIndicator13_4")
  public CohortDefinition findDenominatorTBAndHIVIndicator13_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "RF14",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findDenominatorCategory13SectionIB(false), mappings));

    definition.addSearch(
        "RF15", EptsReportUtils.map(this.findDenominatorCategory13SectionIIB(false), mappings));

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    definition.setCompositionString("(RF14 OR RF15) AND TB-ACTIVA");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorTBAndHIVIndicator13_4")
  public CohortDefinition findNumeratorTBAndHIVIndicator13_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorTBAndHIVIndicator13_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-4-RF53",
        EptsReportUtils.map(this.findDenominatorTBAndHIVIndicator13_4(), mappings));

    definition.addSearch(
        "PEDIDO-CV",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.setCompositionString("DENOMINATOR-13-4-RF53 AND PEDIDO-CV");

    return definition;
  }

  @DocumentedDefinition(value = "findDenominatorTBAndHIVIndicator13_13")
  public CohortDefinition findDenominatorTBAndHIVIndicator13_13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorTBAndHIVIndicator13_13");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsTbActivaEndRevisionDate =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "RF14",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findDenominatorCategory13SectionIB(false), mappings));

    definition.addSearch(
        "RF15", EptsReportUtils.map(this.findDenominatorCategory13SectionIIB(false), mappings));

    definition.addSearch(
        "TB-ACTIVA",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod(),
            mappingsTbActivaEndRevisionDate));

    definition.addSearch(
        "BREASTFEEDING",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries.findPatientsWhoAreBreastfeedingCAT13Part1(),
            mappings));

    definition.setCompositionString("((RF14 OR RF15) AND TB-ACTIVA) NOT BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(value = "findNumeratorTBAndHIVIndicator13_13")
  public CohortDefinition findNumeratorTBAndHIVIndicator13_13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findNumeratorTBAndHIVIndicator13_13");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-13-13-RF54",
        EptsReportUtils.map(this.findDenominatorTBAndHIVIndicator13_13(), mappings));

    definition.addSearch(
        "PEDIDO-CV",
        EptsReportUtils.map(
            mQCategory13Section1CohortQueries
                .findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.setCompositionString("DENOMINATOR-13-13-RF54 AND PEDIDO-CV");

    return definition;
  }
}
