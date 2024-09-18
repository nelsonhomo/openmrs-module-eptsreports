package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.Criteria;
import org.openmrs.module.eptsreports.reporting.library.queries.ListOfPatientsElegibleToCD4RequestQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListOfPatientsElegibleToCD4RequestCohortQueries {

  @DocumentedDefinition(value = "C1")
  public CohortDefinition C1() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C1));

    return definition;
  }

  @DocumentedDefinition(value = "C2")
  public CohortDefinition C2() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C2));

    return definition;
  }

  @DocumentedDefinition(value = "C3")
  public CohortDefinition C3() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C3));

    return definition;
  }

  @DocumentedDefinition(value = "C4")
  public CohortDefinition C4() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C4));

    return definition;
  }

  @DocumentedDefinition(value = "C5")
  public CohortDefinition C5() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C5");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C5));

    return definition;
  }

  @DocumentedDefinition(value = "C6")
  public CohortDefinition C6() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("C6");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        ListOfPatientsElegibleToCD4RequestQueries.findPatientsWhoAreHaveCd4Request(Criteria.C6));

    return definition;
  }
}
