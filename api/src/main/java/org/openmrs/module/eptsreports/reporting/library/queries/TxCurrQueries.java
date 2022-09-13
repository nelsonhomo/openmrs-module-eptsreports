/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

/** @author Stélio Moiane */
public interface TxCurrQueries {

  public class QUERY {

    public enum DispensationIntervalType {
      MONTHLY,

      QUARTERLY,

      SEMI_ANNUAL
    }

    public static final String findPatientsWhoAreCurrentlyEnrolledOnART =
        "	select patient_id                                                                          							"
            + "	from                                                                                        							"
            + "	(select   	inicio_fila_seg_prox.*,																					"
            + "			GREATEST(COALESCE(data_fila,data_seguimento,data_recepcao_levantou),COALESCE(data_seguimento,data_fila,data_recepcao_levantou),COALESCE(data_recepcao_levantou,data_seguimento,data_fila))  data_usar_c,      "
            + "		    GREATEST(COALESCE(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),COALESCE(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar "
            + "	from																														"
            + "		(select 	inicio_fila_seg.*,																							"
            + "		max(obs_fila.value_datetime) data_proximo_lev,																			"
            + "		max(obs_seguimento.value_datetime) data_proximo_seguimento,																"
            + "		date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30												"
            + "  from																															"
            + "(select inicio.*,																													"
            + "		saida.data_estado,																											"
            + "		max_fila.data_fila,																											"
            + "		max_consulta.data_seguimento,																								"
            + "		max_recepcao.data_recepcao_levantou																							"
            + " from																																"
            + " (	Select patient_id,min(data_inicio) data_inicio																				"
            + "		from																														"
            + "			(																														"
            /* Patients on ART who initiated the ARV DRUGS: ART Regimen Start Date */
            + "				Select 	p.patient_id,min(e.encounter_datetime) data_inicio															"
            + "				from 	patient p 																									"
            + "						inner join person pe on pe.person_id = p.patient_id															"
            + "						inner join encounter e on p.patient_id=e.patient_id															"
            + "						inner join obs o on o.encounter_id=e.encounter_id															"
            + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0 and 												"
            + "						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and 								"
            + "						e.encounter_datetime<=:endDate and e.location_id=:location													"
            + "				group by p.patient_id																								"
            + "				union																												"
            /* Patients on ART who have art start date: ART Start date */
            + "				Select 	p.patient_id,min(value_datetime) data_inicio																"
            + "				from 	patient p																									"
            + "						inner join person pe on pe.person_id = p.patient_id															"
            + "						inner join encounter e on p.patient_id=e.patient_id															"
            + "						inner join obs o on e.encounter_id=o.encounter_id															"
            + "				where 	p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and 			"
            + "						o.concept_id=1190 and o.value_datetime is not null and														"
            + "						o.value_datetime<=:endDate and e.location_id=:location														"
            + "				group by p.patient_id																								"
            + "				union																												"
            /* Patients enrolled in ART Program: OpenMRS Program */
            + "				select 	pg.patient_id,min(date_enrolled) data_inicio																"
            + "				from 	patient p 																									"
            + "					inner join person pe on pe.person_id = p.patient_id																"
            + "					inner join patient_program pg on p.patient_id=pg.patient_id														"
            + "				where 	pg.voided=0 and p.voided=0 and pe.voided = 0 and program_id=2 and date_enrolled<=:endDate and location_id=:location "
            + "				group by pg.patient_id																								"
            + "				union																												"
            /*
             * Patients with first drugs pick up date set in Pharmacy: First ART Start Date
             */
            + "				  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio 													"
            + "				  FROM 		patient p																								"
            + "							inner join person pe on pe.person_id = p.patient_id														"
            + "							inner join encounter e on p.patient_id=e.patient_id														"
            + "				  WHERE		p.voided=0 and pe.voided = 0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 	"
            + "				  GROUP BY 	p.patient_id																									 	"
            + "				union																															"
            /* Patients with first drugs pick up date set: Recepcao Levantou ARV */
            + "				Select 	p.patient_id,min(value_datetime) data_inicio																			"
            + "				from 	patient p																												"
            + "						inner join person pe on pe.person_id = p.patient_id																		"
            + "						inner join encounter e on p.patient_id=e.patient_id																		"
            + "						inner join obs o on e.encounter_id=o.encounter_id																		"
            + "				where 	p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and									"
            + "						o.concept_id=23866 and o.value_datetime is not null and 																"
            + "						o.value_datetime<=:endDate and e.location_id=:location																	"
            + "				group by p.patient_id																											"
            + "			) inicio_real																														"
            + "		group by patient_id																														"
            + "  )inicio																																	"
            /* Aqui encontramos os estado do paciente ate a data final */
            + "  left join																																	"
            + "	(																																			"
            + "	select patient_id,max(data_estado) data_estado																								"
            + "	from																																		"
            + "		(																																		"
            /* Estado no programa */
            + "			select distinct max_estado.patient_id, max_estado.data_estado from (                                          						"
            + "				select	pg.patient_id,																											"
            + "						max(ps.start_date) data_estado																							"
            + "				from	patient p																												"
            + "					inner join person pe on pe.person_id = p.patient_id																			"
            + "					inner join patient_program pg on p.patient_id = pg.patient_id																"
            + "					inner join patient_state ps on pg.patient_program_id = ps.patient_program_id												"
            + "				where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2  										"
            + "					and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           "
            + "			) max_estado                                                                                                                        "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id															"
            + "				inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado	        "
            + "			where pp.program_id = 2 and ps.state in (7,8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location					"
            + "			union																																"
            /* Estado no estado de permanencia da Ficha Resumo, Ficha Clinica */
            + "			select 	p.patient_id,																												"
            + "					max(o.obs_datetime) data_estado																								"
            + "			from 	patient p																													"
            + "					inner join person pe on pe.person_id = p.patient_id																			"
            + "					inner join encounter e on p.patient_id=e.patient_id																			"
            + "					inner join obs  o on e.encounter_id=o.encounter_id																			"
            + "			where 	e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0 and															    "
            + "					e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and  						"
            + "					o.obs_datetime<=:endDate and e.location_id=:location																		"
            + "			group by p.patient_id																												"
            + "			union																																"
            /* Obito demografico */
            + "			select person_id as patient_id,death_date as data_estado																			"
            + "			from person																															"
            + "			where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate														"
            + "			union																																"
            /* Obito na ficha de busca */
            + "			select 	p.patient_id,																												"
            + "					max(obsObito.obs_datetime) data_estado																						"
            + "			from 	patient p																													"
            + "					inner join person pe on pe.person_id = p.patient_id																			"
            + "					inner join encounter e on p.patient_id=e.patient_id																			"
            + "					inner join obs obsObito on e.encounter_id=obsObito.encounter_id																"
            + "			where 	e.voided=0 and p.voided=0 and pe.voided = 0 and obsObito.voided=0 and														"
            + "					e.encounter_type in (21,36,37) and  e.encounter_datetime<=:endDate and  e.location_id=:location and 						"
            + "					obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366														"
            + "			group by p.patient_id																												"
            + "			union																																"
            /* Transferido Para na Ficha de Busca */
            + "			select ultimaBusca.patient_id, ultimaBusca.data_estado 																				"
            + "			from ( 																																"
            + "					select p.patient_id,max(e.encounter_datetime) data_estado																	"
            + "					from patient p																												"
            + "						inner join person pe on pe.person_id = p.patient_id																		"
            + "						inner join encounter e on p.patient_id=e.patient_id																		"
            + "						inner join obs o on o.encounter_id=e.encounter_id																		"
            + "					where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= :endDate      									"
            + " 					and e.encounter_type = 21 and  e.location_id= :location  																"
            + "						group by p.patient_id 																									"
            + "				) ultimaBusca 																													"
            + "					inner join encounter e on e.patient_id = ultimaBusca.patient_id 															"
            + "					inner join obs o on o.encounter_id = e.encounter_id 																		"
            + "				where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location "
            + "																																						"
            + "		) allSaida																																		"
            + "	group by patient_id																																	"
            + "  ) saida on inicio.patient_id=saida.patient_id																										"
            /* Aqui encontramos a data do ultimo levantamento de ARV do paciente: Fila */
            + " left join																																			"
            + "  ( Select p.patient_id,max(encounter_datetime) data_fila																								"
            + "	from 	patient p																																	"
            + "			inner join person pe on pe.person_id = p.patient_id																							"
            + "			inner join encounter e on e.patient_id=p.patient_id																							"
            + "	where 	p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 and																		"
            + "			e.location_id=:location and e.encounter_datetime<=:endDate																					"
            + "	group by p.patient_id																																"
            + " ) max_fila on inicio.patient_id=max_fila.patient_id																									"
            /*
             * Aqui encontramos a data da ultima consulta clinica do paciente: Ficha Clinica
             */
            + "  left join																																			"
            + "  (	Select 	p.patient_id,max(encounter_datetime) data_seguimento																					"
            + "	from 	patient p																																	"
            + "			inner join person pe on pe.person_id = p.patient_id																							"
            + "			inner join encounter e on e.patient_id=p.patient_id																							"
            + "	where 	p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9) and																"
            + "			e.location_id=:location and e.encounter_datetime<=:endDate																					"
            + "	group by p.patient_id																																"
            + " ) max_consulta on inicio.patient_id=max_consulta.patient_id																							"
            /*
             * Aqui encontramos a data do ultimo levantamento de ARV do paciente: Recepcao
             */
            + "  left join																																			"
            + "  (																																					"
            + "	Select 	p.patient_id,max(value_datetime) data_recepcao_levantou																						"
            + "	from 	patient p																																	"
            + "			inner join person pe on pe.person_id = p.patient_id																							"
            + "			inner join encounter e on p.patient_id=e.patient_id																							"
            + "			inner join obs o on e.encounter_id=o.encounter_id																							"
            + "	where 	p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 														"
            + "			o.concept_id=23866 and o.value_datetime is not null and																						"
            + "			o.value_datetime<=:endDate and e.location_id=:location																						"
            + "	group by p.patient_id																																"
            + "  ) max_recepcao on inicio.patient_id=max_recepcao.patient_id																						"
            + "  group by inicio.patient_id																															"
            + " ) inicio_fila_seg																																	"
            /*
             * Aqui encontramos a data do proximo levantamento marcado no ultimo
             * levantamento de ARV
             */
            + "  left join																																			"
            + "	 obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id																						"
            + "   and obs_fila.voided=0																																"
            + "	 and obs_fila.obs_datetime=inicio_fila_seg.data_fila																								"
            + "	 and obs_fila.concept_id=5096																														"
            + "	 and obs_fila.location_id=:location																													"
            /* -- Aqui encontramos a data da proxima consulta marcada na ultima consulta */
            + "  left join																																			"
            + "	obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id																			"
            + "	and obs_seguimento.voided=0																															"
            + "	and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento																						"
            + "	and obs_seguimento.concept_id=1410																													"
            + "	and obs_seguimento.location_id=:location																											"
            + " group by inicio_fila_seg.patient_id																													"
            + " ) inicio_fila_seg_prox																																"
            + " group by patient_id																																	"
            + " ) coorte12meses_final																																"
            /* -- Verificação qual é o estado final */
            /* -- where estado_final=6 */
            + " where (data_estado is null or (data_estado is not null and  data_usar_c>data_estado)) and date_add(data_usar, interval 28 day) >=:endDate             ";

