package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtIndeterminateResultProcessor {

  public Map<Integer, Date> getPREPIndeterminateTestResult(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select prep.patient_id, data_consulta from ( "
                + "select p.patient_id, max(e.encounter_datetime) data_consulta from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where e.voided=0 and p.voided=0 "
                + "and e.encounter_type in (80,81) "
                + "and e.encounter_datetime >=:startDate and e.encounter_datetime <=:endDate group by patient_id "
                + ") prep "
                + "left join "
                + "( "
                + "select p.patient_id from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
                + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
                + "and e.encounter_type=81 and obsTestResult.concept_id=1040 and (obsTestResult.value_coded=703 or obsTestResult.value_coded=664 or obsTestResult.value_coded=1138) and obsTestResult.obs_datetime  >=:startDate "
                + "and obsTestResult.obs_datetime <=:endDate and obsTestResult.voided = 0 "
                + "and e.encounter_datetime >=:startDate and e.encounter_datetime <=:endDate and e.location_id=:location group by p.patient_id "
                + ") semTeste on semTeste.patient_id = prep.patient_id "
                + "left join ( "
                + "select p.patient_id, max(obsTestResult.value_datetime) data_teste from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
                + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
                + "and e.encounter_type=80 and obsTestResult.concept_id=165194 and obsTestResult.value_datetime  >=:startDate "
                + "and obsTestResult.value_datetime <=:endDate "
                + "and e.encounter_datetime >=:startDate and e.encounter_datetime <=:endDate and e.location_id=:location group by p.patient_id "
                + ") semDataTesteFichaInicial on semDataTesteFichaInicial.patient_id = prep.patient_id "
                + "where semTeste.patient_id is null "
                + "and semDataTesteFichaInicial.patient_id  is null ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
