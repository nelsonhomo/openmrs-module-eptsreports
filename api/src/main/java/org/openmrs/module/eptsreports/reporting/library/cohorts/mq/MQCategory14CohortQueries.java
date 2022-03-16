package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory14QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.WomanState;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class MQCategory14CohortQueries {

  @DocumentedDefinition(value = "getDenominatorCategory14Indicator")
  public CohortDefinition getDenominatorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - Denominator Category14_Indicator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "DENOMINATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("DENOMINATOR-TXPVLS");

    return definition;
  }

  @DocumentedDefinition(value = "getPregnantDenominatorCategory14Indicator")
  public CohortDefinition getPregnantDenominatorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ Cat 14 - Patients with Viral Supression And Pregnant - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "DENOMINATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "A1-PREGNANT",
        EptsReportUtils.map(
            findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("DENOMINATOR-TXPVLS AND A1-PREGNANT");

    return definition;
  }

  @DocumentedDefinition(value = "getBreastfeedingDenominatorCategory14Indicator")
  public CohortDefinition getBreastfeedingDenominatorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ Cat 14 - Patients with Viral Supression And BreastFeeding - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "DENOMINATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "A2-BREASTFEEDING",
        EptsReportUtils.map(
            findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("DENOMINATOR-TXPVLS AND A2-BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(value = "getPatientsInDSDWithViralSupressionDenominatorCategory14Indicator")
  public CohortDefinition getPatientsInDSDWithViralSupressionDenominatorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ Cat 14 - Get Patients With Viral Supression And At Least One DSD - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "DENOMINATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DSD",
        EptsReportUtils.map(
            findPatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("DENOMINATOR-TXPVLS AND DSD");

    return definition;
  }

  @DocumentedDefinition(value = "getNumeratorCategory14Indicator")
  public CohortDefinition getNumeratorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - Numerator Category14_Indicator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "NUMERATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("NUMERATOR-TXPVLS");

    return definition;
  }

  @DocumentedDefinition(value = "getPregnantNumeratorCategory14Indicator")
  public CohortDefinition getPregnantNumeratorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ Cat 14 - Patients with Viral Supression And Pregnant - Numerator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "NUMERATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "B1-PREGNANT",
        EptsReportUtils.map(
            findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("NUMERATOR-TXPVLS AND B1-PREGNANT");

    return definition;
  }

  @DocumentedDefinition(value = "getBreastfeedingNumeratorCategory14Indicator")
  public CohortDefinition getBreastfeedingNumeratorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ Cat 14 - Patients with Viral Supression And BreastFeeding - Numerator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "NUMERATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "B2-BREASTFEEDING",
        EptsReportUtils.map(
            findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("NUMERATOR-TXPVLS AND B2-BREASTFEEDING");

    return definition;
  }

  @DocumentedDefinition(value = "getPatientsInDSDWithViralSupressionNumeratorCategory14Indicator")
  public CohortDefinition getPatientsInDSDWithViralSupressionNumeratorCategory14Indicator() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ Cat 14 - Get Patients With Viral Supression And At Least One DSD - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "NUMERATOR-TXPVLS",
        EptsReportUtils.map(
            findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DSD",
        EptsReportUtils.map(
            findPatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString("NUMERATOR-TXPVLS AND DSD");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  private CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("endDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory14QueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months")
  private CohortDefinition
      findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months");
    definition.addParameter(new Parameter("endDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory14QueriesInterface.QUERY
            .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value = "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  public CohortDefinition
      findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "PregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        MQCategory14QueriesInterface.QUERY
            .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                WomanState.PREGNANT));

    return definition;
  }

  @DocumentedDefinition(
      value =
          "BreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months")
  public CohortDefinition
      findBreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "BreastfeedingWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        MQCategory14QueriesInterface.QUERY
            .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                WomanState.BREASTFEEDING));

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression")
  private CohortDefinition findPatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - PatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression");
    definition.addParameter(new Parameter("endDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory14QueriesInterface.QUERY
            .findPatientsWhoAreActiveOnArtAndInAtleastOneDSDWithViralSupression;
    definition.setQuery(query);
    return definition;
  }
}