    public static final String findPatientsByGenderAndRage =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endDate) BETWEEN %d AND %d AND gender='%s' AND birthdate IS NOT NULL";

    public static final String findPatientsWhoAreInDispenseType(
        final DispensationIntervalType dispenseType) {
      String query =
          "select patient_id "
              + "from "
              + "( "
              + "	select * "
              + "	from "
              + "	( "
              + "		select * "
              + "		from "
              + "		( "
              + "			select 	maxFila.patient_id, "
              + "					maxFila.last_levantamento data_clinica, "
              + "					if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)<83,1, "
              + "						if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 83 and 173,2,3)) tipoDispensa, "
              + "					1 as fonte "
              + "			from "
              + "				( "
              + "					SELECT p.patient_id, MAX(e.encounter_datetime) last_levantamento "
              + "					FROM 	patient p "
              + "							INNER JOIN encounter e ON e.patient_id = p.patient_id "
              + "					WHERE 	p.voided = 0  AND e.voided = 0  AND e.encounter_type = 18  and "
              + "							e.location_id =:location and date(e.encounter_datetime) <=:endDate "
              + "					GROUP BY p.patient_id "
              + "				) maxFila "
              + "				inner join encounter e on e.patient_id=maxFila.patient_id "
              + "				inner join obs obsNext on e.encounter_id=obsNext.encounter_id "
              + "			where 	date(e.encounter_datetime)=date(maxFila.last_levantamento) and "
              + "					e.encounter_type=18 and e.location_id=:location and e.voided=0 and obsNext.voided=0 and "
              + "					obsNext.concept_id=5096 "
              + "			group  by maxFila.patient_id "
              + " "
              + "			UNION "
              + "			select 	lastTipo.patient_id, "
              + "				lastTipo.data_clinica, "
              + "				case obsTipo.value_coded "
              + "						when 1098 then 1 "
              + "						when 23720 then 2 "
              + "						when 23888 then 3 "
              + "				else null end as tipoDispensa, "
              + "				2 as fonte "
              + "			from "
              + "			( "
              + "				Select 	p.patient_id,max(e.encounter_datetime) data_clinica "
              + "				from 	patient p "
              + "						inner join encounter e on p.patient_id=e.patient_id "
              + "						inner join obs o on o.encounter_id=e.encounter_id "
              + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and "
              + "						e.encounter_type =6 and o.concept_id=23739 and "
              + "						e.encounter_datetime<=:endDate and e.location_id=:location "
              + "				group by p.patient_id "
              + "			) lastTipo "
              + "			inner join encounter e on e.patient_id=lastTipo.patient_id "
              + "			inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id "
              + "			where 	lastTipo.data_clinica=e.encounter_datetime and "
              + "					e.encounter_type=6 and e.voided=0 and obsTipo.voided=0 and "
              + "					e.location_id=:location and obsTipo.concept_id=23739 "
              + " "
              + "			UNION "
              + "			select 	lastMDS.patient_id, "
              + "					lastMDS.data_clinica, "
              + "					case o.value_coded "
              + "						when 23730 then 2 "
              + "						when 23888 then 3 "
              + "						when 165314 then 3 "
              + "					else 4 end as tipoDispensa, "
              + "					3 as fonte "
              + "			from "
              + "			( "
              + "				Select 	p.patient_id,max(e.encounter_datetime) data_clinica "
              + "				from 	patient p "
              + "						inner join encounter e on p.patient_id=e.patient_id "
              + "						inner join obs o on o.encounter_id=e.encounter_id "
              + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and "
              + "						e.encounter_type =6 and o.concept_id=165174 and "
              + "						e.encounter_datetime<=:endDate and e.location_id=:location "
              + "				group by p.patient_id "
              + "			) lastMDS "
              + "			inner join encounter e on lastMDS.patient_id=e.patient_id "
              + "			inner join obs grupo on grupo.encounter_id=e.encounter_id "
              + "			inner join obs o on o.encounter_id=e.encounter_id "
              + "			inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id "
              + "			where  	e.encounter_type=6 and e.location_id=:location and "
              + "					o.concept_id=165174 and o.voided=0 "
              + "					and grupo.concept_id=165323  and grupo.voided=0 "
              + "					and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257) "
              + "					and obsEstado.voided=0  and grupo.voided=0 "
              + "					and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id "
              + "					and e.encounter_datetime=lastMDS.data_clinica "
              + "		) allTipoSource "
              + "		order by patient_id,data_clinica desc,fonte "
              + "	) allTipoSourcefirst "
              + "	group by patient_id "
              + ")finalDispensa ";

      switch (dispenseType) {
        case MONTHLY:
          query = query + "where tipoDispensa=1 ";
          break;

        case QUARTERLY:
          query = query + "where tipoDispensa=2 ";
          break;

        case SEMI_ANNUAL:
          query = query + "where tipoDispensa=3 ";
          break;
      }

      return query;
    }
  }
}
