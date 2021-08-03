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
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory5CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCohortCategory5Test extends DefinitionsFGHLiveTest {

	@Autowired
	MQCategory5CohortQueries mQCohortQueriesCategory5;

	@Autowired
	MQCohortQueries mQCohortQueries;

	@Before
	public void setup() throws Exception {
		initialize();
		executeDataSet("mq-cat5-patient-dataset.xml");
		executeDataSet("mq-cat5-encounter-dataset.xml");
		executeDataSet("mq-cat5-concepts-dataset.xml");
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
	public void shouldFindPatientsWhoAreNewEnrolledOnArtAndHaveFirstConsultMarkedDAMDAGInclusionPeriodCategory5RF19Denominator()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory5
				.findPatientsWhoAreNewEnrolledOnArtAndHaveFirstConsultMarkedDAMDAGInclusionPeriodCategory5RF19Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWhoAreNewEnrolledOnArtAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriodCategory5FR20Numerator()
			throws EvaluationException {

		final Date startInclusionDate = DateUtil.getDateTime(2019, 01, 01);
		final Date endInclusionDate = DateUtil.getDateTime(2019, 04, 01);

		final Date revisionDate = DateUtil.getDateTime(2021, 04, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();

		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());
		parameters.put(new Parameter(EptsReportConstants.START_INCULSION_DATE, "Start Date", Date.class),
				startInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_INCLUSION_DATE, "End Date", Date.class), endInclusionDate);
		parameters.put(new Parameter(EptsReportConstants.END_REVISION_DATE, "End Date", Date.class), revisionDate);

		CohortDefinition cohortDefinition = mQCohortQueriesCategory5
				.findPatientsWhoAreNewEnrolledOnArtAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriodCategory5FR20Numerator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(0, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPregnantsNewlyEnrolledOnARTInInclusionPeriodAndHasNutritionalAssessmentDAMandDAGCategory5RF21Denominator()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory5
				.findPregnantsNewlyEnrolledOnARTInInclusionPeriodAndHasNutritionalAssessmentDAMandDAGCategory5RF21Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPregnantsNewlyEnrolledOnARTInInclusionPeriodAndHasNutritionalAssessmentDAMandDAGAndATPUCategory5RF22Denominator()
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

		CohortDefinition cohortDefinition = mQCohortQueriesCategory5
				.findPregnantsNewlyEnrolledOnARTInInclusionPeriodAndHasNutritionalAssessmentDAMandDAGAndATPUCategory5RF22Denominator();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(0, evaluateCohortDefinition.getMemberIds().size());

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
