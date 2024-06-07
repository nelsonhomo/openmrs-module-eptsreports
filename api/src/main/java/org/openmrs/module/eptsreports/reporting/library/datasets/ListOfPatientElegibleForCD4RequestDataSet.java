package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListOfPatientsElegibleToCD4RequestCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientElegibleForCD4RequestDataSet extends BaseDataSet {

  private static final String LIST_OF_PATIENTS_ELEGIBLE_FOR_CD4_REQUEST =
      "ELEGIBLECD4/LIST_OF_PATIENTS_ELEGIBLE_FOR_CD4_REQUEST.sql";
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  ListOfPatientsElegibleToCD4RequestCohortQueries ListOfPatientsElegibleToCD4RequestCohortQueries;

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("CD");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(LIST_OF_PATIENTS_ELEGIBLE_FOR_CD4_REQUEST));
    return dsd;
  }

  public DataSetDefinition getTotalPatientElegibleForCD4RequestDataSet() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("CD4");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "endDate=${endDate},location=${location}";
    final CohortDefinition C1 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC1();
    final CohortDefinition C2 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC2();
    final CohortDefinition C3 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC3();
    final CohortDefinition C4 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC4();
    final CohortDefinition C5 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC5();
    final CohortDefinition C6 = ListOfPatientsElegibleToCD4RequestCohortQueries.getTotaC6();
    dataSetDefinition.addColumn(
        "C1",
        "C1",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C1", EptsReportUtils.map(C1, mappings)),
            mappings),
        "");
    dataSetDefinition.addColumn(
        "C2",
        "C2",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C2", EptsReportUtils.map(C2, mappings)),
            mappings),
        "");
    dataSetDefinition.addColumn(
        "C3",
        "C3",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C3", EptsReportUtils.map(C3, mappings)),
            mappings),
        "");
    dataSetDefinition.addColumn(
        "C4",
        "C4",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C4", EptsReportUtils.map(C4, mappings)),
            mappings),
        "");
    dataSetDefinition.addColumn(
        "C5",
        "C5",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C5", EptsReportUtils.map(C5, mappings)),
            mappings),
        "");
    dataSetDefinition.addColumn(
        "C6",
        "C6",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator("C6", EptsReportUtils.map(C6, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }
}
