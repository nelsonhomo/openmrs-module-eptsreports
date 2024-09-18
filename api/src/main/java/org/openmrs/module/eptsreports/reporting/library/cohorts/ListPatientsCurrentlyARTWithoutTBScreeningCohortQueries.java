package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARTWithoutTBScreeningCohortQueries {

  private static final String TB5_SUMMARY =
      "TB5/PatientsCurrentlyARTWithoutTBScreening_Summary.sql";
  private static final String TB5_LIST = "TB5/PatientsCurrentlyARTWithoutTBScreening_List.sql";

  @Autowired private GenericCohortQueries genericCohorts;

  public CohortDefinition findPatientsWhoActiveOnARTAndNotHaveTBScreeningSummary() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose - Total");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(TB5_SUMMARY);

    definition.setQuery(query);

    return definition;
  }

  public DataSetDefinition findPatientsWhoActiveOnARTAndNotHaveTBScreeningList(
      List<Parameter> parameters) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose - Lista");
    dsd.addParameters(parameters);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(TB5_LIST));

    return dsd;
  }

  public CohortDefinition
      findPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithOneConsultation() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName(
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Com Consulta Clinica");
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    cd.addSearch(
        "TB5",
        EptsReportUtils.map(
            this.findPatientsWhoActiveOnARTAndNotHaveTBScreeningSummary(), mappings));

    String query =
        "select p.patient_id                                          						"
            + "	from patient p                                                      					"
            + "		inner join encounter e on e.patient_id=p.patient_id                 				"
            + "	where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) 							"
            + "		and e.location_id=:location     													"
            + "		and e.encounter_datetime between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate	";

    cd.addSearch(
        "CONSULTATIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("Finding clinical consultations", query), mappings));

    cd.setCompositionString("TB5 AND CONSULTATIONS");

    return cd;
  }

  public CohortDefinition
      getPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithoutOneConsultation() {
    CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName(
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Sem Consulta Clinica");
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    cd.addSearch(
        "TB5",
        EptsReportUtils.map(
            this.findPatientsWhoActiveOnARTAndNotHaveTBScreeningSummary(), mappings));

    String query =
        "select p.patient_id                                          						"
            + "	from patient p                                                      					"
            + "		inner join encounter e on e.patient_id=p.patient_id                 				"
            + "	where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) 							"
            + "		and e.location_id=:location     													"
            + "		and e.encounter_datetime between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate	";

    cd.addSearch(
        "CONSULTATIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("Finding clinical consultations", query), mappings));

    cd.setCompositionString("TB5 NOT CONSULTATIONS");
    return cd;
  }
}
