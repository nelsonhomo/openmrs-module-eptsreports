/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

public interface IMR1Queries {

  public class QUERY {

    public static final String findPatientsEnrolledInArtCareDuringReportingPeriod =
        "select patient_id                                                                                       "
            + "from                                                                                                   "
            + "	(                                                                                                    "
            + "	select patient_id,min(art_enrolled_date) art_enrolled_date                                           "
            + "	from	(                                                                                            "
            + "			select p.patient_id, min(o.value_datetime) art_enrolled_date from patient p                  "
            + "				join encounter e on e.patient_id = p.patient_id            			                     "
            + "				join obs o on o.encounter_id = e.encounter_id								             "
            + "			where p.voided =0 and e.voided = 0 and o.voided = 0 				                         "
            + "				and e.encounter_type = 53 and o.concept_id =23808 and o.value_datetime is not null       "
            + "				and o.value_datetime <=(:endDate - INTERVAL 1 MONTH )                                    "
            + "              and e.location_id =:location                                                             "
            + "			group by p.patient_id																         "
            + "			union                                                                                        "
            + "			select patient.patient_id, min(patient_program.date_enrolled) art_enrolled_date from patient "
            + "				join patient_program on patient_program.patient_id = patient.patient_id      "
            + "				join program on program.program_id = patient_program.program_id	             "
            + "			where patient.voided = 0 and patient_program.voided = 0 and program.retired =0   "
            + "				and program.program_id = 1 and patient_program.date_enrolled is not null     "
            + "				and patient_program.date_enrolled <=(:endDate - INTERVAL 1 MONTH )		     "
            + "				and patient_program.location_id =:location								     "
            + "			group by patient.patient_id                                                      "
            + "		) art_init                                                                           "
            + "	group by patient_id                                                                      "
            + "	) art_init_real                                                                          "
            + "	where art_enrolled_date between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH ) ";

    public static final String findPatientsWhoAreBreastfeeding =
        "select patient_id from ( select inicio_real.patient_id,gravida_real.data_gravida, lactante_real.data_parto,"
            + "if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null, "
            + "if(max(gravida_real.data_gravida) is null,1, "
            + "if(max(lactante_real.data_parto) is null,2, "
            + "if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),1,2)))) decisao from ("
            + "select p.patient_id "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id = :location "
            + "union "
            + "select pg.patient_id from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id in (1,2) and date_enrolled between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + "union "
            + "Select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23891 and o.value_datetime is not null and o.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location )inicio_real "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select 	p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and pp.location_id=:location "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "select p.patient_id,data_colheita.value_datetime data_gravida from patient p                                                                                                                                                                       	"
            + "	inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                               	"
            + "	inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id                                                                                                                                                                           	"
            + "	inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                        	"
            + "where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982 																																"
            + "	and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                     																					"
            + "	and data_colheita.concept_id =23821 and data_colheita.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id= :location 														"
            + ") gravida_real on gravida_real.patient_id=inicio_real.patient_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and "
            + "e.encounter_type in (5,6) and o.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, obsART.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=:location and "
            + "obsART.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and "
            + "e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pg.patient_id,ps.start_date data_parto from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27 and ps.end_date is null and ps.start_date between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + ") lactante_real on lactante_real.patient_id=inicio_real.patient_id "
            + "where lactante_real.data_parto is not null or gravida_real.data_gravida is not null "
            + "group by inicio_real.patient_id ) gravidaLactante "
            + "inner join person pe on pe.person_id=gravidaLactante.patient_id "
            + "where decisao=1 and pe.voided=0 and pe.gender='F' ";

