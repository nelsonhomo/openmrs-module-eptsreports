select patient_id
from(
select inicio.*,
stopped_treatment.data_estado,
ultimo_levantamento.data_proximo_levantamento,
datediff(ultimo_levantamento.data_proximo_levantamento, data_inicio )   intervaloIIT        
			--ultimo_levantamento.data_proximo_levantamento,
		--	saida.data_estado,
	--		ultimo_fila.data_fila,
		--	datediff(ultimo_levantamento.data_proximo_levantamento, data_inicio )   intervaloIIT         
			from (   
						select patient_id,data_inicio 
						from ( 
							select patient_id,data_inicio from 
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
					) inicio
left join
(
select stopped_treatment.*
from( 
	select stopped_treatment.patient_id, stopped_treatment.data_estado 
	from( 
		select stopped_treatment.patient_id, stopped_treatment.data_estado 
		from( 
			 select stopped_treatment.patient_id, stopped_treatment.data_estado 
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
									and ps.start_date <= :endDate and pg.location_id =:location group by pg.patient_id                                           
					) max_estado                                                                                                                        
						inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
						inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
					where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location  
					 
					 union
					
					select  p.patient_id,                                                                                                               
						max(o.obs_datetime) data_estado                                                                                             
					from patient p                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                         
						inner join encounter e on p.patient_id=e.patient_id                                                                         
						inner join obs  o on e.encounter_id=o.encounter_id                                                                          
					where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
						and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709                          
						and o.obs_datetime <= :endDate and e.location_id=:location                                                                        
						group by p.patient_id                                                                                                               
				) stopped_treatment group by stopped_treatment.patient_id  
			) stopped_treatment 
			left join
			(
				select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
				from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id                                                                                         
				where  p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
						and e.location_id=:location and e.encounter_datetime <= :endDate                                                                                  
						group by p.patient_id  		
			 ) fila_seguimento on stopped_treatment.patient_id = fila_seguimento.patient_id
				where fila_seguimento.data_encountro is null or stopped_treatment.data_estado >= fila_seguimento.data_encountro
		) stopped_treatment
		left join
		(
		 select saida.patient_id, data_estado    
		 from( 
			select allsaida.patient_id,  max(data_estado)  data_estado  
			from (         
					select max_estado.patient_id, max_estado.data_estado 
					from (          
								select  pg.patient_id,  
									max(ps.start_date) data_estado
								from patient p   
										inner join person pe on pe.person_id = p.patient_id 
										inner join patient_program pg on p.patient_id = pg.patient_id         
										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
								where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
										and ps.start_date< :startDate and pg.location_id =:location 
										group by pg.patient_id       
							) max_estado            
								inner join patient_program pp on pp.patient_id = max_estado.patient_id    
								inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
					where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
					
					union 
					
					select  p.patient_id,   
							max(o.obs_datetime) data_estado   
					from patient p       
							inner join person pe on pe.person_id = p.patient_id 
							inner join encounter e on p.patient_id=e.patient_id 
							inner join obs  o on e.encounter_id=o.encounter_id  
					where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
							and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709       
							and o.obs_datetime< :startDate  and e.location_id=:location
							group by p.patient_id   
					union 
			
					 select dead_state.patient_id, dead_state.data_estado 
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
													and ps.start_date < :startDate and pg.location_id =:location group by pg.patient_id                                           
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
										and o.obs_datetime < :startDate and e.location_id=:location                                                                        
										group by p.patient_id                                                                                                               
									
									union                                                                                                                               
									
									select person_id as patient_id,death_date as data_estado                                                                            
									from person                                                                                                                         
									where dead=1 and voided = 0 and death_date is not null and death_date < :startDate 
									
									union                                                                                                                               
									
									select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
									from 
									(                                                                                                                              
										select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
										from patient p                                                                                                              
												inner join person pe on pe.person_id = p.patient_id                                                                     
												inner join encounter e on p.patient_id=e.patient_id                                                                     
												inner join obs o on o.encounter_id=e.encounter_id                                                                       
										 where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime < :startDate                                       
												and e.encounter_type = 21 and  e.location_id= :location                                                                 
											group by p.patient_id                                                                                                   
									 ) ultimaBusca                                                                                                                   
											inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
											inner join obs o on o.encounter_id = e.encounter_id                                                                         
									where e.encounter_type = 21 and o.voided=0 and o.concept_id in (2031,23944,23945) and o.value_coded = 1366 and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 
				                                                                                                              
							) dead_state group by dead_state.patient_id  
					) dead_state 
					left join
					(
							select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
							from(
										select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
										from    patient p                                                                                                                                   
												inner join person pe on pe.person_id = p.patient_id                                                                                         
												inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
												and e.location_id=:location and e.encounter_datetime < :startDate                                                                                  
												group by p.patient_id  
										union
										
										select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
											and e.location_id=:location and e.encounter_datetime < :startDate                                                                                  
											group by p.patient_id   
							) fila_seguimento	group by fila_seguimento.patient_id  
					 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
						where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado
				
					union
			
					select saidas_por_transferencia.patient_id, data_estado 
					from (
								select saidas_por_transferencia.patient_id, max(data_estado) data_estado
								from (
											select distinct max_estado.patient_id, max_estado.data_estado 
											from (          
														select pg.patient_id, max(ps.start_date) data_estado
														from patient p   
																inner join person pe on pe.person_id = p.patient_id 
																inner join patient_program pg on p.patient_id = pg.patient_id         
																inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
														where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																and ps.start_date< :startDate and pg.location_id =:location group by pg.patient_id       
													 ) max_estado            
													inner join patient_program pp on pp.patient_id = max_estado.patient_id    
													inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
											where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
											union 
											select  p.patient_id,max(o.obs_datetime) data_estado   
											from patient p       
													inner join person pe on pe.person_id = p.patient_id 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs  o on e.encounter_id=o.encounter_id  
											where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
													and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
													and o.obs_datetime< :startDate and e.location_id=:location
													group by p.patient_id   
											union 
											select ultimabusca.patient_id, ultimabusca.data_estado      
											from (
														select p.patient_id,max(e.encounter_datetime) data_estado             
														from patient p  
																inner join person pe on pe.person_id = p.patient_id 
																inner join encounter e on p.patient_id=e.patient_id 
																inner join obs o on o.encounter_id=e.encounter_id   
														where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate   
																and e.encounter_type = 21 and  e.location_id= :location           
																group by p.patient_id         
													) ultimabusca       
													inner join encounter e on e.patient_id = ultimabusca.patient_id       
													inner join obs o on o.encounter_id = e.encounter_id 
											where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id = :location 
								) saidas_por_transferencia 
									group by patient_id 
					) saidas_por_transferencia
					left join
					(  
							select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
							from (
									select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id                                                                                        
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
										and e.location_id=:location and e.encounter_datetime < :startDate                                                                               
										group by p.patient_id 
										union
										select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
										from patient p     
												inner join person pe on pe.person_id = p.patient_id   
												inner join encounter e on p.patient_id=e.patient_id   
												inner join obs o on e.encounter_id=o.encounter_id 
										where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
												and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime < :startDate 
												group by p.patient_id
							) ultimo_levantamento group by patient_id
							) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
							where ultimo_levantamento.data_ultimo_levantamento < :startDate
				) allsaida        
				group by patient_id 
			) saida
			left join
			(
				Select p.patient_id,max(encounter_datetime) max_date                                                                                                
				from    patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id                                                                                         
				where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
						and e.location_id=:location  and e.encounter_datetime< :startDate                                                                             
					group by p.patient_id 
			) last_fila on saida.patient_id  = last_fila.patient_id 
					where saida.data_estado >= last_fila.max_date or last_fila.max_date is null
		) saida on stopped_treatment.patient_id = saida.patient_id      
			where saida.patient_id is null
	) stopped_treatment
	) stopped_treatment
) stopped_treatment on inicio.patient_id = stopped_treatment.patient_id
left join
(  
	select patient_id, max(data_proximo_levantamento)  data_proximo_levantamento    
	from (
				select max_fila.patient_id, obs_fila.value_datetime data_proximo_levantamento
				from(
					select p.patient_id, max(e.encounter_datetime) data_fila                                                                                            
					from patient p                                                                                                                                   
						inner join encounter e on e.patient_id= p.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id                                                                                        
					where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
						and e.location_id=:location and e.encounter_datetime <=:endDate                                                                                 
						group by p.patient_id
				) max_fila
				left join     										       
					obs obs_fila on obs_fila.person_id=max_fila.patient_id
					and obs_fila.voided=0   
					and obs_fila.obs_datetime=max_fila.data_fila      
					and obs_fila.concept_id=5096           
					and obs_fila.location_id=208  
				
				union
				select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento             
				from patient p     
						inner join person pe on pe.person_id = p.patient_id   
						inner join encounter e on p.patient_id=e.patient_id   
						inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
						and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <=:endDate 
						group by p.patient_id
		) ultimo_levantamento group by patient_id
	) ultimo_levantamento on inicio.patient_id = ultimo_levantamento.patient_id 
	where stopped_treatment.patient_id is not null 
) Stopped_treatment 