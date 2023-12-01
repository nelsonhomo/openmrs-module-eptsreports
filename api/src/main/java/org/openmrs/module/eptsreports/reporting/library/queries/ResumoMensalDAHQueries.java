package org.openmrs.module.eptsreports.reporting.library.queries;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class ResumoMensalDAHQueries {

  /**
   * Número total de activos em DAH em TARV, até ao fim do mês anterior
   *
   * @return String
   */
  public static String getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0() {

    String query =
        " 	select activeInDAH.patient_id from( "
            + " 	 	select p.patient_id,max(e.encounter_datetime) maxDataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime < :startDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + " 	) activeInDAH left join( "
            + " 	select dah.patient_id  from ( "
            + " 	select p.patient_id,max(e.encounter_datetime) maxDataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime < :startDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + "	) dah "
            + "	left join "
            + "             ( "
            + "                 select p.patient_id, o.obs_datetime from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                     join obs o on o.encounter_id=e.encounter_id "
            + "                 where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "                     and o.concept_id=1708 and o.value_coded in (1366,1707,1706) and e.location_id=:location "
            + "	)saidaDAHj on saidaDAHj.patient_id = dah.patient_id "
            + "	left join "
            + "             ( "
            + "                 select p.patient_id, o.value_datetime from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                     join obs o on o.encounter_id=e.encounter_id "
            + "                 where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "                     and o.concept_id=165386  and o.value_datetime < :startDate and e.location_id=:location "
            + "	)saidaDAHi on saidaDAHi.patient_id = dah.patient_id "
            + "	where ((saidaDAHj.obs_datetime >= dah.maxDataInicioDAH and  saidaDAHj.obs_datetime <= :startDate) "
            + "	or (saidaDAHi.value_datetime >= dah.maxDataInicioDAH and  saidaDAHi.value_datetime <= :startDate)) "
            + "	) saidasDAH on saidasDAH.patient_id = activeInDAH.patient_id "
            + "	where saidasDAH.patient_id is null ";

    return query;
  }

  /**
   * Com registo de “Data de Início no Modelo de DAH”, na Ficha de DAH, ocorrida durante o período
   *
   * @return String
   */
  public static String getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1() {

    String query =
        "	select p.patient_id "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate "
            + " 	and e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location ";

    return query;
  }
  
  public enum ARTSituation {
	  
      NEW_ENROLLED,

      RESTART,
      
      ACTIVE

    }
  
  /**
   * Situação do TARV no início do seguimento
   *
   * @return String
   */
  public static String findPatientsARTSituation(ARTSituation artSituation) {

    String query =
            "            select p.patient_id from patient p "
                    + "           join encounter e on p.patient_id=e.patient_id "
                    + "           join obs o on o.encounter_id=e.encounter_id "
                    + "       where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
                    + "           and o.concept_id=1255 and o.value_coded = %s "
                    + "           and e.encounter_datetime >= :startDate and e.encounter_datetime <= :endDate "
                    + "           and e.location_id=:location ";

    switch (artSituation) {
    case NEW_ENROLLED:
      query = String.format(query, 1256);
      break;
      
    case RESTART:
  	    query = String.format(query, 1705);
  	    break;
  	    
    case ACTIVE:
  	    query = String.format(query, 6275);
  	    break;
  }
    return query;
  }

  /**
   * Número de utentes em seguimento para Doença Avançada que saíram da abordagem durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientsWhoLeftDAHDuringReportPeriod4() {

    String query =
        "  select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=1708 and o.value_coded in (1366,1707,1706) "
            + "      and  o.obs_datetime >= :startDate and  o.obs_datetime <= :endDate and e.location_id=:location "
            + "      union "
            + "  select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=165386  and  o.value_datetime >= :startDate and  o.value_datetime <= :endDate and e.location_id=:location ";

    return query;
  }

  /**
   * 6 - Com registo de “Data de Início no Modelo de DAH”, na Ficha de DAH, ocorrida na coorte de 6
   * meses
   *
   * @return String
   */
  public static String getNumberOfPatientsWithDAHInSixMonthsCoorte() {

    String query =
        " 	 	select p.patient_id "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate "
            + " 	AND e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location ";

    return query;
  }

  /**
   * 6 - Número total de óbitos nos utentes com diagnóstico de Doença Avançada na coorte de 6 meses
   * após o início de seguimento
   *
   * @return String
   */
  public static String getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6() {

    String query =
        "		select dah.patient_id from ( "
            + " 	 	select p.patient_id,e.encounter_datetime "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate - INTERVAL 6 MONTH "
            + " 	AND e.encounter_datetime <= endDate - INTERVAL 6 MONTH - INTERVAL 1 DAY "
            + "	and e.location_id=:location "
            + "	) dah "
            + "	inner join ( "
            + "       select p.patient_id, o.obs_datetime from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and p.voided=0 and e.encounter_type = 90 and "
            + "      o.voided = 0 and o.concept_id=1708 and o.value_coded = 1366 and e.location_id=:location "
            + "	) saidaDAH on saidaDAH.patient_id = dah.patient_id "
            + "	where saidaDAH.obs_datetime >= dah.encounter_datetime and saidaDAH.obs_datetime <= endDate ";

    return query;
  }

  /**
   * 8 - Número de utentes com pedido de CD4 de rastreio durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientsRequestingCD4ScreeningDuringThePeriod8() {

    String query =
        "	      Select p.patient_id from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and o.concept_id = 23722 and o.value_coded=1695 and e.encounter_type=6 and o.voided=0 "
            + "and e.encounter_datetime >= :startDate and e.encounter_datetime <= :endDate "
            + "and e.location_id=:location ";

    return query;
  }

  /**
   * 9 -Número de utentes com resultado de CD4 de rastreio disponível durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientWithCD4ResultDuringThePeriod9() {

    String query =
        "	Select p.patient_id "
            + "	From patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs o on e.encounter_id=o.encounter_id "
            + "	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type = 90 and o.value_numeric is not null "
            + "	and e.location_id=:location and o.obs_datetime >= :startDate and o.obs_datetime <= :endDate "
            + "	union "
            + "	Select p.patient_id "
            + "	From patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs o on e.encounter_id=o.encounter_id "
            + "	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type = 6 and o.value_numeric is not null "
            + "	and e.location_id=:location and e.encounter_datetime >= :startDate and e.encounter_datetime <= :endDate ";

    return query;
  }

  /**
   * 10) Número de utentes com resultado de CD4 baixo* durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientWithLowCD4ResultDuringThePeriod10() {

    String query =
        "	select f.patient_id "
            + "from "
            + "( "
            + "select p.patient_id, o.obs_datetime,o.value_numeric, e.encounter_type,e.encounter_id "
            + "   from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "  where (p.voided=0 and  e.voided=0 and e.encounter_type in (6,90) and o.concept_id=1695  and o.voided=0 and o.value_numeric is not null) "
            + "        and  e.location_id=:location and o.obs_datetime between :startDate and :endDate "
            + ")f "
            + "inner join person pe on pe.person_id=f.patient_id "
            + "WHERE       (TIMESTAMPDIFF(year,pe.birthdate,:endDate)>=5 and f.value_numeric<200 ) "
            + "        OR  ((TIMESTAMPDIFF(year,pe.birthdate,:endDate) between 1 and 4) and f.value_numeric<500 ) "
            + "        OR  (TIMESTAMPDIFF(year,pe.birthdate,:endDate)<1 and f.value_numeric<750 ) "
            + "GROUP BY f.patient_id ";

    return query;
  }
  
  public static final String
  findPatientsWhoHaveSpecificExamTestResults(
      TypesOfExams typeOfExam) {

String query =
"select p.patient_id "
        + "   from patient p "
        + "inner join encounter e on e.patient_id=p.patient_id "
        + "inner join obs o on o.encounter_id=e.encounter_id "
        + "  where p.voided=0 and  e.voided=0 and e.encounter_type = 90 and o.concept_id=%s  and o.voided=0 and o.value_coded in (%s) "
        + "        and  e.location_id=:location and o.obs_datetime between :startDate and :endDate "
        + "        union "
        + "select p.patient_id "
        + "   from patient p "
        + "inner join encounter e on e.patient_id=p.patient_id "
        + "inner join obs o on o.encounter_id=e.encounter_id "
        + "  where p.voided=0 and  e.voided=0 and e.encounter_type = 6 and o.concept_id=%s  and o.voided=0 and o.value_coded in (%s) "
        + "        and  e.location_id=:location and e.encounter_datetime between :startDate and :endDate ";

switch (typeOfExam) {
  case TBLAM:
    query = String.format(query, 23951, StringUtils.join(Arrays.asList(703, 664), ","));
    break;
    
  case TBLAM_POS:
	    query = String.format(query, 23951, 703);
	    break;

  case CRAG_SORO:
    query = String.format(query, 165390 , StringUtils.join(Arrays.asList(703, 664), ","));
    break;
    
  case CRAG_SORO_POS:
	    query = String.format(query, 165390, 703);
	    break;
    
  case CRAG_LCR:
	    query = String.format(query, 165362 , StringUtils.join(Arrays.asList(703, 664), ","));
	    break;
	    
  case CRAG_LCR_POS:
	    query = String.format(query, 165362 , 703);
	    break;
}
return query;
}
  
  
  public enum MCCTreatment {
	  
      MCC_PREVENTIVE_TREATMENT,

      MCC_TREATMENT,
      
      QUIMIOTHERAPHY_CICLE_ONE

    }
  
  /**
   * 16) Número de utentes com CrAg sérico positivo que iniciaram tratamento preventivo de MCC durante o mês
   *
   * @return String
   */
  public static String findPatientsWhoInMCCTreatment(MCCTreatment treatment) {

    String query =
    		"	select p.patient_id from patient p " +
    				"	join encounter e on p.patient_id=e.patient_id " +
    				"	join obs o on o.encounter_id=e.encounter_id " +
    				"	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 " +
    				"	and o.concept_id=%s and " +
    				"	o.value_datetime >= :startDate and o.value_datetime <= :endDate " +
    				"	and e.location_id=:location ";

    switch (treatment) {
    case MCC_PREVENTIVE_TREATMENT:
      query = String.format(query, 165393);
      break;
      
    case MCC_TREATMENT:
  	    query = String.format(query, 165363);
  	    break;
  	    
    case QUIMIOTHERAPHY_CICLE_ONE:
  	    query = String.format(query, 165379);
  	    break;
  }
    return query;
  }
  
  /**
   * 18) Número de utentes com novo diagnóstico de sarcoma de kaposi e com indicação para quimioterapia durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriod18() {

    String query =
    		"	select p.patient_id from patient p " +
    				"	join encounter e on p.patient_id=e.patient_id " +
    				"	join obs o on o.encounter_id=e.encounter_id " +
    				"     join obs oQuimio on oQuimio.encounter_id = e.encounter_id " +
    				"	where e.voided=0 and o.voided=0 and oQuimio.voided = 0 and p.voided=0 and e.encounter_type = 90 " +
    				"	and o.concept_id=1413 and oQuimio.concept_id = 20294 and oQuimio.value_coded = 1065 " +
    				"	and o.value_datetime >= :startDate and o.value_datetime <= :endDate " +
    				"	and e.location_id=:location ";

    return query;
  }
  
  /**
   * O sistema irá identificar mulheres grávidas HIV+, para desagregação dos indicadores 8 a 19
   *
   * @return String
   */
  public static String findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate() {

    String query =
    		"Select p.patient_id from person pe " +
    				"inner join patient p on pe.person_id=p.patient_id " +
    				"inner join encounter e on p.patient_id=e.patient_id " +
    				"inner join obs o on e.encounter_id=o.encounter_id " +
    				"where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F' and " +
    				"o.concept_id=1982 and o.value_coded=1065 and e.encounter_datetime " +
    				"between date_add(date_sub(:startDate, interval 3 month), interval 1 day) and :endDate ";

    return query;
  }
  
  /**
   * RF30 O sistema irá identificar utentes em seguimento de DAH, para desagregação dos indicadores 10 a 19
   *
   * @return String
   */
  public static String findPatientsInDAHExcludingPatientsWithSaidaDAH() {

    String query =
    		"	select final.patient_id from ( " +
    				"	select inicioDAH.patient_id, inicioDAH.dataInicioDAH, saidasDAH.dataSaidaDAH from ( " +
    				"	select p.patient_id,max(e.encounter_datetime) dataInicioDAH " +
    				" 	from " +
    				"  	patient p inner join encounter e on p.patient_id=e.patient_id " +
    				" 	where p.voided=0 and e.voided=0 and e.encounter_type=90 " +
    				" 	and e.encounter_datetime <= :endDate " +
    				"	and e.location_id=:location " +
    				"	group by p.patient_id " +
    				"	) inicioDAH left join " +
    				"	( " +
    				"  select p.patient_id, o.obs_datetime dataSaidaDAH from patient p " +
    				"      join encounter e on p.patient_id=e.patient_id " +
    				"      join obs o on o.encounter_id=e.encounter_id " +
    				"  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 " +
    				"      and o.concept_id=1708 and o.value_coded in (1366,1707,1706) " +
    				"      and e.location_id=:location " +
    				"      union " +
    				"  select p.patient_id, o.value_datetime dataSaidaDAH from patient p " +
    				"      join encounter e on p.patient_id=e.patient_id " +
    				"      join obs o on o.encounter_id=e.encounter_id " +
    				"  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 " +
    				"      and o.concept_id=165386 and e.location_id=:location " +
    				"      )saidasDAH on saidasDAH.patient_id = inicioDAH.patient_id " +
    				"      and saidasDAH.dataSaidaDAH between inicioDAH.dataInicioDAH and :endDate " +
    				"      ) final " +
    				"        where final.dataSaidaDAH is null ";

    return query;
  }
  
}
