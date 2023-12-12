/** */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.rtt.TxRTTPatientsWhoAreTransferedOutCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.TxRttQueries;
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

  private static final String FIND_PATIENTS_WITH_CD4 =
      "TX_RTT/PATIENTS_IIT_PREVIOUS_PERIOD_WITH_CD4.sql";

  private static final String FIND_PATIENTS_NOT_ELIGIBLE_TO_CD4 =
      "TX_RTT/PATIENTS_IIT_PREVIOUS_PERIOD_NOT_ELIGIBLE_TO_CD4.sql";

  final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

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
            mappings));

    composition.addSearch(
        "RTT-TRANFERRED-OUT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Patients who experienced interruption in treatment by end of previous reporting period",
                TxRttQueries.QUERY
                    .findPatientsWhoWhereTransferredOutByEndOfPreviousReportingPeriod),
            mappings));

    composition.addSearch(
        "TX-CURR",
        EptsReportUtils.map(
            this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(),
            "endDate=${endDate},location=${location}"));

    composition.addSearch(
        "TRF-IN",
        EptsReportUtils.map(this.tRFINCohortQueries.getPatiensWhoAreTransferredIn(), mappings));

    composition.setCompositionString(
        "((IIT-PREVIOUS-PERIOD NOT RTT-TRANFERRED-OUT) AND TX-CURR) NOT TRF-IN");

    return composition;
  }

  public CohortDefinition findPatientsWithCD4LessThan200() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("CD4 LESS THAN 200");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_CD4),
            " coorte_final.data_cd4 is not null and  (coorte_final.data_cd4_greater is null or coorte_final.data_cd4_greater >= coorte_final.data_cd4 ) ");

    composition.addSearch(
        "CD4-LESS-200",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("findPatientsWithCD4LessThan200", query), mappings));

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.addSearch(
        "CD4-NOT-ELIGIBLE", EptsReportUtils.map(this.findPatientsNotEligibleToCD4(), mappings));

    composition.setCompositionString("(CD4-LESS-200 AND RTT) NOT CD4-NOT-ELIGIBLE");

    return composition;
  }

  public CohortDefinition findPatientsWIthCD4GreaterOrEqual200() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("CD4 GREATER OR EQUAL 200");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_CD4),
            " coorte_final.data_cd4_greater is not null and ( coorte_final.data_cd4 is null or coorte_final.data_cd4_greater < coorte_final.data_cd4) ");

    composition.addSearch(
        "CD4-GREATER-OR-EQUAL-200",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("findPatientsWIthCD4GreaterOrEqual200", query),
            mappings));

    composition.addSearch(
        "CD4-NOT-ELIGIBLE", EptsReportUtils.map(this.findPatientsNotEligibleToCD4(), mappings));
    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.setCompositionString("(CD4-GREATER-OR-EQUAL-200 AND RTT) NOT CD4-NOT-ELIGIBLE");

    return composition;
  }

  public CohortDefinition findPatientsWithUnknownCD4() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("Unkwown CD4");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

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

  public CohortDefinition findPatientsNotEligibleToCD4() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("CD4 GREATER OR EQUAL 200");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    composition.addSearch(
        "CD4-NOT-ELIGIBLE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findPatientsNotEligibleToCD4",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_NOT_ELIGIBLE_TO_CD4)),
            mappings));

    composition.setCompositionString("RTT AND CD4-NOT-ELIGIBLE");

    return composition;
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
    return getDurationofIITInterval(
        "Patients who experienced treatment interruption of  <3 months before returning to treatment",
        "  where iit_art_interval < 90 ");
  }

  @DocumentedDefinition(value = "DurationInterruptionOfTreatmentBetween3And5Months")
  public CohortDefinition getDurationInterruptionOfTreatmentBetween3And5Months() {
    return getDurationofIITInterval(
        "Patients who experienced treatment interruption of 3-5 months before returning to treatmentt",
        "  where iit_art_interval >= 90 and iit_art_interval < 180 ");
  }

  @DocumentedDefinition(value = "DurationInterruptionOfTreatmentGreaterOrEqual6Months")
  public CohortDefinition getDurationInterruptionOfTreatmentGreaterOrEqual6Months() {
    return getDurationofIITInterval(
        "Patients who experienced treatment interruption of 6 or more months before returning to treatment",
        "  where iit_art_interval >= 180 ");
  }

  @DocumentedDefinition(value = "TxRttPLHIVLess12MonthCalculation")
  public CohortDefinition getPLHIVLess12MonthCalculation() {
    return getDurationofIITInterval(
        "Patients who experienced treatment interruption of  <12 months before returning to treatment",
        "  where iit_art_interval < 365 ");
  }

  @DocumentedDefinition(value = "TxRttPLHIVGreater12MonthCalculation")
  public CohortDefinition getPLHIVGreather12MonthCalculation() {
    return getDurationofIITInterval(
        "Patients who experienced treatment interruption of  12 or more months before returning to treatment",
        "  where iit_art_interval >= 365 ");
  }

  @DocumentedDefinition(value = "TxRttPLHIVUnknownDesaggregation")
  public CohortDefinition getPLHIVUnknownDesaggregation() {

    return getDurationofIITInterval(
        "Patients who experienced - Unknown Duration",
        " where data_iit is null and  data_restart is not null ");
  }

  @DocumentedDefinition(value = "TxRttPLHIVTotal")
  public CohortDefinition getPLHIVTotal() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();

    compositionDefinition.setName("Tx RTT- Total PLHIV");
    compositionDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compositionDefinition.addSearch(
        "RTT-GREATER12MONTHS",
        EptsReportUtils.map(this.getPLHIVGreather12MonthCalculation(), mappings));

    compositionDefinition.addSearch(
        "RTT-LESS12MONTHS", EptsReportUtils.map(this.getPLHIVLess12MonthCalculation(), mappings));

    compositionDefinition.addSearch(
        "RTT-PLHIVUNKNOWN", EptsReportUtils.map(this.getPLHIVUnknownDesaggregation(), mappings));

    compositionDefinition.setCompositionString(
        "RTT-LESS12MONTHS OR RTT-GREATER12MONTHS OR RTT-PLHIVUNKNOWN");

    return compositionDefinition;
  }

  private CohortDefinition getDurationofIITInterval(String intervalLabel, String interval) {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("IIT -" + intervalLabel);
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    composition.addSearch("RTT", EptsReportUtils.map(this.getPatientsOnRTT(), mappings));

    String query = EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_IIT_PREVIOUS_PERIOD) + interval;

    composition.addSearch(
        "IIT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Patients who experienced treatment interruption of  <3 months before returning to treatment",
                query),
            mappings));

    composition.setCompositionString("RTT AND IIT");

    return composition;
  }
}
