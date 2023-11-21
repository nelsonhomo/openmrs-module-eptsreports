/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

public interface PrepNewQueries {

  class QUERY {

    public static final String findClientsNewlyEnrolledInPrep =
        "														"
            + " select patient_id  																						"
            + " from (	select patient_id, min(data_inicio_prep) data_inicio_prep 										"
            + " 		from	(	select client.patient_id, min(o.obs_datetime) data_inicio_prep   					"
            + " 				from patient client 																	"
            + " 					inner join encounter e on e.patient_id = client.patient_id 							"
            + " 					inner join obs o on o.encounter_id = e.encounter_id 								"
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 								"
            + " 					and e.encounter_type = 80   and o.concept_id =165296 and o.value_coded = 1256 		"
            + " 					and e.location_id = :location and o.obs_datetime <= :endDate 						"
            + " 					group by client.patient_id 															"
            + " 				union 																					"
            + " 				select client.patient_id, min(o.value_datetime) data_inicio_prep  						"
            + " 				from patient client 																	"
            + " 					inner join encounter e on e.patient_id = client.patient_id 							"
            + " 					inner join obs o on o.encounter_id = e.encounter_id 								"
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 								"
            + " 					and e.encounter_type = 80   and o.concept_id =165211 and e.location_id = :location  "
            + " 					and o.value_datetime <= :endDate 													"
            + " 					group by client.patient_id 															"
            + " 			)  																							"
            + " 		inicio_prep group by inicio_prep.patient_id 													"
            + " 	)  																									"
            + " prep_new 																								"
            + "		inner join person pe on pe.person_id=prep_new.patient_id											"
            + "where prep_new.data_inicio_prep between :startDate and :endDate 											"
            + "		 and ((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:endDate) >= 15	) or pe.birthdate is  null	)			";

    public static final String findClientsWhoWhereTransferredIn =
        "														"
            + " select patient_id  																										"
            + " from (	select minState.patient_id ,data_transferencia 																	"
            + " 		from	(	select client.patient_id, pg.patient_program_id, min(ps.start_date) as data_transferencia   		"
            + " 				from patient client   																					"
            + " 		          	inner join patient_program pg on pg.patient_id = client.patient_id  								"
            + " 		             	inner join patient_state ps on ps.patient_program_id = pg.patient_program_id  					"
            + " 		       	where client.voided = 0 and  pg.voided = 0 and ps.voided = 0 and pg.program_id = 25  					"
            + " 		       		and location_id = :location  and ps.start_date <= :endDate  										"
            + " 		             	group by pg.patient_program_id 																	"
            + " 		     )  																										"
            + " 		minState  																										"
            + " 			inner join patient_state ps on ps.patient_program_id = minState.patient_program_id  						"
            + " 		where ps.start_date=minState.data_transferencia and ps.state=76 and ps.voided=0  								"
            + " 		union 																											"
            + " 		select client.patient_id, min(e.encounter_datetime) data_transferencia  										"
            + " 		from patient client 																							"
            + " 			inner join encounter e on e.patient_id = client.patient_id 													"
            + " 			inner join obs o on o.encounter_id = e.encounter_id 														"
            + " 		where client.voided = 0 and e.voided = 0 and o.voided = 0 														"
            + " 			and e.encounter_type = 80   and o.concept_id =1594 and o.value_coded =1369  and e.location_id = :location  	"
            + " 			and e.encounter_datetime <= :endDate 																		"
            + " 			group by client.patient_id 																					"
            + " 	) 																													"
            + " transferido_para 																										";

    public static final String findClientsNewlyEnrolledInPrepByGenderAndAgeRange =
        "														"
            + " select patient_id  																						"
            + " from (	select patient_id, min(data_inicio_prep) data_inicio_prep 										"
            + " 		from	(	select client.patient_id, min(o.obs_datetime) data_inicio_prep   					"
            + " 				from patient client 																	"
            + " 					inner join encounter e on e.patient_id = client.patient_id 							"
            + " 					inner join obs o on o.encounter_id = e.encounter_id 								"
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 								"
            + " 					and e.encounter_type = 80   and o.concept_id =165296 and o.value_coded = 1256 		"
            + " 					and e.location_id = :location and o.obs_datetime <= :endDate 						"
            + " 					group by client.patient_id 															"
            + " 				union 																					"
            + " 				select client.patient_id, min(o.value_datetime) data_inicio_prep  						"
            + " 				from patient client 																	"
            + " 					inner join encounter e on e.patient_id = client.patient_id 							"
            + " 					inner join obs o on o.encounter_id = e.encounter_id 								"
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 								"
            + " 					and e.encounter_type = 80   and o.concept_id =165211 and e.location_id = :location  "
            + " 					and o.value_datetime <= :endDate 													"
            + " 					group by client.patient_id 															"
            + " 			)  																							"
            + " 		inicio_prep group by inicio_prep.patient_id 													"
            + " 	)  																									"
            + " prep_new 																								"
            + "		inner join person pe on pe.person_id=prep_new.patient_id											"
            + "where prep_new.data_inicio_prep between :startDate and :endDate 											"
            + "		 and pe.birthdate is not null and pe.birthdate is not null and pe.gender='%s' and timestampdiff(year,pe.birthdate,prep_new.data_inicio_prep) between %s and %s ";

