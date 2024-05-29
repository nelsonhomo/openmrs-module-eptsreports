package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListPatientsCurrentlyARTWithoutTBScreeningCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARWithoutTBScreeningDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  private ListPatientsCurrentlyARTWithoutTBScreeningCohortQueries
      patientsCurrentlyARTWithoutTBScreeningCohortQueries;

  public DataSetDefinition getPatientsOnArtWithoutTBScreenedList(List<Parameter> parameters) {

    return this.patientsCurrentlyARTWithoutTBScreeningCohortQueries
        .findPatientsWhoActiveOnARTAndNotHaveTBScreeningList(parameters);
  }

  public DataSetDefinition getTotalPatientsWhoOnTB5(List<Parameter> parameters) {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setParameters(parameters);

    final String mappings = "endDate=${endDate},location=${location}";

    final CohortDefinition TB5 =
        this.patientsCurrentlyARTWithoutTBScreeningCohortQueries
            .findPatientsWhoActiveOnARTAndNotHaveTBScreeningSummary();

    final CohortDefinition TB5_WITH_CLINICAL_CONSULTATION =
        this.patientsCurrentlyARTWithoutTBScreeningCohortQueries
            .findPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithOneConsultation();

    final CohortDefinition TB5_WITHOUT_CLINICAL_CONSULTATION =
        this.patientsCurrentlyARTWithoutTBScreeningCohortQueries
            .getPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithoutOneConsultation();

    dataSetDefinition.addColumn(
        "TOTAL",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
                EptsReportUtils.map(TB5, mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TOTALTBFC",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Com consulta clinica nos ultimos 6 meses",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose",
                EptsReportUtils.map(TB5_WITH_CLINICAL_CONSULTATION, mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TOTALTBNOTFC",
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Sem consulta clinica nos ultimos 6 meses",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Sem consulta clinica nos ultimos 6 meses",
                EptsReportUtils.map(TB5_WITHOUT_CLINICAL_CONSULTATION, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }
}
