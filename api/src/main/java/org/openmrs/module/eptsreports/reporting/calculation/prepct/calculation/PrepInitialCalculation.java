package org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.processor.PrepCtInitiatedPrepProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class PrepInitialCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    CalculationResultMap resultMap = new CalculationResultMap();
    Map<Integer, Date> processorResult =
        Context.getRegisteredComponents(PrepCtInitiatedPrepProcessor.class)
            .get(0)
            .getPatientsWhoInitiatedPrEP(context);
    for (Integer pId : processorResult.keySet()) {
      Date date = (Date) processorResult.get(pId);
      if (date != null) {
        resultMap.put(pId, new SimpleResult(date, this));
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
