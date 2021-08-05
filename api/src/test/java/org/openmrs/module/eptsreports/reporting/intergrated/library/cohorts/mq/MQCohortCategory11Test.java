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
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory11CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQGenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCohortCategory11Test extends DefinitionsFGHLiveTest {

	@Autowired
	MQCategory11CohortQueries mQCohortQueriesCategory11;

	@Autowired
	MQGenericCohortQueries mQGenericCohortQueries;

	@Before
	public void setup() throws Exception {
		initialize();
		executeDataSet("mq/mq-cat11-patient-dataset.xml");
		executeDataSet("mq/mq-cat11-encounter-dataset.xml");
		executeDataSet("mq/mq-cat11-concepts-dataset.xml");
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
	public void shouldFindPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory11
				.findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTCategory11NUMERATOR()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory11
				.findPatientsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTCategory11NUMERATOR();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTransferedOutCategory11SectionAPSS_I()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory11
				.findPatientsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTransferedOutCategory11SectionAPSS_I();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(0, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11NUMERATOR()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory11
				.findPatietsOnARTStartedExcludingPregantAndBreastfeedingAndTransferredInTRANSFEREDOUTWITH1000CVCategory11NUMERATOR();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());
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
