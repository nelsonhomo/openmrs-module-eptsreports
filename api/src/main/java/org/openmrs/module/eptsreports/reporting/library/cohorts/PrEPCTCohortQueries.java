package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.PrEPCTAdultsPatientsWhoInitiatedPrEPCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepIndeterminateTestResultCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepNegativeTestResultCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepPositiveTestResultCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

/** All queries needed for PREP CT report needed for EPTS project */
@Component
public class PrEPCTCohortQueries {

  @DocumentedDefinition(value = "patientsWhoInitiatedPrep")
  public CohortDefinition getPatientsWhoInitiatedPrep() {
    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "prEPCTAdultsPatientsWhoInitiatedPrEPCalculation",
            Context.getRegisteredComponents(PrEPCTAdultsPatientsWhoInitiatedPrEPCalculation.class)
                .get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }

  @DocumentedDefinition(value = "patientsWithPositiveTestResult")
  public CohortDefinition getPatientsWithPositiveTestResult() {
    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "positiveTestResultCalculation",
            Context.getRegisteredComponents(PrepPositiveTestResultCalculation.class).get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }

  @DocumentedDefinition(value = "patientsWithNegativeTestResult")
  public CohortDefinition getPatientsWithNegativeTestResult() {
    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "negativeTestResultCalculation",
            Context.getRegisteredComponents(PrepNegativeTestResultCalculation.class).get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }

  @DocumentedDefinition(value = "patientsWithIndeterminateTestResult")
  public CohortDefinition getPatientsWithIndeterminateTestResult() {
    BaseFghCalculationCohortDefinition cd =
        new BaseFghCalculationCohortDefinition(
            "indeterminateTestResultCalculation",
            Context.getRegisteredComponents(PrepIndeterminateTestResultCalculation.class).get(0));
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "end Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));

    return cd;
  }
}
