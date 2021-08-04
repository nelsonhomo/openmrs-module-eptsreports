package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts.mq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsFGHLiveTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory7CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCohortCategory7Test extends DefinitionsFGHLiveTest {

	@Autowired
	MQCategory7CohortQueries mQCohortQueriesCategory7;

	@Before
	public void setup() throws Exception {
		initialize();
		executeDataSet("mq-cat7-patient-dataset.xml");
		executeDataSet("mq-cat7-encounter-dataset.xml");
		executeDataSet("mq-cat7-concepts-dataset.xml");
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
	public void shouldFindPregnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndStartTPIAndElegibleTPTCategory7RF23Denominator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2019, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPregnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndStartTPIAndElegibleTPTCategory7RF23Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPregnantNewlyEnrolledOnARTInInclusionPeriodAndStartTPIAndElegibleTPTDuringInclusionPeriodCategory7RF24Numerator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPregnantNewlyEnrolledOnARTInInclusionPeriodAndStartTPIAndElegibleTPTDuringInclusionPeriodCategory7RF24Numerator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF25Denominator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 01, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPatientsWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF25Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF26Numerator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPatientsWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF26Numerator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPragnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF29Denominator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPragnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF29Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPregnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF30Numerator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPregnantWhoAreNewlyEnrolledOnARTDuringInclusionPeriodAndEndTPICategory7RF30Numerator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientWhoAreNewlyEnrolledOnARTDuringRevisionPeriodAndStartTPIAndElegibleTPTCategory7RF19Denominator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPatientWhoAreNewlyEnrolledOnARTDuringRevisionPeriodAndStartTPIAndElegibleTPTCategory7RF19Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(4, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsNewlyEnrolledOnARTInInclusionPeriodAndHaveINHDuringPeriodCategory7RF20Numerator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory7
				.findPatientsNewlyEnrolledOnARTInInclusionPeriodAndHaveINHDuringPeriodCategory7RF20Numerator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Override
	protected String username() {
		return null;
	}

	@Override
	protected String password() {
		return null;
	}

}
