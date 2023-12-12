select patient_id 
from ( 
select patient_id,data_inicio 
from (
  select patient_id, min(data_inicio) data_inicio 
  from (
			  select p.patient_id, min(e.encounter_datetime) data_inicio 
			  from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on o.encounter_id=e.encounter_id 
			  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
					and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
					group by p.patient_id 
			  union 
			  
			  select p.patient_id, min(value_datetime) data_inicio 
			  from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
			  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
					and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
					group by p.patient_id 
			  
			  union 
   
			  select pg.patient_id, min(date_enrolled) data_inicio 
			  from patient p 
					inner join patient_program pg on p.patient_id=pg.patient_id 
			  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
					group by pg.patient_id 
			  
			  union 

			  select e.patient_id, min(e.encounter_datetime) as data_inicio 
			  from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
			  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
					group by p.patient_id 
			  
			  union 
   
			  select p.patient_id, min(value_datetime) data_inicio 
			  from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
			  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
					and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
					group by p.patient_id
		) 
  art_start group by patient_id 
) tx_new where data_inicio <= :endDate and data_inicio < '2023-12-21'
union
select tx_new.patient_id, tx_new.data_inicio
from 
(
  select tx_new.patient_id, tx_new.data_inicio 
  from
  ( 
		select patient_id, data_inicio from 
		(
			  select patient_id, min(data_inicio) data_inicio 
			  from 
					(
						  select e.patient_id, min(e.encounter_datetime) as data_inicio 
						  from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
						  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
								group by p.patient_id 
						  
						  union 
			   
						  select p.patient_id, min(value_datetime) data_inicio 
						  from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
						  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
								and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
								group by p.patient_id
					) 
			  art_start group by patient_id 
		) tx_new where data_inicio <= :endDate and data_inicio >= '2023-12-21'
  ) tx_new
  left join
  (
		select patient_id from 
		(
			  select patient_id, min(data_inicio) data_inicio 
			  from 
					(
						  select p.patient_id, min(e.encounter_datetime) data_inicio 
						  from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on o.encounter_id=e.encounter_id 
						  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
								and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
								group by p.patient_id 
						  union 
						  
						  select p.patient_id, min(value_datetime) data_inicio 
						  from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
						  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
								and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
								group by p.patient_id 
						  
						  union 
			   
						  select pg.patient_id, min(date_enrolled) data_inicio 
						  from patient p 
								inner join patient_program pg on p.patient_id=pg.patient_id 
						  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
								group by pg.patient_id 
					) 
			  art_start group by patient_id 
		) tx_new where data_inicio < '2023-12-21'
  ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
   where tx_new_period_anterior.patient_id is null
) tx_new
) inicio