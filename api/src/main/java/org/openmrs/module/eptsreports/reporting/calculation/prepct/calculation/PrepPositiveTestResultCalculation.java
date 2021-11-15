package org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.processor.PrepCtPositiveResultProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class PrepPositiveTestResultCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Date> prepPositiveProcessorResults =
        Context.getRegisteredComponents(PrepCtPositiveResultProcessor.class)
            .get(0)
            .getPREPPositiveTestResult(context);

    CalculationResultMap resultMap = new CalculationResultMap();
    for (Integer patientId : prepPositiveProcessorResults.keySet()) {
      if (prepPositiveProcessorResults.get(patientId) != null) {
        resultMap.put(
            patientId, new SimpleResult(prepPositiveProcessorResults.get(patientId), this));
      }
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }
}
