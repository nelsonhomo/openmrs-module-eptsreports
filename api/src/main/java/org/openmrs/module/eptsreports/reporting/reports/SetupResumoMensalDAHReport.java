package org.openmrs.module.eptsreports.reporting.reports;

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.LocationDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ResumoMensalDAHDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;

public class SetupResumoMensalDAHReport extends EptsPeriodIndicatorDataExportManager {

  @Autowired private ResumoMensalDAHDataSetDefinition resumoMensalDAHDataSetDefinition;

  @Autowired protected GenericCohortQueries genericCohortQueries;

  @Autowired
  public SetupResumoMensalDAHReport(
      ResumoMensalDAHDataSetDefinition resumoMensalDAHDataSetDefinition) {
    this.resumoMensalDAHDataSetDefinition = resumoMensalDAHDataSetDefinition;
  }

  @Override
  public String getExcelDesignUuid() {
    return "eb8e2380-8d14-11ee-95c2-4365f9ce2f2e";
  }

  @Override
  public String getUuid() {
    return "f46052f8-8d14-11ee-8b82-6be2aa180891";
  }

  @Override
  public String getName() {
    return "Resumo Mensal de DAH";
  }

  @Override
  public String getDescription() {
    return "Este relatório apresenta os dados do resumo mensal da Unidade Sanitária para a doença avançada por HIV, provenientes da ferramenta Ficha de DAH e Ficha Mestra no sistema.";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition rd =
        SetupResumoMensalReport.getDefaultPeriodIndicatorReportDefinition();

    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(resumoMensalDAHDataSetDefinition.getParameters());

    rd.addDataSetDefinition("HF", mapStraightThrough(new LocationDataSetDefinition()));
    rd.addDataSetDefinition(
        "R",
        mapStraightThrough(resumoMensalDAHDataSetDefinition.constructResumoMensalDAHDataSet()));

    rd.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));
    return rd;
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "Relatorio_Mensal_DAH.xls",
              "Resumo Mensal de DAH Report",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public static PeriodIndicatorReportDefinition getDefaultPeriodIndicatorReportDefinition() {
    PeriodIndicatorReportDefinition rd = new PeriodIndicatorReportDefinition();
    rd.removeParameter(ReportingConstants.START_DATE_PARAMETER);
    rd.removeParameter(ReportingConstants.END_DATE_PARAMETER);
    rd.removeParameter(ReportingConstants.LOCATION_PARAMETER);

    return rd;
  }
}
