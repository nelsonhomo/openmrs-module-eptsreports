package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARTNoTBScreeningCohortQueries {

  private static final String TB5 = "TB5/ListPatientsCurrentlyARTNoTBScreeningSummary.sql";

  public CohortDefinition findPatientsWhoActiveOnARTAndNotHaveTBScreening() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(TB5);

    definition.setQuery(query);

    return definition;
  }
}
