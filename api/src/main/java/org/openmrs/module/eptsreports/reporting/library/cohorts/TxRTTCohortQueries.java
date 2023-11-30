/** */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTDurationOfTreatmentInterruptionBetween3And5MonthsCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTDurationOfTreatmentInterruptionGreaterOrEqual6MonthsCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTDurationOfTreatmentInterruptionLess3MonthsCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTPLHIVGreater12MonthCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTPLHIVLess12MonthCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTPatientsWhoAreTransferedOutCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTPatientsWhoExperiencedIITCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author Stélio Moiane */
@Component
public class TxRTTCohortQueries {

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  @Autowired private TRFINCohortQueries tRFINCohortQueries;

  @Autowired private GenericCohortQueries genericCohorts;

  private static final String FIND_PATIENTS_WHO_ARE_IIT_PREVIOUS_PERIOD =
      "TX_RTT/PATIENTS_WHO_ARE_IIT_PREVIOUS_PERIOD.sql";

  private static final String FIND_PATIENTS_WITH_CD4_LESS_THAN_200 =
      "TX_RTT/PATIENTS_IIT_PREVIOUS_PERIOD_WITH_CD4_LESS_THAN_200.sql";

  private static final String FIND_PATIENTS_WITH_CD4_GREATER_OR_EQUAL_200 =
      "TX_RTT/PATIENTS_IIT_PREVIOUS_PERIOD_WITH_CD4_GREATER_THAN_200.sql";

  private static final String FIND_PATIENTS_NOT_ELIGIBLE_TO_CD4 =
      "TX_RTT/PATIENTS_IIT_PREVIOUS_PERIOD_NOT_ELIGIBLE_TO_CD4.sql";

  @DocumentedDefinition(value = "TxRttPatientsOnRTT")
  public CohortDefinition getPatientsOnRTT() {

    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("Tx RTT - Patients on RTT");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    composition.addSearch(
        "IIT-PREVIOUS-PERIOD",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Patients who experienced interruption in treatment by end of previous reporting period",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_IIT_PREVIOUS_PERIOD)),
            "startDate=${startDate},endDate=${endDate},location=${location}"));

    composition.addSearch(
        "RTT-TRANFERRED-OUT",
        EptsReportUtils.map(
            this.getPatientsWhoWhereTransferredOutCalculation(),
            "endDate=${startDate},location=${location}"));

    composition.addSearch(
        "TX-CURR",
        EptsReportUtils.map(
            this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(),
            "endDate=${endDate},location=${location}"));

    composition.addSearch(
        "TRF-IN",
        EptsReportUtils.map(
            this.tRFINCohortQueries.getPatiensWhoAreTransferredIn(),
            "startDate=${startDate},endDate=${endDate},location=${location}"));

    composition.setCompositionString(
        "((IIT-PREVIOUS-PERIOD NOT RTT-TRANFERRED-OUT) AND TX-CURR) NOT TRF-IN");

