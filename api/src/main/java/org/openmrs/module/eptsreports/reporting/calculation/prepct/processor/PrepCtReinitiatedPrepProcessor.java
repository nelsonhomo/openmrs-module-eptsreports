package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtReinitiatedPrepProcessor {

  public Map<Integer, Date> getPatientsWhoReinitiatedPrEP(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select p.patient_id, max(e.encounter_datetime) data_estado from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs obsReinitiated on e.encounter_id= obsReinitiated.encounter_id "
                + "where e.voided=0 and obsReinitiated.voided=0 and p.voided=0 "
                + "and e.encounter_type=80 and obsReinitiated.concept_id=165296 and obsReinitiated.value_coded=1705 and obsReinitiated.obs_datetime  >=:startDate "
                + "and obsReinitiated.obs_datetime <=:endDate "
                + "and e.encounter_datetime >=:startDate and e.encounter_datetime <=:endDate and e.location_id=:location group by p.patient_id ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
