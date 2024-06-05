select distinct ultima_cv.patient_id
from(
		
	select p.patient_id ,o.obs_datetime data_cv, o.value_numeric  
	from patient p 
		inner join encounter e on p.patient_id=e.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric > 1000
	and o.obs_datetime between :startDate and :endDate and e.location_id=:location 
) ultima_cv
inner join( 	
	
	select p.patient_id ,o.obs_datetime data_cv, o.value_numeric  
	from patient p 
		inner join encounter e on p.patient_id=e.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric>1000
		and o.obs_datetime < :endDate and e.location_id=:location 
	
) penultima_cv on penultima_cv.patient_id = ultima_cv.patient_id
left join(

	select p.patient_id ,o.obs_datetime data_cv, o.value_numeric  
	from patient p 
		inner join encounter e on p.patient_id=e.patient_id 
		inner join obs o on e.encounter_id=o.encounter_id 
	where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (13,51) and o.value_numeric <=1000
	and o.obs_datetime < :endDate and e.location_id=:location
) outras_cv on ultima_cv.patient_id = outras_cv.patient_id
where penultima_cv.data_cv < ultima_cv.data_cv 
	and (outras_cv.patient_id is null or outras_cv.data_cv < penultima_cv.data_cv or outras_cv.data_cv > ultima_cv.data_cv )