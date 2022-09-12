package org.openmrs.module.eptsreports.reporting.calculation.txcurr;

import java.util.Map;
import java.util.Map.Entry;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxCurrPatientsOnArvDispenseLessThan3MonthCalculation
    extends TxCurrPatientsOnArtOnArvDispenseIntervalsCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    return super.evaluate(parameterValues, context);
  }

  @Override
  protected void evaluateDisaggregatedPatients(
      EvaluationContext context,
      CalculationResultMap resultMap,
      Map<Integer, PatientDisaggregated> resultDisaggregated) {

    for (Entry<Integer, PatientDisaggregated> patientDisaggregated :
        resultDisaggregated.entrySet()) {
      PatientDisaggregated disaggregated = patientDisaggregated.getValue();
      if (DisaggregationInterval.MONTHLY.equals(disaggregated.getDisaggregationInterval())) {
        resultMap.put(disaggregated.getPatientId(), new BooleanResult(Boolean.TRUE, this));
      }
    }
  }
}
