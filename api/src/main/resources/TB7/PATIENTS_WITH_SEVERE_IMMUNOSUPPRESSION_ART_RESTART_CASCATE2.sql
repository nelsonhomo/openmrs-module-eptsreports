select cd4_eligible.patient_id      
 from(
 
	 select cd4_eligible.patient_id, cd4_eligible.data_reinicio                                                                                                
	 from(                                                                                                                         
	                                                                                                                               
	     select p.patient_id, max(o.obs_datetime) data_reinicio, e.encounter_id                                                    
	     from patient p                                                                                                            
	             inner join encounter e on e.patient_id=p.patient_id                                                               
	             inner join obs o on o.encounter_id=e.encounter_id                                                                 
	     where e.voided=0 and e.encounter_type in (6,53) and o.concept_id in(6272,6273) and o.value_coded=1705 and o.voided=0      
	         and  e.location_id= :location and o.obs_datetime between  :startDate and :endDate                                     
	         group by p.patient_id                                                                                                 
	 )cd4_eligible                                                                                                                 
	 left join(                                                                                                                    
	                                                                                                                               
	     select p.patient_id, o.obs_datetime                                                                                       
	     from patient p                                                                                                            
	         inner join encounter e on e.patient_id=p.patient_id                                                                   
	         inner join obs o on o.encounter_id=e.encounter_id                                                                     
	     where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0                
	         and  e.location_id= :location and o.obs_datetime <= date_add( :endDate, interval  33 day)                             
	 )cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id                                                            
	 where cd4_absolute.obs_datetime between cd4_eligible.data_reinicio and date_add(cd4_eligible.data_reinicio, interval 33 day)  
 
) cd4_eligible                                             
 left join(
 	
		select max_cd4.patient_id, max_cd4.max_data_cd4 
		from(
		
			select max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,max(o.obs_datetime) max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
					group by p.patient_id 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 1695 and o.value_numeric<200 and o.location_id= :location 
		
			union
		
			select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,max(o.obs_datetime) max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
					group by p.patient_id 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165515 and o.value_coded = 165513  and o.location_id= :location
	
			union
	
			select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,max(o.obs_datetime) max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165519 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
					group by p.patient_id 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165519 and o.value_coded = 165513  and o.location_id= :location
		)max_cd4
	
		union
	
		select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
		from( 
			
			select p.patient_id,max(o.obs_datetime) max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
				group by p.patient_id 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(month,per.birthdate,:startDate)>=12 and timestampdiff(year,per.birthdate,:endDate)< 5  and o.concept_id = 1695 and o.value_numeric < 500 and o.location_id= :location 
	
		union
	
		select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
		from( 
			
			select p.patient_id,max(o.obs_datetime) max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
				group by p.patient_id 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)< 12   and o.concept_id = 1695 and o.value_numeric < 750 and o.location_id= :location 
 
 )cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id                   
  where  cd4_absolute.max_data_cd4 between :startDate and :endDate 
  