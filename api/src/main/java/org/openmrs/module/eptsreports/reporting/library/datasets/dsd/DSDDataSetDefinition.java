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
package org.openmrs.module.eptsreports.reporting.library.datasets.dsd;

import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ADULT;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIVE_TO_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TEN_TO_FOURTEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWO_TO_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.UNDER_TWO;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DSDCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.dimensions.DSDCommonDimensions;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.AgeRange;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DSDDataSetDefinition extends BaseDataSet {

  private EptsCommonDimension eptsCommonDimension;

  private EptsGeneralIndicator eptsGeneralIndicator;

  private DSDCommonDimensions dsdCommonDimensions;

  private DSDCohortQueries dsdCohortQueries;

  @Autowired
  public DSDDataSetDefinition(
      EptsCommonDimension eptsCommonDimension,
      EptsGeneralIndicator eptsGeneralIndicator,
      DSDCohortQueries dsdCohortQueries,
      DSDCommonDimensions dsdCommonDimensions) {
    this.eptsCommonDimension = eptsCommonDimension;
    this.eptsGeneralIndicator = eptsGeneralIndicator;
    this.dsdCohortQueries = dsdCohortQueries;
    this.dsdCommonDimensions = dsdCommonDimensions;
  }

  public DataSetDefinition constructDSDDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    dsd.setName("DSD Dataset");
    dsd.addParameters(getParameters());
    final String mappings = "endDate=${endDate},location=${location}";

    boolean addWonaState = true;

    this.addAgeDimensions(dsd, UNDER_TWO, TWO_TO_FOUR, FIVE_TO_NINE, TEN_TO_FOURTEEN, ADULT);
    dsd.addDimension(
        "state", EptsReportUtils.map(this.dsdCommonDimensions.getDimensions(), mappings));

    this.addColumns(
        "D1",
        "D1: Number of active patients on ART, Non-Pregnant and Non-Breastfeeding and not on TB Treatment, Eligible for DSD for Stable Patients",
        dsd,
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "patientsActiveOnArtEligibleForDsd",
                EptsReportUtils.map(dsdCohortQueries.getDSDDenominator1(), mappings)),
            mappings),
        !addWonaState,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "D2",
        "D2: Number of active patients on ART Not Eligible for DSD for Stable Patients Number of Patients Number of  active patients on ART (Non-pregnant and Non-Breastfeeding not on TB treatment) Not Eligible  for DSD (Unstable)",
        dsd,
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "patientsActiveOnArtEligibleForDsd",
                EptsReportUtils.map(dsdCohortQueries.getDSDDenominator2(), mappings)),
            mappings),
        addWonaState,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    this.addColumns(
        "D3",
        "D3: Número de Pacientes Activos em TARV",
        dsd,
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "patientsActiveOnArtEligibleForDsd",
                EptsReportUtils.map(dsdCohortQueries.getDSDDenominator3(), mappings)),
            mappings),
        addWonaState,
        UNDER_TWO,
        TWO_TO_FOUR,
        FIVE_TO_NINE,
        TEN_TO_FOURTEEN,
        ADULT);

    return dsd;
  }

  private void addAgeDimensions(
      final CohortIndicatorDataSetDefinition definition, final AgeRange... ranges) {

    for (final AgeRange range : ranges) {
      definition.addDimension(
          range.getName(),
          EptsReportUtils.map(
              this.eptsCommonDimension.findPatientsByRange(range.getName(), range),
              "endDate=${endDate}"));
    }
  }

  private void addColumns(
      final String name,
      final String label,
      final CohortIndicatorDataSetDefinition definition,
      final Mapped<CohortIndicator> indicator,
      boolean addWomanState,
      final AgeRange... ranges) {

    definition.addColumn(name, label, indicator, "");

    for (final AgeRange range : ranges) {

      String columnName = getColumnNameByRange(name, range);

      definition.addColumn(
          columnName,
          name + " - (" + range.getName() + ")",
          indicator,
          range.getName() + "=" + range.getName());
    }

    if (addWomanState) {
      definition.addColumn(name + "-pregnant", label + "- Pergnant", indicator, "state=PREGNANT");
      definition.addColumn(
          name + "-breastfeeding", label + "- breastfeeding", indicator, "state=BREASTFEEDING");
    }
  }

  private String getColumnNameByRange(String columnNamePrefix, AgeRange range) {
    StringBuilder sb = new StringBuilder(columnNamePrefix);
    sb.append("-");
    sb.append(range.getName());
    return sb.toString();
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
