package org.openmrs.module.eptsreports.reporting.library.datasets.resumo;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;
import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalDAHCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.disaggregations.ResumoMensalDAHDisaggregations;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalDAHDataSetDefinition extends BaseDataSet {

  private EptsCommonDimension eptsCommonDimension;

  private EptsGeneralIndicator eptsGeneralIndicator;

  private ResumoMensalDAHCohortQueries resumoMensalDAHCohortQueries;

  private ResumoMensalDAHDisaggregations resumoMensalDAHDisaggregations;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired
  public ResumoMensalDAHDataSetDefinition(
      EptsCommonDimension eptsCommonDimension,
      EptsGeneralIndicator eptsGeneralIndicator,
      ResumoMensalDAHCohortQueries resumoMensalDAHCohortQueries,
      ResumoMensalDAHDisaggregations resumoMensalDAHDisaggregations) {
    this.eptsCommonDimension = eptsCommonDimension;
    this.eptsGeneralIndicator = eptsGeneralIndicator;
  }

  public DataSetDefinition constructResumoMensalDAHDataSet() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    dsd.setName("Resumo Mensal DAH");
    dsd.addParameters(getParameters());
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addDimension("gender", map(eptsCommonDimension.gender(), ""));
    dsd.addDimension(
        "age", map(eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    addRow(
        dsd,
        "DAHI0",
        "Total de pacientes activos em DAH",
        getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0(),
        resumoMensalDAHDisaggregations.getUnder14YearsColumns());

    return dsd;
  }

  private Mapped<CohortIndicator>
      getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0() {
    return mapStraightThrough(
        eptsGeneralIndicator.getIndicator(
            "I0",
            mapStraightThrough(
                resumoMensalDAHCohortQueries
                    .getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicatorI0())));
  }
}
