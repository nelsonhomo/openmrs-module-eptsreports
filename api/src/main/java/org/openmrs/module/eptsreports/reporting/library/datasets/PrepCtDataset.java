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
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIFTEEN_TO_NINETEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_FIVE_TO_FORTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_TO_FORTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_FIVE_TO_THIRTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_TO_THRITY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_FIVE_TO_TWENTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_TO_TWENTY_FOUR;

import org.openmrs.module.eptsreports.reporting.library.cohorts.PrepCtCohortQueries;
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
public class PrepCtDataset extends BaseDataSet {

  @Autowired private PrepCtCohortQueries prepCtCohortQueries;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private KeyPopulationDimension keyPopulationDimension;

  public DataSetDefinition constructPrepCtDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("PREP_CT Data Set");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition clientsNewlyEnrolledInPrep =
        this.prepCtCohortQueries.getClientsNewlyEnrolledInPrep();

    final CohortIndicator clientsNewlyEnrolledInPrepIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "clientsNewlyEnrolledInPrepIndicator",
            EptsReportUtils.map(clientsNewlyEnrolledInPrep, mappings));

    this.addDimensions(
        dataSetDefinition,
        mappings,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY);

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        this.getColumnName(AgeRange.UNKNOWN, Gender.MALE),
        EptsReportUtils.map(
            this.eptsCommonDimension.findPatientsWithUnknownAgeByGender(
                this.getColumnName(AgeRange.UNKNOWN, Gender.MALE), Gender.MALE),
            ""));

    dataSetDefinition.addDimension(
        this.getColumnName(AgeRange.UNKNOWN, Gender.FEMALE),
        EptsReportUtils.map(
            this.eptsCommonDimension.findPatientsWithUnknownAgeByGender(
                this.getColumnName(AgeRange.UNKNOWN, Gender.FEMALE), Gender.FEMALE),
            ""));

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
        "PREP-C-All",
        "PREP_CT: PREP CT",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        "");

    this.addColums(
        dataSetDefinition,
        mappings,
        clientsNewlyEnrolledInPrepIndicator,
        FIFTEEN_TO_NINETEEN,
        TWENTY_TO_TWENTY_FOUR,
        TWENTY_FIVE_TO_TWENTY_NINE,
        THIRTY_TO_THRITY_FOUR,
        THIRTY_FIVE_TO_THIRTY_NINE,
        FORTY_TO_FORTY_FOUR,
        FORTY_FIVE_TO_FORTY_NINE,
        ABOVE_FIFTY);

    dataSetDefinition.addColumn(
        "PREP-C-males-unknownM",
        "unknownM",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        this.getColumnName(AgeRange.UNKNOWN, Gender.MALE)
            + "="
            + this.getColumnName(AgeRange.UNKNOWN, Gender.MALE));

    dataSetDefinition.addColumn(
        "PREP-C-females-unknownF",
        "unknownF",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        this.getColumnName(AgeRange.UNKNOWN, Gender.FEMALE)
            + "="
            + this.getColumnName(AgeRange.UNKNOWN, Gender.FEMALE));

    dataSetDefinition.addColumn(
        "PREP-C-MSM",
        "Homosexual",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        "gender=M|homosexual=homosexual");

    dataSetDefinition.addColumn(
        "PREP-C-PWID",
        "Drugs User",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        "drug-user=drug-user");

    dataSetDefinition.addColumn(
        "PREP-C-PRI",
        "Prisioners",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        "prisioner=prisioner");

    dataSetDefinition.addColumn(
        "PREP-C-FSW",
        "Sex Worker",
        EptsReportUtils.map(clientsNewlyEnrolledInPrepIndicator, mappings),
        "gender=F|sex-worker=sex-worker");

    return dataSetDefinition;
  }

  private void addColums(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      final String mappings,
      final CohortIndicator cohortIndicator,
      final AgeRange... rannges) {

    for (final AgeRange range : rannges) {

      final String maleName = this.getColumnName(range, Gender.MALE);
      final String femaleName = this.getColumnName(range, Gender.FEMALE);

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
      final AgeRange... ranges) {

    for (final AgeRange range : ranges) {

      cohortIndicatorDataSetDefinition.addDimension(
          this.getColumnName(range, Gender.MALE),
          EptsReportUtils.map(
              this.eptsCommonDimension.findClientsWhoAreNewlyEnrolledInPrepByGenderAndAgeRange(
                  this.getColumnName(range, Gender.MALE), Gender.MALE.getName(), range),
              mappings));

      cohortIndicatorDataSetDefinition.addDimension(
          this.getColumnName(range, Gender.FEMALE),
          EptsReportUtils.map(
              this.eptsCommonDimension.findClientsWhoAreNewlyEnrolledInPrepByGenderAndAgeRange(
                  this.getColumnName(range, Gender.FEMALE), Gender.FEMALE.getName(), range),
              mappings));
    }
  }

  private String getColumnName(AgeRange range, Gender gender) {
    return range.getDesagregationColumnName("PREP-C", gender);
  }
}
