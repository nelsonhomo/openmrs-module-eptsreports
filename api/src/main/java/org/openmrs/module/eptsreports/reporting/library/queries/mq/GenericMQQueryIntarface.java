package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface GenericMQQueryIntarface {

  class QUERY {
    public static final String findPatientsWhoAreNewlyEnrolledOnARTByAgeRenge(
        int startAge, int endAge) {

      final String sql =
          " SELECT patient_id FROM ( "
              + " SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
              + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
              + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
              + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
              + " GROUP BY p.patient_id "
              + " ) art_start "
              + " GROUP BY patient_id "
              + " ) tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,art_start_date)) BETWEEN %s  AND %s "
              + "AND birthdate IS NOT NULL and pe.voided = 0  AND art_start_date BETWEEN :startInclusionDate AND :endInclusionDate ";

      return String.format(sql, startAge, endAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonth(
        int startAge, int endAge) {

      final String sql =
          " SELECT patient_id FROM ( "
              + " SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
              + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
              + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
              + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
              + " GROUP BY p.patient_id "
              + " ) art_start "
              + " GROUP BY patient_id "
              + " ) tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(month,birthdate,art_start_date)) BETWEEN %s AND %s AND birthdate IS NOT NULL and pe.voided = 0 "
              + "AND art_start_date BETWEEN :startInclusionDate AND :endInclusionDate ";

      return String.format(sql, startAge, endAge);
    }

    public static final String
        findPatientsWhoAreNewlyEnrolledOnARTByAgeRengeUsingMonthEndRevisionDate(
            int startAge, int endAge) {

      final String sql =
          " SELECT patient_id FROM ( "
              + " SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
              + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
              + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
              + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endRevisionDate AND e.location_id = :location "
              + " GROUP BY p.patient_id "
              + " ) art_start "
              + " GROUP BY patient_id "
              + " ) tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(month,birthdate,art_start_date)) BETWEEN %s AND %s AND birthdate IS NOT NULL and pe.voided = 0 "
              + "AND art_start_date BETWEEN :startInclusionDate AND :endInclusionDate ";

      return String.format(sql, startAge, endAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTBiggerThanParam(int startAge) {

      final String sql =
          "select f.patient_id from   ( "
              + "SELECT tx_new.patient_id,tx_new.art_start_date,(TIMESTAMPDIFF(year,pe.birthdate,art_start_date)) FROM  "
              + "( "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM (  "
              + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p  "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id  "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id  "
              + "WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53  "
              + "AND o.concept_id = 1190  "
              + "AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate   "
              + "AND e.location_id = :location "
              + "GROUP BY p.patient_id  "
              + ") art_start  "
              + "GROUP BY patient_id  "
              + ") tx_new  "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id  "
              + "WHERE (TIMESTAMPDIFF(year,pe.birthdate,art_start_date)) >= %s AND pe.birthdate IS NOT NULL and pe.voided = 0   "
              + "AND art_start_date BETWEEN  :startInclusionDate AND :endInclusionDate "
              + ")f ";

      return String.format(sql, startAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTBiggerThanParamOrBreastfeeding(
        int startAge) {

      final String sql =
          " select f.patient_id from   (  "
              + "SELECT tx_new.patient_id,tx_new.art_start_date,(TIMESTAMPDIFF(year,pe.birthdate,art_start_date)) FROM   "
              + "( SELECT patient_id, MIN(art_start_date) art_start_date FROM (   "
              + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + " INNER JOIN encounter e ON p.patient_id = e.patient_id   "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id   "
              + "WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53   "
              + "AND o.concept_id = 1190   "
              + "AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate    "
              + "AND e.location_id = :location  "
              + "GROUP BY p.patient_id   "
              + ") art_start   "
              + "GROUP BY patient_id   "
              + ") tx_new   "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id   "
              + "WHERE (TIMESTAMPDIFF(year,pe.birthdate,art_start_date)) >= %s AND pe.birthdate IS NOT NULL and pe.voided = 0    "
              + "AND art_start_date BETWEEN  :startInclusionDate AND :endInclusionDate  "
              + ")f  "
              + "union "
              + "Select p.patient_id from person pe  "
              + "inner join patient p on pe.person_id=p.patient_id  "
              + "inner join encounter e on p.patient_id=e.patient_id  "
              + "inner join obs o on e.encounter_id=o.encounter_id  "
              + "inner join obs obsLactante on e.encounter_id=obsLactante.encounter_id  "
              + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsLactante.voided=0 and e.encounter_type=53 and e.location_id=:location and  "
              + "o.concept_id=1190 and o.value_datetime is not null and  "
              + "obsLactante.concept_id=6332 and obsLactante.value_coded=1065 and pe.gender='F' ";
      return String.format(sql, startAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTLessThanParam(int startAge) {

      final String sql =
          " SELECT patient_id FROM ( "
              + " SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
              + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
              + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
              + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
              + " GROUP BY p.patient_id "
              + " ) art_start "
              + " GROUP BY patient_id "
              + " ) tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,art_start_date)) < %s AND birthdate IS NOT NULL and pe.voided = 0  "
              + "AND art_start_date BETWEEN :startInclusionDate AND :endInclusionDate ";

      return String.format(sql, startAge);
    }

    public static final String findPAtientWithCVOver1000CopiesAgeRenge(int startAge, int endAge) {
      final String sql =
          "select carga_viral.patient_id from ( "
              + "Select p.patient_id, max(o.obs_datetime) data_carga from patient p "
              + "inner join encounter e on p.patient_id = e.patient_id "
              + "inner join obs o on e.encounter_id=o.encounter_id "
              + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
              + "o.obs_datetime between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric > 1000 "
              + "group by p.patient_id "
              + ") carga_viral "
              + "inner join person on person_id = carga_viral.patient_id "
              + "WHERE (TIMESTAMPDIFF(year, birthdate, carga_viral.data_carga)) BETWEEN %s  AND %s AND birthdate IS NOT NULL and voided = 0  ";

      return String.format(sql, startAge, endAge);
    }

    public static final String findPAtientWithCVOver1000CopiesBiggerThanParam(int startAge) {
      final String sql =
          " select carga_viral.patient_id from ( "
              + " Select p.patient_id, min(e.encounter_datetime) data_carga from patient p "
              + " inner join encounter e on p.patient_id = e.patient_id "
              + " inner join obs o on e.encounter_id=o.encounter_id "
              + " where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
              + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
              + " group by p.patient_id "
              + " UNION "
              + " Select p.patient_id, min(o.obs_datetime) data_carga from patient p "
              + " inner join encounter e on p.patient_id = e.patient_id "
              + " inner join obs o on e.encounter_id=o.encounter_id "
              + " where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 53 and  o.concept_id = 856 and "
              + " DATE(o.obs_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
              + " group by p.patient_id "
              + " ) carga_viral "
              + " inner join person on person_id = carga_viral.patient_id "
              + " WHERE (TIMESTAMPDIFF(year, birthdate, carga_viral.data_carga)) >= %s  AND birthdate IS NOT NULL and voided = 0 ";

      return String.format(sql, startAge);
    }

    public static final String findPAtientWithCVOver1000CopiesBiggerThanParamOrBreastfeeding(
        int startAge) {
      final String sql =
          "select carga_viral.patient_id from (  "
              + "Select p.patient_id, min(e.encounter_datetime) data_carga from patient p  "
              + "inner join encounter e on p.patient_id = e.patient_id  "
              + "inner join obs o on e.encounter_id=o.encounter_id  "
              + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and  "
              + "DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000  "
              + "group by p.patient_id  "
              + "UNION  "
              + "Select p.patient_id, min(o.obs_datetime) data_carga from patient p  "
              + "inner join encounter e on p.patient_id = e.patient_id  "
              + "inner join obs o on e.encounter_id=o.encounter_id  "
              + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 53 and  o.concept_id = 856 and  "
              + "DATE(o.obs_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000  "
              + "group by p.patient_id  "
              + ") carga_viral  "
              + "inner join person on person_id = carga_viral.patient_id  "
              + "WHERE (TIMESTAMPDIFF(year, birthdate, carga_viral.data_carga)) >= %s  AND birthdate IS NOT NULL and voided = 0  "
              + "UNION "
              + "select primeiraCVAlta.patient_id  from  (  "
              + "Select p.patient_id, min(e.encounter_datetime) data_carga  "
              + "from    patient p  "
              + "inner join encounter e on p.patient_id = e.patient_id  "
              + "inner join obs o on e.encounter_id=o.encounter_id  "
              + "where   p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and  "
              + "DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate  and e.location_id = :location and o.value_numeric >= 1000  "
              + "group by p.patient_id   "
              + ") primeiraCVAlta  "
              + "inner join encounter consultaLactante on consultaLactante.patient_id = primeiraCVAlta.patient_id  "
              + "inner join obs obsLactante on consultaLactante.encounter_id = obsLactante.encounter_id  "
              + "inner join person on person.person_id = primeiraCVAlta.patient_id  "
              + "where   consultaLactante.voided = 0 and consultaLactante.encounter_type = 6 and consultaLactante.encounter_datetime = primeiraCVAlta.data_carga and consultaLactante.location_id = :location and  "
              + "obsLactante.voided = 0 and obsLactante.concept_id = 6332 and obsLactante.value_coded = 1065 and person.gender = 'F' ";
      return String.format(sql, startAge);
    }

    public static final String findPAtientWithCVOver1000CopiesLessThanParam(int startAge) {
      final String sql =
          " select carga_viral.patient_id from ( "
              + " Select p.patient_id, min(e.encounter_datetime) data_carga from patient p "
              + " inner join encounter e on p.patient_id = e.patient_id "
              + " inner join obs o on e.encounter_id=o.encounter_id "
              + " where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
              + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
              + " group by p.patient_id "
              + " UNION "
              + " Select p.patient_id, min(o.obs_datetime) data_carga from patient p "
              + " inner join encounter e on p.patient_id = e.patient_id "
              + " inner join obs o on e.encounter_id=o.encounter_id "
              + " where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 53 and  o.concept_id = 856 and "
              + " DATE(o.obs_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
              + " group by p.patient_id "
              + " ) carga_viral "
              + " inner join person on person_id = carga_viral.patient_id "
              + " WHERE (TIMESTAMPDIFF(year, birthdate, carga_viral.data_carga)) < %s  AND birthdate IS NOT NULL and voided = 0 ";
      ;

      return String.format(sql, startAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTUntilRevisionDateByAgeRenge(
        int startAge, int endAge) {

      final String sql =
          "SELECT patient_id FROM ( "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
              + "INNER JOIN obs o ON e.encounter_id=o.encounter_id "
              + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53 "
              + "AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endRevisionDate AND e.location_id=:location "
              + "GROUP BY p.patient_id "
              + ") art_start GROUP "
              + "BY patient_id "
              + ") tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,art_start_date)) BETWEEN %s AND %s AND birthdate IS NOT NULL and pe.voided=0 "
              + "AND art_start_date BETWEEN date_add(:endRevisionDate, interval -14 MONTH) AND  date_add(:endRevisionDate, interval -11 MONTH) ";

      return String.format(sql, startAge, endAge);
    }

    public static final String
        findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateBiggerThanParam(int startAge) {

      final String sql =
          "SELECT patient_id FROM ( "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
              + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
              + "INNER JOIN obs o ON e.encounter_id=o.encounter_id "
              + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53 "
              + "AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endRevisionDate AND e.location_id=:location "
              + "GROUP BY p.patient_id "
              + ") art_start GROUP "
              + "BY patient_id "
              + ") tx_new "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,art_start_date)) > %s AND birthdate IS NOT NULL and pe.voided=0 "
              + "AND art_start_date BETWEEN date_add(:endRevisionDate, interval -14 MONTH) AND  date_add(:endRevisionDate, interval -11 MONTH) ";

      return String.format(sql, startAge);
    }

    public static final String findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateLessThanParam(
        int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND o.voided = 0    "
              + "AND e.encounter_type = 53    "
              + "AND o.concept_id = 1190    "
              + "AND e.location_id=:location  "
              + "AND date(o.value_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH) and DATE_SUB(:endRevisionDate, INTERVAL 11 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) < %s and  birthdate IS NOT NULL ";

      return String.format(sql, startAge);
    }

    public static final String
        findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateLessThanParamFC(int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, e.encounter_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND e.encounter_type = 6    "
              + "AND e.location_id=:location  "
              + "AND date(e.encounter_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) < %s and  birthdate IS NOT NULL ";

      return String.format(sql, startAge);
    }

    public static final String calculateAgeOnTheFirstConsultationDateBiggerThanParam(int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND o.voided = 0    "
              + "AND e.encounter_type = 53    "
              + "AND o.concept_id = 1190    "
              + "AND e.location_id=:location  "
              + "AND date(o.value_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 11 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) >= %s and  birthdate IS NOT NULL ";
      return String.format(sql, startAge);
    }

    public static final String calculateAgeOnTheFirstConsultationDateBiggerThanParamFC(
        int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, e.encounter_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND e.encounter_type = 6    "
              + "AND e.location_id=:location  "
              + "AND date(e.encounter_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) >= %s and  birthdate IS NOT NULL ";
      return String.format(sql, startAge);
    }

    public static final String calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding(
        int startAge) {

      String sql =
          "select patient_id from ( "
              + "select age.patient_id from   (   "
              + "SELECT * FROM (     "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (   "
              + "SELECT p.patient_id, e.encounter_datetime art_start_date FROM patient p     "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id     "
              + "WHERE p.voided = 0     "
              + "AND e.voided = 0     "
              + "AND e.encounter_type = 6     "
              + "AND e.location_id=:location   "
              + "AND date(e.encounter_datetime) <= :endRevisionDate     "
              + ") art_start     "
              + "GROUP BY patient_id     "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)   "
              + ")age   "
              + "inner join person pe on pe.person_id=age.patient_id   "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) >= %s and  birthdate IS NOT NULL  "
              + ")f  "
              + "union "
              + "select f.patient_id from  "
              + "( "
              + "select f.patient_id,f.data_lactante,f.data_gravida, if(f.data_lactante is null,1, if(f.data_gravida is null,2, if(f.data_gravida>=f.data_lactante,1,2))) decisao  "
              + "from  (  "
              + "select p.person_id as patient_id ,gravida.data_gravida,lactante.data_lactante from person  p  "
              + "inner join (  "
              + "Select p.patient_id,obsGravida.obs_datetime data_gravida  from person pe   "
              + "inner join patient p on pe.person_id=p.patient_id   "
              + "inner join encounter e on p.patient_id=e.patient_id   "
              + "inner join obs o on e.encounter_id=o.encounter_id   "
              + "inner join obs obsGravida on e.encounter_id=obsGravida.encounter_id   "
              + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsGravida.voided=0 and e.encounter_type=53 and e.location_id=:location and   "
              + "o.concept_id=1190 and o.value_datetime is not null and   "
              + "obsGravida.concept_id=1982 and obsGravida.value_coded=1065 and pe.gender='F'   "
              + ")gravida on gravida.patient_id=p.person_id  "
              + "left join  "
              + "( Select p.patient_id,obsLactante.obs_datetime data_lactante from person pe  "
              + "inner join patient p on pe.person_id=p.patient_id   "
              + "inner join encounter e on p.patient_id=e.patient_id   "
              + "inner join obs o on e.encounter_id=o.encounter_id   "
              + "inner join obs obsLactante on e.encounter_id=obsLactante.encounter_id   "
              + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsLactante.voided=0 and e.encounter_type=53 and e.location_id=:location and   "
              + "o.concept_id=1190 and o.value_datetime is not null and   "
              + "obsLactante.concept_id=6332 and obsLactante.value_coded=1065 and pe.gender='F'   "
              + ")lactante  on lactante.patient_id=gravida.patient_id  "
              + ")f  "
              + "GROUP by f.patient_id   "
              + ")f WHERE decisao=2 "
              + "union "
              + "select f.patient_id from (    "
              + "select  "
              + "f.patient_id, "
              + "f.encounter_datetime, "
              + "f.data_gravida, "
              + "f.data_lactante,   "
              + "if(f.data_lactante is null,1, if(f.data_gravida is null,2, if(f.data_gravida>=f.data_lactante,1,2))) decisao  from  (    "
              + "Select p.patient_id,p.encounter_datetime,gravida.data_gravida,lactante.data_lactante from  "
              + "(  select * from  (    "
              + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location    "
              + "group by p.patient_id   "
              + ")firstConsultation    "
              + "Where firstConsultation.encounter_datetime between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)   "
              + ")p    "
              + "inner join   (    "
              + "Select p.patient_id,o.obs_datetime as data_gravida  from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=1982    "
              + "and e.encounter_datetime BETWEEN DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)    "
              + "and pe.voided = 0 and pe.gender = 'F'   "
              + "group by p.patient_id    "
              + ")gravida on gravida.patient_id=p.patient_id and p.encounter_datetime=gravida.data_gravida     "
              + "left join  (    "
              + "Select p.patient_id,o.obs_datetime as data_lactante from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=6332    "
              + "and e.encounter_datetime BETWEEN DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)    "
              + "and pe.gender = 'F' and pe.voided = 0    "
              + "group by p.patient_id    "
              + ") lactante on lactante.patient_id=gravida.patient_id and lactante.data_lactante=p.encounter_datetime  "
              + ")f   "
              + ")f WHERE f.decisao=2 ";

      return String.format(sql, startAge);
    }

    public static final String calculateAgeOnTheFirstConsultationDateLessThanParam(int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND o.voided = 0    "
              + "AND e.encounter_type = 53    "
              + "AND o.concept_id = 1190    "
              + "AND e.location_id=:location  "
              + "AND date(o.value_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 11 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) <= %s and  birthdate IS NOT NULL ";
      return String.format(sql, startAge);
    }

    public static final String calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(
        int startAge, int endAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND o.voided = 0    "
              + "AND e.encounter_type = 53    "
              + "AND o.concept_id = 1190    "
              + "AND e.location_id=:location  "
              + "AND date(o.value_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 11 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) between %s AND %s ";

      return String.format(sql, startAge, endAge);
    }
  }
}
