package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Date;
import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientsWithDAHDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  private static final String FIND_PATIENTS_WITH_DAH_LIST =
      "LIST_PATIENTS_WITH_DAH/PATIENTS_WITH_DAH_LIST.sql";
  private static final String FIND_PATIENTS_WITH_DAH_TOTAL =
      "LIST_PATIENTS_WITH_DAH/PATIENTS_WITH_DAH_TOTAL.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Find list of patients with DAH");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_DAH_LIST));
    return dsd;
  }

  public DataSetDefinition getTotalOfPatietsWithDAHDataset() {
    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Patients with DAH Total");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final CohortDefinition dahTotal = this.findPatientsWithDAHTotal();
    dataSetDefinition.addColumn(
        "TOTALWITHDAH",
        "Total de Pacientes com DAH",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "dahTotal", EptsReportUtils.map(dahTotal, mappings)),
            mappings),
        "");

    return dataSetDefinition;
  }

  @DocumentedDefinition(value = "findPatientsWithDAHTotal")
  private CohortDefinition findPatientsWithDAHTotal() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWithDAHTotal");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_DAH_TOTAL));

    return definition;
  }
}
