select dead_state.patient_id 
from (
	select patient_id,max(data_estado) data_estado                                                                                              
	from (  
	
		select distinct max_estado.patient_id, max_estado.data_estado 
		from (                                                                
					select  pg.patient_id,                                                                                                          
						max(ps.start_date) data_estado                                                                                          
					from patient p                                                                                                               
						inner join person pe on pe.person_id = p.patient_id                                                                         
						inner join patient_program pg on p.patient_id = pg.patient_id                                                               
						inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
					where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
						and ps.start_date <= CURDATE() and pg.location_id =:location group by pg.patient_id                                           
		) max_estado                                                                                                                        
			inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
			inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
		where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location  
		
		 union
		
		select  p.patient_id,                                                                                                               
			max(o.obs_datetime) data_estado                                                                                             
		from patient p                                                                                                                   
			inner join person pe on pe.person_id = p.patient_id                                                                         
			inner join encounter e on p.patient_id=e.patient_id                                                                         
			inner join obs  o on e.encounter_id=o.encounter_id                                                                          
		where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
			and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
			and o.obs_datetime <= CURDATE() and e.location_id=:location                                                                        
			group by p.patient_id                                                                                                               
		
		union                                                                                                                               
		
		select person_id as patient_id,death_date as data_estado                                                                            
		from person                                                                                                                         
		where dead=1 and voided = 0 and death_date is not null and death_date <= CURDATE() 
		
		union                                                                                                                               
		
		select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
		from 
		(                                                                                                                              
			select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
			from patient p                                                                                                              
					inner join person pe on pe.person_id = p.patient_id                                                                     
					inner join encounter e on p.patient_id=e.patient_id                                                                     
					inner join obs o on o.encounter_id=e.encounter_id                                                                       
			 where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime <= CURDATE()                                       
					and e.encounter_type = 21 and  e.location_id= :location                                                                 
				group by p.patient_id                                                                                                   
		 ) ultimaBusca                                                                                                                   
				inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
				inner join obs o on o.encounter_id = e.encounter_id                                                                         
		where e.encounter_type = 21 and o.voided=0 and o.concept_id in (2031,23944,23945) and o.value_coded = 1366 and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 
	                                                                                     
 	) dead_state group by dead_state.patient_id  
) dead_state
left join(
	
	select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
	from(
				select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
				from    patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id                                                                                         
				where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
						and e.location_id=:location and e.encounter_datetime <= CURDATE()                                                                                  
						group by p.patient_id  
				union
				
				select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
				from patient p                                                                                                                                   
					inner join person pe on pe.person_id = p.patient_id                                                                                         
					inner join encounter e on e.patient_id=p.patient_id                                                                                         
				where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
					and e.location_id=:location and e.encounter_datetime <= CURDATE()                                                                                  
					group by p.patient_id   
	) fila_seguimento	group by fila_seguimento.patient_id  
	) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
	where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado