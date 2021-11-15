package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtInitiatedPrepProcessor {

  public Map<Integer, Date> getPatientsWhoInitiatedPrEP(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select patient_id, min(data_prep_inicial) data_consulta from ( "
                + "Select p.patient_id,min(o.obs_datetime) data_prep_inicial "
                + "	from 	patient p inner join encounter e on p.patient_id=e.patient_id "
                + "			inner join obs o on e.encounter_id=o.encounter_id "
                + "	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=80 and "
                + "			o.concept_id=165296 and o.value_coded = 1256 and "
                + "			o.obs_datetime < :startDate and o.voided = 0 and e.location_id= :location group by p.patient_id "
                + "union "
                + " Select p.patient_id,min(o.obs_datetime) data_prep_inicial "
                + "	from 	patient p inner join encounter e on p.patient_id=e.patient_id "
                + "			inner join obs o on e.encounter_id=o.encounter_id "
                + "	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=80 and "
                + "			o.concept_id=165211 and "
                + "			o.value_datetime < :startDate and o.voided = 0 and e.location_id= :location group by p.patient_id "
                + "			) inicioPrep group by patient_id ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
