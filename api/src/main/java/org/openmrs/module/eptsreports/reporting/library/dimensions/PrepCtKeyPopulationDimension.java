/** */
package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.KeyPopType;
import org.openmrs.module.eptsreports.reporting.library.queries.PrepCtKeyPopQuery;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrepCtKeyPopulationDimension {

  @Autowired GenericCohortQueries genericCohortQueries;

  public CohortDefinitionDimension findPatientsWhoAreHomosexual() {

    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("HOMOSEXUAL Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "homosexual",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "homosexual", PrepCtKeyPopQuery.findPatientsWhoAreKeyPop(KeyPopType.HOMOSEXUAL)),
            mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoUseDrugs() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Drugs User Patients");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "drug-user",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "drug-user", PrepCtKeyPopQuery.findPatientsWhoAreKeyPop(KeyPopType.DRUGUSER)),
            mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoAreInPrison() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Patients in prision");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "prisioner",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "prisioner", PrepCtKeyPopQuery.findPatientsWhoAreKeyPop(KeyPopType.PRISIONER)),
            mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoAreSexWorker() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Sex worker patients");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "sex-worker",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "sex-worker", PrepCtKeyPopQuery.findPatientsWhoAreKeyPop(KeyPopType.SEXWORKER)),
            mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoAreTransGender() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Transgender patients");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "transgender",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "transgender", PrepCtKeyPopQuery.findPatientsWhoAreKeyPop(KeyPopType.TRANSGENDER)),
            mappings));

    return dimension;
  }
}
