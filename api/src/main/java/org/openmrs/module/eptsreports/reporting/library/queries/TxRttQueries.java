/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

/** @author Stélio Moiane */
public interface TxRttQueries {

  class QUERY {

    public static final String findMinEncounterDateByPatientInReportingPeriod =
        "select patient_id, min(min_date) from ( "
            + "SELECT e.patient_id, MIN(e.encounter_datetime)  min_date "
            + "FROM patient p inner join encounter e on p.patient_id=e.patient_id "
            + "WHERE p.voided=0 and e.encounter_type in (6,9,18) AND e.voided=0 and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.patient_id in(:patientIds) and e.location_id=:location GROUP BY p.patient_id "
            + "union Select p.patient_id,min(value_datetime) min_date from patient p "
            + " inner join encounter e on p.patient_id=e.patient_id "
            + " inner join obs o on e.encounter_id=o.encounter_id "
            + " where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null "
            + " and o.value_datetime>=:startDate and o.value_datetime<=:endDate and e.patient_id in(:patientIds) and e.location_id=:location group by p.patient_id) min_data_consulta_levantamento group by patient_id ";

    public static final String findMaxEncounterDateByPatientInReportingPeriod =
        "select patient_id, max(last_date) from ( "
            + "SELECT e.patient_id, max(e.encounter_datetime)  last_date "
            + "FROM patient p inner join encounter e on p.patient_id=e.patient_id "
            + "WHERE p.voided=0 and e.encounter_type in (6,9,18) AND e.voided=0 and e.encounter_datetime<:startDate and e.location_id=:location and e.patient_id in(:patientIds) GROUP BY p.patient_id "
            + "union Select p.patient_id,max(value_datetime) last_date from patient p "
            + " inner join encounter e on p.patient_id=e.patient_id "
            + " inner join obs o on e.encounter_id=o.encounter_id "
            + " where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null "
            + " and o.value_datetime<:startDate and e.location_id=:location and e.patient_id in(:patientIds) group by p.patient_id ) last_consulta_levantamento group by patient_id ";

