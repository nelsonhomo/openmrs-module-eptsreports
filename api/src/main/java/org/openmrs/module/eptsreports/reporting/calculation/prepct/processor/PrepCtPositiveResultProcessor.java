package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtPositiveResultProcessor {

  public Map<Integer, Date> getPREPPositiveTestResult(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select p.patient_id, max(obsTestResult.obs_datetime) data_positivo from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
                + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
                + "and e.encounter_type=81 and obsTestResult.concept_id=1040 and obsTestResult.value_coded=703 and obsTestResult.obs_datetime  >=:startDate "
                + "and obsTestResult.obs_datetime <=:endDate and obsTestResult.voided = 0 "
                + "and e.location_id=:location group by p.patient_id ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
