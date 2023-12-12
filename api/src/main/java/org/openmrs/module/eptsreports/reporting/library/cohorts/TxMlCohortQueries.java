package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TXMLQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** All queries needed for TxMl report needed for EPTS project */
@Component
public class TxMlCohortQueries {

  private static final String FIND_PATIENTS_WHO_LOST_FOLLOWP =
      "TX_ML/PATIENTS_WHO_LOST_FOLLOWP.sql";

  private static final String FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT =
      "TX_ML/PATIENTS_WHO_WHERE_TRANSFERRED_OUT.sql";

  private static final String FIND_PATIENTS_WHO_ARE_DEAD = "TX_ML/PATIENTS_WHO_ARE_DEAD.sql";

  private static final String FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA =
      "TX_ML/PATIENTS_WHO_LOST_FOLLOWP_DATA.sql";

  @Autowired private GenericCohortQueries genericCohorts;

  private String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

  public CohortDefinition getPatientstotalIIT() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients who are IIT (Totals)");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    cd.addSearch("TX-ML", EptsReportUtils.map(this.getPatientsWhoMissedNextApointment(), mappings));
    cd.addSearch("DEAD", EptsReportUtils.map(this.getPatientsMarkedAsDead(), mappings));
    cd.addSearch(
        "TRANSFERREDOUT", EptsReportUtils.map(this.getPatientsWhoAreTransferedOut(), mappings));

    cd.setCompositionString("(TX-ML NOT (DEAD OR TRANSFERREDOUT)");
    return cd;
  }

  public CohortDefinition getPatientsWhoAreIITLessThan3Months() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients who are LTFU less than 3 months");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        String.format(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA), " < 90 ");

    cd.addSearch(
        "missedAppointmentIITLess3Month",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("Finding IIT patients < 90 days", query), mappings));

    cd.addSearch("IIT", EptsReportUtils.map(this.getPatientstotalIIT(), mappings));

    cd.setCompositionString("IIT AND missedAppointmentIITLess3Month");
    return cd;
  }

  public CohortDefinition getPatientsWhoAreIITGreaterOrEqual6Months() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients who are LTFU less than 6 months");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        String.format(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA), " >= 180 ");

    cd.addSearch(
        "missedAppointmentIITGreaterOrEqual6Month",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("Finding IIT patients >= 180 days", query), mappings));
    cd.addSearch("IIT", EptsReportUtils.map(this.getPatientstotalIIT(), mappings));

    cd.setCompositionString("IIT AND missedAppointmentIITGreaterOrEqual6Month");
    return cd;
  }

  public CohortDefinition getPatientsWhoAreIITBetween3And5Months() {
    String mapping = "startDate=${startDate},endDate=${endDate},location=${location}";
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("Get patients who are LTFU Greater than 3 months And Less Than 6 Months");
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA), " between 90 and 180 ");

    cd.addSearch(
        "missedAppointmentIITBetween3And5Months",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("Finding IIT patients between 90 and 180 days", query),
            mappings));
    cd.addSearch("IIT", EptsReportUtils.map(this.getPatientstotalIIT(), mappings));

    cd.setCompositionString("IIT AND missedAppointmentIITBetween3And5Months");
    return cd;
  }

  @DocumentedDefinition(value = "patientsWhoMissedNextApointment")
  public CohortDefinition getPatientsWhoMissedNextApointment() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("PatientsWhoMissedNextApointment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXML",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding patients who missed next appointment",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_LOST_FOLLOWP)),
            mappings));

    definition.setCompositionString("TXML");

    return definition;
  }

  @DocumentedDefinition(value = "patientsMarkedAsDead")
  public CohortDefinition getPatientsMarkedAsDead() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsMarkedAsDead");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXML", EptsReportUtils.map(this.getPatientsWhoMissedNextApointment(), mappings));

    definition.addSearch(
        "DEAD",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding patients who are marked as dead",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_DEAD)),
            mappings));

    definition.setCompositionString("TXML and DEAD");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoAreTransferedOut")
  public CohortDefinition getPatientsWhoAreTransferedOut() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsMarkedAsDead");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXML", EptsReportUtils.map(this.getPatientsWhoMissedNextApointment(), mappings));

    definition.addSearch(
        "TRANSFERREDOUT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding patients who are transferred out",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_WHERE_TRANSFERRED_OUT)),
            mappings));

    definition.setCompositionString("TRANSFERREDOUT AND TXML");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWhoRefusedStoppedTreatment")
  public CohortDefinition getPatientsWhoRefusedOrStoppedTreatment() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("patientsWhoRefusedStoppedTreatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "TXML", EptsReportUtils.map(this.getPatientsWhoMissedNextApointment(), mappings));

    definition.addSearch(
        "REFUSEDTREATMENT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding patients who stopped/suspended treatment ",
                TXMLQueriesInterface.QUERY
                    .findPatiensWhoStoppedOrRefusedTreatmentByTheEndOfReportingDate),
            mappings));

    definition.setCompositionString("(TXML and REFUSEDTREATMENT) NOT REFUSEDTREATMENT");
    return definition;
  }
}
