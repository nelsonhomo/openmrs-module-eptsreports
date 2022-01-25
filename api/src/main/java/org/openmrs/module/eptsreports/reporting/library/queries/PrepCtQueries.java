/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

public interface PrepCtQueries {

  class QUERY {

    public static final String findClientsNewlyEnrolledInPrep =
        " select patient_id "
            + " from (	select patient_id, min(data_inicio_prep) data_inicio_prep "
            + " 		from	(	select client.patient_id, min(o.obs_datetime) data_inicio_prep "
            + " 				from patient client "
            + " 					inner join encounter e on e.patient_id = client.patient_id "
            + " 					inner join obs o on o.encounter_id = e.encounter_id "
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 "
            + " 					and e.encounter_type = 80   and o.concept_id =165296 and o.value_coded = 1256 "
            + " 					and e.location_id = :location and o.obs_datetime < :startDate "
            + " 					group by client.patient_id "
            + " 				union "
            + " 				select client.patient_id, min(o.value_datetime) data_inicio_prep "
            + " 				from patient client "
            + " 					inner join encounter e on e.patient_id = client.patient_id "
            + " 					inner join obs o on o.encounter_id = e.encounter_id "
            + " 				where client.voided = 0 and e.voided = 0 and o.voided = 0 "
            + " 					and e.encounter_type = 80   and o.concept_id =165211 and e.location_id = :location "
            + " 					and o.value_datetime < :startDate "
            + " 					group by client.patient_id "
            + " 			) "
            + " 		inicio_prep group by inicio_prep.patient_id "
            + " 	) "
            + " prep_new "
            + "		inner join person pe on pe.person_id=prep_new.patient_id "
            + "where prep_new.data_inicio_prep < :startDate "
            + "		 and ((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:startDate) >= 15	) or pe.birthdate is  null	) ";

    public static final String findClientsWhoWhereTransferredInBeforeReportingPeriod =
        " select patient_id "
            + " from (	select minState.patient_id ,data_transferencia "
            + " 		from	(	select client.patient_id, pg.patient_program_id, min(ps.start_date) as data_transferencia "
            + " 				from patient client "
            + " 		          	inner join patient_program pg on pg.patient_id = client.patient_id "
            + " 		             	inner join patient_state ps on ps.patient_program_id = pg.patient_program_id "
            + " 		       	where client.voided = 0 and  pg.voided = 0 and ps.voided = 0 and pg.program_id = 25 "
            + " 		       		and location_id = :location  and ps.start_date < :startDate "
            + " 		             	group by pg.patient_program_id "
            + " 		     ) "
            + " 		minState "
            + " 			inner join patient_state ps on ps.patient_program_id = minState.patient_program_id "
            + " 		where ps.start_date=minState.data_transferencia and ps.state=76 and ps.voided=0 "
            + " 		union "
            + " 		select client.patient_id, min(e.encounter_datetime) data_transferencia "
            + " 		from patient client "
            + " 			inner join encounter e on e.patient_id = client.patient_id "
            + " 			inner join obs o on o.encounter_id = e.encounter_id "
            + " 		where client.voided = 0 and e.voided = 0 and o.voided = 0 "
            + " 			and e.encounter_type = 80   and o.concept_id =1594 and o.value_coded =1369  and e.location_id = :location "
            + " 			and e.encounter_datetime < :startDate "
            + " 			group by client.patient_id "
            + " 	) transferido_de "
            + "inner join person pe on pe.person_id=transferido_de.patient_id "
            + "    				where "
            + "    			((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:startDate) >= 15) or pe.birthdate is  null) ";

