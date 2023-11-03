/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.eptsreports.reporting.library.datasets;

import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ABOVE_FIFTY;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ABOVE_SIXTY_FIVE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIFTEEN_TO_NINETEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIFTY_FIVE_TO_FIFTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIFTY_TO_FIFTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIVE_TO_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_FIVE_TO_FORTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_TO_FORTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ONE_TO_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.SIXTY_TO_SIXTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TEN_TO_FOURTEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_FIVE_TO_THIRTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_TO_THRITY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_FIVE_TO_TWENTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_TO_TWENTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.UNDER_ONE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.UNKNOWN;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxNewCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.KeyPopulationDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.AgeRange;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.Gender;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TxNewDataset extends BaseDataSet {

  @Autowired private TxNewCohortQueries txNewCohortQueries;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  @Qualifier("txNewAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private KeyPopulationDimension keyPopulationDimension;

  public DataSetDefinition constructTxNewDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("TX_NEW Data Set");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition patientEnrolledInART =
        this.txNewCohortQueries.getTxNewCompositionCohort("patientEnrolledInART");

    final CohortIndicator patientEnrolledInHIVStartedARTIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "patientNewlyEnrolledInHIVIndicator",
            EptsReportUtils.map(patientEnrolledInART, mappings));

    final CohortDefinition patientWithLessCD4Definition =
        this.txNewCohortQueries.findPatientsWithCD4LessThan200();

    final CohortIndicator patientWithLessCD4Indicator =
        this.eptsGeneralIndicator.getIndicator(
            "patientWithLessCD4Indicator",
            EptsReportUtils.map(patientWithLessCD4Definition, mappings));

    final CohortDefinition patientWithGreaterCD4Definition =
        this.txNewCohortQueries.findPatientsWIthCD4GreaterOrEqual200();

    final CohortIndicator patientWithGreaterCD4DIndicatior =
        this.eptsGeneralIndicator.getIndicator(
            "patientWithGreaterCD4DIndicatior",
            EptsReportUtils.map(patientWithGreaterCD4Definition, mappings));

    final CohortDefinition patientsWithUnknownCD4Definition =
        this.txNewCohortQueries.findPatientsWithUnknownCD4();

    final CohortIndicator patientsWithUnknownCD4Indicator =
        this.eptsGeneralIndicator.getIndicator(
            "patientsWithUnknownCD4Definition",
            EptsReportUtils.map(patientsWithUnknownCD4Definition, mappings));

    dataSetDefinition.addDimension(
        "breastfeeding",
        EptsReportUtils.map(this.eptsCommonDimension.findPatientsWhoAreBreastfeeding(), mappings));

    this.addDimensions(
        dataSetDefinition,
        mappings,
        Arrays.asList("N", "LCD4", "GCD4", "UCD4"),
        UNDER_ONE,
        ONE_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY,
        FIFTY_TO_FIFTY_FOUR,
        FIFTY_FIVE_TO_FIFTY_NINE,
        SIXTY_TO_SIXTY_FOUR,
        ABOVE_SIXTY_FIVE);

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    addAGenderDimensionForUnkwonAgeDimension(
        dataSetDefinition, Arrays.asList("N", "LCD4", "GCD4", "UCD4"));

    dataSetDefinition.addDimension(
        "homosexual",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreHomosexual(), mappings));

    dataSetDefinition.addDimension(
        "drug-user",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoUseDrugs(), mappings));

    dataSetDefinition.addDimension(
        "prisioner",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreInPrison(), mappings));

    dataSetDefinition.addDimension(
        "sex-worker",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreSexWorker(), mappings));

    dataSetDefinition.addColumn(
        "1All",
        "TX_NEW: New on ART",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "");

    dataSetDefinition.addColumn(
        "LCD4",
        "TX_NEW: CD4 Less than 200",
        EptsReportUtils.map(patientWithLessCD4Indicator, mappings),
        "");

    dataSetDefinition.addColumn(
        "GCD4",
        "TX_NEW: CD4 greater or equal 200",
        EptsReportUtils.map(patientWithGreaterCD4DIndicatior, mappings),
        "");

    dataSetDefinition.addColumn(
        "UCD4",
        "TX_NEW: Unknown CD4",
        EptsReportUtils.map(patientsWithUnknownCD4Indicator, mappings),
        "");

    dataSetDefinition.addColumn(
        "ANC",
        "TX_NEW: Breastfeeding Started ART",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "breastfeeding=breastfeeding");

    this.addColums(
        dataSetDefinition,
        mappings,
        "LCD4",
        patientWithLessCD4Indicator,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY,
        FIFTY_TO_FIFTY_FOUR,
        FIFTY_FIVE_TO_FIFTY_NINE,
        SIXTY_TO_SIXTY_FOUR,
        ABOVE_SIXTY_FIVE);

    this.addColums(
        dataSetDefinition,
        mappings,
        "GCD4",
        patientWithGreaterCD4DIndicatior,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY,
        FIFTY_TO_FIFTY_FOUR,
        FIFTY_FIVE_TO_FIFTY_NINE,
        SIXTY_TO_SIXTY_FOUR,
        ABOVE_SIXTY_FIVE);

    this.addColums(
        dataSetDefinition,
        mappings,
        "UCD4",
        patientsWithUnknownCD4Indicator,
        UNDER_ONE,
        ONE_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY,
        FIFTY_TO_FIFTY_FOUR,
        FIFTY_FIVE_TO_FIFTY_NINE,
        SIXTY_TO_SIXTY_FOUR,
        ABOVE_SIXTY_FIVE);

    this.addColums(dataSetDefinition, "", "N", patientEnrolledInHIVStartedARTIndicator, UNKNOWN);

    this.addColums(dataSetDefinition, "", "LCD4", patientWithLessCD4Indicator, UNKNOWN);

    this.addColums(dataSetDefinition, "", "GCD4", patientWithGreaterCD4DIndicatior, UNKNOWN);

    this.addColums(dataSetDefinition, "", "UCD4", patientsWithUnknownCD4Indicator, UNKNOWN);

    dataSetDefinition.addColumn(
        "N-MSM",
        "Homosexual",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "gender=M|homosexual=homosexual");

    dataSetDefinition.addColumn(
        "N-PWID",
        "Drugs User",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "drug-user=drug-user");

    dataSetDefinition.addColumn(
        "N-PRI",
        "Prisioners",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "prisioner=prisioner");

    dataSetDefinition.addColumn(
        "N-FSW",
        "Sex Worker",
        EptsReportUtils.map(patientEnrolledInHIVStartedARTIndicator, mappings),
        "gender=F|sex-worker=sex-worker");

    return dataSetDefinition;
  }

  private void addAGenderDimensionForUnkwonAgeDimension(
      CohortIndicatorDataSetDefinition dataSetDefinition, List<String> columnPrefixs) {

    for (String columnPrefix : columnPrefixs) {

      dataSetDefinition.addDimension(
          this.getColumnName(columnPrefix, AgeRange.UNKNOWN, Gender.MALE),
          EptsReportUtils.map(
              this.eptsCommonDimension.findPatientsWithUnknownAgeByGender(
                  this.getColumnName(columnPrefix, AgeRange.UNKNOWN, Gender.MALE), Gender.MALE),
              ""));

      dataSetDefinition.addDimension(
          this.getColumnName(columnPrefix, AgeRange.UNKNOWN, Gender.FEMALE),
          EptsReportUtils.map(
              this.eptsCommonDimension.findPatientsWithUnknownAgeByGender(
                  this.getColumnName(columnPrefix, AgeRange.UNKNOWN, Gender.FEMALE), Gender.FEMALE),
              ""));
    }
  }

  private void addColums(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      final String mappings,
      String columnPrefix,
      CohortIndicator cohortIndicator,
      final AgeRange... rannges) {

    for (final AgeRange range : rannges) {

      final String maleName = this.getColumnName(columnPrefix, range, Gender.MALE);
      final String femaleName = this.getColumnName(columnPrefix, range, Gender.FEMALE);

      dataSetDefinition.addColumn(
          maleName,
          maleName.replace("-", " "),
          EptsReportUtils.map(cohortIndicator, mappings),
          maleName + "=" + maleName);

      dataSetDefinition.addColumn(
          femaleName,
          femaleName.replace("-", " "),
          EptsReportUtils.map(cohortIndicator, mappings),
          femaleName + "=" + femaleName);
    }
  }

  private void addDimensions(
      final CohortIndicatorDataSetDefinition cohortIndicatorDataSetDefinition,
      final String mappings,
      List<String> columnPrefixs,
      final AgeRange... ranges) {

    for (final AgeRange range : ranges) {
      for (String columnPrefix : columnPrefixs) {

        cohortIndicatorDataSetDefinition.addDimension(
            this.getColumnName(columnPrefix, range, Gender.MALE),
            EptsReportUtils.map(
                this.eptsCommonDimension.findPatientsWhoAreNewlyEnrolledOnArtByAgeAndGender(
                    this.getColumnName(columnPrefix, range, Gender.MALE),
                    range,
                    Gender.MALE.getName()),
                mappings));

        cohortIndicatorDataSetDefinition.addDimension(
            this.getColumnName(columnPrefix, range, Gender.FEMALE),
            EptsReportUtils.map(
                this.eptsCommonDimension.findPatientsWhoAreNewlyEnrolledOnArtByAgeAndGender(
                    this.getColumnName(columnPrefix, range, Gender.FEMALE),
                    range,
                    Gender.FEMALE.getName()),
                mappings));
      }
    }
  }

  private String getColumnName(String columnPrefix, AgeRange range, Gender gender) {
    return range.getDesagregationColumnName(columnPrefix, gender);
  }
}