    public static final String rttFromEurico =
        "select patient_id            "
            + "from                                   "
            + "(select   	inicio_fila_seg_prox.*,  "
            + "			GREATEST(COALESCE(data_fila,data_seguimento,data_recepcao_levantou),COALESCE( "
            + "data_seguimento,data_fila,data_recepcao_levantou),COALESCE "
            + "(data_recepcao_levantou,data_seguimento,data_fila))  data_usar_c, "
            + "GREATEST(COALESCE(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),COALESCE( "
            + "data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),COALESCE( "
            + "data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar  "
            + "from                                      "
            + "(select 	inicio_fila_seg.*,                                          "
            + "		max(obs_fila.value_datetime) data_proximo_lev,                               "
            + "		max(obs_seguimento.value_datetime) data_proximo_seguimento,                 "
            + "		date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30 "
            + "		                                                      "
            + "from                                                                                   "
            + "(select inicio.*,		                                                                "
            + "		saida.data_estado,		                                                        "
            + "		max_fila.data_fila,                                                            "
            + "		max_consulta.data_seguimento,                                                  "
            + "		max_recepcao.data_recepcao_levantou                                         "
            + "		                                                                          "
            + "from                                                                                  "
            + "(	Select patient_id,min(data_inicio) data_inicio                                    "
            + "		from                                                                      "
            + "			(	                                                                                 "
            + "				Select 	p.patient_id,min(e.encounter_datetime) data_inicio                              "
            + "				from 	patient p                                                                       "
            + "						inner join encounter e on p.patient_id=e.patient_id	                            "
            + "						inner join obs o on o.encounter_id=e.encounter_id                                "
            + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and                                     "
            + "						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and    "
            + "						e.encounter_datetime<=:endDate and e.location_id=:location                         "
            + "				group by p.patient_id                                                                 "
            + "				union                                                                                 "
            + "				Select 	p.patient_id,min(value_datetime) data_inicio                                    "
            + "				from 	patient p                                                                   "
            + "						inner join encounter e on p.patient_id=e.patient_id                         "
            + "						inner join obs o on e.encounter_id=o.encounter_id                           "
            + "				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and "
            + "						o.concept_id=1190 and o.value_datetime is not null and          "
            + "						o.value_datetime<=:endDate and e.location_id=:location           "
            + "				group by p.patient_id                                            "
            + "				union                                         "
            + "				select 	pg.patient_id,min(date_enrolled) data_inicio              "
            + "				from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id     "
            + "				where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and "
            + "location_id=:location                                                                                  "
            + "				group by pg.patient_id                                                   "
            + "	      		union                                                                    "
            + "				  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio         "
            + "				  FROM 		patient p                                                "
            + "							inner join encounter e on p.patient_id=e.patient_id                "
            + "				  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and              "
            + "e.encounter_datetime<=:endDate and e.location_id=:location                                     "
            + "				  GROUP BY 	p.patient_id                                                         "
            + "			   	union                                                                           "
            + "				Select 	p.patient_id,min(value_datetime) data_inicio                            "
            + "				from 	patient p                                                               "
            + "						inner join encounter e on p.patient_id=e.patient_id                    "
            + "						inner join obs o on e.encounter_id=o.encounter_id                      "
            + "				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and   "
            + "						o.concept_id=23866 and o.value_datetime is not null and            "
            + "						o.value_datetime<=:endDate and e.location_id=:location             "
            + "				group by p.patient_id	                                              "
            + "			) inicio_real                                                        "
            + "		group by patient_id                                                      "
            + ")inicio                                                                                          "
            + "left join                                                                                       "
            + "(                                                                                               "
            + "	select patient_id,max(data_estado) data_estado                                                "
            + "	from                                                                                          "
            + "		(                                                                                         "
            + "			select 	pg.patient_id,                                                                "
            + "					max(ps.start_date) data_estado                                               "
            + "			from 	patient p                                                                  "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id                 "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  "
            + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and                           "
            + "					pg.program_id=2 and ps.state=7 and ps.end_date is null and              "
            + "					ps.start_date<=:endDate and location_id=:location                       "
            + "			group by pg.patient_id                                                        "
            + "			union                                                                    "
            + "			select 	p.patient_id,                                                     "
            + "					max(o.obs_datetime) data_estado                                                       "
            + "                                                                                                        "
            + "			from 	patient p                                                                             "
            + "					inner join encounter e on p.patient_id=e.patient_id                                   "
            + "					inner join obs  o on e.encounter_id=o.encounter_id                                    "
            + "			where 	e.voided=0 and o.voided=0 and p.voided=0 and                                          "
            + "					e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded=1706 and "
            + "					o.obs_datetime<=:endDate and e.location_id=:location                                 "
            + "			group by p.patient_id			                                                           "
            + "		) allSaida                                                                                     "
            + "	group by patient_id			                                                                       "
            + ") saida on inicio.patient_id=saida.patient_id                                                        "
            + "left join                                                                                            "
            + "(	Select 	p.patient_id,max(encounter_datetime) data_fila                                             "
            + "	from 	patient p                                                                                  "
            + "			inner join encounter e on e.patient_id=p.patient_id                                        "
            + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and                                      "
            + "			e.location_id=:location and e.encounter_datetime<=:endDate                                   "
            + "	group by p.patient_id                                                                              "
            + ") max_fila on inicio.patient_id=max_fila.patient_id	                                               "
            + "left join                                                                                            "
            + "(	Select 	p.patient_id,max(encounter_datetime) data_seguimento                                       "
            + "	from 	patient p                                                                                  "
            + "			inner join encounter e on e.patient_id=p.patient_id                                        "
            + "	where 	p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and                                "
            + "			e.location_id=:location and e.encounter_datetime<=:endDate                                   "
            + "	group by p.patient_id                                                                              "
            + ") max_consulta on inicio.patient_id=max_consulta.patient_id                                          "
            + "left join                                                                                           "
            + "(                                                                                                    "
            + "	Select 	p.patient_id,max(value_datetime) data_recepcao_levantou                                     "
            + "	from 	patient p                                                                                   "
            + "			inner join encounter e on p.patient_id=e.patient_id                                         "
            + "			inner join obs o on e.encounter_id=o.encounter_id                                           "
            + "	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                        "
            + "			o.concept_id=23866 and o.value_datetime is not null and                                     "
            + "			o.value_datetime<=:endDate and e.location_id=:location                                        "
            + "	group by p.patient_id                                                                               "
            + ") max_recepcao on inicio.patient_id=max_recepcao.patient_id                                           "
            + "group by inicio.patient_id                                                                            "
            + ") inicio_fila_seg                                                                                     "
            + "left join                                                                                             "
            + "	obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id                                       "
            + "	and obs_fila.voided=0                                                                               "
            + "	and obs_fila.obs_datetime=inicio_fila_seg.data_fila                                                 "
            + "	and obs_fila.concept_id=5096                                                                        "
            + "	and obs_fila.location_id=:location                                                                        "
            + "left join                                                                                             "
            + "	obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id                           "
            + "	and obs_seguimento.voided=0                                                                         "
            + "	and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento                                     "
            + "	and obs_seguimento.concept_id=1410                                                                  "
            + "	and obs_seguimento.location_id=:location                                                                  "
            + "group by inicio_fila_seg.patient_id                                                                   "
            + ") inicio_fila_seg_prox                                                                                "
            + "group by patient_id                                                                                   "
            + ") coorte12meses_final                                                                                 "
            + "left join                                                                                             "
            + "(                                                                                                     "
            + "	Select 	p.patient_id person_id                                                                      "
            + "	from 	patient p                                                                                   "
            + "			inner join encounter e on p.patient_id=e.patient_id                                         "
            + "			inner join obs obsTrans on e.encounter_id=obsTrans.encounter_id and obsTrans.voided=0 and    "
            + "obsTrans.concept_id=1369 and obsTrans.value_coded=1065                                                "
            + "			inner join obs obsTarv on e.encounter_id=obsTarv.encounter_id and obsTarv.voided=0 and      "
            + "obsTarv.concept_id=6300 and obsTarv.value_coded=6276                                                  "
            + "			inner join obs obsInscricao on e.encounter_id=obsInscricao.encounter_id and                "
            + "obsInscricao.voided=0 and obsInscricao.concept_id=23891                                              "
            + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=53 and                                       "
            + "			obsInscricao.value_datetime between (:endDate + INTERVAL 1 DAY)                         "
            + "and (:endDate + INTERVAL 3 MONTH) and                                                              "
            + "e.location_id=:location                                                                                       "
            + "	union		                                                                                         "
            + "	select 	minEstado.patient_id person_id                                                              "
            + "	from 	(                                                                                           "
            + "				select pg.patient_id,min(ps.start_date) data_transferidode                              "
            + "				from 	patient p                                                                       "
            + "						inner join patient_program pg on p.patient_id=pg.patient_id                     "
            + "						inner join patient_state ps on pg.patient_program_id=ps.patient_program_id      "
            + "				where 	pg.voided=0 and ps.voided=0 and p.voided=0 and                                  "
            + "						pg.program_id=2 and ps.start_date between (:endDate + INTERVAL 1 DAY) and   "
            + "(:endDate + INTERVAL 3 MONTH) and pg.location_id=:location                                    "
            + "				group by p.patient_id                                                       "
            + "			) minEstado                                                                     "
            + "			inner join patient_program pg2 on pg2.patient_id=minEstado.patient_id          "
            + "			inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id  "
            + "	where 	pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and              "
            + "			ps2.start_date=minEstado.data_transferidode and pg2.location_id=:location and ps2.state=29       "
            + ") transferidode on coorte12meses_final.patient_id=transferidode.person_id                               "
            + " where (((data_estado is null or (data_estado is not null and  data_usar_c>data_estado)) and date_add(data_usar, interval 28 day) <:endDate) and transferidode.person_id is null ) or (data_usar is null and transferidode.person_id is null) ";
  }
}
