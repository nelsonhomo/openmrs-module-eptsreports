package org.openmrs.module.eptsreports.reporting.calculation.prepct;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class PrEPCTAdultsPatientsWhoInitiatedPrEPCalculation extends PrepCtPatientCalculation {

  protected CalculationResultMap evaluateUsingCalculationRules(
      Map<String, Object> parameterValues,
      EvaluationContext context,
      Set<Integer> cohort,
      Date startDate,
      Date endDate,
      CalculationResultMap resultMap,
      CalculationResultMap inicioPrepResult,
      CalculationResultMap transferredInBeforeStartDateCalculationResult,
      CalculationResultMap transferredInDuringReportPeriodCalculationResult,
      CalculationResultMap prepReinitiationCalculationResult,
      CalculationResultMap prepContinuationCalculationResult) {

    for (Integer patientId : cohort) {

      resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
    }

    return resultMap;
  }
}
