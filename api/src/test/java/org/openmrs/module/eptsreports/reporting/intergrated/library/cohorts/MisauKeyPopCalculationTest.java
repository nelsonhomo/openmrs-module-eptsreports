package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.MisauKeyPopReportCohortQueries;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Nesta classe apenas dois testes executam com sucesso, o restante não passam
 * devido ao erro deste
 * MisauKeyPopReportQueries.findPatientsCurrentlyOnTarvTarvMisauDefinition(DATE_ADD)
 * que usa a function DATE_ADD
 */
public class MisauKeyPopCalculationTest extends DefinitionsTest {

	@Autowired
	private MisauKeyPopReportCohortQueries misauKeyPopReportCohortQueries;

	@Before
	public void initialise() throws Exception {
		executeDataSet("misau-keypop-patients-dataset.xml");
		executeDataSet("misau-keypop-encounter-dataset.xml");
		executeDataSet("misau-keypop-obs-dataset.xml");
	}

	@Test
	public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries.getPatientsCoort12StartArt();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	// TODO Este teste não corre porque o H2 não é compativel com a função DATE_ADD
	// usado na query
	@Ignore
	@Test
	public void shouldGetPatientsCurrentOnTarvMisauDefinition() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries
				.getPatientsCurrentOnTarvMisauDefinition();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	// TODO Este teste não corre porque o H2 não é compativel com a função DATE_ADD
	// usado na query
	@Ignore
	@Test
	public void shouldGetPatientsCurrentlyOnTarvWhoReceicevedVLResults() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries
				.getPatientsCurrentlyOnTarvWhoReceicevedVLResults();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	// TODO Este teste não corre porque o H2 não é compativel com a função DATE_ADD
	// usado na query
	// MisauKeyPopReportQueries.findPatientsCurrentlyOnTarvTarvMisauDefinition(DATE_ADD)
	@Ignore
	@Test
	public void shouldGetPatientsCurrentlyOnTarvWhitSuppressedVLResults() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries
				.getPatientsCurrentlyOnTarvWhitSuppressedVLResults();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	// TODO Este teste não corre porque o H2 não é compativel com a função DATE_ADD
	// usado na query
	// MisauKeyPopReportQueries.findPatientsCurrentlyOnTarvTarvMisauDefinition(DATE_ADD)
	@Ignore
	@Test
	public void shouldGetPatientsCoort12CurrentOnArt() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries
				.getPatientsCurrentlyOnTarvWhitSuppressedVLResults();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetPatientsCoort12StartArt() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2013, 01, 01);
		final Date endDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition patientsCoort12Start = this.misauKeyPopReportCohortQueries.getPatientsCoort12StartArt();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(patientsCoort12Start,
				parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}
}
