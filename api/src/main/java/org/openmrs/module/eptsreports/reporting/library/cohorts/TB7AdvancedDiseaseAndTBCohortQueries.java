package org.openmrs.module.eptsreports.reporting.library.cohorts;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB7AdvancedDiseaseAndTBCohortQueries {

  private static final String PREGNANT_OR_BREASTEFEEDING = "TB7/PREGNANT_OR_BREASTFEEDING.sql";
  private static final String HIGH_VIRAL_LOAD = "TB7/HIGH_VIRAL_LOAD.sql";
  private static final String REINITIATED_ART = "TB7/REINITIATED_ART.sql";
  private static final String CD4_RESULT = "TB7/CD4_RESULT.sql";
  private static final String CD4_RESULT_2 = "TB7/CD4_RESULT_2.sql";
  private static final String TB_LAM_1 = "TB7/TB_LAM_1.sql";
  private static final String TR_OUT_CURR_DATE = "TB7/TR_OUT_CURR_DATE.sql";
  private static final String DEAD_CURR_DATE = "TB7/DEAD_CURR_DATE.sql";
  private static final String CD4_CURR_DATE = "TB7/CD4_CURR_DATE.sql";
  private static final String TB_LAM_CURR_DATE = "TB7/TB_LAM_CURR_DATE.sql";

  @Autowired private GenericCohortQueries genericCohortQueries;
  @Autowired private TxNewCohortQueries txNewCohortQueries;

  public static String findPregnantOrBreatfeeding(TypePTV typePTV) {
    String query = EptsQuerysUtils.loadQuery(PREGNANT_OR_BREASTEFEEDING);

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

  public static String findPatientWithHighViralLoad() {
    String query = EptsQuerysUtils.loadQuery(HIGH_VIRAL_LOAD);
    return query;
  }

  public static String findPatientWhoReinitiatedART() {
    String query = EptsQuerysUtils.loadQuery(REINITIATED_ART);
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

  public static String findPatientWhoHaveTbLamResul1() {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_1);
    return query;
  }

  public static String findPatientWhoHaveTbLamResulUntilCurrentDate() {
    String query = EptsQuerysUtils.loadQuery(TB_LAM_CURR_DATE);
    return query;
  }

  public static String findPatientWhoAreTransferedOut() {
    String query = EptsQuerysUtils.loadQuery(TR_OUT_CURR_DATE);
    return query;
  }

  public static String findPatientWhoAreDead() {
    String query = EptsQuerysUtils.loadQuery(DEAD_CURR_DATE);
    return query;
  }

  public static String findPatientWhoHaveCd4UntilCurrentDate() {
    String query = EptsQuerysUtils.loadQuery(CD4_CURR_DATE);
    return query;
  }

  public CohortDefinition
      getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    definition.addSearch(
        "TXNEW", map(txNewCohortQueries.getTxNewCompositionCohort("TX NEW"), mappings));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PREGNANT", findPregnantOrBreatfeeding(TypePTV.PREGNANT)),
            mappings));

    definition.addSearch(
        "HIGHVIRALLOAD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("HIGHVIRALLOAD", findPatientWithHighViralLoad()),
            mappings));

    definition.addSearch(
        "REINITIATEDART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("REINITIATEDART", findPatientWhoReinitiatedART()),
            mappings));

    definition.setCompositionString("TXNEW OR PREGNANT OR HIGHVIRALLOAD OR REINITIATEDART");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
    final String mappingsDen = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "ELEGIBLE",
        map(
            this.getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1(),
            mappingsDen));

    definition.addSearch(
        "CD4RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql("CD4RESULT", findPatientWhoHaveCd4Result()),
            mappings));

    definition.setCompositionString("ELEGIBLE AND CD4RESULT");

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

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
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

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
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
            this.genericCohortQueries.generalSql("CD4RESULT", findPatientWhoHaveCd4Result2()),
            mappings));

    definition.setCompositionString("ELEGIBLE2 NOT CD4RESULT");
    return definition;
  }

  public CohortDefinition
      getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
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

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";
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
}
