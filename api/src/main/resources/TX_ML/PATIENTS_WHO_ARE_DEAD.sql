

select patient_id from  
( 
select entrada_obitos.patient_id,entrada_obitos.data_estado data_entrada_obito, saida.data_estado as data_estado_saida, max_fila.data_fila  from  
( 
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
									and ps.start_date<= :endDate and pg.location_id =:location 
									group by pg.patient_id       
						) max_estado            
							inner join patient_program pp on pp.patient_id = max_estado.patient_id    
							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
				where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
				union 
				select  p.patient_id,   
						max(o.obs_datetime) data_estado   
				from patient p       
						inner join person pe on pe.person_id = p.patient_id 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs  o on e.encounter_id=o.encounter_id  
				where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
						and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
						and o.obs_datetime<= :endDate  and e.location_id=:location
						group by p.patient_id   
				union 
				select person_id as patient_id,death_date as data_estado    
				from person             
				where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate
				union 
				select  p.patient_id,   
						max(obsobito.obs_datetime) data_estado
				from patient p       
						inner join person pe on pe.person_id = p.patient_id 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
				where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
						and e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id=:location        
						and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
						group by p.patient_id   
		) allsaida        
				group by patient_id 
) entrada_obitos 
left join    
( 
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
								select  p.patient_id,                                                                                                               
									max(obsObito.obs_datetime) data_estado                                                                                      
								from patient p                                                                                                                   
									inner join person pe on pe.person_id = p.patient_id                                                                         
									inner join encounter e on p.patient_id=e.patient_id                                                                         
									inner join obs obsObito on e.encounter_id=obsObito.encounter_id                                                             
								where e.voided=0 and p.voided=0 and pe.voided = 0 and obsObito.voided=0                                                        
									and e.encounter_type in (21,36,37) and  e.encounter_datetime < :startDate and  e.location_id=:location                          
									and obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366                                                     
								group by p.patient_id                                                                                                               
						) dead_state group by dead_state.patient_id  
				) dead_state 
				inner join
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
) saida on entrada_obitos.patient_id = saida.patient_id
left join             
	(select p.patient_id,max(encounter_datetime) data_fila      
	from patient p     
		   inner join person pe on pe.person_id = p.patient_id   
		   inner join encounter e on e.patient_id=p.patient_id   
	where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
			and e.location_id=:location and e.encounter_datetime<= :startDate          
			group by p.patient_id 
) max_fila on entrada_obitos.patient_id=max_fila.patient_id  
group by entrada_obitos.patient_id 
)entrada_obitos   
 where ( (data_estado_saida is null or (data_estado_saida is not null and  data_fila > data_estado_saida))  and data_entrada_obito >= data_fila and data_entrada_obito between :startDate  and :endDate )