    public static final String findClientsWhoWhereTransferredInDuringReportingPeriod =
        " select patient_id "
            + " from (	select minState.patient_id ,data_transferencia "
            + " 		from	(	select client.patient_id, pg.patient_program_id, min(ps.start_date) as data_transferencia "
            + " 				from patient client "
            + " 		          	inner join patient_program pg on pg.patient_id = client.patient_id "
            + " 		             	inner join patient_state ps on ps.patient_program_id = pg.patient_program_id "
            + " 		       	where client.voided = 0 and  pg.voided = 0 and ps.voided = 0 and pg.program_id = 25 "
            + " 		       		and location_id = :location  and ps.start_date >= :startDate and  ps.start_date <= :endDate "
            + " 		             	group by pg.patient_program_id "
            + " 		     ) "
            + " 		minState "
            + " 			inner join patient_state ps on ps.patient_program_id = minState.patient_program_id "
            + " 		where ps.start_date=minState.data_transferencia and ps.state=76 and ps.voided=0 "
            + " 		union "
            + " 		select client.patient_id, min(e.encounter_datetime) data_transferencia "
            + " 		from patient client "
            + " 			inner join encounter e on e.patient_id = client.patient_id "
            + " 			inner join obs o on o.encounter_id = e.encounter_id "
            + " 		where client.voided = 0 and e.voided = 0 and o.voided = 0 "
            + " 			and e.encounter_type = 80   and o.concept_id =1594 and o.value_coded =1369  and e.location_id = :location "
            + " 			and e.encounter_datetime >= :startDate and   e.encounter_datetime <= :endDate "
            + " 			group by client.patient_id "
            + " 	) transferido_de "
            + "inner join person pe on pe.person_id=transferido_de.patient_id "
            + "    				where "
            + "    			((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:startDate) >= 15) or pe.birthdate is  null) ";

    public static final String findClientsWhoReinitiatedPrep =
        "select patient_id from ( "
            + "select p.patient_id, max(obsReinitiated.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs obsReinitiated on e.encounter_id= obsReinitiated.encounter_id "
            + "where e.voided=0 and obsReinitiated.voided=0 and p.voided=0 "
            + "and e.encounter_type=80 and obsReinitiated.concept_id=165296 and obsReinitiated.value_coded=1705 and obsReinitiated.obs_datetime  >=:startDate "
            + "and obsReinitiated.obs_datetime <=:endDate "
            + "and e.location_id=:location group by p.patient_id "
            + ") reinicio "
            + "inner join person pe on pe.person_id=reinicio.patient_id "
            + "    				where "
            + "    			((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:startDate ) >= 15) or pe.birthdate is  null) ";

    public static final String findClientsWhoContinuePrep =
        "select patient_id from ( "
            + "select p.patient_id, max(obsContinued.obs_datetime) data_continuacao from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs obsContinued on e.encounter_id= obsContinued.encounter_id "
            + "where e.voided=0 and obsContinued.voided=0 and p.voided=0 "
            + "and e.encounter_type=80 and obsContinued.concept_id=165296 and obsContinued.value_coded=1257 and obsContinued.obs_datetime  >=:startDate "
            + "and obsContinued.obs_datetime <=:endDate and e.location_id=:location group by p.patient_id "
            + ") continuePrep "
            + "inner join person pe on pe.person_id=continuePrep.patient_id "
            + "    				where "
            + "    			((pe.birthdate is not null and timestampdiff(year,pe.birthdate,:startDate ) >= 15) or pe.birthdate is  null) ";

    public static final String findClientsWithPregnancyStatusDuringReportingPeriod =
        "select patient_id from( "
            + "select consulta.patient_id,gravida.data_gravida, lactante.data_lactante, "
            + "            if(gravida.data_gravida is null and lactante.data_lactante is null,null, "
            + "            if(gravida.data_gravida is null,2, "
            + "            if(lactante.data_lactante is null,1, "
            + "            if(gravida.data_gravida>lactante.data_lactante,1,if(gravida.data_gravida=lactante.data_lactante,1,2))))) decisao "
            + " from "
            + "(select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) data_consulta "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (80,81) and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id = :location "
            + "group by patient_id "
            + ") prep group by patient_id "
            + ") consulta "
            + "left join "
            + "(select * from ( "
            + "select * from ( "
            + " Select p.patient_id, max(e.encounter_datetime) data_gravida, 1 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and "
            + "concept_id=165223 and value_coded=1982 and e.encounter_type=81 "
            + "and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "Select p.patient_id, max(e.encounter_datetime) data_gravida, 2 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + ") pregnant order by patient_id,data_gravida desc, ordemSource "
            + ")final group by patient_id "
            + ") gravida on gravida.patient_id = consulta.patient_id "
            + "left join "
            + "( "
            + "select * from ( "
            + "select * from ( "
            + " Select p.patient_id, max(e.encounter_datetime) data_lactante, 1 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and "
            + "concept_id=165223 and value_coded=6332 and e.encounter_type=81 "
            + "and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "Select p.patient_id, max(e.encounter_datetime) data_lactante, 2 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + ") lactante order by patient_id,data_lactante desc, ordemSource "
            + ")final group by patient_id "
            + ") lactante on lactante.patient_id = consulta.patient_id where (lactante.data_lactante is not null or gravida.data_gravida is not null) "
            + ") final "
            + "where decisao = 1 ";

