package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts.mq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsFGHLiveTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory15CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCohortCategory15Test extends DefinitionsFGHLiveTest {

	@Autowired
	MQCategory15CohortQueries mQCohortQueriesCategory15;
	
	@Before
	public void setup() throws Exception {
			initialize();
			executeDataSet("mq/mq-patient-dataset.xml");
			executeDataSet("mq/mq-encounter-dataset.xml");
			executeDataSet("mq/mq-concepts-dataset.xml");
	}

	@Override
	public void initialize() throws ContextAuthenticationException {
		try {
			setupWithStandardDataAndAuthentication();
		} catch (Exception e) {
		}
	}

	@Override
	protected Location getLocation() {
		return Context.getLocationService().getLocation(1);
	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_1() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2016, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 06, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_1();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithDTandGaacWithRequestForVLCategory15H1() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsWithDTandGaacWithRequestForVLCategory15H1();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(4, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithDTandGaacWithRequestForVLCategory15H2() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsWithDTandGaacWithRequestForVLCategory15H2();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithDTandGaacWithCV1000Category15I() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.findPatientsWithDTandGaacWithCV1000Category15I();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_2() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_2();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_3() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.getDenominatorCategory15_Indicator_2_and_3_And_4(); // 5 6

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_4() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_4();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_5() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_5();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_6() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_6();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_7() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_7();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_8() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_8();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_9() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_9();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_10() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_10();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_11() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_11();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetNumeratorCategory15_Indicator_12() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		parameters.put(new Parameter("endDate", "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getNumeratorCategory15_Indicator_12();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_1() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getDenominatorCategory15_Indicator_1();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_2_and_3_And_4() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.getDenominatorCategory15_Indicator_2_and_3_And_4();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_5() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getDenominatorCategory15_Indicator_5();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_7_And_9_And_11() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.getDenominatorCategory15_Indicator_7_And_9_And_11();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_6() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15.getDenominatorCategory15_Indicator_6();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetDenominatorCategory15_Indicator_8_And_10_And_12() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.getDenominatorCategory15_Indicator_8_And_10_And_12();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(5, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(5, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 10, 21);
		final Date endInclusionDate = DateUtil.getDateTime(2020, 1, 20);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory15
				.findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

	}

	@Override
	protected String username() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String password() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
