package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB7AdvancedDiseaseAndTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.TB7CascadeReportDimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TB7AdvancedDiseaseAndTBDataSet extends BaseDataSet {

  @Autowired private TB7AdvancedDiseaseAndTBCohortQueries tb7AdvancedDiseaseAndTBCohortQueries;
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private TB7CascadeReportDimensions tb7CascadeReportDimensions;

  public DataSetDefinition constructDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.setName("TX_PVLS Data Set");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorOneDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorOneDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorTwoDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorTwoDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorThreeDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorThreeDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorFourDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorFourDimension(), mappings));

    dataSetDefinition.addDimension(
        "tblam-grade-level",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getTBLAMGradeLevelDimension(), mappings));

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.setName("TB7");
    dataSetDefinition.addParameters(this.getParameters());

    // final CohortDefinition patienWithoutCD4CountButWithTBLAMResults =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    //
    // .getNumberOfClientsWithoutCD4CountButWithTBLAMResultsDuringTheInclusionPeriodIncludingClientsWhoWereNotEligibleForCD4();
    //
    // final CohortDefinition numberOfClientsWithTBLAMResultsByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithTBLAMResultsByReportGenerationDate();
    //
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsByReportGenerationDate =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate();
    //
    // final CohortDefinition
    // numberOfClientsWithNegativeTBLAMResultsByReportGenerationDate =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithNegativeTBLAMResultsByReportGenerationDate();
    //
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsGrade4ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
    // LevelOfPositivity.Grade_4);
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsGrade3ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
    // LevelOfPositivity.Grade_3);
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsGrade2ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
    // LevelOfPositivity.Grade_2);
    //
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsGrade1ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
    // LevelOfPositivity.Grade_1);
    //
    // final CohortDefinition
    // numberOfClientsWithPositiveTBLAMResultsGradeBlankByReportGenerationDate =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate(
    // LevelOfPositivity.Blank);
    //
    // final CohortDefinition clientsWithoutPositiveTBLAMButNotTestedWithGeneXpert =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithoutPositiveTBLAMButNotTestedWithGeneXpert();
    //
    // final CohortDefinition clientsWithPositiveTBLAMButNotTestedWithGeneXpert =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMAndAlsoTestedWithGeneXpert();
    //
    // final CohortDefinition clientsWithPositiveTBLAMWithGeneXpertAndTb =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTB();
    //
    // final CohortDefinition clientsWithPositiveTBLAMAndGeneXpertPositiveforTb =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMAndGeneXpertPositiveforTB();
    // final CohortDefinition
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade4ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
    // LevelOfPositivity.Grade_4);
    // final CohortDefinition
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade3ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
    // LevelOfPositivity.Grade_3);
    // final CohortDefinition
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade2ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
    // LevelOfPositivity.Grade_2);
    //
    // final CohortDefinition
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade1ByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
    // LevelOfPositivity.Grade_1);
    //
    // final CohortDefinition
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGradeBlankByReportGenerationDate
    // =
    // this.tb7AdvancedDiseaseAndTBCohortQueries
    // .getClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeDesagregation(
    // LevelOfPositivity.Blank);

    final CohortIndicator tb7Indicator1 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1(),
                mappings));

    final CohortIndicator tb7Indicator2 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2(),
                mappings));

    final CohortIndicator tb7Indicator3 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3(),
                mappings));

    final CohortIndicator tb7Indicator4 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator4(),
                mappings));

    final CohortIndicator tb7ImmunoCD4 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressions(),
                mappings));

    final CohortIndicator tb7ImmunoCD4WithTBLam =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMResults(),
                mappings));

    final CohortIndicator tb7ImmunoCD4WithTBLamNegativeResults =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithNegativeTBLAMResults(),
                mappings));

    // final CohortIndicator tb7IndicatorpatienWithoutCD4CountButWithTBLAMResults =
    // this.eptsGeneralIndicator
    // .getIndicator("TB7",
    // EptsReportUtils.map(patienWithoutCD4CountButWithTBLAMResults,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithTBLAMResultsByReportGenerationDate =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(numberOfClientsWithTBLAMResultsByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsByReportGenerationDate, mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithNegativeTBLAMResultsByReportGenerationDate =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithNegativeTBLAMResultsByReportGenerationDate, mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade4ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsGrade4ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade3ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsGrade3ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade2ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsGrade2ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade1ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsGrade1ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorclientsWithoutPositiveTBLAMButNotTestedWithGeneXpert =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(clientsWithoutPositiveTBLAMButNotTestedWithGeneXpert,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorclientsWithPositiveTBLAMButNotTestedWithGeneXpert =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(clientsWithPositiveTBLAMButNotTestedWithGeneXpert,
    // mappings));
    //
    // final CohortIndicator tb7IndicatorClientsWithPositiveTBLAMWithGeneXpertAndTb
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7", EptsReportUtils.map(clientsWithPositiveTBLAMWithGeneXpertAndTb,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorClientsWithPositiveTBLAMAndGeneXpertPositiveforTb =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(clientsWithPositiveTBLAMAndGeneXpertPositiveforTb,
    // mappings));
    //
    // final CohortIndicator
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGradeBlankByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    // numberOfClientsWithPositiveTBLAMResultsGradeBlankByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade4ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade4ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade3ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade3ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade2ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade2ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade1ByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade1ByReportGenerationDate,
    // mappings));
    //
    // final CohortIndicator
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGradeBlankByReportGenerationDate
    // =
    // this.eptsGeneralIndicator.getIndicator(
    // "TB7",
    // EptsReportUtils.map(
    //
    // clientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGradeBlankByReportGenerationDate,
    // mappings));

    // TI1
    dataSetDefinition.addColumn(
        "TI1",
        "TI1-Number of clients eligible for CD4 count during the inclusion period",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "");

    addRow(
        dataSetDefinition,
        "EL",
        "Number of clients eligible for CD4 count during the inclusion period",
        EptsReportUtils.map(tb7Indicator1, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI1-newlyOnArt",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI1-pregnant",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI1-consecutiveHighVL",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI1-artRestart",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=artRestart");

    // TI2
    dataSetDefinition.addColumn(
        "TI2",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RL",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
        EptsReportUtils.map(tb7Indicator2, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI2-newlyOnArt",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI2-pregnant",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI2-consecutiveHighVL",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI2-artRestart",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=artRestart");

    // T3
    dataSetDefinition.addColumn(
        "TI3",
        "TI3-Number of clients with CD4 count showing severe immunosuppression during the inclusion period",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RM",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period",
        EptsReportUtils.map(tb7Indicator3, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI3-newlyOnArt",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI3-pregnant",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI3-consecutiveHighVL",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI3-artRestart",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=artRestart");

    // T4
    dataSetDefinition.addColumn(
        "TI4",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RN",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date",
        EptsReportUtils.map(tb7Indicator4, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI4-newlyOnArt",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Newly On Art",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI4-pregnant",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Pregnant",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI4-consecutiveHighVL",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI4-artRestart",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Art Restart",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=artRestart");

    // Cascate 2

    dataSetDefinition.addColumn(
        "CASCADE2-TI1",
        "Number of clients with a CD4 count during inclusion period (end date - 2 months + 1 day) and (end date - 1 month) showing severe immunosuppression",
        EptsReportUtils.map(tb7ImmunoCD4, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month))",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE4",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 4+",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "tblam-grade-level=four");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE3",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 3+",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "tblam-grade-level=three");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE2",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 2+",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "tblam-grade-level=two");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE1",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 1+",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "tblam-grade-level=one");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADENONE",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade not reported",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLam, mappings),
        "tblam-grade-level=no-level");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-NEGATIVE",
        "Number of clients with a CD4 count during inclusion period (end date - 2 months + 1 day) and (end date - 1 month) showing severe immunosuppression - Negative Results",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLamNegativeResults, mappings),
        "");

    // dataSetDefinition.addColumn(
    // "TITBLAM3",
    // "TITBLAM3-Number of clients with CD4 count during inclusion period showing
    // severe
    // immunodepressio(With TBLam)",
    // EptsReportUtils.map(tb7Indicator3TbLam, mappings),
    // "");
    // dataSetDefinition.addColumn(
    // "TINTBLAM3",
    // "TINTBLAM3-Number of clients with CD4 count during inclusion period showing
    // severe
    // immunodepressio(Without TBLam)",
    // EptsReportUtils.map(tb7Indicator3WithoutTbLam, mappings),
    // "");

    // addRow(
    // dataSetDefinition,
    // "RL2",
    // "Number of clients with CD4 count during inclusion period showing severe
    // immunodepression "
    // + "(CD4 count < 200/mm3 for patients > =5 , <500/mm3 for children 1-4,
    // <750/mm3
    // for children under 12 months) ",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "Number of clients with CD4 count during inclusion period showing severe
    // immunodepression "
    // + "(CD4 count < 200/mm3 for patients > =5 , <500/mm3 for children 1-4,
    // <750/mm3 for children under 12 months) ",
    // EptsReportUtils.map(
    // tb7AdvancedDiseaseAndTBCohortQueries
    //
    // .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithTBLam(),
    // mappings)),
    // mappings),
    // getColumns());
    //
    // addRow(
    // dataSetDefinition,
    // "NRL2",
    // "Number of clients with CD4 count during inclusion period showing severe
    // immunodepression "
    // + "(CD4 count < 200/mm3 for patients > =5 , <500/mm3 for children 1-4,
    // <750/mm3
    // for children under 12 months) ",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "Number of clients with CD4 count during inclusion period showing severe
    // immunodepression "
    // + "(CD4 count < 200/mm3 for patients > =5 , <500/mm3 for children 1-4,
    // <750/mm3 for children under 12 months) ",
    // EptsReportUtils.map(
    // tb7AdvancedDiseaseAndTBCohortQueries
    //
    // .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam(),
    // mappings)),
    // mappings),
    // getColumns());
    //
    //
    // addRow(
    // dataSetDefinition,
    // "CDNRL2",
    // "Number of clients with CD4 count during inclusion period without severe
    // immunodepression (CD4 count > = 200/mm3 for patients >5, > = 500 for children
    // 1-5, >
    // =750/mm3 for children <12 months)",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "Number of clients with CD4 count during inclusion period without severe
    // immunodepression (CD4 count > = 200/mm3 for patients >5, > = 500 for children
    // 1-5, >
    // =750/mm3 for children <12 months)",
    // EptsReportUtils.map(
    // tb7AdvancedDiseaseAndTBCohortQueries
    //
    // .getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam(),
    // mappings)),
    // mappings),
    // getColumns());
    //
    // dataSetDefinition.addColumn(
    // "TBLAM1T",
    // "TBLAM1T - Number of clients without CD4 count but with TB LAM results during
    // the
    // inclusion period (including clients who were not eligible for CD4(Total)",
    // EptsReportUtils.map(tb7IndicatorpatienWithoutCD4CountButWithTBLAMResults,
    // mappings),
    // "");
    //
    // addRow(
    // dataSetDefinition,
    // "TBLAM1",
    // "Number of clients without CD4 count but with TB LAM results during the
    // inclusion
    // period (including clients who were not eligible for CD4) ",
    // EptsReportUtils.map(
    // eptsGeneralIndicator.getIndicator(
    // "Number of clients without CD4 count but with TB LAM results during the
    // inclusion period (including clients who were not eligible for CD4) ",
    // EptsReportUtils.map(
    // tb7AdvancedDiseaseAndTBCohortQueries
    //
    // .getNumberOfClientsWithoutCD4CountButWithTBLAMResultsDuringTheInclusionPeriodIncludingClientsWhoWereNotEligibleForCD4(),
    // mappings)),
    // mappings),
    // getColumns());
    //
    // dataSetDefinition.addColumn(
    // "TBLAM2T",
    // "TBLAM2T - Number of clients with TBLAM Results By Report Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithTBLAMResultsByReportGenerationDate, mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP2T",
    // "TBLAMP2T - Number of clients with Positive TBLAM Results By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMN2T",
    // "TBLAMN2T - Number of clients with Negative TBLAM Results By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithNegativeTBLAMResultsByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP2L4T",
    // "TBLAMP2L4T - Number of clients with Positive TBLAM Results Grade 4 By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade4ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP2L3T",
    // "TBLAMP2L3T - Number of clients with Positive TBLAM Results Grade 3 By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade3ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP2L2T",
    // "TBLAMP2L2T - Number of clients with Positive TBLAM Results Grade 2 By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade2ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP2L1T",
    // "TBLAMP2L1T - Number of clients with Positive TBLAM Results Grade 1 By Report
    // Generation Date",
    // EptsReportUtils.map(
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGrade1ByReportGenerationDate,
    // mappings),
    // "");
    // dataSetDefinition.addColumn(
    // "TBLAMP2LBT",
    // "TBLAMP2LBT - Number of clients with Positive TBLAM Results Grade Blank By
    // Report
    // Generation Date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorNumberOfClientsWithPositiveTBLAMResultsGradeBlankByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP3T",
    // "TBLAMP3T - Number of clients with positive TB LAM but NOT tested with
    // GeneXpert ",
    // EptsReportUtils.map(
    // tb7IndicatorclientsWithoutPositiveTBLAMButNotTestedWithGeneXpert, mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP4T",
    // "TBLAMP4T - Number of clients with positive TB LAM and also tested with
    // GeneXpert",
    // EptsReportUtils.map(
    // tb7IndicatorclientsWithPositiveTBLAMButNotTestedWithGeneXpert, mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP5T",
    // "TBLAMP5T - Number of clients with positive TB LAM and GeneXpert positive for
    // TB",
    // EptsReportUtils.map(
    // tb7IndicatorClientsWithPositiveTBLAMAndGeneXpertPositiveforTb, mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP6T",
    // "TBLAMP6T - Number of clients with positive TB LAM and on TB treatment by
    // report
    // generation date",
    // EptsReportUtils.map(tb7IndicatorClientsWithPositiveTBLAMWithGeneXpertAndTb,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP7T",
    // "TBLAMP7T - Number of clients with Positive TBLAM but not tested with
    // genexpert for TB
    // grade 4 by report generation date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade4ByReportGenerationDate,
    // mappings),
    // "");
    // dataSetDefinition.addColumn(
    // "TBLAMP8T",
    // "TBLAMP8T - Number of clients with Positive TBLAM but not tested with
    // genexpert for TB
    // grade 3 by report generation date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade3ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP9T",
    // "TBLAMP9T - Number of clients with Positive TBLAM but not tested with
    // genexpert for TB
    // grade 2 by report generation date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade2ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP10T",
    // "TBLAMP10T - Number of clients with Positive TBLAM but not tested with
    // genexpert for
    // TB grade 1 by report generation date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGrade1ByReportGenerationDate,
    // mappings),
    // "");
    //
    // dataSetDefinition.addColumn(
    // "TBLAMP11T",
    // "TBLAMP11T - Number of clients with Positive TBLAM but not tested with
    // genexpert for
    // TB grade blank by report generation date",
    // EptsReportUtils.map(
    //
    // tb7IndicatorClientsWithPositiveTBLAMButNotTestedWithGeneXpertForTBGradeGradeBlankByReportGenerationDate,
    // mappings),
    // "");

    return dataSetDefinition;
  }

  private List<ColumnParameters> getColumns() {

    ColumnParameters a1 = new ColumnParameters("<1", "<1", "gender=F|age=<1", "01");
    ColumnParameters a2 = new ColumnParameters("1-4", "1-4 years female", "gender=F|age=1-4", "02");
    ColumnParameters a3 = new ColumnParameters("5-9", "5-9 years female", "gender=F|age=5-9", "03");
    ColumnParameters a4 = new ColumnParameters("10-14", "10-14 female", "gender=F|age=10-14", "04");
    ColumnParameters a5 = new ColumnParameters("15-19", "15-19 female", "gender=F|age=15-19", "05");
    ColumnParameters a6 = new ColumnParameters("20+", "20+ female", "gender=F|age=20+", "06");
    ColumnParameters unknownF =
        new ColumnParameters("unknownF", "Unknown age", "gender=F|age=UK", "07");

    ColumnParameters a7 = new ColumnParameters("<1", "<1 male", "gender=M|age=<1", "08");
    ColumnParameters a8 = new ColumnParameters("1-4", "1-4 years male", "gender=M|age=1-4", "09");
    ColumnParameters a9 = new ColumnParameters("5-9", "5-9 years male", "gender=M|age=5-9", "10");
    ColumnParameters a10 = new ColumnParameters("10-14", "10-14 male", "gender=M|age=10-14", "11");
    ColumnParameters a11 = new ColumnParameters("15-19", "15-19 male", "gender=M|age=15-19", "12");
    ColumnParameters a12 = new ColumnParameters("20+", "20+ male", "gender=M|age=20+", "13");
    ColumnParameters unknownM =
        new ColumnParameters("unknownF", "Unknown age", "gender=M|age=UK", "14");

    return Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, unknownF, a9, a10, a11, a12, unknownM);
  }
}
