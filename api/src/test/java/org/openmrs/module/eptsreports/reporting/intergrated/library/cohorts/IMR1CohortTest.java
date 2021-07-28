package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.IMR1BCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.IMR1CohortQueries;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

/** Nesta classe todos testes não executam, excepto o último */
public class IMR1CohortTest extends DefinitionsTest {

  @Autowired private IMR1CohortQueries imr1CohortQueries;

  @Autowired private IMR1BCohortQueries imr1BCohortQueries;

  @Before
  public void initialise() throws Exception {
    executeDataSet("imr-patient-dataset.xml");
    executeDataSet("imr-encounter-dataset.xml");
    executeDataSet("imr-concepts-dataset.xml");
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Test
  public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1BCohortQueries.getPatientsNewlyEnrolledOnArtCare();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Test
  public void shouldGetPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastfeedingDenominator()
      throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B =
        imr1BCohortQueries
            .getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastfeedingDenominator();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Test
  public void shouldGetPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreasFeedingNumerator()
      throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B =
        imr1BCohortQueries
            .getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreasFeedingNumerator();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Ignore
  @Test
  public void shouldGetPatientsNewlyEnrolledOnArtWhoInitiatedArtTreatment()
      throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B =
        imr1BCohortQueries.getPatientsNewlyEnrolledOnArtWhoInitiatedArtTreatment();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Ignore
  @Test
  public void shouldGetAllPatientsEnrolledInArtCareDuringReportingPeriod()
      throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2019, 01, 01);
    final Date endDate = DateUtil.getDateTime(2020, 01, 01);

    Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), getLocation());

    CohortDefinition cohortDefinition =
        this.imr1CohortQueries.getAllPatientsEnrolledInArtCareDuringReportingPeriod();

    final EvaluatedCohort evaluateCohortDefinition =
        evaluateCohortDefinition(cohortDefinition, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

    assertEquals(2, evaluateCohortDefinition.getMemberIds().size());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Ignore
  @Test
  public void shouldGetPatientsNewlyEnrolledOnArtCare() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1CohortQueries.getPatientsNewlyEnrolledOnArtCare();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque não reconhece o uso do INTERVAL no MYSQL
  @Ignore
  @Test
  public void shouldGetPatientsNewlyEnrolledOnArtCareNumerator() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1CohortQueries.getPatientsNewlyEnrolledOnArtCareNumerator();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque FUNCTION IF do MYSQL não é reconhecida pelo H2
  @Ignore
  @Test
  public void shouldGetAllPatientsWhoAreBreastfeeding() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1CohortQueries.getAllPatientsWhoAreBreastfeeding();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  // TODO O teste não corre porque FUNCTION IF do MYSQL não é reconhecida pelo H2
  @Ignore
  @Test
  public void shouldGetAllPatientsWhoArePregnantInAPeriod() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1CohortQueries.getAllPatientsWhoArePregnantInAPeriod();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());
  }

  @Test
  public void shouldGetAllPatientsTransferredInByEndReportingDate() throws EvaluationException {

    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2021, 01, 01);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

    CohortDefinition i1B = imr1CohortQueries.getAllPatientsTransferredInByEndReportingDate();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

    assertEquals(1, evaluateCohortDefinition.getMemberIds().size());
  }
}
