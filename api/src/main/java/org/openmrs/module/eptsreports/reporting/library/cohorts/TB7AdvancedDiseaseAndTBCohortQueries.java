package org.openmrs.module.eptsreports.reporting.library.cohorts;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TxNewQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.LevelOfPositivity;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
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

  private static final String CD4_RESULT = "TB7/CD4_RESULT.sql";
  private static final String CD4_RESULT_2 = "TB7/CD4_RESULT_2.sql";
  private static final String CD4_RESULT_3 = "TB7/CD4_RESULT_3.sql";

  private static final String CD4_CURR_DATE = "TB7/CD4_CURR_DATE.sql";
  private static final String TB_LAM_CURR_DATE = "TB7/TB_LAM_CURR_DATE.sql";
  private static final String TB_LAM_POSITIVE_OR_NEGATIVE = "TB7/TB_LAM_POSITIVE_OR_NEGATIVE.sql";
  private static final String TB_LAM_LEVEL_OF_POSITIVITY = "TB7/TB_LAM_LEVEL_OF_POSITIVITY.sql";
  private static final String GENEXPERT = "TB7/GENEXPERT_CURR_DATE.sql";
  private static final String GENEXPERT_POSITIVE = "TB7/GENEXPERT_POSITIVE.sql";
  private static final String TB = "TB7/TB_CUUR_DATE.sql";

  private static int TB_LAM_POSITIVE = 703;
  private static int TB_LAM_NEGATIVE = 664;

  @Autowired private GenericCohortQueries genericCohortQueries;

  public static String findPregnantOrBreatfeeding(TypePTV typePTV) {
    String query =
        EptsQuerysUtils.loadQuery(TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANT_OR_BREASTEFEEDING);

    switch (typePTV) {
      case PREGNANT:
        query = query + "where final.decisao = 1 ";
        break;

      case BREASTFEEDING:
        query = query + "where final.decisao = 2 ";
        break;
    }
    return query;
  }

  public static String findLevelOfPosotivity(LevelOfPositivity levelOfPositivity) {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_LEVEL_OF_POSITIVITY);

    switch (levelOfPositivity) {
      case Grade_1:
        query = query + "where f.value_coded = 165186 ";
        break;

      case Grade_2:
        query = query + "where f.value_coded = 165187 ";
        break;

      case Grade_3:
        query = query + "where f.value_coded = 165188 ";
        break;
      case Grade_4:
        query = query + "where f.value_coded = 165348 ";
        break;

      case Blank:
        query = query + "where f.value_coded is null ";
        break;
    }
    return query;
  }

  public static String findPatientWhoHaveCd4Result() {
    String query = EptsQuerysUtils.loadQuery(CD4_RESULT);
    return query;
  }

  public static String findPatientWhoHaveCd4Result2() {
    String query = EptsQuerysUtils.loadQuery(CD4_RESULT_2);
    return query;
  }

  public static String findPatientWhoHaveCd4Result3() {
    String query = EptsQuerysUtils.loadQuery(CD4_RESULT_3);
    return query;
  }

  public static String findPatientWhoHaveNegativeTBLAMResults() {
    return "select p.patient_id  																			"
        + "   from patient p  																					"
        + "inner join encounter e on e.patient_id=p.patient_id 													"
        + "inner join obs o on o.encounter_id=e.encounter_id                              						"
        + "  where p.voided=0 and  e.voided=0 and e.encounter_type in (6,13, 51, 90) and o.concept_id=23951 and o.value_coded in(703,664)  and o.voided=0 "
        + "        and  e.location_id=:location and e.encounter_datetime between :startDate and :endDate 		";
  }

  public static String findPatientWhoHaveCd4UntilCurrentDate() {
    String query = EptsQuerysUtils.loadQuery(CD4_CURR_DATE);
    return query;
  }

  public static String findPatientWhoHaveTBLamPositiveOrNegative() {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_POSITIVE_OR_NEGATIVE);
    return query;
  }

  public static String findPatientWhoHaveTBLamLevelOfPositivity() {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_LEVEL_OF_POSITIVITY);
    return query;
  }

  public static String findPatientWhoHaveGenexpert() {
    String query = EptsQuerysUtils.loadQuery(GENEXPERT);
    return query;
  }

  public static String findPatientWhoHavePositiveGenexpert() {
    String query = EptsQuerysUtils.loadQuery(GENEXPERT_POSITIVE);
    return query;
  }

  public static String findPatientWhoHavePositiveGenexpertTb() {
    String query = EptsQuerysUtils.loadQuery(TB);
    return query;
  }

  public static String findPatientWhoHaveTbLamResulUntilCurrentDate() {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_CURR_DATE);
    return query;
  }

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

    definition.addSearch(
        "TRANSFERREDOUT",
        Mapped.mapStraightThrough(this.findPatientsWhoAreTransferredOutByReportGenerationDate()));

    definition.addSearch(
        "DEAD", Mapped.mapStraightThrough(this.findPatientsWhoAreDeadByReportGenerationDate()));

    definition.setCompositionString(
        "(TXNEW or PREGNANT or HIGH-VL or ART-RESTART) not (TRANSFERREDOUT or DEAD)");

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

    definition.setCompositionString(
        "(CD4-TXNEW or PREGNANT or HIGH-VL or ART-RESTART) not (TRANSFERREDOUT or DEAD)");

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
                "Finding patients who are newly enrolled on ART",
                TxNewQueries.QUERY.findPatientsWhoAreNewlyEnrolledOnART),
            mappings));

    definition.setCompositionString("TXNEW");

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
                "Finding clients with a CD4 count within 33 days - Newly on ART",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoHaveCD4CountWithin33Days),
            mappings));

    definition.setCompositionString("TXNEW");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResults")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Find patients with Consecutive High VL Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Finding patients who have consecutive High VLT",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWithHighViralLoad),
            mappings));

    definition.setCompositionString("HIGHVL");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithConsecutiveViralLoadResultsWithCD4Count")
  public CohortDefinition findPatientsWithConsecutiveViralLoadResultsWithCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Find patients with Consecutive High VL Results with CD4 count");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Finding patients who have consecutive High VLT",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWIthHighVLWithCD4Count),
            mappings));

    definition.setCompositionString("HIGHVL");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTTreatment")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatment() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Find patients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Finding patients who have Restarted ART",
                TB7AdvancedDiseaseQueries.QUERY.findPatientWhoReinitiatedART),
            mappings));

    definition.setCompositionString("ART-RESTART");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedARTWhoHaveCD4Count")
  public CohortDefinition findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Find patients Who reinitiated ART Treatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ART-RESTART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "Finding patients who have Restarted ART and have CD4 Count",
                TB7AdvancedDiseaseQueries.QUERY.findPatientsWhoReinitiatedARTWhoHaveCD4Count),
            mappings));

    definition.setCompositionString("ART-RESTART");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreTransferrdOut")
  public CohortDefinition findPatientsWhoAreTransferredOutByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Finding Patients Who Are TransferredOut by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(
            TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreDead")
  public CohortDefinition findPatientsWhoAreDeadByReportGenerationDate() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("Finding Patients Who Are Dead by Report Generation  Date ");
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        EptsQuerysUtils.loadQuery(TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_ARE_DEAD));

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE2",
        map(this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2(), mappingsDen));

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("CD4RESULT", findPatientWhoHaveCd4Result2()),
            mappings));

    definition.setCompositionString("ELEGIBLE2 AND CD4RESULT");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithTBLam() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE3",
        map(this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3(), mappingsDen));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TBLAM", findPatientWhoHaveTbLamResulUntilCurrentDate()),
            mappings));

    definition.setCompositionString("ELEGIBLE3 AND TBLAM");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE3",
        map(this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3(), mappingsDen));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TBLAM", findPatientWhoHaveTbLamResulUntilCurrentDate()),
            mappings));

    definition.setCompositionString("ELEGIBLE3 NOT TBLAM");
    return definition;
  }

  public CohortDefinition getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE2",
        map(this.getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2(), mappingsDen));

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("CD4RESULT", findPatientWhoHaveCd4Result3()),
            mappings));

    definition.setCompositionString("ELEGIBLE2 AND CD4RESULT");
    return definition;
  }

  public CohortDefinition
      getumberOfClientsWithCD4CountDuringInclusionPeriodWithoutSevereImmunodepression() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "WITHTBLAM",
        map(
            this.getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam(),
            mappings));

    definition.addSearch(
        "WITHOUTTBLAM",
        map(
            this.getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam(),
            mappings));

    definition.setCompositionString("WITHTBLAM OR WITHOUTTBLAM");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE3",
        map(this.getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3(), mappingsDen));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TBLAM", findPatientWhoHaveTbLamResulUntilCurrentDate()),
            mappings));

    definition.setCompositionString("ELEGIBLE3 AND TBLAM");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE3",
        map(this.getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3(), mappingsDen));

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TBLAM", findPatientWhoHaveTbLamResulUntilCurrentDate()),
            mappings));

    definition.setCompositionString("ELEGIBLE3 NOT TBLAM");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithoutCD4CountButWithTBLAMResultsDuringTheInclusionPeriodIncludingClientsWhoWereNotEligibleForCD4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "NEGATIVE-TBLAM" + "",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("TBLAM", findPatientWhoHaveNegativeTBLAMResults()),
            mappings));

    definition.addSearch(
        "CD4" + "",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("CD4", findPatientWhoHaveCd4Result()), mappings));

    definition.addSearch(
        "TROUT" + "",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TROUT",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT)),
            mappings));

    definition.addSearch(
        "DEAD" + "",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DEAD",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_ARE_DEAD)),
            mappings));

    definition.setCompositionString("NEGATIVE-TBLAM NOT(CD4 OR TROUT OR DEAD)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithTBLAMResultsByReportGenerationDate() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";

    definition.addSearch(
        "TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TBLAM", findPatientWhoHaveTbLamResulUntilCurrentDate()),
            mappings));

    definition.addSearch(
        "TROUT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TROUT",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT)),
            mappings));

    definition.addSearch(
        "DEAD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DEAD",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PATIENTS_WHO_ARE_DEAD)),
            mappings));

    definition.setCompositionString("TBLAM NOT (TROUT OR DEAD)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAM", map(this.getNumberOfClientsWithTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "POSITIVE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "POSITIVE",
                String.format(findPatientWhoHaveTBLamPositiveOrNegative(), TB_LAM_POSITIVE)),
            mappings));

    definition.setCompositionString("TBLAM AND POSITIVE");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithNegativeTBLAMResultsByReportGenerationDate() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAM", map(this.getNumberOfClientsWithTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "NEGATIVE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "NEGATIVE",
                String.format(findPatientWhoHaveTBLamPositiveOrNegative(), TB_LAM_NEGATIVE)),
            mappings));

    definition.setCompositionString("TBLAM AND NEGATIVE");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
      LevelOfPositivity levelOfPositivity) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "GRADE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("GRADE", findLevelOfPosotivity(levelOfPositivity)),
            mappings));

    definition.setCompositionString("TBLAMPOSITIVE AND GRADE");

    return definition;
  }

  public CohortDefinition getClientsWithoutPositiveTBLAMButNotTestedWithGeneXpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("GENEXPERT", findPatientWhoHaveGenexpert()),
            mappings));

    definition.setCompositionString("TBLAMPOSITIVE NOT GENEXPERT");

    return definition;
  }

  public CohortDefinition getClientsWithPositiveTBLAMAndAlsoTestedWithGeneXpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("GENEXPERT", findPatientWhoHaveGenexpert()),
            mappings));

    definition.setCompositionString("TBLAMPOSITIVE AND GENEXPERT");

    return definition;
  }

  public CohortDefinition getClientsWithPositiveTBLAMButNotTestedWithGeneXpert() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(), mappingsDen));

    definition.addSearch(
        "GENEXPERT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("GENEXPERT", findPatientWhoHaveGenexpert()),
            mappings));

    definition.setCompositionString("TBLAMPOSITIVE NOT GENEXPERT");

    return definition;
  }

  public CohortDefinition getClientsWithPositiveTBLAMAndGeneXpertPositiveforTB() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getClientsWithPositiveTBLAMAndAlsoTestedWithGeneXpert(), mappingsDen));

    definition.addSearch(
        "GENEXPERTPOSITIVE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "GENEXPERTPOSITIVE", findPatientWhoHavePositiveGenexpert()),
            mappings));

    definition.setCompositionString("TBLAMPOSITIVE AND GENEXPERTPOSITIVE");

    return definition;
  }

  public CohortDefinition getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTB() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "TBLAMPOSITIVE",
        map(this.getClientsWithPositiveTBLAMAndAlsoTestedWithGeneXpert(), mappingsDen));

    definition.addSearch(
        "TBLAMNOTGENEXPERT",
        map(this.getClientsWithPositiveTBLAMButNotTestedWithGeneXpert(), mappingsDen));

    definition.addSearch(
        "TB",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("TB", findPatientWhoHavePositiveGenexpertTb()),
            mappings));

    definition.setCompositionString("(TBLAMPOSITIVE OR TBLAMNOTGENEXPERT) AND TB");

    return definition;
  }

  public CohortDefinition
      getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
          LevelOfPositivity levelOfPositivity) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate-2m+1d},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        map(this.getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTB(), mappingsDen));

    definition.addSearch(
        "GRADE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("GRADE", findLevelOfPosotivity(levelOfPositivity)),
            mappings));

    definition.setCompositionString("DENOMINATOR AND GRADE");

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
                "PREGNANT", findPregnantOrBreatfeeding(TypePTV.PREGNANT)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("PREGNANT NOT CD4");

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
                "PREGNANT",
                EptsQuerysUtils.loadQuery(
                    TB7AdvancedDiseaseQueries.QUERY.FIND_PREGNANTS_WITH_COUNT_CD4)),
            "startDate=${endDate-10m+1d},endDate=${endDate-1m},location=${location}"));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CD4", TB7AdvancedDiseaseQueries.QUERY.findPatientsWithCD4),
            "startDate=${endDate-10m+1d},endDate=${endDate-2m+1d},location=${location}"));

    definition.setCompositionString("PREGNANT NOT CD4");

    return definition;
  }
}
