package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtBreastfeedingProcessor {

  public Map<Integer, Date> getPregnantInFichaInicialPREP(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "Select p.patient_id, max(e.encounter_datetime) data_gravida from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs o on e.encounter_id=o.encounter_id "
                + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 "
                + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getPregnantInFichaSeguimentoPREP(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "Select p.patient_id, max(e.encounter_datetime) data_gravida from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs o on e.encounter_id=o.encounter_id "
                + "where p.voided=0 and e.voided=0 and o.voided=0 and "
                + "concept_id=165223 and value_coded=1982 and e.encounter_type=81 "
                + "and e.encounter_datetime between :startDate and :endDate and e.location_id=:location ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
