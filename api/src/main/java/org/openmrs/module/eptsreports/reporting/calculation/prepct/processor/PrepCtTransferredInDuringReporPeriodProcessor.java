package org.openmrs.module.eptsreports.reporting.calculation.prepct.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class PrepCtTransferredInDuringReporPeriodProcessor {

  public Map<Integer, Date> getPatientsTransferredInDuringReportPeriod(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            " select patient_id, min(data_estado) data_transferencia from( "
                + "select pg.patient_id, min(ps.start_date) data_estado from patient p "
                + "inner join patient_program pg on p.patient_id=pg.patient_id "
                + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
                + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=25 "
                + " and ps.state= 76 and ps.end_date is null and ps.start_date>=:startDate and ps.start_date<=:endDate "
                + " and location_id=:location group by pg.patient_id "
                + "union "
                + "select p.patient_id, min(e.encounter_datetime) data_estado from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs obsTransIn on e.encounter_id= obsTransIn.encounter_id "
                + "where e.voided=0 and obsTransIn.voided=0 and p.voided=0 "
                + "and e.encounter_type=80 and obsTransIn.concept_id=1594 and obsTransIn.value_coded=1369 "
                + "and e.encounter_datetime >=:startDate and e.encounter_datetime<='2021-11-11' and e.location_id=:location group by p.patient_id) result group by patient_id ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
