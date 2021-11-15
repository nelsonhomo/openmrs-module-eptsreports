package org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.processor.PrepCtTransferredInDuringReporPeriodProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class PrepTransferredInDuringReportPeriodCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Date> prepTransferredInDuringReportPeriodProcessorResults =
        Context.getRegisteredComponents(PrepCtTransferredInDuringReporPeriodProcessor.class)
            .get(0)
            .getPatientsTransferredInDuringReportPeriod(context);

    CalculationResultMap resultMap = new CalculationResultMap();
    for (Integer patientId : cohort) {
      if (prepTransferredInDuringReportPeriodProcessorResults.get(patientId) != null) {
        resultMap.put(
            patientId,
            new SimpleResult(
                prepTransferredInDuringReportPeriodProcessorResults.get(patientId), this));
      }
    }
    return resultMap;
  }
}
