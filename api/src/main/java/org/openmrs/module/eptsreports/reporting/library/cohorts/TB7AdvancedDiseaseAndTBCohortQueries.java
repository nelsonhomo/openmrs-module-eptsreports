package org.openmrs.module.eptsreports.reporting.library.cohorts;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;

import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries.QUERY.PositivityLevel;
import org.openmrs.module.eptsreports.reporting.library.queries.TxNewQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB7AdvancedDiseaseAndTBCohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  public CohortDefinition
      getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-Indicator 1- Cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "TXNEW", EptsReportUtils.map(findPatientsWhoAreNewlyEnrolledOnArt(), mappings));

    definition.addSearch("PREGNANT", Mapped.mapStraightThrough(this.getPatientsWhoArePregnant()));

    definition.addSearch(
        "HIGH-VL", EptsReportUtils.map(findPatientsWithConsecutiveViralLoadResults(), mappings));

    definition.addSearch(
        "ART-RESTART", EptsReportUtils.map(findPatientsWhoReinitiatedARTTreatment(), mappings));

    definition.setCompositionString("TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 2 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4-TXNEW",
        map(this.findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days(), mappings));

    definition.addSearch(
        "PREGNANT", Mapped.mapStraightThrough(this.getPatientsWhoArePregnantsWithCountCD4()));

    definition.addSearch(
        "HIGH-VL",
        EptsReportUtils.map(findPatientsWithConsecutiveViralLoadResultsWithCD4Count(), mappings));

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count(), mappings));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));

    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4-TXNEW",
        map(
            this
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions(),
            mappings));

    definition.addSearch(
        "PREGNANT",
        Mapped.mapStraightThrough(
            this.getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression()));

    definition.addSearch(
        "HIGH-VL",
        EptsReportUtils.map(
            findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions(),
            mappings));

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression(),
            mappings));

    definition.setCompositionString("CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 4 -cascade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4-TXNEW",
        map(
            this
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults(),
            mappings));

    definition.addSearch(
        "PREGNANT",
        Mapped.mapStraightThrough(
            this
                .getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppressionWithTBLAMResults()));

    definition.addSearch(
        "HIGH-VL",
        EptsReportUtils.map(
            findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults(),
            mappings));

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults(),
            mappings));

    definition.setCompositionString("CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCD4ShowingImmunoSuppressions() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 1 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("CD4RESULT not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 2 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4RESULT",
        Mapped.mapStraightThrough(this.getNumberOfClientsWithCD4ShowingImmunoSuppressions()));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with TBM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("(CD4RESULT and TBLAM) not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "IMMUNO-SUPRR-AND-TBLAM",
        EptsReportUtils.map(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMResults(), mappings));

    definition.addSearch(
        "POSITIVE-RESULTS",
        EptsReportUtils.map(this.findPatientsWithPositiveTBLAMResults(), mappings));

    definition.setCompositionString("IMMUNO-SUPRR-AND-TBLAM and POSITIVE-RESULTS");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithoutGenexpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "POSITIVE-RESULTS",
        EptsReportUtils.map(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults(),
            mappings));

    definition.addSearch(
        "NO-GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "No GeneXpert by report generation date",
                String.format(
                    TB7AdvancedDiseaseQueries.QUERY.eValuatePatientsCheckingGenExpertTest,
                    "is null")),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("NO-GENEXPERT and POSITIVE-RESULTS");

    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Indicator 3 -cascade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "POSITIVE-RESULTS",
        EptsReportUtils.map(
            this.getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults(),
            mappings));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "GeneXpert by report generation date",
                String.format(
                    TB7AdvancedDiseaseQueries.QUERY.eValuatePatientsCheckingGenExpertTest,
                    "is not null")),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("GENEXPERT and POSITIVE-RESULTS");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreNewlyEnrolledOnArt")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnArt() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsWhoAreNewlyEnrolledOnArt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who initiated ART during inclusion period",
                TxNewQueries.QUERY.findPatientsWhoAreNewlyEnrolledOnART),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("TXNEW not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days")
  public CohortDefinition findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsWhoAreNewlyEnrolledOnArt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who initiated ART during inclusion period with a CD4 count within 33 days - Newly on ART",
                TB7AdvancedDiseaseQueries.QUERY
                    .findPatientsWhoInitiatedARTAndHaveCD4CountWithin33Days),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("TXNEW not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions")
  public CohortDefinition
      findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count within 33 days - Newly on ART",
                TB7AdvancedDiseaseQueries.QUERY
                    .findPatientsWhoInitiatedARTAndHaveCD4CountWithin33Days),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("(TXNEW not (TRANSFERREDOUT or DEAD)) and CD4RESULT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults")
  public CohortDefinition
      findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TXNEW",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count within 33 days - Newly on ART",
                TB7AdvancedDiseaseQueries.QUERY
                    .findPatientsWhoInitiatedARTAndHaveCD4CountWithin33Days),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));
    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with TBM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString(
        "((TXNEW not (TRANSFERREDOUT or DEAD)) and CD4RESULT) and TBLAM");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResults")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Clients with Consecutive High VL Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                " Clients with two consecutive high viral load results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithHighViralLoad),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("HIGHVL not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResultsWithCD4Count")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResultsWithCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Clients with Consecutive High VL Results with CD4 count");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with two consecutive high viral load results with CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWIthHighVLWithCD4Count),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("HIGHVL not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions")
  public CohortDefinition
      findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with two consecutive high viral load results with CD4 Count and immunoSuppression",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWIthHighVLWithCD4Count),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("(HIGHVL not (TRANSFERREDOUT or DEAD)) and CD4RESULT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults")
  public CohortDefinition
      findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "PatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with two consecutive high viral load results With CD4 count and ImmunoSuppression and TBLAM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWIthHighVLWithCD4Count),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with TBM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            "endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString(
        "((HIGHVL not (TRANSFERREDOUT or DEAD)) and CD4RESULT) and TBLAM");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTTreatment")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatment() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who reinitiated ART during inclusion period",
                TB7AdvancedDiseaseQueries.QUERY.findPatientWhoReinitiatedART),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("ART-RESTART not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTWhoHaveCD4Count")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who reinitiated ART during inclusion period and have CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoReinitiatedARTWhoHaveCD4Count),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("ART-RESTART not (TRANSFERREDOUT or DEAD)");

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression")
  public CohortDefinition
      findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "Clients who reinitiated ART during inclusion period and are ImmunoSuppression");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who have Restarted ART and have CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoReinitiatedARTWhoHaveCD4Count),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString("(ART-RESTART not (TRANSFERREDOUT or DEAD)) and CD4RESULT");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults")
  public CohortDefinition
      findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Clients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients who have Restarted ART and have CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoReinitiatedARTWhoHaveCD4Count),
            mappings));
    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));
    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients with TBM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            "endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString(
        "((ART-RESTART not (TRANSFERREDOUT or DEAD)) and CD4RESULT) and TBLAM");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnant() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Women Who are Pregnant",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANT_OR_BREASTEFEEDING)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients With CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("PREGNANT not (CD4 or TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnantsWithCountCD4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Women Who are Pregnant ande have CD4 Count",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANTS_WITH_COUNT_CD4)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients With CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("PREGNANT not (CD4 or TRANSFERREDOUT or DEAD)");

    return definition;
  }

  public CohortDefinition getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4-with-immunosupression");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Women Who are Pregnant",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANTS_WITH_COUNT_CD4)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients With CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString("(PREGNANT not (CD4 or TRANSFERREDOUT or DEAD)) and CD4RESULT");

    return definition;
  }

  public CohortDefinition
      getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppressionWithTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7-PREGNANT-with-count-cd4-with-immunosupressionWithTBLAMResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Women Who are Pregnant",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANTS_WITH_COUNT_CD4)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with a CD4 count showing severe immunosuppression during the inclusion period",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION)),
            mappings));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with TBM Results",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithTBLAMResults),
            "endDate=${endDate-2m+1d},location=${location}"));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Clients With CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));
    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString(
        "((PREGNANT not (CD4 or TRANSFERREDOUT or DEAD)) and CD4RESULT) and TBLAM");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithNegativeTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Negative TB LAM Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "NEGATIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Negative TBLAM Results during the inclusion period",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithNegativeTBLAMResults),
            mappings));

    definition.addSearch(
        "POSITIVE-RESULTS",
        EptsReportUtils.map(this.findPatientsWithPositiveTBLAMResults(), mappings));

    definition.addSearch(
        "CD4RESULT",
        Mapped.mapStraightThrough(this.getNumberOfClientsWithCD4ShowingImmunoSuppressions()));

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));
    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));
    definition.setCompositionString(
        "(NEGATIVE-TBLAM-RESULTS not (TRANSFERREDOUT or DEAD or POSITIVE-RESULTS)) and CD4RESULT");

    return definition;
  }

  private CohortDefinition findPatientsWithPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "LEVEL4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "LEVEL1",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults(), mappings));
    definition.addSearch(
        "NO-LEVEL",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults(), mappings));

    definition.setCompositionString("LEVEL4 or LEVEL3 or LEVEL2 or LEVEL1 or NO-LEVEL");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_FOUR.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_THREE.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_TWO.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_ONE.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7 - Positive TB LAM Results: Grade no Level");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB7AdvancedDiseaseQueries.QUERY
                    .FIND_PATIENTS_WITH_SEVERE_IMMUNOSUPPRESSION_WITH_TBLAM_GRADE_LEVEL),
            PositivityLevel.NO_GRADE.getValue(),
            PositivityLevel.NO_GRADE.getValue(),
            PositivityLevel.NO_GRADE.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue(),
                    PositivityLevel.GRADE_ONE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue(),
                    PositivityLevel.GRADE_ONE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue(),
                    PositivityLevel.GRADE_ONE.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.NO_GRADE.name(),
                query),
            mappings));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreTransferrdOut")
  private CohortDefinition findPatientsWhoAreTransferredOutByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Clients Who Are TransferredOut by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(
            TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  private CohortDefinition findPatientsWhoAreDeadByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Clients Who Are Dead by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_ARE_DEAD));

    return definition;
  }
}
