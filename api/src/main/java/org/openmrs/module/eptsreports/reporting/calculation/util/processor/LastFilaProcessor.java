package org.openmrs.module.eptsreports.reporting.calculation.util.processor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.txcurr.TxCurrPatientsOnArtOnArvDispenseIntervalsCalculation.DisaggregationInterval;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class LastFilaProcessor {

  public Map<Integer, Date> getLastLevantamentoOnFila(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "Select p.patient_id,max(encounter_datetime) data_fila from patient p "
                + "			inner join encounter e on e.patient_id=p.patient_id "
                + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and "
                + "			e.location_id= :location and e.encounter_datetime<= :endDate group by p.patient_id",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getLastLevantamentoOnFilaForInterval(
      EvaluationContext context, DisaggregationInterval disaggregationInterval) {

    String sql =
        "																							"
            + "select levantamento.patient_id, data_levantamento   																									"
            + "from (  																																				"
            + "	select p.patient_id,max(e.encounter_datetime) data_levantamento  																					"
            + "	from patient p  																																	"
            + "		inner join encounter e on p.patient_id=e.patient_id  																							"
            + "	where p.voided=0 and e.voided=0 and e.encounter_type=18 and date(e.encounter_datetime)<=:endDate and e.location_id=:location group by p.patient_id  "
            + "	) 																																					"
            + "	levantamento  																																		"
            + "		inner join encounter e on e.patient_id = levantamento.patient_id  																				"
            + "		inner join obs o on o.encounter_id=e.encounter_id  																								"
            + "	where levantamento.data_levantamento=e.encounter_datetime and o.concept_id=5096  																	"
            + "		and e.encounter_type=18 and e.voided=0 and o.voided=0 																							"
            + "	and e.location_id=:location and  datediff(o.value_datetime,levantamento.data_levantamento) %s	 															";

    switch (disaggregationInterval) {
      case MONTHLY:
        sql = String.format(sql, "< 83");
        break;

      case QUARTERLY:
        sql = String.format(sql, "between 83 and 173");
        break;

      case SEMI_ANNUAL:
        sql = String.format(sql, "> 173");
        break;
    }

    SqlQueryBuilder qb = new SqlQueryBuilder(sql, context.getParameterValues());
    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getLastTipoDeLevantamentoMensalOnFichaClinicaMasterCard(
      EvaluationContext context) {

    String sql =
        "																						"
            + "select consultas.patient_id, consultas.data_consulta  																								"
            + "from( 																																				"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p inner join encounter e on p.patient_id=e.patient_id  																				"
            + "		inner join obs o on o.encounter_id=e.encounter_id  																								"
            + "	where e.voided=0 and o.voided=0 and p.voided=0  																									"
            + "		and e.encounter_type =6 and o.concept_id=23739  																								"
            + "		and e.encounter_datetime<=:endDate and e.location_id=:location  																				"
            + "	group by p.patient_id  																																"
            + " ) 																																					"
            + " consultas group by consultas.patient_id																																		";

    SqlQueryBuilder qb = new SqlQueryBuilder(sql, context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getValuesForMensalOnFichaClinicaMasterCard(
      EvaluationContext context, List<Integer> patientIds) {

    String sql =
        "																																					"
            + "select consultas.patient_id, consultas.data_consulta  																								"
            + "from( 																																				"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p inner join encounter e on p.patient_id=e.patient_id  																				"
            + "		inner join obs o on o.encounter_id=e.encounter_id  																								"
            + "	where e.voided=0 and o.voided=0 and p.voided=0  																									"
            + "		and e.encounter_type =6 and o.concept_id=23739 and o.value_coded = 1098 																		"
            + "		and e.encounter_datetime<=:endDate and e.location_id=:location and e.patient_id in (%s)															"
            + "	group by p.patient_id  																																"
            + " ) 																																					"
            + " consultas 	group by consultas.patient_id																											";

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            String.format(
                sql, StringUtils.join(patientIds, ","), StringUtils.join(patientIds, ",")),
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date>
      getLastMarkedInModelosDiferenciadosDeCuidadosOrTipoDeLevantamentoOnFichaClinicaMasterCard(
          EvaluationContext context) {

    String query =
        "																																				"
            + "select consultas.patient_id, max(consultas.data_consulta)  																							"
            + "from( 																																				"
            + "	 																																					"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p   																																	"
            + "		inner join encounter e on p.patient_id=e.patient_id   																							"
            + "	     inner join obs grupo on grupo.encounter_id=e.encounter_id   																					"
            + "	     inner join obs o on o.encounter_id=e.encounter_id  																							"
            + "	     inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id   																			"
            + "	where e.encounter_type in(6) and e.location_id=:location and o.concept_id=165174 and o.voided=0   													"
            + "		and grupo.concept_id=165323  and grupo.voided=0 and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257)   						"
            + "		and obsEstado.voided=0  and grupo.voided=0   																									"
            + "		and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id  and e.encounter_datetime<=:endDate  									"
            + "		group by p.patient_id 																															"
            + "	 																																					"
            + "	union 																																				"
            + "	 																																					"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p inner join encounter e on p.patient_id=e.patient_id  																				"
            + "		inner join obs o on o.encounter_id=e.encounter_id  																								"
            + "	where e.voided=0 and o.voided=0 and p.voided=0  																									"
            + "		and e.encounter_type =6 and o.concept_id=23739  																								"
            + "		and e.encounter_datetime<=:endDate and e.location_id=:location  																				"
            + "	group by p.patient_id  																																"
            + " ) 																																					"
            + " consultas group by consultas.patient_id 																																			";

    SqlQueryBuilder qb = new SqlQueryBuilder(query, context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date>
      getValuesForModelosDiferenciadosDeCuidadosOrTipoDeLevantamentoOnFichaClinicaMasterCard(
          EvaluationContext context,
          List<Integer> patientIds,
          Integer tipoDeLevantamentoValueCoded,
          List<Integer> mdcValueCoded) {

    String query =
        "																					"
            + "select consultas.patient_id, max(consultas.data_consulta) 																								"
            + "from( 																																				"
            + "	 																																					"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p   																																	"
            + "		inner join encounter e on p.patient_id=e.patient_id   																							"
            + "	     inner join obs grupo on grupo.encounter_id=e.encounter_id   																					"
            + "	     inner join obs o on o.encounter_id=e.encounter_id  																							"
            + "	     inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id   																			"
            + "	where e.encounter_type in(6) and e.location_id=:location and o.concept_id=165174 and o.value_coded in( %s)  and o.voided=0   													"
            + "		and grupo.concept_id=165323  and grupo.voided=0 and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257)   						"
            + "		and obsEstado.voided=0  and grupo.voided=0 and p.patient_id in(%s) 																									"
            + "		and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id  and e.encounter_datetime<=:endDate  									"
            + "		group by p.patient_id 																															"
            + "	 																																					"
            + "	union 																																				"
            + "	 																																					"
            + "	select p.patient_id,max(e.encounter_datetime) data_consulta  																						"
            + "	from patient p inner join encounter e on p.patient_id=e.patient_id  																				"
            + "		inner join obs o on o.encounter_id=e.encounter_id  																								"
            + "	where e.voided=0 and o.voided=0 and p.voided=0  																									"
            + "		and e.encounter_type =6 and o.concept_id=23739 and o.value_coded = %s and p.patient_id in(%s)																								"
            + "		and e.encounter_datetime<=:endDate and e.location_id=:location  																				"
            + "	group by p.patient_id  																																"
            + " ) 																																					"
            + " consultas group by consultas.patient_id																																		";

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            String.format(
                query,
                StringUtils.join(mdcValueCoded, ","),
                StringUtils.join(patientIds, ","),
                tipoDeLevantamentoValueCoded,
                StringUtils.join(patientIds, ",")),
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public List<Object[]> getMaxFilaWithProximoLevantamento(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select pickup.patient_id, pickup.last_levantamento, max(obs_proximo_levantamento.value_datetime) proximo_levantamento "
                + "from ( "
                + "select maxpkp.patient_id,maxpkp.last_levantamento,e.encounter_id from ( "
                + "SELECT p.patient_id, MAX(e.encounter_datetime) last_levantamento FROM patient p "
                + "INNER JOIN encounter e ON e.patient_id = p.patient_id "
                + "WHERE p.voided = 0  AND e.voided = 0  AND e.encounter_type = 18  and e.location_id =:location  and date(e.encounter_datetime) <=:endDate "
                + "GROUP BY p.patient_id "
                + ") maxpkp "
                + "inner join encounter e on e.patient_id=maxpkp.patient_id "
                + "where date(e.encounter_datetime)=date(maxpkp.last_levantamento) and e.encounter_type=18 and e.location_id=:location and e.voided=0 "
                + "order by maxpkp.patient_id "
                + ") pickup "
                + "inner join obs obs_proximo_levantamento on pickup.encounter_id=obs_proximo_levantamento.encounter_id "
                + "where obs_proximo_levantamento.concept_id = 5096  and obs_proximo_levantamento.voided = 0 "
                + "group by pickup.patient_id ",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToList(qb, context);
  }
}
