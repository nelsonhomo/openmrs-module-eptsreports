package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.PrEPCTCohortQueries;
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
public class PrEPCtDataset extends BaseDataSet {

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private PrEPCTCohortQueries prepCTCohortQueries;

  public DataSetDefinition constructPrepCTDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
    dsd.setName("PREP CT Data Set");
    dsd.addParameters(getParameters());
    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    CohortDefinition adultsPatientsWhoInitiatedPrEP =
        prepCTCohortQueries.getPatientsWhoInitiatedPrep();
    CohortDefinition adultsPatientsWithIndeterminateTestResult =
        prepCTCohortQueries.getPatientsWithIndeterminateTestResult();
    CohortDefinition adultsPatientsWithNegativeTestResult =
        prepCTCohortQueries.getPatientsWithNegativeTestResult();
    CohortDefinition adultsPatientsWithPositiveTestResult =
        prepCTCohortQueries.getPatientsWithPositiveTestResult();

    final CohortIndicator adultsPatientsWhoInitiatedPrEPIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findAdultsPatientsWhoInitiatedPrEP",
            EptsReportUtils.map(adultsPatientsWhoInitiatedPrEP, mappings));
    final CohortIndicator adultsPatientsWithIndeterminateTestResultIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findAdultsPatientsWhoInitiatedPrEP",
            EptsReportUtils.map(adultsPatientsWithIndeterminateTestResult, mappings));
    final CohortIndicator adultsPatientsWithNegativeTestResultIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findAdultsPatientsWhoInitiatedPrEP",
            EptsReportUtils.map(adultsPatientsWithNegativeTestResult, mappings));
    final CohortIndicator adultsPatientsWithPositiveTestResultIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findAdultsPatientsWhoInitiatedPrEP",
            EptsReportUtils.map(adultsPatientsWithPositiveTestResult, mappings));

    dsd.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
    dsd.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dsd.addColumn(
        "P1",
        "Total Patients Initiated PREP",
        EptsReportUtils.map(adultsPatientsWhoInitiatedPrEPIndicator, mappings),
        "");
    super.addRow(
        dsd,
        "P2",
        "Age and Gender",
        EptsReportUtils.map(adultsPatientsWhoInitiatedPrEPIndicator, mappings),
        getColumnsForAgeAndGender());

    dsd.addColumn(
        "P2-TotalMale",
        " Age and Gender (Totals male) ",
        EptsReportUtils.map(adultsPatientsWhoInitiatedPrEPIndicator, mappings),
        "gender=M");
    dsd.addColumn(
        "P2-TotalFemale",
        "Age and Gender (Totals female) ",
        EptsReportUtils.map(adultsPatientsWhoInitiatedPrEPIndicator, mappings),
        "gender=F");

    dsd.addColumn(
        "P3",
        "Total Patients with Indeterminate Test Result",
        EptsReportUtils.map(adultsPatientsWithIndeterminateTestResultIndicator, mappings),
        "");
    dsd.addColumn(
        "P4",
        "Total Patients with Negative Test Result",
        EptsReportUtils.map(adultsPatientsWithNegativeTestResultIndicator, mappings),
        "");
    dsd.addColumn(
        "P5",
        "Total Patients with Positive Test Result",
        EptsReportUtils.map(adultsPatientsWithPositiveTestResultIndicator, mappings),
        "");

    return dsd;
  }

  private List<ColumnParameters> getColumnsForAgeAndGender() {

    ColumnParameters fifteenTo19M =
        new ColumnParameters("fifteenTo19M", "15 - 19 male", "gender=M|age=15-19", "01");
    ColumnParameters twentyTo24M =
        new ColumnParameters("twentyTo24M", "20 - 24 male", "gender=M|age=20-24", "02");
    ColumnParameters twenty5To29M =
        new ColumnParameters("twenty4To29M", "25 - 29 male", "gender=M|age=25-29", "03");
    ColumnParameters thirtyTo34M =
        new ColumnParameters("thirtyTo34M", "30 - 34 male", "gender=M|age=30-34", "04");
    ColumnParameters thirty5To39M =
        new ColumnParameters("thirty5To39M", "35 - 39 male", "gender=M|age=35-39", "05");
    ColumnParameters foutyTo44M =
        new ColumnParameters("foutyTo44M", "40 - 44 male", "gender=M|age=40-44", "06");
    ColumnParameters fouty5To49M =
        new ColumnParameters("fouty5To49M", "45 - 49 male", "gender=M|age=45-49", "07");
    ColumnParameters above50M =
        new ColumnParameters("above50M", "50+ male", "gender=M|age=50+", "08");
    ColumnParameters unknownM =
        new ColumnParameters("unknownM", "Unknown age male", "gender=M|age=UK", "09");

    ColumnParameters fifteenTo19F =
        new ColumnParameters("fifteenTo19F", "15 - 19 female", "gender=F|age=15-19", "10");
    ColumnParameters twentyTo24F =
        new ColumnParameters("twentyTo24F", "20 - 24 female", "gender=F|age=20-24", "11");
    ColumnParameters twenty5To29F =
        new ColumnParameters("twenty4To29F", "25 - 29 female", "gender=F|age=25-29", "12");
    ColumnParameters thirtyTo34F =
        new ColumnParameters("thirtyTo34F", "30 - 34 female", "gender=F|age=30-34", "13");
    ColumnParameters thirty5To39F =
        new ColumnParameters("thirty5To39F", "35 - 39 female", "gender=F|age=35-39", "14");
    ColumnParameters foutyTo44F =
        new ColumnParameters("foutyTo44F", "40 - 44 female", "gender=F|age=40-44", "15");
    ColumnParameters fouty5To49F =
        new ColumnParameters("fouty5To49F", "45 - 49 female", "gender=F|age=45-49", "16");
    ColumnParameters above50F =
        new ColumnParameters("above50F", "50+ female", "gender=F|age=50+", "17");
    ColumnParameters unknownF =
        new ColumnParameters("unknownF", "Unknown age female", "gender=F|age=UK", "18");
    ColumnParameters total = new ColumnParameters("totals", "Totals", "", "19");

    return Arrays.asList(
        fifteenTo19M,
        twentyTo24M,
        twenty5To29M,
        thirtyTo34M,
        thirty5To39M,
        foutyTo44M,
        fouty5To49M,
        above50M,
        unknownM,
        fifteenTo19F,
        twentyTo24F,
        twenty5To29F,
        thirtyTo34F,
        thirty5To39F,
        foutyTo44F,
        fouty5To49F,
        above50F,
        unknownF,
        total);
  }
}
