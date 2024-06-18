select grade_level.patient_id                                                                                                                                                                                                 
from(
	select grade_level.patient_id                                                                                                                                                                                                 
	from(                                                                                                                                                                                                                        
	                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id=p.patient_id                                                                                                                                                                         
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185)                                                         
		where p.voided=0 and  e.voided=0 and o.voided=0                                                                                                                                                                             
		and e.encounter_type = 51 and o.concept_id=23951 and o.value_coded = 703                                                                                                                                                    
		   and nivelPositividade.value_coded = %s                                                                                                                                                                                  
		   and e.location_id=  :location and e.encounter_datetime between   :startDate and date_add(:endDate, interval 33 day)                                                                                                                              
		                                                                                                                                                                                                                           
		union                                                                                                                                                                                                                       
		                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id = p.patient_id                                                                                                                                                                       
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   left join obs tbLamGrupo on (e.encounter_id = tbLamGrupo.encounter_id and tbLamGrupo.voided = 0 and tbLamGrupo.concept_id = 165349)                                                                                     
		 left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185  and nivelPositividade.obs_group_id = tbLamGrupo.obs_id)   
		where p.voided=0 and  e.voided=0 and o.voided=0                                                                                                                                                                             
		and e.encounter_type in(6, 13) and o.concept_id=23951 and o.value_coded = 703                                                                                                                                               
		 and nivelPositividade.value_coded = %s                                                                                                                                                                                    
		 and e.location_id=  :location and e.encounter_datetime between   :startDate and  date_add(:endDate, interval 33 day)                                                                                                                                
		                                                                                                                                                                                                                           
		union                                                                                                                                                                                                                       
		                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id = p.patient_id                                                                                                                                                                       
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   inner join obs tbLamGrupo on tbLamGrupo.encounter_id = e.encounter_id                                                                                                                                                   
		 left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185  and nivelPositividade.obs_group_id = tbLamGrupo.obs_id)   
		where p.voided=0 and  e.voided=0 and o.voided=0  and tbLamGrupo.voided = 0                                                                                                                                                  
		and e.encounter_type = 90 and o.concept_id=23951 and o.value_coded = 703                                                                                                                                                    
		   and tbLamGrupo.concept_id = 165391 and o.obs_group_id = tbLamGrupo.obs_id                                                                                                                                               
		 and nivelPositividade.value_coded = %s                                                                                                                                                                                    
	   and e.location_id=  :location and e.encounter_datetime between   :startDate and  date_add(:endDate, interval 33 day)                                                                                                                              
	) grade_level                                                                                                                                                                                                                 
	left join(                                                                                                                                                                                                                    
	                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id=p.patient_id                                                                                                                                                                         
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185)                                                         
		where p.voided=0 and  e.voided=0 and o.voided=0                                                                                                                                                                             
		and e.encounter_type = 51 and o.concept_id=23951 and o.value_coded = 703                                                                                                                                                    
		   and nivelPositividade.value_coded in (%s)                                                                                                                                                                               
		   and e.location_id=  :location and e.encounter_datetime between   :startDate and  date_add(:endDate, interval 33 day)                                                                                                                              
		                                                                                                                                                                                                                           
		union                                                                                                                                                                                                                       
		                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id = p.patient_id                                                                                                                                                                       
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   left join obs tbLamGrupo on (e.encounter_id = tbLamGrupo.encounter_id and tbLamGrupo.voided = 0 and tbLamGrupo.concept_id = 165349)                                                                                     
		 left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185  and nivelPositividade.obs_group_id = tbLamGrupo.obs_id)   
		where p.voided=0 and  e.voided=0 and o.voided=0                                                                                                                                                                             
		and e.encounter_type in(6, 13) and o.concept_id=23951 and o.value_coded = 703                                                                                                                                               
		 and nivelPositividade.value_coded in (%s)                                                                                                                                                                                 
		 and e.location_id=  :location and e.encounter_datetime between :startDate and  date_add(:endDate, interval 33 day)                                                                                                                                
		                                                                                                                                                                                                                           
		union                                                                                                                                                                                                                       
		                                                                                                                                                                                                                           
		select p.patient_id                                                                                                                                                                                                         
		from patient p                                                                                                                                                                                                              
		inner join encounter e on e.patient_id = p.patient_id                                                                                                                                                                       
		   inner join obs o on o.encounter_id = e.encounter_id                                                                                                                                                                     
		   inner join obs tbLamGrupo on tbLamGrupo.encounter_id = e.encounter_id                                                                                                                                                   
		 left join obs nivelPositividade on (e.encounter_id = nivelPositividade.encounter_id and nivelPositividade.voided = 0 and nivelPositividade.concept_id = 165185  and nivelPositividade.obs_group_id = tbLamGrupo.obs_id)   
		where p.voided=0 and  e.voided=0 and o.voided=0  and tbLamGrupo.voided = 0                                                                                                                                                  
		and e.encounter_type = 90 and o.concept_id=23951 and o.value_coded = 703                                                                                                                                                    
		   and tbLamGrupo.concept_id = 165391 and o.obs_group_id = tbLamGrupo.obs_id                                                                                                                                               
		 and nivelPositividade.value_coded in (%s)                                                                                                                                                                                 
		   and e.location_id=  :location and e.encounter_datetime between   :startDate and   date_add(:endDate, interval 33 day)                                                                                                                              
	) exclusion on grade_level.patient_id = exclusion.patient_id                                                                                                                                                                  
	where exclusion.patient_id is null
) grade_level 
left join (
	
	select max_cd4.patient_id 
	from(
	
		select distinct max_cd4.patient_id 
		from( 
			
			select p.patient_id,max(o.obs_datetime) max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime between :startDate and date_add(:endDate, interval 33 day) and e.location_id=:location 
				group by p.patient_id 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 1695 and o.value_numeric<200 and o.location_id= :location 
	
		union
	
		select distinct max_cd4.patient_id 
		from( 
			
			select p.patient_id,max(o.obs_datetime) max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime between :startDate and date_add(:endDate, interval 33 day) and e.location_id=:location 
				group by p.patient_id 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165515 and o.value_coded = 165513  and o.location_id= :location

		union

		select distinct max_cd4.patient_id 
		from( 
			
			select p.patient_id,max(o.obs_datetime) max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165519 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime between :startDate and date_add(:endDate, interval 33 day) and e.location_id=:location 
				group by p.patient_id 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165519 and o.value_coded = 165513  and o.location_id= :location
	)max_cd4

	union

	select distinct max_cd4.patient_id 
	from( 
		
		select p.patient_id,max(o.obs_datetime) max_data_cd4  
		from patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
			and o.obs_datetime between :startDate and date_add(:endDate, interval 33 day) and e.location_id=:location 
			group by p.patient_id 
	)max_cd4 
		inner join person per on per.person_id=max_cd4.patient_id 
		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
		and per.voided=0 and timestampdiff(month,per.birthdate,:startDate)>=12 and timestampdiff(year,per.birthdate,:endDate)< 5  and o.concept_id = 1695 and o.value_numeric < 500 and o.location_id= :location 

	union

	select distinct max_cd4.patient_id 
	from( 
		
		select p.patient_id,max(o.obs_datetime) max_data_cd4  
		from patient p 
			inner join encounter e on p.patient_id=e.patient_id 
			inner join obs o on e.encounter_id=o.encounter_id 
		where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
			and o.obs_datetime between :startDate and date_add(:endDate, interval 33 day) and e.location_id=:location 
			group by p.patient_id 
	)max_cd4 
		inner join person per on per.person_id=max_cd4.patient_id 
		inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
		and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)< 12   and o.concept_id = 1695 and o.value_numeric < 750 and o.location_id= :location		
)  severes on grade_level.patient_id = severes.patient_id
where severes.patient_id is not null                                                                                                                                                                                