    public static final String findPatientsWhoArePregnantInAPeriod =
        "select patient_id from ( select inicio_real.patient_id,gravida_real.data_gravida, lactante_real.data_parto,"
            + "if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null, "
            + "if(max(gravida_real.data_gravida) is null,1, "
            + "if(max(lactante_real.data_parto) is null,2, "
            + "if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),1,2)))) decisao from ("
            + "select p.patient_id "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id = :location "
            + "union "
            + "select pg.patient_id from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id in (1,2) and date_enrolled between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + "union "
            + "Select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23891 and o.value_datetime is not null and o.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location )inicio_real "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select 	p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and pp.location_id=:location "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "select p.patient_id,data_colheita.value_datetime data_gravida from patient p                                                                                                                                                                       	"
            + "	inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                               	"
            + "	inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id                                                                                                                                                                           	"
            + "	inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                        	"
            + "where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982 																																"
            + "	and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                     																					"
            + "	and data_colheita.concept_id =23821 and data_colheita.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id= :location 														"
            + ") gravida_real on gravida_real.patient_id=inicio_real.patient_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and "
            + "e.encounter_type in (5,6) and o.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, obsART.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=:location and "
            + "obsART.value_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and "
            + "e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pg.patient_id,ps.start_date data_parto from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27 and ps.end_date is null and ps.start_date between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + ") lactante_real on lactante_real.patient_id=inicio_real.patient_id "
            + "where lactante_real.data_parto is not null or gravida_real.data_gravida is not null "
            + "group by inicio_real.patient_id ) gravidaLactante "
            + "inner join person pe on pe.person_id=gravidaLactante.patient_id "
            + "where decisao=2 and pe.voided=0 and pe.gender='F' ";

    public static final String findPatiensTransferredInByEndDate =
        "Select 	p.patient_id                                      "
            + "from 	patient p                                                                                             "
            + "		inner join encounter e on p.patient_id=e.patient_id                                                   "
            + "		inner join obs obsTrans on e.encounter_id=obsTrans.encounter_id and obsTrans.voided=0 and             "
            + "		obsTrans.concept_id=1369 and obsTrans.value_coded=1065                                                "
            + "		inner join obs obsTarv on e.encounter_id=obsTarv.encounter_id and obsTarv.voided=0 and                "
            + "		obsTarv.concept_id=6300 and obsTarv.value_coded in (6276,6275)                                        "
            + "		inner join obs obsInscricao on e.encounter_id=obsInscricao.encounter_id and obsInscricao.voided=0 and "
            + "		obsInscricao.concept_id=23891                                                                "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=53 and                                       "
            + "		obsInscricao.value_datetime<=:endDate and e.location_id=:location                            "
            + "union		                                                                                     "
            + "select 	minEstado.patient_id                                                                     "
            + "from 	(                                                                                        "
            + "			select pg.patient_id,min(ps.start_date) data_transferidode                               "
            + "			from 	patient p                                                                        "
            + "					inner join patient_program pg on p.patient_id=pg.patient_id                      "
            + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id       "
            + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0                                       "
            + "                 and ps.start_date<=:endDate and pg.location_id=:location                         "
            + "			group by p.patient_id                                                                    "
            + "		) minEstado                                                                                  "
            + "		inner join patient_program pg2 on pg2.patient_id=minEstado.patient_id                        "
            + "		inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id                "
            + "  where pg2.voided=0 and ps2.voided=0 and ps2.start_date=minEstado.data_transferidode and pg2.location_id=:location "
            + "   and ((pg2.program_id=1 and ps2.state=28) or (pg2.program_id=2 and ps2.state=29)) ";

    public static final String findPatientsWithClinicianConsultationInTheSameDiagnosisDay =
        "select diagnostico.patient_id                                                                        "
            + "from                                                                                                "
            + "(                                                                                                   "
            + "   Select  p.patient_id,o.obs_datetime data_diagnostico                                            "
            + "   from    patient p                                                                               "
            + "           inner join encounter e on p.patient_id=e.patient_id                                     "
            + "           inner join obs o on e.encounter_id=o.encounter_id                                       "
            + "   where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and                    "
            + "           o.concept_id=22772 and o.value_coded in (1030,1040) and                                 "
            + "           o.obs_datetime<=:endDate and e.location_id=:location                                    "
            + ") diagnostico                                                                                       "
            + "inner join encounter e on e.patient_id=diagnostico.patient_id                                       "
            + "where   e.encounter_type in (6,9) and date(e.encounter_datetime)=date(diagnostico.data_diagnostico) "
            + "        and e.voided=0 and e.location_id=:location                                                 "
            + "union                                                                                              "
            + "select diagnostico.patient_id                                                                      "
            + "from                                                                                               "
            + "(                                                                                                  "
            + "    Select  p.patient_id,o.obs_datetime data_diagnostico                                           "
            + "    from    patient p                                                                              "
            + "            inner join encounter e on p.patient_id=e.patient_id                                    "
            + "            inner join obs o on e.encounter_id=o.encounter_id                                      "
            + "            inner join obs obslocal on obslocal.encounter_id=e.encounter_id                        "
            + "    where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and                   "
            + "            o.concept_id=22772 and o.value_coded in (1030,1040) and                                "
            + "            obslocal.concept_id=23884 and obslocal.value_coded=6245 and obslocal.voided=0 and      "
            + "            o.obs_datetime<=:endDate and e.location_id=:location                                    "
            + ") diagnostico                                                                                       "
            + "inner join encounter e on e.patient_id=diagnostico.patient_id                                       "
            + "where   e.encounter_type in (6,9) and e.encounter_datetime between diagnostico.data_diagnostico     "
            + "  and (diagnostico.data_diagnostico + INTERVAL  7 DAY)                                              "
            + "        and e.voided=0 and e.location_id=:location                                                  ";
  }
}