    return composition;
  }

  public CohortDefinition findPatientsWithCD4LessThan200() {
    final CompositionCohortDefinition txNewCompositionCohort = new CompositionCohortDefinition();

    txNewCompositionCohort.setName("CD4 LESS THAN 200");
    txNewCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    txNewCompositionCohort.addSearch(
        "CD4-LESS-200",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsWithCD4LessThan200",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_CD4_LESS_THAN_200)),
            mappings));

    txNewCompositionCohort.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    txNewCompositionCohort.setCompositionString("CD4-LESS-200 AND RTT");

    return txNewCompositionCohort;
  }

  public CohortDefinition findPatientsWIthCD4GreaterOrEqual200() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("CD4 GREATER OR EQUAL 200");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "CD4-GREATER-OR-EQUAL-200",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsWithCD4LessThan200",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_CD4_GREATER_OR_EQUAL_200)),
            mappings));

    composition.addSearch(
        "CD4-LESS-200", EptsReportUtils.map(this.findPatientsWithCD4LessThan200(), mappings));

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.setCompositionString("(CD4-GREATER-OR-EQUAL-200 AND RTT) NOT CD4-LESS-200");

    return composition;
  }

  public CohortDefinition findPatientsNotEligibleToCD4() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("CD4 GREATER OR EQUAL 200");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "CD4-NOT-ELIGIBLE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsNotEligibleToCD4",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_NOT_ELIGIBLE_TO_CD4)),
            mappings));

    composition.addSearch(
        "CD4-LESS-200", EptsReportUtils.map(this.findPatientsWithCD4LessThan200(), mappings));
    composition.addSearch(
        "CD4-GREATER-OR-EQUAL-200",
        EptsReportUtils.map(this.findPatientsWIthCD4GreaterOrEqual200(), mappings));

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.setCompositionString(
        "(RTT AND CD4-NOT-ELIGIBLE) NOT (CD4-GREATER-OR-EQUAL-200 AND CD4-LESS-200)");

    return composition;
  }

  public CohortDefinition findPatientsWithUnknownCD4() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("Unkwown CD4");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.addSearch(
        "CD4-LESS-200", EptsReportUtils.map(this.findPatientsWithCD4LessThan200(), mappings));

    composition.addSearch(
        "CD4-GREATER-OR-EQUAL-200",
        EptsReportUtils.map(this.findPatientsWIthCD4GreaterOrEqual200(), mappings));

    composition.addSearch(
        "CD4-NOT-ELIGIBLE", EptsReportUtils.map(this.findPatientsNotEligibleToCD4(), mappings));

    composition.setCompositionString(
        "RTT NOT (CD4-LESS-200 OR CD4-GREATER-OR-EQUAL-200 OR CD4-NOT-ELIGIBLE)");

    return composition;
  }

  @DocumentedDefinition(value = "TxRttPatientsWhoExperiencedIITCalculation")
  public CohortDefinition getPatientsWhoExperiencedIITCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "txRTTPatientsWhoExperiencedIITCalculation",
            Context.getRegisteredComponents(TxRTTPatientsWhoExperiencedIITCalculation.class)
                .get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "TxRttPatientsWhoWhereTransferredOutCalculation")
  public CohortDefinition getPatientsWhoWhereTransferredOutCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "txRTTPatientsWhoWhereTransferredOutCalculation",
            Context.getRegisteredComponents(TxRTTPatientsWhoAreTransferedOutCalculation.class)
                .get(0));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    return definition;
  }

  @DocumentedDefinition(value = "DurationInterruptionOfTreatmentLessThan3Months")
  public CohortDefinition getDurationInterruptionOfTreatmentLessThan3Months() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "DurationInterruptionOfTreatmentLessThan3Months",
            Context.getRegisteredComponents(
                    TxRTTDurationOfTreatmentInterruptionLess3MonthsCalculation.class)
                .get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "DurationInterruptionOfTreatmentBetween3And5Months")
  public CohortDefinition getDurationInterruptionOfTreatmentBetween3And5Months() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "DurationInterruptionOfTreatmentBetween3And5Months",
            Context.getRegisteredComponents(
                    TxRTTDurationOfTreatmentInterruptionBetween3And5MonthsCalculation.class)
                .get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "DurationInterruptionOfTreatmentGreaterOrEqual6Months")
  public CohortDefinition getDurationInterruptionOfTreatmentGreaterOrEqual6Months() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "DurationInterruptionOfTreatmentGreaterOrEqual6Months",
            Context.getRegisteredComponents(
                    TxRTTDurationOfTreatmentInterruptionGreaterOrEqual6MonthsCalculation.class)
                .get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "TxRttPLHIVLess12MonthCalculation")
  public CohortDefinition getPLHIVLess12MonthCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "txRttPLHIVLess12MonthCalculation",
            Context.getRegisteredComponents(TxRTTPLHIVLess12MonthCalculation.class).get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "TxRttPLHIVGreater12MonthCalculation")
  public CohortDefinition getPLHIVGreather12MonthCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "txRttPLHIVGreater12MonthCalculation",
            Context.getRegisteredComponents(TxRTTPLHIVGreater12MonthCalculation.class).get(0));
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "end Date", Date.class));
    definition.addParameter(new Parameter("realEndDate", "Real End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }

  @DocumentedDefinition(value = "TxRttPLHIVUnknownDesaggregation")
  public CohortDefinition getPLHIVUnknownDesaggregation() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();

    compositionDefinition.setName("Tx RTT- Unknown Desaggretation");
    compositionDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings =
        "startDate=${startDate},endDate=${endDate},realEndDate=${endDate},location=${location}";

    compositionDefinition.addSearch(
        "RTT-NUMERATOR",
        EptsReportUtils.map(
            this.getPatientsOnRTT(),
            "startDate=${startDate},endDate=${endDate},location=${location}"));

    compositionDefinition.addSearch(
        "RTT-GREATER12MONTHS",
        EptsReportUtils.map(this.getPLHIVGreather12MonthCalculation(), mappings));

    compositionDefinition.addSearch(
        "RTT-LESS12MONTHS", EptsReportUtils.map(this.getPLHIVLess12MonthCalculation(), mappings));

    compositionDefinition.setCompositionString(
        "RTT-NUMERATOR NOT (RTT-GREATER12MONTHS OR RTT-LESS12MONTHS)");

    return compositionDefinition;
  }

  @DocumentedDefinition(value = "TxRttPLHIVTotal")
  public CohortDefinition getPLHIVTotal() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();

    compositionDefinition.setName("Tx RTT- Total PLHIV");
    compositionDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings =
        "startDate=${startDate},endDate=${endDate},realEndDate=${endDate},location=${location}";

    compositionDefinition.addSearch(
        "RTT-GREATER12MONTHS",
        EptsReportUtils.map(this.getPLHIVGreather12MonthCalculation(), mappings));

    compositionDefinition.addSearch(
        "RTT-LESS12MONTHS", EptsReportUtils.map(this.getPLHIVLess12MonthCalculation(), mappings));

    compositionDefinition.addSearch(
        "RTT-PLHIVUNKNOWN",
        EptsReportUtils.map(
            this.getPLHIVUnknownDesaggregation(),
            "startDate=${startDate},endDate=${endDate},location=${location}"));

    compositionDefinition.setCompositionString(
        "RTT-LESS12MONTHS OR RTT-GREATER12MONTHS OR RTT-PLHIVUNKNOWN");

    return compositionDefinition;
  }
}
