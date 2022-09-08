package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.AgeRange;

public interface DSDQueries {

  class QUERY {

    public static final String findPatientsBetween2And9YearsOnArtFor12Months(AgeRange ageRange) {
      String query =
          "select inicio.patient_id 																																									"
              + "from(  																																													"
              + "	select inicio.patient_id,min(data_inicio) data_inicio  																																	"
              + "	from( 																							 																						"
              + "		select p.patient_id,min(e.encounter_datetime) data_inicio  																															"
              + "		from  patient p 																				 																					"
              + "			inner join encounter e on p.patient_id=e.patient_id 																								 							"
              + "			inner join obs o on o.encounter_id=e.encounter_id 																									 							"
              + "		where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 							 							"
              + "			and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_datetime > '0000-00-00 00:00' group by p.patient_id 					 							"
              + "		union 																																					 							"
              + "		select p.patient_id,min(value_datetime) data_inicio  																																"
              + "		from  patient p 																					 																				"
              + "			inner join encounter e on p.patient_id=e.patient_id 																								 							"
              + "			inner join obs o on e.encounter_id=o.encounter_id 																									 							"
              + "		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  o.concept_id=1190 												 							"
              + "			and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location and o.value_datetime > '0000-00-00 00:00' group by p.patient_id  					"
              + "		union 																																					 							"
              + "		select pg.patient_id,min(date_enrolled) data_inicio  																																"
              + "		from patient p 																						 																				"
              + "			inner join patient_program pg on p.patient_id=pg.patient_id 																						 							"
              + "		where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location and date_enrolled > '0000-00-00 00:00' group by pg.patient_id  				"
              + "		union 																																					 							"
              + "		select e.patient_id, MIN(e.encounter_datetime) AS data_inicio  																														"
              + "		from patient p 																			 																							"
              + "			inner join encounter e on p.patient_id=e.patient_id where 																							 							"
              + "				p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_datetime > '0000-00-00 00:00' GROUP BY  p.patient_id  "
              + "		union 																																					 							"
              + "		select p.patient_id,min(value_datetime) data_inicio  																																"
              + "		from patient p 																						 																				"
              + "			inner join encounter e on p.patient_id=e.patient_id 																								 							"
              + "			inner join obs o on e.encounter_id=o.encounter_id 																									 							"
              + "		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 																					 							"
              + "			and o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location and o.value_datetime > '0000-00-00 00:00' group by p.patient_id  "
              + "		)  																																													"
              + "		inicio group by patient_id  																																						"
              + "	) inicio 																																												"
              + "	inner join person pe on pe.person_id = inicio.patient_id 																																"
              + "where 																																													";

      switch (ageRange) {
        case CHILDREN:
          query =
              query
                  + "TIMESTAMPDIFF(YEAR,pe.birthdate,:endDate) between 2 and 9 and TIMESTAMPDIFF(MONTH, inicio.data_inicio, :endDate) >= 12 ";
          break;

        case ADULT:
          query =
              query
                  + "TIMESTAMPDIFF(YEAR,pe.birthdate,:endDate) >= 10 and TIMESTAMPDIFF(MONTH, inicio.data_inicio, :endDate) >= 6 ";
          break;
      }
      return query;
    }

    public static final String findPatientsByAgeRange() {

      return "select patient.patient_id from patient                         		"
          + "	inner join person on person.person_id = patient.patient_id		"
          + "where patient.voided = 0 and person.voided = 0 					"
          + "	and floor(datediff(:endDate,person.birthdate)/365) 			    ";
    }
  }
}
