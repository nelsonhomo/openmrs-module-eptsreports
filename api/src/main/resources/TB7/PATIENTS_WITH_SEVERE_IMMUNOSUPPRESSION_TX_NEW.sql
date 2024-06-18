select cd4_eligible.patient_id      
 from(                                                
                                                      
   select patient_id, art_start_date                                    
   from(                                                                          
                                                      
     select patient_id, min(art_start_date) art_start_date                        
     from(                                              
                                                        
       select p.patient_id, min(e.encounter_datetime) art_start_date                    
       from patient p                                         
               inner join encounter e on p.patient_id=e.patient_id                    
                 inner join obs o on o.encounter_id=e.encounter_id                    
             where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9)        
               and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id= 248  
                 group by p.patient_id                                  
                                                          
           union                                              
                                                            
             select p.patient_id, min(value_datetime) art_start_date                    
             from patient p                                       
               inner join encounter e on p.patient_id=e.patient_id                    
                 inner join obs o on e.encounter_id=o.encounter_id                    
       where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53)         
                and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id= 248  
                group by p.patient_id                                 
                                                          
             union                                            
                                                            
             select pg.patient_id, min(date_enrolled) art_start_date                    
             from patient p                                       
               inner join patient_program pg on p.patient_id=pg.patient_id                
             where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id= 248  
               group by pg.patient_id                                 
                                                          
             union                                            
                                                            
             select e.patient_id, min(e.encounter_datetime) as art_start_date             
             from patient p                                       
               inner join encounter e on p.patient_id=e.patient_id                    
         where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id= 248  
               group by p.patient_id                                    
                                                          
             union                                            
                                                            
             select p.patient_id, min(value_datetime) art_start_date                    
             from patient p                                       
               inner join encounter e on p.patient_id=e.patient_id                    
                 inner join obs o on e.encounter_id=o.encounter_id                    
           where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52             
               and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime <= :endDate and e.location_id= 248  
                 group by p.patient_id                                  
       ) art_start                                            
                group by patient_id                                   
   ) tx_new where art_start_date between :startDate and :endDate                      
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
  where  cd4_absolute.max_data_cd4 between cd4_eligible.art_start_date and date_add(cd4_eligible.art_start_date, interval 33 day)
  