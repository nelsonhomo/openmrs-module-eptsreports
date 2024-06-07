select saidas.patient_id from 
(
select dead_state.patient_id, dead_state.data_estado
								from(
									
									select patient_id,max(data_estado) data_estado                                                                                              
									from (  
										
										select distinct max_estado.patient_id, max_estado.data_estado 
										from(                                                                
											
											select  pg.patient_id,                                                                                                          
												max(ps.start_date) data_estado                                                                                          
											from patient p                                                                                                               
												inner join person pe on pe.person_id = p.patient_id                                                                         
												inner join patient_program pg on p.patient_id = pg.patient_id                                                               
												inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
											where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
														and ps.start_date<=:endDate and pg.location_id =:location group by pg.patient_id                                           
										) max_estado                                                                                                                        
											inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
											inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
										where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location  
										
										union
										
										select  p.patient_id,max(o.obs_datetime) data_estado                                                                                              
										from patient p                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                         
											inner join encounter e on p.patient_id=e.patient_id                                                                         
											inner join obs  o on e.encounter_id=o.encounter_id                                                                          
										where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
											and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
											and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
											group by p.patient_id                                                                                                               
										
										union                                                                                                                               
										
										select person_id as patient_id,death_date as data_estado                                                                            
										from person                                                                                                                         
										where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate 
										                                                                                                           
									) dead_state group by dead_state.patient_id  
							) dead_state 
							left join(
								
								select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
								from(
									
									select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
										group by p.patient_id  
									
									union
											
									select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
										and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
										group by p.patient_id   
								) fila_seguimento group by fila_seguimento.patient_id  
						 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
							where fila_seguimento.data_encountro <= dead_state.data_estado or fila_seguimento.data_encountro is null
			
						union
			
			         		select saidas_por_transferencia.patient_id,ultimo_levantamento.data_ultimo_levantamento data_estado
			          	from (
			         			
			         			select saidas_por_transferencia.patient_id, saidas_por_transferencia.data_estado
			                  	from (
			                       
			                       select saidas_por_transferencia.patient_id, max(data_estado) data_estado
			                       from(
			                           	select distinct max_estado.patient_id, max_estado.data_estado 
			                           	from(                                                                
				                                 select pg.patient_id, max(ps.start_date) data_estado                                                                                          
				                                 from patient p                                                                                                               
				                                 		inner join person pe on pe.person_id = p.patient_id                                                                         
				                                     	inner join patient_program pg on p.patient_id = pg.patient_id                                                               
				                                     	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                           
				                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                    
				                                 		and ps.start_date< :endDate and pg.location_id =:location group by pg.patient_id                                          
			                             	) max_estado                                                                                                                        
			                              	inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
			                                 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
			                             	where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location                 
			                             
			                             union                                                                                                                               
			                             		
			                        		select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
			                        		from patient p                                                                                                                   
			                              	inner join person pe on pe.person_id = p.patient_id                                                                         
			                                	inner join encounter e on p.patient_id=e.patient_id                                                                         
			                                	inner join obs  o on e.encounter_id=o.encounter_id                                                                          
			                        		where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                    
			                              	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
			                                	and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
			                        			group by p.patient_id                                                                                                               
			
			                          ) saidas_por_transferencia group by saidas_por_transferencia.patient_id
			                     ) saidas_por_transferencia
			                       left join (
								      
								      select p.patient_id,max(e.encounter_datetime) encounter_datetime
								      from patient p
								      	inner join encounter e on e.patient_id = p.patient_id
								      where p.voided = 0 and e.voided = 0 and e.encounter_datetime < :endDate and e.location_id =:location and e.encounter_type=18
								      	group by p.patient_id
			                      	)lev on saidas_por_transferencia.patient_id=lev.patient_id
			                 	where lev.encounter_datetime<=saidas_por_transferencia.data_estado or lev.encounter_datetime is null
			                 		group by saidas_por_transferencia.patient_id 
			           ) saidas_por_transferencia
			           inner join (  
			           		
			           		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
			                    from (
			     
			                   		select maxFila.patient_id, date_add(max(obs_fila.value_datetime), interval 1 day) data_ultimo_levantamento 
			                   		from ( 
			                   			
			                   			select fila.patient_id,fila.data_fila data_fila,e.encounter_id 
			                   			from( 
						                  
						                   select p.patient_id,max(encounter_datetime) data_fila  
						                   from patient p 
						                          inner join encounter e on e.patient_id=p.patient_id 
						                   where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate 
						                         group by p.patient_id 
						                  )fila 
						                   inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
						            )maxFila  
						   		  left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=maxFila.patient_id 
								 	and ultimo_fila_data_criacao.voided=0 
								   	and ultimo_fila_data_criacao.encounter_type = 18 
								   	and date(ultimo_fila_data_criacao.encounter_datetime) = date(maxFila.data_fila) 
								   	and ultimo_fila_data_criacao.location_id=:location 
								   left join obs obs_fila on obs_fila.person_id=maxFila.patient_id 
								   	and obs_fila.voided=0 
								   	and (date(obs_fila.obs_datetime)=date(maxFila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
								   	and obs_fila.concept_id=5096 
								   	and obs_fila.location_id=:location 
			                      		group by maxFila.patient_id 
			
			              			union
			
				                   	select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
				                   	from patient p                                                                                                                                   
				                    	inner join person pe on pe.person_id = p.patient_id                                                                                         
				                         inner join encounter e on p.patient_id=e.patient_id                                                                                         
				                         inner join obs o on e.encounter_id=o.encounter_id                                                                                           
				                   	where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
				                         and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime<=:endDate                                                                                        
				                   	group by p.patient_id
				               ) ultimo_levantamento group by patient_id
			           	) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
			          		where (ultimo_levantamento.data_ultimo_levantamento <= :endDate	and saidas_por_transferencia.data_estado<=:endDate)
			          )saidas
			       group by saidas.patient_id