    public static final String findPrepNewClientsWithPregnantStatusDuringReportingPeriod =
        "select patient_id from( "
            + "select consulta.patient_id,gravida.data_gravida, lactante.data_lactante, "
            + "            if(gravida.data_gravida is null and lactante.data_lactante is null,null, "
            + "            if(gravida.data_gravida is null,2, "
            + "            if(lactante.data_lactante is null,1, "
            + "            if(gravida.data_gravida<lactante.data_lactante,1,if(gravida.data_gravida=lactante.data_lactante,1,2))))) decisao "
            + " from "
            + "(select patient_id from ( "
            + "select p.patient_id, min(e.encounter_datetime) data_consulta "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type = 80 and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id = :location "
            + "group by patient_id "
            + ") prep group by patient_id "
            + ") consulta "
            + "left join "
            + "(select * from ( "
            + "select * from ( "
            + "Select p.patient_id, min(e.encounter_datetime) data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "select minkp.patient_id,o.obs_datetime from ( "
            + "Select p.patient_id,min(e.encounter_datetime) minkpdate from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + ") minkp "
            + "inner join encounter e on e.patient_id=minkp.patient_id and minkp.minkpdate=e.encounter_datetime "
            + "inner join obs o on o.encounter_id=e.encounter_id and minkp.minkpdate=o.obs_datetime "
            + "inner join person pe on pe.person_id=minkp.patient_id "
            + "where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "AND o.value_coded=1982 "
            + ") pregnant order by patient_id,data_gravida asc "
            + ")final group by patient_id "
            + ") gravida on gravida.patient_id = consulta.patient_id "
            + "left join "
            + "( "
            + "select * from ( "
            + "select * from ( "
            + "Select p.patient_id, min(e.encounter_datetime) data_lactante from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "select minkp.patient_id,o.obs_datetime from ( "
            + "Select p.patient_id,min(e.encounter_datetime) minkpdate from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + ") minkp "
            + "inner join encounter e on e.patient_id=minkp.patient_id and minkp.minkpdate=e.encounter_datetime "
            + "inner join obs o on o.encounter_id=e.encounter_id and minkp.minkpdate=o.obs_datetime "
            + "inner join person pe on pe.person_id=minkp.patient_id "
            + "where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "AND o.value_coded=6332 "
            + ") lactante order by patient_id,data_lactante asc "
            + ")final group by patient_id "
            + ") lactante on lactante.patient_id = consulta.patient_id where (lactante.data_lactante is not null or gravida.data_gravida is not null) "
            + ") final "
            + "where decisao = 1 ";

    public static final String findPrepNewClientsWithBreastfeedingStatusDuringReportingPeriod =
        "select patient_id from( "
            + "select consulta.patient_id,gravida.data_gravida, lactante.data_lactante, "
            + "            if(gravida.data_gravida is null and lactante.data_lactante is null,null, "
            + "            if(gravida.data_gravida is null,2, "
            + "            if(lactante.data_lactante is null,1, "
            + "            if(gravida.data_gravida<lactante.data_lactante,1,if(gravida.data_gravida=lactante.data_lactante,1,2))))) decisao "
            + " from "
            + "(select patient_id from ( "
            + "select p.patient_id, min(e.encounter_datetime) data_consulta "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type = 80 and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id = :location "
            + "group by patient_id "
            + ") prep group by patient_id "
            + ") consulta "
            + "left join "
            + "(select * from ( "
            + "select * from ( "
            + "Select p.patient_id, min(e.encounter_datetime) data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "select minkp.patient_id,o.obs_datetime from ( "
            + "Select p.patient_id,min(e.encounter_datetime) minkpdate from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + ") minkp "
            + "inner join encounter e on e.patient_id=minkp.patient_id and minkp.minkpdate=e.encounter_datetime "
            + "inner join obs o on o.encounter_id=e.encounter_id and minkp.minkpdate=o.obs_datetime "
            + "inner join person pe on pe.person_id=minkp.patient_id "
            + "where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "AND o.value_coded=1982 "
            + ") pregnant order by patient_id,data_gravida asc "
            + ")final group by patient_id "
            + ") gravida on gravida.patient_id = consulta.patient_id "
            + "left join "
            + "( "
            + "select * from ( "
            + "select * from ( "
            + "Select p.patient_id, min(e.encounter_datetime) data_lactante from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "select minkp.patient_id,o.obs_datetime from ( "
            + "Select p.patient_id,min(e.encounter_datetime) minkpdate from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=165196 and  e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + ") minkp "
            + "inner join encounter e on e.patient_id=minkp.patient_id and minkp.minkpdate=e.encounter_datetime "
            + "inner join obs o on o.encounter_id=e.encounter_id and minkp.minkpdate=o.obs_datetime "
            + "inner join person pe on pe.person_id=minkp.patient_id "
            + "where o.concept_id=165196 and o.voided=0  and e.encounter_type=80 and e.voided=0 and e.location_id=:location and pe.voided=0 "
            + "AND o.value_coded=6332 "
            + ") lactante order by patient_id,data_lactante asc "
            + ")final group by patient_id "
            + ") lactante on lactante.patient_id = consulta.patient_id where (lactante.data_lactante is not null or gravida.data_gravida is not null) "
            + ") final "
            + "where decisao = 1 ";
  }
}
