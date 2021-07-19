package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsFGHLiveTest;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.IMR1BCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.IMR1CohortQueries;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

public class IMR1CohortTest extends DefinitionsTest {

  @Autowired private IMR1CohortQueries imr1CohortQueries;

  @Autowired private IMR1BCohortQueries imr1BCohortQueries;
  
	@Before
	public void initialise() throws Exception {
		executeDataSet("imr.xml");
	}

  @Test
  public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {

    final Location location = Context.getLocationService().getLocation(398);
    final Date startDate = DateUtil.getDateTime(2020, 11, 21);
    final Date endDate = DateUtil.getDateTime(2020, 12, 20);

    System.out.println(startDate);
    System.out.println(endDate);

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), location);

    CohortDefinition cohortDefinitionDemoninator =
        imr1CohortQueries.getPatientsNewlyEnrolledOnArtCare();

    CohortDefinition cohortDefinitionNumerator =
        imr1CohortQueries.getPatientsNewlyEnrolledOnArtCareNumerator();

    CohortDefinition i1B = imr1BCohortQueries.getPatientsNewlyEnrolledOnArtCare();

    final EvaluatedCohort evaluateCohortDefinition = this.evaluateCohortDefinition(i1B, parameters);

    System.out.println(evaluateCohortDefinition.getMemberIds().size());
    assertFalse(evaluateCohortDefinition.getMemberIds().isEmpty());

    System.out.println("----------------------------------");

    for (int t : evaluateCohortDefinition.getMemberIds()) {
      System.out.println(t);
    }
  }

}
