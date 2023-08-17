package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB7AdvancedDiseaseAndTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
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

  public DataSetDefinition constructDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.setName("TX_PVLS Data Set");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.setName("TB7");
    dataSetDefinition.addParameters(this.getParameters());

    final CohortDefinition tb7CohortIndicator =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1();

    final CohortDefinition tb7CohortIndicator2 =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2();

    final CohortDefinition tb7CohortIndicator3 =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3();

    final CohortDefinition tb7CohortIndicator3TbLam =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithTBLam();

    final CohortDefinition tb7CohortIndicator3WithoutTbLam =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam();

    final CohortDefinition tb7CohortIndicator3WithoutCd4WithTbLam =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3();

    final CohortDefinition tb7CohortIndicator3WithoutCd4WithTbLamTotal =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam();

    final CohortDefinition tb7CohortIndicator3WithoutCd4WithoutTbLamTotal =
        this.tb7AdvancedDiseaseAndTBCohortQueries
            .getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam();

    final CohortIndicator tb7Indicator1 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator, mappings));

    final CohortIndicator tb7Indicator2 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator2, mappings));

    final CohortIndicator tb7Indicator3 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3, mappings));

    final CohortIndicator tb7Indicator3TbLam =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3TbLam, mappings));

    final CohortIndicator tb7Indicator3WithoutTbLam =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3WithoutTbLam, mappings));

    final CohortIndicator tb7Indicator3WithoutCd4WithTbLam =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3WithoutCd4WithTbLam, mappings));

    final CohortIndicator tb7Indicator3WithoutCd4WithTbLamTotal =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3WithoutCd4WithTbLamTotal, mappings));

    final CohortIndicator tb7Indicator3WithoutCd4WithoutTbLamTotal =
        this.eptsGeneralIndicator.getIndicator(
            "TB7", EptsReportUtils.map(tb7CohortIndicator3WithoutCd4WithoutTbLamTotal, mappings));

    dataSetDefinition.addColumn("TI1", "TI1", EptsReportUtils.map(tb7Indicator1, mappings), "");

    addRow(
        dataSetDefinition,
        "EL",
        "Number of clients eligible for CD4 count during the inclusion period",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Number of clients eligible for CD4 count during the inclusion period",
                EptsReportUtils.map(
                    tb7AdvancedDiseaseAndTBCohortQueries
                        .getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1(),
                    mappings)),
            mappings),
        getColumns());

    dataSetDefinition.addColumn("TI2", "TI2", EptsReportUtils.map(tb7Indicator2, mappings), "");

    addRow(
        dataSetDefinition,
        "RL",
        "Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
                EptsReportUtils.map(
                    tb7AdvancedDiseaseAndTBCohortQueries
                        .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2(),
                    mappings)),
            mappings),
        getColumns());

    dataSetDefinition.addColumn("TI3", "TI3", EptsReportUtils.map(tb7Indicator3, mappings), "");
    dataSetDefinition.addColumn(
        "TITBLAM3", "TITBLAM3", EptsReportUtils.map(tb7Indicator3TbLam, mappings), "");
    dataSetDefinition.addColumn(
        "TINTBLAM3", "TINTBLAM3", EptsReportUtils.map(tb7Indicator3WithoutTbLam, mappings), "");

    addRow(
        dataSetDefinition,
        "RL2",
        "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
            + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
                    + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
                EptsReportUtils.map(
                    tb7AdvancedDiseaseAndTBCohortQueries
                        .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithTBLam(),
                    mappings)),
            mappings),
        getColumns());

    addRow(
        dataSetDefinition,
        "NRL2",
        "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
            + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
                    + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
                EptsReportUtils.map(
                    tb7AdvancedDiseaseAndTBCohortQueries
                        .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3WithoutTBLam(),
                    mappings)),
            mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TINCD4TBLAM3",
        "TINCD4TBLAM3",
        EptsReportUtils.map(tb7Indicator3WithoutCd4WithTbLam, mappings),
        "");

    dataSetDefinition.addColumn(
        "TINOTCD4TBLAM",
        "TINOTCD4TBLAM",
        EptsReportUtils.map(tb7Indicator3WithoutCd4WithTbLamTotal, mappings),
        "");

    dataSetDefinition.addColumn(
        "TINOTCD4NOTTBLAM",
        "TINOTCD4NOTTBLAM",
        EptsReportUtils.map(tb7Indicator3WithoutCd4WithoutTbLamTotal, mappings),
        "");

    addRow(
        dataSetDefinition,
        "CDNRL2",
        "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
            + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Number of clients with CD4 count during inclusion period showing severe immunodepression  "
                    + "(CD4 count < 200/mm3  for patients > =5 , <500/mm3 for children 1-4, <750/mm3 for children under 12 months) ",
                EptsReportUtils.map(
                    tb7AdvancedDiseaseAndTBCohortQueries
                        .getNumberOfClientsWithoutCd4ResultDuringInclusionPeriodIndicator3WithTBLam(),
                    mappings)),
            mappings),
        getColumns());

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

    ColumnParameters a7 = new ColumnParameters("<1", "<1", "gender=M|age=<1", "08");
    ColumnParameters a8 = new ColumnParameters("1-4", "1-4 years female", "gender=M|age=1-4", "09");
    ColumnParameters a9 = new ColumnParameters("5-9", "5-9 years female", "gender=M|age=5-9", "10");
    ColumnParameters a10 =
        new ColumnParameters("10-14", "10-14 female", "gender=M|age=10-14", "11");
    ColumnParameters a11 =
        new ColumnParameters("15-19", "15-19 female", "gender=M|age=15-19", "12");
    ColumnParameters a12 = new ColumnParameters("20+", "20+ female", "gender=M|age=20+", "13");
    ColumnParameters unknownM =
        new ColumnParameters("unknownF", "Unknown age", "gender=M|age=UK", "14");

    return Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, unknownF, a9, a10, a11, a12, unknownM);
  }
}
