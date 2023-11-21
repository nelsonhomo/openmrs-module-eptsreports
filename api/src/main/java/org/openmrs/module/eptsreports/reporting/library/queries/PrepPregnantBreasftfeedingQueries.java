package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsReportConstants.PregnantOrBreastfeedingWomen;

public class PrepPregnantBreasftfeedingQueries {
  public static final String findPatientsWhoArePregnantOrBreastfeeding(
      final PregnantOrBreastfeedingWomen womenState) {

    String query =
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
            + ") final ";

    switch (womenState) {
      case PREGNANTWOMEN:
        query = query + "where decisao=1 ";
        break;

      case BREASTFEEDINGWOMEN:
        query = query + "where decisao=2 ";
        break;
    }

    return query;
  }
}
