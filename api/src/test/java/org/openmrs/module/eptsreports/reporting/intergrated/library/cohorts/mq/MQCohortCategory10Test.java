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
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory10CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQGenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCohortCategory10Test extends DefinitionsFGHLiveTest {

	@Autowired
	MQCategory10CohortQueries mQCohortQueriesCategory10;

	@Autowired
	MQCohortQueries mQCohortQueries;

	@Autowired
	MQGenericCohortQueries mQGenericCohortQueries;

	@Before
	public void setup() throws Exception {
		initialize();
		executeDataSet("mq-cat10-patient-dataset.xml");
		executeDataSet("mq-cat10-encounter-dataset.xml");
		executeDataSet("mq-cat10-concepts-dataset.xml");
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
	public void shouldFindAllPatientsDiagnosedWithThePCRTestCategory10_B() throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2019, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 01, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory10
				.findAllPatientsDiagnosedWithThePCRTestCategory10_B();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindAllPatientsDiagnosedWithThePCRTestAndStartARTWithInMaximumOf15DaysCategory10_D()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 01, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 04, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory10
				.findAllPatientsDiagnosedWithThePCRTestAndStartARTWithInMaximumOf15DaysCategory10_D();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 01, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 04, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory10
				.findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(0, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWithPCRTestPositiveForHIVAndStartARTWithinTwoWeeksCategory10_Denominador_10_3()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 01, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 04, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory10
				.findPatientsWithPCRTestPositiveForHIVAndStartARTWithinTwoWeeksCategory10_Denominador_10_3();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(6, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWithPCRTestPositiveForHIVAndStartARTWithinTwoWeeksCategory10_Numerador_10_3()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2021, 01, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 04, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory10
				.findPatientsWithPCRTestPositiveForHIVAndStartARTWithinTwoWeeksCategory10_Numerador_10_3();

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
