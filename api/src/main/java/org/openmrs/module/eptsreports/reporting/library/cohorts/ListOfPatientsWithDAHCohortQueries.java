package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.ListOfPatientsWithDAHQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientsWithDAHCohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  @DocumentedDefinition(value = "patientsInMDSDuringPeriod")
  public CohortDefinition getPatientsInMDSDuringPeriod() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    compositionDefinition.setName("Total de utentes em MDS de DAH");
    compositionDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    compositionDefinition.addSearch(
        "MDS-IN-PERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsInMDSOfDAHDuringPeriodTotal",
                ListOfPatientsWithDAHQueries.QUERY.findPatientsInMDSOfDAHDuringPeriodTotal),
            mappings));

    compositionDefinition.addSearch(
        "MDS-BEFORE-PERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsInMDSOfDAHBeforePeriodTotal",
                ListOfPatientsWithDAHQueries.QUERY.findPatientsInMDSOfDAHBeforePeriodTotal),
            mappings));

    compositionDefinition.addSearch(
        "TRANSFERRED-OUT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsTransferredOut",
                ListOfPatientsWithDAHQueries.QUERY.findPatientsTransferredOut),
            mappings));

    compositionDefinition.setCompositionString(
        "MDS-IN-PERIOD NOT (MDS-BEFORE-PERIOD OR TRANSFERRED-OUT)");

    return compositionDefinition;
  }
}