    public static final String findClientsWithBreastfeedingStatusDuringReportingPeriod =
        "select patient_id from( "
            + "select consulta.patient_id,gravida.data_gravida, lactante.data_lactante, "
            + "            if(gravida.data_gravida is null and lactante.data_lactante is null,null, "
            + "            if(gravida.data_gravida is null,2, "
            + "            if(lactante.data_lactante is null,1, "
            + "            if(gravida.data_gravida>lactante.data_lactante,1,if(gravida.data_gravida=lactante.data_lactante,1,2))))) decisao "
            + " from "
            + "(select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) data_consulta "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (80,81) and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id = :location "
            + "group by patient_id "
            + ") prep group by patient_id "
            + ") consulta "
            + "left join "
            + "(select * from ( "
            + "select * from ( "
            + " Select p.patient_id, max(e.encounter_datetime) data_gravida, 1 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and "
            + "concept_id=165223 and value_coded=1982 and e.encounter_type=81 "
            + "and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "Select p.patient_id, max(e.encounter_datetime) data_gravida, 2 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + ") pregnant order by patient_id,data_gravida desc, ordemSource "
            + ")final group by patient_id "
            + ") gravida on gravida.patient_id = consulta.patient_id "
            + "left join "
            + "( "
            + "select * from ( "
            + "select * from ( "
            + " Select p.patient_id, max(e.encounter_datetime) data_lactante, 1 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and "
            + "concept_id=165223 and value_coded=6332 and e.encounter_type=81 "
            + "and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + "union "
            + "Select p.patient_id, max(e.encounter_datetime) data_lactante, 2 ordemSource from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 "
            + "and e.encounter_type=80 and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by patient_id "
            + ") lactante order by patient_id,data_lactante desc, ordemSource "
            + ")final group by patient_id "
            + ") lactante on lactante.patient_id = consulta.patient_id where (lactante.data_lactante is not null or gravida.data_gravida is not null) "
            + ") final "
            + "where decisao = 2 ";

    public static final String findClientsWithIndeterminateTestResult =
        "select prep.patient_id from ( "
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
            + "and semDataTesteFichaInicial.patient_id  is null ";

    public static final String findClientsWithNegativeTestResult =
        "select patient_id from ( "
            + "select patient_id, max(data_negativo) data_resultado from ( "
            + "select p.patient_id, max(obsTestResult.obs_datetime) data_negativo from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
            + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
            + "and e.encounter_type=81 and obsTestResult.concept_id=1040 and obsTestResult.value_coded=664 and obsTestResult.obs_datetime  >=:startDate "
            + "and obsTestResult.obs_datetime <=:endDate and obsTestResult.voided = 0 "
            + "and e.location_id=:location group by p.patient_id "
            + "union "
            + " select p.patient_id, max(obsTestResult.value_datetime) data_negativo from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
            + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
            + "and e.encounter_type=80 and obsTestResult.concept_id=165194 and obsTestResult.value_datetime  >=:startDate "
            + "and obsTestResult.value_datetime <=:endDate "
            + "and e.encounter_datetime >=:startDate and e.encounter_datetime <=:endDate and e.location_id=:location group by p.patient_id "
            + ") negativo group by patient_id "
            + ") final group by patient_id ";

    public static final String findClientsWithPositiveTestResult =
        "select patient_id from ( "
            + "select p.patient_id, max(obsTestResult.obs_datetime) data_positivo from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs obsTestResult on e.encounter_id= obsTestResult.encounter_id "
            + "where e.voided=0 and obsTestResult.voided=0 and p.voided=0 "
            + "and e.encounter_type=81 and obsTestResult.concept_id=1040 and obsTestResult.value_coded=703 and obsTestResult.obs_datetime  >=:startDate "
            + "and obsTestResult.obs_datetime <=:endDate and obsTestResult.voided = 0 "
            + "and e.location_id=:location group by p.patient_id "
            + ") final group by patient_id ";

    public static final String findClientsWithAtLeastOneFollowUpVisitInFichaSeguimento =
        "select "
            + "p.patient_id "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=81 "
            + "and e.encounter_datetime >=:startDate "
            + "and e.encounter_datetime <=:endDate "
            + "and e.location_id=:location "
            + "group by p.patient_id ";
  }
}