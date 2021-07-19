package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.CxCaSCRNCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.CxCaTXCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.CxCxSRNPositiveCohortQueries;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class CXCACohortTest extends DefinitionsTest {

	@Autowired
	private CxCaSCRNCohortQueries cxCaSCRNCohortQueries;
	@Autowired
	private CxCxSRNPositiveCohortQueries cxCxSRNPositiveCohortQueries;
	@Autowired
	private CxCaTXCohortQueries CxxCaTXCohortQueries;

	@Before
	public void initialise() throws Exception {
		executeDataSet("cxca.xml");
		executeDataSet("cxca1.xml");
		executeDataSet("cxca2.xml");
		executeDataSet("cxca3.xml");
		executeDataSet("cxca4.xml");
	}

	@Test
	public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), this.getStartDate());
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCxSRNPositiveCohortQueries.findpatientwithCxCaPositive();

		final EvaluatedCohort evaluateCohortDefinition = evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithScreeningTestForCervicalCancerDuringReportingPeriod() throws EvaluationException {

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), this.getStartDate());
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientsWithScreeningTestForCervicalCancerDuringReportingPeriod();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);
		
		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(9, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithScreeningTestForCervicalCancerPreviousReportingPeriod()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2019, 01, 01);

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientsWithScreeningTestForCervicalCancerPreviousReportingPeriod();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

		assertNotEquals(0, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodNegative()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2019, 01, 01);

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodNegative();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(7, evaluateCohortDefinition.getMemberIds().size());

		assertNotEquals(0, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodPositive()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2019, 01, 01);

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodPositive();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(3, evaluateCohortDefinition.getMemberIds().size());

		assertNotEquals(evaluateCohortDefinition.getMemberIds().size(), 0);
	}

	@Test
	public void shouldFindPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodSuspectCancer()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2019, 01, 01);

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), this.getEndDate());
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientsWithScreeningTestForCervicalCancerDuringReportingPeriodSuspectCancer();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

		assertNotEquals(0, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindPatientWithScreeningTypeVisitAsRescreenedAfterPreviousNegative() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2020, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);

		Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientWithScreeningTypeVisitAsRescreenedAfterPreviousNegative();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertEquals(2, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldFindPatientWithScreeningTypeVisitAsRescreenedAfterPreviousPositive() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2020, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findPatientWithScreeningTypeVisitAsRescreenedAfterPreviousPositive();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

		assertEquals(1, evaluateCohortDefinition.getMemberIds().size());

		assertNotEquals(0, evaluateCohortDefinition.getMemberIds().size());
	}

	@Test
	public void shouldFindpatientwithScreeningTypeVisitAsPostTreatmentFollowUp() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);

		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.findpatientwithScreeningTypeVisitAsPostTreatmentFollowUp();

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohortDefinition, parameters);

		assertTrue(evaluateCohortDefinition.getMemberIds().isEmpty());

	}

	// TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void shouldFindPatientsWhoerceivedTreatmentTypeDuringReportingPeriod() throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2020, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.CxxCaTXCohortQueries
				.findPatientsWhoerceivedTreatmentTypeDuringReportingPeriod();

		System.out.println(this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

		assertEquals(3, this.evaluateCohortDefinition(cohortDefinition, parameters));

	}

	// TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void getTotalNumerator() throws EvaluationException {

		CohortDefinition cohort = cxCaSCRNCohortQueries.getTotalNumerator();

		final Date startDate = DateUtil.getDateTime(2020, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(cohort, parameters);

		System.out.println(evaluateCohortDefinition.getMemberIds().size());

		assertEquals(5, evaluateCohortDefinition.getMemberIds().size());

	}

	@Test
	public void shouldGetTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpNegative()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2019, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpNegative();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}

	@Test
	public void shouldGetTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpPositive()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpPositive();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}
	
	@Test
	public void shouldGetTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpSuspectCancer()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorfindpatientwithScreeningTypeVisitAsPostTreatmentFollowUpSuspectCancer();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}
	
	//TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void shouldGetTotalNumeratorRescreenedAfterPreviousPositiveTotal()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorRescreenedAfterPreviousPositiveTotal();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}
	
	//TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void shouldGetTotalNumeratorRescreenedAfterPreviousPositiveNegative()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorRescreenedAfterPreviousPositiveNegative();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}
	
	//TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void shouldGetTotalNumeratorRescreenedAfterPreviousPositivePositive()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorRescreenedAfterPreviousPositivePositive();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}
	
	//TODO O teste não passa devido a incompatibilidade das funcões mysql com o H2
	@Ignore
	@Test
	public void shouldGetTotalNumeratorRescreenedAfterPreviousPositiveSuspectCancer()
			throws EvaluationException {

		final Date startDate = DateUtil.getDateTime(2018, 01, 01);

		final Date endDate = DateUtil.getDateTime(2021, 06, 01);
		final Map<Parameter, Object> parameters = new HashMap<>();
		parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
		parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
		parameters.put(new Parameter("location", "Location", Location.class), this.getLocation());

		CohortDefinition cohortDefinition = this.cxCaSCRNCohortQueries
				.getTotalNumeratorRescreenedAfterPreviousPositiveSuspectCancer();

		assertEquals(0, this.evaluateCohortDefinition(cohortDefinition, parameters).getSize());

	}

}
