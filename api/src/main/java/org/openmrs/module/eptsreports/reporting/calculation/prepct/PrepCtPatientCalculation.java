package org.openmrs.module.eptsreports.reporting.calculation.prepct;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepContinuationCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepInitialCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepReinitiationCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepTransferredInBeforeReportStartDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.prepct.calculation.PrepTransferredInDuringReportPeriodCalculation;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public abstract class PrepCtPatientCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    Date startDate = (Date) context.getParameterValues().get("startDate");
    Date endDate = (Date) context.getParameterValues().get("endDate");

    CalculationResultMap inicioPrepResult =
        Context.getRegisteredComponents(PrepInitialCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);
    Set<Integer> cohort = inicioPrepResult.keySet();

    CalculationResultMap transferredInBeforeStartDateCalculationResult =
        Context.getRegisteredComponents(PrepTransferredInBeforeReportStartDateCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    CalculationResultMap transferredInDuringReportPeriodCalculationResult =
        Context.getRegisteredComponents(PrepTransferredInDuringReportPeriodCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    CalculationResultMap prepReinitiationCalculationResult =
        Context.getRegisteredComponents(PrepReinitiationCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    CalculationResultMap prepContinuationCalculationResult =
        Context.getRegisteredComponents(PrepContinuationCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    return this.evaluateUsingCalculationRules(
        parameterValues,
        context,
        cohort,
        startDate,
        endDate,
        resultMap,
        inicioPrepResult,
        transferredInBeforeStartDateCalculationResult,
        transferredInDuringReportPeriodCalculationResult,
        prepReinitiationCalculationResult,
        prepContinuationCalculationResult);
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }

  protected abstract CalculationResultMap evaluateUsingCalculationRules(
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
      CalculationResultMap prepContinuationCalculationResult);
}
