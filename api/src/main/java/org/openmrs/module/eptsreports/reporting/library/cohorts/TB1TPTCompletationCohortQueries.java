package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TPTCompletationQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TxTbPrevQueriesInterface.QUERY.DisaggregationTypes;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB1TPTCompletationCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  @Autowired private TXTBCohortQueries tXTBCohortQueries;

  @Autowired private TxTbPrevCohortQueries txTbPrevCohortQueries;

  private static final String TPT_CASCADE_INICIO_INH = "TPTCOMPLETION/TPT_CASCADE_INICIO_INH.sql";

  private static final String TPT_CASCADE_INICIO_3HP = "TPTCOMPLETION/TPT_CASCADE_INICIO_3HP.sql";

  private static final String FIND_PATIENTS_WHO_COMPLETED_INH_THERAPY_BY_END_OF_REPORTING_PERIOD =
      "TPTCOMPLETION/PATIENTS_WHO_COMPLETED_INH_THERAPY_BY_END_OF_REPORTING_PERIOD.sql";

  private static final String FIND_PATIENTS_WHO_COMPLETED_3HP_THERAPY_BY_END_OF_REPORTING_PERIOD =
      "TPTCOMPLETION/PATIENTS_WHO_COMPLETED_3HP_THERAPY_BY_END_OF_REPORTING_PERIOD.sql";

  private static final String
      FIND_PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD =
          "TPTCOMPLETION/PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD.sql";

  private static final String
      FIND_PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD =
          "TPTCOMPLETION/PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD.sql";

  @DocumentedDefinition(value = "findTxCurrWithTPTCompletation")
  public CohortDefinition findTxCurrWithTPTCompletation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get TxCurr With TPT Completation");
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TXCURR",
        EptsReportUtils.map(this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(), mappings));
    dsd.addSearch(
        "STARTED-INH",
        EptsReportUtils.map(
            this.findPatientsWhoStartedINHTherapyBeforeReportingEndDate(), mappings));
    dsd.addSearch(
        "COMPLETED-INH", EptsReportUtils.map(this.findPatientsWhoCompletedINHTherapy(), mappings));
    dsd.addSearch(
        "STARTED-3HP",
        EptsReportUtils.map(this.findPatientsWhoStarted3HPTherapyBeforeReportEndDate(), mappings));
    dsd.addSearch(
        "COMPLETED-3HP",
        EptsReportUtils.map(
            this.findPatientsWhoCompleted3HPTherapyBeforeReportEndDate(), mappings));

    dsd.setCompositionString(
        "TXCURR AND ((STARTED-INH AND COMPLETED-INH) OR (STARTED-3HP AND COMPLETED-3HP))");

    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletation")
  public CohortDefinition findTxCurrWithoutTPTCompletation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get TxCurr Without TPT Completation");
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TXCURR",
        EptsReportUtils.map(this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(), mappings));
    dsd.addSearch(
        "TPT-COMPLETATION", EptsReportUtils.map(this.findTxCurrWithTPTCompletation(), mappings));

    dsd.setCompositionString("TXCURR NOT TPT-COMPLETATION");

    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletionWhoWereTreatedForTBForLast3Years")
  public CohortDefinition findTxCurrWithoutTPTCompletionWhoWereTreatedForTBForLast3Years() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();
    dsd.setName("get TxCurr Without TPT Completion But Who were treated for TB In last 3 Years");

    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";
    String generalParameterMapping =
        "startDate=${endDate-1095d},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TPT-NO-COMPLETION",
        EptsReportUtils.map(this.findTxCurrWithoutTPTCompletation(), mappings));

    dsd.addSearch(
        "TB-NUMERATOR",
        EptsReportUtils.map(tXTBCohortQueries.txTbNumerator(), generalParameterMapping));

    dsd.setCompositionString("TPT-NO-COMPLETION AND TB-NUMERATOR");

    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletionWithPositivTBScreening")
  public CohortDefinition findTxCurrWithoutTPTCompletionWithPositivTBScreening() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();
    dsd.setName("get TxCurr Without TPT Completion With Positive TB Screening");

    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TPT-NO-COMPLETION",
        EptsReportUtils.map(this.findTxCurrWithoutTPTCompletation(), mappings));
    dsd.addSearch(
        "TB-POSITIVE-SCREENING",
        EptsReportUtils.map(this.getTxTBDenominatorAndPositiveScreening(), mappings));

    dsd.setCompositionString("TPT-NO-COMPLETION AND TB-POSITIVE-SCREENING");

    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletionButEligibleForTPTCompletation")
  public CohortDefinition findTxCurrWithoutTPTCompletionButEligibleForTPTCompletation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();
    dsd.setName("get TxCurr Without TPT Completion That Are Eligible for TPT Completation");

    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TPT-NO-COMPLETION",
        EptsReportUtils.map(this.findTxCurrWithoutTPTCompletation(), mappings));

    dsd.addSearch(
        "TPT-NO-COMPLETION-WITH-TB",
        EptsReportUtils.map(
            this.findTxCurrWithoutTPTCompletionWhoWereTreatedForTBForLast3Years(), mappings));

    dsd.addSearch(
        "TPT-NO-COMPLETION-WITH-TB-POSITIVE-SCREENING",
        EptsReportUtils.map(this.findTxCurrWithoutTPTCompletionWithPositivTBScreening(), mappings));

    dsd.setCompositionString(
        "TPT-NO-COMPLETION NOT (TPT-NO-COMPLETION-WITH-TB OR TPT-NO-COMPLETION-WITH-TB-POSITIVE-SCREENING)");
    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletionWhoInitiatedTPTInLast7Months")
  public CohortDefinition findTxCurrWithoutTPTCompletionWhoInitiatedTPTInLast7Months() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();
    dsd.setName("get TxCurr Without TPT Completion Who Initated TPT in the laste 7 Months");

    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TPT-NO-COMPLETION",
        EptsReportUtils.map(this.findTxCurrWithoutTPTCompletation(), mappings));
    dsd.addSearch(
        "TBPREV-DENOMINATOR", EptsReportUtils.map(this.getTbPrevTotalDenominator(), mappings));

    dsd.setCompositionString("TPT-NO-COMPLETION AND TBPREV-DENOMINATOR");

    return dsd;
  }

  @DocumentedDefinition(value = "findTxCurrWithoutTPTCompletionButEligibleForTPTInitiation")
  public CohortDefinition findTxCurrWithoutTPTCompletionButEligibleForTPTInitiation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();
    dsd.setName("get TxCurr Without TPT Completion That Are Eligible for TPT Initiation");

    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "TXCURR-ELIGIBLE-TPT-COMPLETION",
        EptsReportUtils.map(
            this.findTxCurrWithoutTPTCompletionButEligibleForTPTCompletation(), mappings));

    dsd.addSearch(
        "TXCURR-TPT-INITIATION",
        EptsReportUtils.map(
            this.findTxCurrWithoutTPTCompletionWhoInitiatedTPTInLast7Months(), mappings));

    dsd.setCompositionString("TXCURR-ELIGIBLE-TPT-COMPLETION NOT TXCURR-TPT-INITIATION");
    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsWhoStartedINHTherapyBeforeReportingEndDate")
  private CohortDefinition findPatientsWhoStartedINHTherapyBeforeReportingEndDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("get Patients Who have started INH Therapy Before Reporting endDate");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(EptsQuerysUtils.loadQuery(TPT_CASCADE_INICIO_INH));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoStarted3HPTherapyBeforeReportEndDate")
  private CohortDefinition findPatientsWhoStarted3HPTherapyBeforeReportEndDate() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get Patients Who have Started 3HP Therapy before Report End Date");
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "STARTED-3HP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Patients Who have Started 3HP Therapy before Report End Date",
                EptsQuerysUtils.loadQuery(TPT_CASCADE_INICIO_3HP)),
            mappings));

    dsd.addSearch(
        "COMPLETED-INH", EptsReportUtils.map(this.findPatientsWhoCompletedINHTherapy(), mappings));
    dsd.setCompositionString("STARTED-3HP NOT COMPLETED-INH");
    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsWhoCompleted3HPTherapyBeforeReportEndDate")
  private CohortDefinition findPatientsWhoCompleted3HPTherapyBeforeReportEndDate() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get Patients Who have Started 3HP Therapy before Report End Date");
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "endDate=${endDate},location=${location}";

    dsd.addSearch(
        "COMPLETED-3HP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "get Patients Who Completed 3HP Therapy",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_COMPLETED_3HP_THERAPY_BY_END_OF_REPORTING_PERIOD)),
            mappings));
    dsd.addSearch(
        "COMPLETED-INH", EptsReportUtils.map(this.findPatientsWhoCompletedINHTherapy(), mappings));
    dsd.setCompositionString("COMPLETED-3HP NOT COMPLETED-INH");
    return dsd;
  }

  @DocumentedDefinition(value = "getTxTBDenominatorAndPositiveScreening")
  private CohortDefinition getTxTBDenominatorAndPositiveScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    String generalParameterMapping =
        "startDate=${endDate-14d},endDate=${endDate},location=${location}";

    definition.setName("TxTB - Denominator and Positive Sreening");
    definition.addSearch(
        "denominator-positive-results",
        EptsReportUtils.map(
            tXTBCohortQueries.getDenominatorAndPositiveScreening(generalParameterMapping),
            generalParameterMapping));

    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator-positive-results");
    return definition;
  }

  @DocumentedDefinition(value = "getTbPrevTotalDenominator")
  public CohortDefinition getTbPrevTotalDenominator() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("Patients Who Started TPT");
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${endDate-7m},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "STARTED-TPT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Who have Started TPT During Previous Reporting Period",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD)),
            mappings));
    dsd.addSearch(
        "TRF-OUT",
        EptsReportUtils.map(txTbPrevCohortQueries.findPatientsTransferredOut(), mappings));
    dsd.addSearch(
        "ENDED-TPT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Who have Completed TPT",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_7MONTHS_PREVIOUS_REPORTING_ENDDATE_PERIOD)),
            mappings));
    dsd.addSearch(
        "NEWLY-ART",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients New on ART Who Have Started TPT",
                TPTCompletationQueries.QUERY
                    .findPatientsWhoStartedArtAndTbPrevPreventiveTreatmentInDisaggregation(
                        DisaggregationTypes.NEWLY_ENROLLED)),
            mappings));

    dsd.addSearch(
        "PREVIOUS-ART",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Previously on ART Who Have Started TPT",
                TPTCompletationQueries.QUERY
                    .findPatientsWhoStartedArtAndTbPrevPreventiveTreatmentInDisaggregation(
                        DisaggregationTypes.PREVIOUSLY_ENROLLED)),
            mappings));

    dsd.setCompositionString(
        "(STARTED-TPT AND (NEWLY-ART OR PREVIOUS-ART)) NOT (TRF-OUT NOT ENDED-TPT) ");

    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsWhoCompletedINHTherapy")
  public CohortDefinition findPatientsWhoCompletedINHTherapy() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Finding Patients Who completed INH Therapy ");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(
            FIND_PATIENTS_WHO_COMPLETED_INH_THERAPY_BY_END_OF_REPORTING_PERIOD));

    return definition;
  }

  private void addGeneralParameters(final CohortDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }
}
