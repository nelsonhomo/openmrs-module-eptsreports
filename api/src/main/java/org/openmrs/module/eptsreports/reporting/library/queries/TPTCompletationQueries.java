package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.library.queries.TxTbPrevQueriesInterface.QUERY.DisaggregationTypes;

public interface TPTCompletationQueries {

  class QUERY {

    public static final String
        findPatientsWhoStartedArtAndTbPrevPreventiveTreatmentInDisaggregation(
            DisaggregationTypes disaggregationType) {

      String query =
          "select inicio_TPT.patient_id 										"
              + "from (																				"
              + "select patient_id, data_inicio_tpi                                                   "
              + "from (                                                                               "
              + "    select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_tpi     "
              + "    from (                                                                           "
              + "        select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p                                                                                                "
              + "            inner join encounter e on p.patient_id = e.patient_id                    "
              + "            inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id                "
              + "            inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id          "
              + "        where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  "
              + "            and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256                       "
              + "            and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:endDate - interval 7 month) and :endDate          "
              + "        group by p.patient_id                                                        "
              + "        union                                                                        "
              + "        select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p   "
              + "            inner join encounter e on p.patient_id = e.patient_id                    "
              + "            inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                     "
              + "        where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:endDate - interval 7 month) and :endDate "
              + "             and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location                                "
              + "        group by p.patient_id                                                        "
              + "        union                                                                        "
              + "          select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p           "
              + "            inner join encounter e on p.patient_id=e.patient_id                      "
              + "            inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id        "
              + "            inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                  "
              + "        where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate		                                              "
              + "            and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location                             "
              + "            and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)                                                                      "
              + "        group by p.patient_id                                                        "
              + "        union (                                                                        "
              + "                select inicio.patient_id,inicio.data_inicio_tpi from                 "
              + "                (   select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p "
              + "                        inner join encounter e on p.patient_id=e.patient_id          "
              + "                        inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id              "
              + "                        inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id      "
              + "                    where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate			                                  "
              + "                        and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location                 "
              + "                        and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267)                                                          "
              + "                    group by p.patient_id                                            "
              + "                    union                                                            "
              + "                    select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p     "
              + "                        inner join encounter e on p.patient_id=e.patient_id          "
              + "                        inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id              "
              + "                        left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id     "
              + "                            and seguimentoTPT.concept_id =23987                      "
              + "                            and seguimentoTPT.value_coded in(1256,1257,1705,1267)    "
              + "                            and seguimentoTPT.voided =0)                             "
              + "                    where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:endDate - interval 7 month) and :endDate					                                      "
              + "                        and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location                 "
              + "                        and seguimentoTPT.obs_id is null group by p.patient_id       "
              + "             ) inicio left join (                                                    "
              + "                    select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p          "
              + "                        inner join encounter e on p.patient_id=e.patient_id          "
              + "                        inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id              "
              + "                    where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:endDate - INTERVAL 17 MONTH)  and :endDate 			                                    "
              + "                         and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location                "
              + "                     union                                                           "
              + "                        select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p        "
              + "                        inner join encounter e on p.patient_id = e.patient_id        "
              + "                        inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id    "
              + "                        inner join obs estado on estado.encounter_id = e.encounter_id                  "
              + "                    where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0    "
              + "                         and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256                                      "
              + "                         and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:endDate - INTERVAL 17 MONTH)  and :endDate 				         "
              + "                     union                                                           "
              + "                     select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p                                                                                    "
              + "                        inner join encounter e on p.patient_id=e.patient_id          "
              + "                        inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id                                                                             "
              + "                    where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:endDate - INTERVAL 17 MONTH)  and :endDate 		                          "
              + "                        and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location "
              + "                    )  inicioAnterior on inicioAnterior.patient_id=inicio.patient_id                  "
              + "                        and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day)                                  "
              + "                    where inicioAnterior.patient_id is null )                        "
              + "        )                                                                            "
              + "    inicio_3HP group by inicio_3HP.patient_id                                        "
              + "    union                                                                            "
              + "    select inicio_INH.patient_id,min(inicio_INH.data_inicio_tpi) data_inicio_tpi     "
              + "    from (                                                                           "
              + "             select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_tpi      "
              + "            from patient p                                                           "
              + "                inner join encounter e on p.patient_id = e.patient_id                "
              + "                inner join obs o on o.encounter_id = e.encounter_id                  "
              + "                inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id              "
              + "            where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656                                                       "
              + "                and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0                                                                                "
              + "                and obsInicioINH.obs_datetime between (:endDate - INTERVAL 7 MONTH)  and :endDate and  e.location_id=:location                                            "
              + "                group by p.patient_id                                                "
              + "            union                                                                    "
              + "             select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi     "
              + "            from    patient p                                                        "
              + "                inner join encounter e on p.patient_id=e.patient_id                  "
              + "                inner join obs o on o.encounter_id=e.encounter_id                    "
              + "                inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id              "
              + "            where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - INTERVAL 7 MONTH)  and :endDate 	                                          "
              + "                and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)                                                                 "
              + "                and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location                                                   "
              + "                group by p.patient_id                                                "
              + "            union                                                                    "
              + "                                                                                     "
              + "            (   select inicio.patient_id, inicio.data_inicio_tpi                     "
              + "                from (                                                               "
              + "                        select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi            "
              + "                        from    patient p                                            "
              + "                            inner join encounter e on p.patient_id=e.patient_id      "
              + "                            inner join obs o on o.encounter_id=e.encounter_id        "
              + "                            inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id  "
              + "                        where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - INTERVAL 7 MONTH)  and :endDate 	                              "
              + "                            and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)                                                           "
              + "                            and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location                                       "
              + "                            group by p.patient_id                                    "
              + "                        union                                                        "
              + "                        select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from    patient p                                                                                 "
              + "                            inner join encounter e on p.patient_id=e.patient_id      "
              + "                            inner join obs o on o.encounter_id=e.encounter_id        "
              + "                            left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id "
              + "                            and seguimentoTPT.concept_id =23987                      "
              + "                            and seguimentoTPT.value_coded in(1256,1257,1705,1267)    "
              + "                            and seguimentoTPT.voided =0)                             "
              + "                        where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - INTERVAL 7 MONTH)  and :endDate 	                               "
              + "                            and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location                                       "
              + "                            and seguimentoTPT.obs_id is null                         "
              + "                            group by p.patient_id                                    "
              + "                    )                                                                "
              + "                inicio                                                               "
              + "                left join                                                            "
              + "                (                                                                    "
              + "                    select p.patient_id,o.obs_datetime data_inicio_tpi               "
              + "                    from patient p                                                   "
              + "                        inner join encounter e on p.patient_id=e.patient_id          "
              + "                        inner join obs o on o.encounter_id=e.encounter_id            "
              + "                    where e.voided=0 and p.voided=0 and o.obs_datetime between (:endDate - INTERVAL 20 MONTH)  and :endDate 	                                             "
              + "                        and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location                                           "
              + "                    union                                                            "
              + "                    select p.patient_id,obsInicioINH.obs_datetime data_inicio_tpi from patient p       "
              + "                        inner join encounter e on p.patient_id = e.patient_id        "
              + "                            inner join obs o on o.encounter_id = e.encounter_id      "
              + "                            inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id  "
              + "                     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656                                             "
              + "                        and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0                                                                        "
              + "                        and obsInicioINH.obs_datetime between (:endDate - INTERVAL 20 MONTH)  and :endDate 	 and  e.location_id=:location                                  "
              + "                )                                                                    "
              + "                inicioAnterior on inicioAnterior.patient_id=inicio.patient_id        "
              + "                    and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 7 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day)                                      "
              + "                where inicioAnterior.patient_id is null                              "
              + "            )                                                                        "
              + "        )                                                                            "
              + "    inicio_INH group by inicio_INH.patient_id                                        "
              + ") inicio_TPT where inicio_TPT.patient_id is not null         "
              + ") inicio_TPT																																							"
              + "inner join ( 																																							"
              + "      	Select inicio_real.patient_id,min(data_inicio) data_inicio from( 																							"
              + "      		select p.patient_id,min(e.encounter_datetime) data_inicio from  patient p 																				"
              + "      			inner join encounter e on p.patient_id=e.patient_id 																								"
              + "      			inner join obs o on o.encounter_id=e.encounter_id 																									"
              + "      		where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 							"
              + "      			and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_datetime > '0000-00-00 00:00' group by p.patient_id 					"
              + "      		union 																																					"
              + "      		select p.patient_id,min(value_datetime) data_inicio from  patient p 																					"
              + "      			inner join encounter e on p.patient_id=e.patient_id 																								"
              + "      			inner join obs o on e.encounter_id=o.encounter_id 																									"
              + "      		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  o.concept_id=1190 												"
              + "      			and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location and o.value_datetime > '0000-00-00 00:00' group by p.patient_id "
              + "      		union 																																					"
              + "      		select pg.patient_id,min(date_enrolled) data_inicio from patient p 																						"
              + "      			inner join patient_program pg on p.patient_id=pg.patient_id 																						"
              + "      		where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location and date_enrolled > '0000-00-00 00:00' group by pg.patient_id "
              + "      		union 																																					"
              + "      		SELECT e.patient_id, MIN(e.encounter_datetime) AS data_inicio FROM patient p 																			"
              + "      			inner join encounter e on p.patient_id=e.patient_id WHERE 																							"
              + "      				p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_datetime > '0000-00-00 00:00' GROUP BY  p.patient_id "
              + "      		union 																																					"
              + "      		select p.patient_id,min(value_datetime) data_inicio from patient p 																						"
              + "      			inner join encounter e on p.patient_id=e.patient_id 																								"
              + "      			inner join obs o on e.encounter_id=o.encounter_id 																									"
              + "      		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 																					"
              + "      			and o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location and o.value_datetime > '0000-00-00 00:00' group by p.patient_id "
              + "      	) inicio_real group by patient_id 																															"
              + "    ) inicioTarv on inicioTarv.patient_id = inicio_TPT.patient_id 																									"
              + "																																										";

      switch (disaggregationType) {
        case NEWLY_ENROLLED:
          query =
              query
                  + "and inicio_TPT.data_inicio_tpi <= DATE_ADD(inicioTarv.data_inicio, INTERVAL 6 MONTH)";
          break;

        case PREVIOUSLY_ENROLLED:
          query =
              query
                  + " and inicio_TPT.data_inicio_tpi > DATE_ADD(inicioTarv.data_inicio, INTERVAL 6 MONTH)";
          break;
      }
      return query;
    }
  }
}
