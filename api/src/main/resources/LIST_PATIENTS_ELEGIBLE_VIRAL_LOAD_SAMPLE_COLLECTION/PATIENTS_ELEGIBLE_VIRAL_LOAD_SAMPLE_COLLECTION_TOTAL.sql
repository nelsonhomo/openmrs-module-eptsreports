        select distinct marcadoVisita.patient_id
            from 
            ( 
			select distinct txCurr.patient_id,
            	       consultaLevantamento.data_consulta, 
            		  consultaLevantamento.data_proxima_consulta,
            		  inicio_real.data_inicio, 
            	       carga_viral.data_carga, 
            	       carga_viral.valor_carga 
			from
				(
			   select patient_id                                                                                                   
             from                                                                                                                    
             (select inicio_fila_seg_prox.*,     
             	    GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar 
             from                                                                                                                        
                 (select     inicio_fila_seg.*,                                                                                          
                 max(obs_fila.value_datetime) data_proximo_lev,                                                                          
                 date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30                                              
              from                                                                                                                           
            (select inicio.*,                                                                                                                    
                 saida.data_estado,                                                                                                          
                 max_fila.data_fila,
                 max_recepcao.data_recepcao_levantou                                                                                         
             from                                                                                                                                
             (   
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
													and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:startDate and e.location_id=:location 
													group by p.patient_id 
											  union 
											  
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
													and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:startDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select pg.patient_id, min(date_enrolled) data_inicio 
											  from patient p 
													inner join patient_program pg on p.patient_id=pg.patient_id 
											  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:startDate and location_id=:location 
													group by pg.patient_id 
											  
											  union 
							
											  select e.patient_id, min(e.encounter_datetime) as data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
											  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:startDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
													and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:startDate and e.location_id=:location 
													group by p.patient_id
										) 
								  art_start group by patient_id 
							) tx_new where data_inicio <= :startDate and data_inicio < '2023-12-21'
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
														  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:startDate and e.location_id=:location 
																group by p.patient_id 
														  
														  union 
											   
														  select p.patient_id, min(value_datetime) data_inicio 
														  from patient p 
																inner join encounter e on p.patient_id=e.patient_id 
																inner join obs o on e.encounter_id=o.encounter_id 
														  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:startDate and e.location_id=:location 
																group by p.patient_id
													) 
											  art_start group by patient_id 
										) tx_new where data_inicio <= :startDate and data_inicio >= '2023-12-21'
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
																and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:startDate and e.location_id=:location 
																group by p.patient_id 
														  union 
														  
														  select p.patient_id, min(value_datetime) data_inicio 
														  from patient p 
																inner join encounter e on p.patient_id=e.patient_id 
																inner join obs o on e.encounter_id=o.encounter_id 
														  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
																and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:startDate and e.location_id=:location 
																group by p.patient_id 
														  
														  union 
											   
														  select pg.patient_id, min(date_enrolled) data_inicio 
														  from patient p 
																inner join patient_program pg on p.patient_id=pg.patient_id 
														  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:startDate and location_id=:location 
																group by pg.patient_id 
													) 
											  art_start group by patient_id 
										) tx_new where data_inicio < '2023-12-21'
								  ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
								   where tx_new_period_anterior.patient_id is null
							) tx_new
						) inicio
              )inicio                                                                                                                                    
              left join                                                                                                                                  
             ( 
                select patient_id,max(data_estado) data_estado                                                                                              
                     from                                                                                                                                        
                         (                                                                                                                                       
                             select distinct max_estado.patient_id, max_estado.data_estado from (                                                                
                                 select  pg.patient_id,                                                                                                          
                                         max(ps.start_date) data_estado                                                                                          
                                 from    patient p                                                                                                               
                                     inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                                     inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
                                     and ps.start_date<= :startDate and pg.location_id =:location group by pg.patient_id                                           
                             ) max_estado                                                                                                                        
                                 inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                                 inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                             where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
                             
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
															inner join patient_program pg on p.patient_id = pg.patient_id                                                               
															inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
														where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
															and ps.start_date<= :startDate and pg.location_id =:location group by pg.patient_id                                           
											) max_estado                                                                                                                        
												inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
												inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
											where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location  
											 union
											select  p.patient_id,                                                                                                               
												max(o.obs_datetime) data_estado                                                                                             
											from patient p                                                                                                                   
												inner join encounter e on p.patient_id=e.patient_id                                                                         
												inner join obs  o on e.encounter_id=o.encounter_id                                                                          
											where e.voided=0 and o.voided=0 and p.voided=0                                                                
												and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
												and o.obs_datetime<=:startDate and e.location_id=:location                                                                        
												group by p.patient_id                                                                                                               
											union                                                                                                                               
											select person_id as patient_id,death_date as data_estado                                                                            
											from person                                                                                                                         
											where dead=1 and voided = 0 and death_date is not null and death_date<=:startDate 
											union                                                                                                                               
											select  p.patient_id,                                                                                                               
												max(obsObito.obs_datetime) data_estado                                                                                      
											from patient p                                                                                                                   
												inner join encounter e on p.patient_id=e.patient_id                                                                         
												inner join obs obsObito on e.encounter_id=obsObito.encounter_id                                                             
											where e.voided=0 and p.voided=0 and obsObito.voided=0                                                        
												and e.encounter_type in (21,36,37) and  e.encounter_datetime<=:startDate and  e.location_id=:location                          
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
														inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and e.voided=0 and e.encounter_type=18                                                                      
														and e.location_id=:location and e.encounter_datetime<=:startDate                                                                                  
														group by p.patient_id  
												union
												
												select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
												from patient p                                                                                                                                   
													inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9)                                                                
													and e.location_id=:location and e.encounter_datetime<=:startDate                                                                                  
													group by p.patient_id   
									) fila_seguimento	group by fila_seguimento.patient_id  
							 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
								where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado
							 
							 union
							 
							 select  p.patient_id,                                                                                                               
                                     max(o.obs_datetime) data_estado                                                                                             
                             from    patient p                                                                                                                   
                                     inner join encounter e on p.patient_id=e.patient_id                                                                         
                                     inner join obs  o on e.encounter_id=o.encounter_id                                                                          
                             where   e.voided=0 and o.voided=0 and p.voided=0 and                                                              
                                     e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709 and                        
                                     o.obs_datetime<=:startDate and e.location_id=:location                                                                        
                             group by p.patient_id                                                                                                               
                             union
 					         select saidas_por_transferencia.patient_id, data_estado 
	                         from
	                        	(
		                            select saidas_por_transferencia.patient_id, max(data_estado) data_estado
		                            from
		                                (
			                           	select distinct max_estado.patient_id, max_estado.data_estado 
			                           	from 
			                           		(                                                                
				                                 select pg.patient_id, max(ps.start_date) data_estado                                                                                          
				                                 from patient p                                                                                                               
				                                 		inner join patient_program pg on p.patient_id = pg.patient_id                                                               
				                                     	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
				                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
				                                 		and ps.start_date<= :startDate and pg.location_id =:location group by pg.patient_id                                           
			                             		) max_estado                                                                                                                        
			                                 		inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
			                                 		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
			                             		where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
			                             
			                             		union                                                                                                                               
			                             		
			                             		select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
			                             		from patient p                                                                                                                   
			                                     	inner join encounter e on p.patient_id=e.patient_id                                                                         
			                                     	inner join obs o on e.encounter_id=o.encounter_id                                                                          
			                             		where e.voided=0 and o.voided=0 and p.voided=0                                                                
			                                   	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
			                                     	and o.obs_datetime<=:startDate and e.location_id=:location                                                                        
			                             			group by p.patient_id                                                                                                               
			                             		
			                             	     union                                                                                                                               
				                             
				                             	select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
				                             	from (                                                                                                                              
				                                     select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
				                                     from patient p                                                                                                              
				                                         inner join encounter e on p.patient_id=e.patient_id                                                                     
				                                         inner join obs o on o.encounter_id=e.encounter_id                                                                       
				                                     where e.voided=0 and p.voided=0 and e.encounter_datetime<= :startDate                                       
				                                         and e.encounter_type = 21 and  e.location_id= :location                                                                 
				                                         group by p.patient_id                                                                                                   
				                                 ) ultimaBusca                                                                                                                   
				                                     inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
				                                     inner join obs o on o.encounter_id = e.encounter_id                                                                         
				                                where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 
		                                ) saidas_por_transferencia 
	                                	group by patient_id 
                               	) saidas_por_transferencia
                                left join
			                 	(  
				                    select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
				                    from
				                    (
		                        		select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id= p.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id                                                                                        
										where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
											and e.location_id=:location and e.encounter_datetime <= :startDate                                                                               
											group by p.patient_id 
		                        
		                        		union
		                        
			                        	select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
			                        	from patient p                                                                                                                                   
			                         	  inner join encounter e on p.patient_id=e.patient_id                                                                                         
			                              inner join obs o on e.encounter_id=o.encounter_id                                                                                           
			                        	where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
			                              and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :startDate                                                                                        
			                        	group by p.patient_id
				                    	) ultimo_levantamento group by patient_id
			                		) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
			               		where ultimo_levantamento.data_ultimo_levantamento <= :startDate
				                                                                                                                                                                         
                         	) allSaida                                                                                                                                      
                 				group by patient_id 
                
              		) saida on inicio.patient_id = saida.patient_id                                                                                                      
             left join                                                                                                                                           
              ( select p.patient_id,max(encounter_datetime) data_fila                                                                                                
             from    patient p                                                                                                                                   
                     inner join encounter e on e.patient_id=p.patient_id                                                                                         
             where   p.voided=0 and e.voided=0 and e.encounter_type=18 and                                                                     
                     e.location_id=:location and e.encounter_datetime<=:startDate                                                                                  
             group by p.patient_id                                                                                                                               
             ) max_fila on inicio.patient_id=max_fila.patient_id  
              left join                                                                                                                                          
              (                                                                                                                                                  
             select  p.patient_id,max(value_datetime) data_recepcao_levantou                                                                                     
             from    patient p                                                                                                                                   
                     inner join encounter e on p.patient_id=e.patient_id                                                                                         
                     inner join obs o on e.encounter_id=o.encounter_id                                                                                           
             where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                                                      
                     o.concept_id=23866 and o.value_datetime is not null and                                                                                     
                     o.value_datetime<=:startDate and e.location_id=:location                                                                                      
             group by p.patient_id                                                                                                                               
              ) max_recepcao on inicio.patient_id=max_recepcao.patient_id                                                                                        
              group by inicio.patient_id                                                                                                                         
             ) inicio_fila_seg
             left join                                                                                                                                          
              encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=inicio_fila_seg.patient_id                                                                                      
             	and ultimo_fila_data_criacao.voided=0                                     
               	and ultimo_fila_data_criacao.encounter_type = 18  
              	and ultimo_fila_data_criacao.encounter_datetime = inicio_fila_seg.data_fila                                                                                            
              	and ultimo_fila_data_criacao.location_id=:location                     
              left join                                                                                                                                          
              obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id                                                                                      
               	and obs_fila.voided=0                                                                                                                             
              	and (obs_fila.obs_datetime=inicio_fila_seg.data_fila  or (ultimo_fila_data_criacao.date_created = obs_fila.date_created and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id ))                                                                                                       
              	and obs_fila.concept_id=5096                                                                                                                       
              	and obs_fila.location_id=:location                                                                                                                 
             group by inicio_fila_seg.patient_id                                                                                                                 
             ) inicio_fila_seg_prox                                                                                                                              
             group by patient_id                                                                                                                                 
             ) coorte12meses_final                                                                                                                               
             where (data_estado is null or (data_estado is not null and  data_fila > data_estado)) and date_add(data_usar, interval 28 day) >=:startDate
            	)txCurr
            	inner join
			(
            	select 	consultaLevantamento.patient_id, 
            			consultaLevantamento.data_consulta, 
            			consultaLevantamento.data_proxima_consulta
            	from 
			( 
            		select patient_id,max(data_consulta) data_consulta,max(data_proxima_consulta) data_proxima_consulta 
            		from 
            		( 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime data_consulta ,o.value_datetime data_proxima_consulta 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type=18 and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				inner join obs o on o.encounter_id=e.encounter_id 
            			where 	o.concept_id=5096 and o.voided=0 and e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type=18 and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			union 
            			Select 	p.patient_id,max(value_datetime) data_consulta,(max(value_datetime) + INTERVAL 30 day) data_proxima_consulta 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            					o.concept_id=23866 and o.value_datetime is not null and 
            					o.value_datetime<= :startDate and e.location_id=:location and (o.value_datetime + interval 30 day) between  :startDate and :endDate 
            			group by p.patient_id 
            			union 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime,o.value_datetime 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type in (6,9) and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				left join obs o on o.encounter_id=e.encounter_id and o.concept_id=1410 and o.voided=0 
            			where  	e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type in (6,9) and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			group by ultimavisita.patient_id 
            		) consultaRecepcao 
            		group by patient_id 
            	) consultaLevantamento 
            	) consultaLevantamento on consultaLevantamento.patient_id = txCurr.patient_id
            	inner join 
            	( 
            		select patient_id,max(encounter_datetime) ultimoLevConsulta 
            		from 
            		( 
            			select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            			where 	e.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) and 
            					e.location_id=:location and e.encounter_datetime<=:startDate 
            			group by p.patient_id 
            			union 
            			Select 	p.patient_id,max(value_datetime) encounter_datetime 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            					o.concept_id=23866 and o.value_datetime is not null and 
            					o.value_datetime<=:startDate and e.location_id=:location 
            			group by p.patient_id 
            		) allEncounter 
            		group by patient_id 
            	)maxConsultaLeva on maxConsultaLeva.patient_id=consultaLevantamento.patient_id 
            	inner join 
            	(	Select patient_id,min(data_inicio) data_inicio 
            		from 
            			( 
            				Select 	p.patient_id,min(e.encounter_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on o.encounter_id=e.encounter_id 
            				where 	e.voided=0 and o.voided=0 and p.voided=0 and 
            						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and 
            						e.encounter_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            				union 
            				Select 	p.patient_id,min(value_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on e.encounter_id=o.encounter_id 
            				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and 
            						o.concept_id=1190 and o.value_datetime is not null and 
            						o.value_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            				union 
            				select 	pg.patient_id,min(date_enrolled) data_inicio 
            				from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id 
            				where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:startDate and location_id=:location 
            				group by pg.patient_id 
            				union 
            				  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio 
            				  FROM 		patient p 
            							inner join encounter e on p.patient_id=e.patient_id 
            				  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:startDate and e.location_id=:location 
            				  GROUP BY 	p.patient_id 
            				union 
            				Select 	p.patient_id,min(value_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on e.encounter_id=o.encounter_id 
            				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            						o.concept_id=23866 and o.value_datetime is not null and 
            						o.value_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            			) inicio 
            		group by patient_id 
            	) inicio_real on consultaLevantamento.patient_id=inicio_real.patient_id 
            	left join 
            	( 
            			select maxCarga.patient_id,maxCarga.data_carga,o.concept_id,if(o.concept_id=856,o.value_numeric,'Indetectavel') valor_carga 
            		from 
            		( 
            			Select 	p.patient_id,max(o.obs_datetime) data_carga, o.value_coded 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9,53,51) and 
            					o.concept_id in (1305,856) and o.obs_datetime <=:endDate and e.location_id=:location 
            			group by p.patient_id 
            		)maxCarga 
            		inner join encounter e on maxCarga.patient_id=e.patient_id 
            		inner join obs o on o.encounter_id=e.encounter_id 
            		where 	o.obs_datetime=maxCarga.data_carga and 
            				o.concept_id in (1305,856) and e.encounter_type in (13,6,9,53,51) and e.location_id=:location and 
            				e.voided=0 and o.voided=0 
            	) carga_viral on consultaLevantamento.patient_id=carga_viral.patient_id and 
            		( 
            			(carga_viral.concept_id=1305 and carga_viral.data_carga < date_add(consultaLevantamento.data_proxima_consulta, interval -12 MONTH)) 
            			or 
            			(carga_viral.concept_id=856 and carga_viral.valor_carga<1000  and carga_viral.data_carga < date_add(consultaLevantamento.data_proxima_consulta, interval -12 MONTH)) 
            			or 
            			(carga_viral.concept_id=856 and carga_viral.valor_carga>=1000  and carga_viral.data_carga < date_add(consultaLevantamento.data_proxima_consulta, interval -3 MONTH)) 
            		) 
            	left join 
            	( 
            		select patient_id,min(data_gravida) dataGravidaLactante 
            		from 
            		( 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and 
            					e.encounter_type in (5,6) and e.encounter_datetime  between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			select 	pp.patient_id,pp.date_enrolled data_gravida 
            			from 	patient_program pp 
            			where 	pp.program_id=8 and pp.voided=0 and 
            					pp.date_enrolled between date_add(:endDate, interval -9 month) AND :endDate and pp.location_id=:location 
            			union 
            			Select 	p.patient_id,obsART.value_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            					inner join obs obsART on e.encounter_id=obsART.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and 
            					e.encounter_type=53 and obsART.value_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location and 
            					obsART.concept_id=1190 and obsART.voided=0 
            			union 
            			select p.patient_id,data_colheita.value_datetime data_gravida from patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id 
            			where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51 
            			  and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,o.value_datetime data_parto 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and 
            					e.encounter_type in (5,6) and o.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id, e.encounter_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and 
            					e.encounter_type=6 and e.encounter_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id, obsART.value_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            					inner join obs obsART on e.encounter_id=obsART.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and 
            					e.encounter_type=53 and e.location_id=:location and 
            					obsART.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and 
            					obsART.concept_id=1190 and obsART.voided=0 
            			union 
            			Select 	p.patient_id, e.encounter_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			select 	pg.patient_id,ps.start_date data_parto 
            			from 	patient p 
            					inner join patient_program pg on p.patient_id=pg.patient_id 
            					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
            			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and 
            					pg.program_id=8 and ps.state=27 and ps.end_date is null and 
            					ps.start_date between date_add(:endDate, interval -18 month) AND :endDate and location_id=:location 
            		     union 
                           select p.patient_id,data_colheita.value_datetime data_parto from patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id 
            			where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51 
            						  and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            		) lactante_real 
            		inner join person pe on pe.person_id=lactante_real.patient_id and pe.voided=0 and pe.gender='F' 
            		group by lactante_real.patient_id 
            	) gravida on consultaLevantamento.patient_id=gravida.patient_id 
            	left join 
            	( 
            		select patient_id,max(data_estado) data_estado 
            		from 
            			( 
            				select distinct max_estado.patient_id, max_estado.data_estado 
            				from ( 
            					select pg.patient_id, 
            						max(ps.start_date) data_estado 
            					from patient p 
            						inner join patient_program pg on p.patient_id = pg.patient_id 
            						inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
            					where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2 
            						and ps.start_date<= :startDate and pg.location_id =:location group by pg.patient_id 
            					) 
            				max_estado 
            					inner join patient_program pp on pp.patient_id = max_estado.patient_id 
            					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado 
            				where pp.program_id = 2 and ps.state in (7,8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = 240 
            				union 
            				select 	p.patient_id, 
            						max(o.obs_datetime) data_estado 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs  o on e.encounter_id=o.encounter_id 
            				where 	e.voided=0 and o.voided=0 and p.voided=0 and 
            						e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and 
            						o.obs_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            				union 
            				select person_id as patient_id,death_date as data_estado 
            				from person 
            				where dead=1 and death_date is not null and death_date<=:startDate 
            				union 
            				select 	p.patient_id, 
            						max(obsObito.obs_datetime) data_estado 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs obsObito on e.encounter_id=obsObito.encounter_id 
            				where 	e.voided=0 and p.voided=0 and obsObito.voided=0 and 
            						e.encounter_type in (21,36,37) and  e.encounter_datetime<=:startDate and  e.location_id=:location and 
            						obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366 
            				group by p.patient_id 
            			) allSaida 
            		group by patient_id 
            	) saida on consultaLevantamento.patient_id=saida.patient_id 
            	where 	consultaLevantamento.data_proxima_consulta between :startDate and :endDate and 
            			gravida.patient_id is null and 
            			(saida.patient_id is null or (saida.patient_id is not null and maxConsultaLeva.ultimoLevConsulta>saida.data_estado)) and 
            			timestampdiff(month,inicio_real.data_inicio,:endDate)>= 6 
            ) marcadoVisita 
            left join 
            ( 
            	Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime data_consulta,min(o.value_datetime) proxima_consulta 
            	from 
            		(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            			from 	encounter e 
            					inner join patient p on p.patient_id=e.patient_id 
            			where 	e.voided=0 and p.voided=0 and e.encounter_type in (6,9) and e.location_id=:location and e.encounter_datetime<=:startDate 
            			group by p.patient_id 
            		) ultimavisita 
            		inner join encounter e on e.patient_id=ultimavisita.patient_id 
            		left join obs o on o.encounter_id=e.encounter_id and o.concept_id=1410 and o.voided=0 
            	where  	e.encounter_datetime=ultimavisita.encounter_datetime and 
            			e.encounter_type in (6,9) and e.location_id=:location 
            	group by ultimavisita.patient_id 
            ) consulta on marcadoVisita.patient_id=consulta.patient_id 
            left join 
            ( 
            	Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime data_levantamento_fila ,min(o.value_datetime) proximo_levantamento_fila 
            	from 
            		(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            			from 	encounter e 
            					inner join patient p on p.patient_id=e.patient_id 
            			where 	e.voided=0 and p.voided=0 and e.encounter_type=18 and e.location_id=:location and e.encounter_datetime<=:startDate 
            			group by p.patient_id 
            		) ultimavisita 
            		inner join encounter e on e.patient_id=ultimavisita.patient_id 
            		inner join obs o on o.encounter_id=e.encounter_id 
            	where 	o.concept_id=5096 and o.voided=0 and e.encounter_datetime=ultimavisita.encounter_datetime and 
            			e.encounter_type=18 and e.location_id=:location 
            	group by ultimavisita.patient_id 
            )levantamento on marcadoVisita.patient_id=levantamento.patient_id 
            left join 
            ( 
            	Select 	p.patient_id,max(value_datetime) data_levantamento_recepcao,(max(value_datetime) + INTERVAL 30 day) proximo_levantamento_recepcao 
            	from 	patient p 
            			inner join encounter e on p.patient_id=e.patient_id 
            			inner join obs o on e.encounter_id=o.encounter_id 
            	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            			o.concept_id=23866 and o.value_datetime is not null and 
            			o.value_datetime<=:startDate and e.location_id=:location 
            	group by p.patient_id 
            ) levantamentoRecepcao on marcadoVisita.patient_id=levantamentoRecepcao.patient_id 
            left join 
            ( 
            	select distinct maxCargaViral.patient_id,maxCargaViral.data_carga,if(o.concept_id=856,if(o.value_numeric<=0,'Indetectavel',o.value_numeric),'Indetectavel') valor_carga 
            	from 
            	( 
            		Select 	p.patient_id,max(o.obs_datetime) data_carga 
            		from 	patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9,53,51) and 
            				o.concept_id in (856,1305) and 
            				date(o.obs_datetime)<=:endDate and e.location_id=:location 
            		group by p.patient_id 
            	) maxCargaViral 
            	inner join obs o on maxCargaViral.patient_id=o.person_id and maxCargaViral.data_carga=o.obs_datetime and o.voided=0 and o.concept_id in (856,1305) and o.location_id=:location 
            ) ultimaCarga on marcadoVisita.patient_id=ultimaCarga.patient_id 
            left join 
            ( 
            	select altacv.patient_id, count(distinct e.encounter_id) nrapss 
            	from 
            	( 
            		select maxCargaViral.patient_id,maxCargaViral.data_carga,o.value_numeric valor_carga 
            		from 
            		( 
            			Select 	p.patient_id,max(o.obs_datetime) data_carga 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9,53,51) and 
            					o.concept_id in (856,1305) and 
            					o.obs_datetime<=:startDate and e.location_id=:location 
            			group by p.patient_id 
            		) maxCargaViral 
            		inner join obs o on maxCargaViral.patient_id=o.person_id and maxCargaViral.data_carga=o.obs_datetime and o.voided=0 and 
            					o.concept_id=856 and o.value_numeric>=1000 and o.location_id=:location 
            	) altacv 
            	inner join encounter e on altacv.patient_id=e.patient_id 
            	where e.voided=0 and e.encounter_datetime between altacv.data_carga and :startDate 
            			and e.encounter_type=35 and e.location_id=:location 
            	group by altacv.patient_id 
            ) sessoesApss on marcadoVisita.patient_id=sessoesApss.patient_id 
            left join (
              			select maxkp.patient_id, o.value_coded,o.obs_datetime from (                                                            
                            select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p                                                                      
                                inner join encounter e on p.patient_id=e.patient_id                                                                                     
                                inner join obs o on e.encounter_id=o.encounter_id                                                                                       
                            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=6 and e.encounter_datetime<=:endDate and     
                                  e.location_id=:location                                                                                                           
                                group by p.patient_id                                                                                                               
                            )                                                                                                                                       
                        maxkp                                                                                                                                       
                            inner join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime                                            
                            inner join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime                                                        
                            inner join person pe on pe.person_id=maxkp.patient_id                                                                                       
                        where o.concept_id=23703 and o.voided=0 and e.encounter_type=6 and e.voided=0 and e.location_id=:location and pe.voided=0                   
                            and o.value_coded in (1377, 20454,20426,1901)
            )keyPop on marcadoVisita.patient_id=keyPop.patient_id 
            left join person_attribute patOVCEntryDate on patOVCEntryDate.person_id=marcadoVisita.patient_id and patOVCEntryDate.person_attribute_type_id=50 and patOVCEntryDate.value is not null and patOVCEntryDate.value <> '' and patOVCEntryDate.voided=0 
            left join person_attribute patOVCExitDate on patOVCExitDate.person_id=marcadoVisita.patient_id and patOVCExitDate.person_attribute_type_id=55 and patOVCExitDate.value is not null and patOVCExitDate.value <> '' and patOVCExitDate.voided=0 
            left join person_attribute patOVCStatus on patOVCStatus.person_id=marcadoVisita.patient_id and patOVCStatus.person_attribute_type_id=55 and patOVCStatus.value is not null and patOVCStatus.value <> '' and patOVCStatus.voided=0 
            left join 
            ( 
            	select maxLinha.patient_id,maxLinha.data_linha,if(o.value_coded=21150,'1ª Linha',if(o.value_coded=21148,'2ª Linha','3ª Linha')) valorLinha 
            	from 
            	( 
            		Select 	p.patient_id,max(o.obs_datetime) data_linha 
            		from 	patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and 
            				o.concept_id=21151 and o.obs_datetime<=:startDate and e.location_id=:location 
            		group by p.patient_id 
            	) maxLinha 
            	inner join obs o on maxLinha.patient_id=o.person_id and maxLinha.data_linha=o.obs_datetime and o.voided=0 and o.concept_id=21151 and o.location_id=:location 
            ) linha on marcadoVisita.patient_id=linha.patient_id 
            left join 
            ( 
            	select 	ultimo_lev.patient_id, 
            			case o.value_coded 
            			when 1703 then 'AZT+3TC+EFV' 
            			when 6100 then 'AZT+3TC+LPV/r' 
            			when 1651 then 'AZT+3TC+NVP' 
            			when 6324 then 'TDF+3TC+EFV' 
            			when 6104 then 'ABC+3TC+EFV' 
            			when 23784 then 'TDF+3TC+DTG' 
            			when 23786 then 'ABC+3TC+DTG' 
            			when 23785 then 'TDF+3TC+DTG' 
            			when 6116 then 'AZT+3TC+ABC' 
            			when 6106 then 'ABC+3TC+LPV/r' 
            			when 6105 then 'ABC+3TC+NVP' 
            			when 6108 then 'TDF+3TC+LPV/r' 
            			when 23790 then 'TDF+3TC+LPV/r+RTV' 
            			when 23791 then 'TDF+3TC+ATV/r' 
            			when 23792 then 'ABC+3TC+ATV/r' 
            			when 23793 then 'AZT+3TC+ATV/r' 
            			when 23795 then 'ABC+3TC+ATV/r+RAL' 
            			when 23796 then 'TDF+3TC+ATV/r+RAL' 
            			when 23801 then 'AZT+3TC+RAL' 
            			when 23802 then 'AZT+3TC+DRV/r' 
            			when 23815 then 'AZT+3TC+DTG' 
            			when 6329 then 'TDF+3TC+RAL+DRV/r' 
            			when 23797 then 'ABC+3TC+DRV/r+RAL' 
            			when 23798 then '3TC+RAL+DRV/r' 
            			when 23803 then 'AZT+3TC+RAL+DRV/r' 
            			when 6243 then 'TDF+3TC+NVP' 
            			when 6103 then 'D4T+3TC+LPV/r' 
            			when 792 then 'D4T+3TC+NVP' 
            			when 1827 then 'D4T+3TC+EFV' 
            			when 6102 then 'D4T+3TC+ABC' 
            			when 1311 then 'ABC+3TC+LPV/r' 
            			when 1312 then 'ABC+3TC+NVP' 
            			when 1313 then 'ABC+3TC+EFV' 
            			when 1314 then 'AZT+3TC+LPV/r' 
            			when 1315 then 'TDF+3TC+EFV' 
            			when 6330 then 'AZT+3TC+RAL+DRV/r' 
            			when 6325 then 'D4T+3TC+ABC+LPV/r' 
            			when 6326 then 'AZT+3TC+ABC+LPV/r' 
            			when 6327 then 'D4T+3TC+ABC+EFV' 
            			when 6328 then 'AZT+3TC+ABC+EFV' 
            			when 6109 then 'AZT+DDI+LPV/r' 
            			when 21163 then 'AZT+3TC+LPV/r' 
            			when 23799 then 'TDF+3TC+DTG' 
            			when 23800 then 'ABC+3TC+DTG' 
            			when 6110 then 'D4T20+3TC+NVP' 
            			when 1702 then 'AZT+3TC+NFV' 
            			when 817  then 'AZT+3TC+ABC' 
            			when 6244 then 'AZT+3TC+RTV' 
            			when 1700 then 'AZT+DDl+NFV' 
            			when 633  then 'EFV' 
            			when 625  then 'D4T' 
            			when 631  then 'NVP' 
            			when 628  then '3TC' 
            			when 635  then 'NFV' 
            			when 797  then 'AZT' 
            			when 814  then 'ABC' 
            			when 6107 then 'TDF+AZT+3TC+LPV/r' 
            			when 6236 then 'D4T+DDI+RTV-IP' 
            			when 1701 then 'ABC+DDI+NFV' 
            			when 6114 then '3DFC' 
            			when 6115 then '2DFC+EFV' 
            			when 6233 then 'AZT+3TC+DDI+LPV' 
            			when 6234 then 'ABC+TDF+LPV' 
            			when 6242 then 'D4T+DDI+NVP' 
            			when 6118 then 'DDI50+ABC+LPV' 
            			else 'OUTRO' end as ultimo_regime, 
            			ultimo_lev.encounter_datetime data_regime 
            	from 	obs o, 
            			(	select p.patient_id,max(encounter_datetime) as encounter_datetime 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            				where 	encounter_type=18 and e.voided=0 and 
            						encounter_datetime <=:startDate and e.location_id=:location and p.voided=0 
            				group by patient_id 
            			) ultimo_lev 
            	where 	o.person_id=ultimo_lev.patient_id and o.obs_datetime=ultimo_lev.encounter_datetime and o.voided=0 and 
            			o.concept_id=1088 and o.location_id=:location 
            ) regime on marcadoVisita.patient_id=regime.patient_id 
            left join person_attribute pat on pat.person_id=marcadoVisita.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value <> '' and pat.voided=0 
            left join
            (
             select p.patient_id,max(e.encounter_datetime) vl_request
              from patient p   
              inner join encounter e on p.patient_id = e.patient_id   
              inner join obs o on o.encounter_id = e.encounter_id   
              where p.voided = 0 and e.voided = 0  and o.voided = 0     
              and  o.concept_id = 23722 and o.value_coded=856 and e.encounter_datetime<=:startDate
              and e.encounter_type=6 and e.location_id=:location 
              group by p.patient_id
            )vl  on vl.patient_id=marcadoVisita.patient_id
            left join (
        			select *
        			from(
	        			select abandono.patient_id, date_add(max(abandono.data_proximo_levantamento), interval 1 day) data_estado, RespostaEstadoPermanencia 
	        			from (
		        			select abandono.patient_id, abandono.data_proximo_levantamento, 1 as RespostaEstadoPermanencia 
		        			from ( 
		   						select patient_id, max(data_proximo_levantamento) data_proximo_levantamento
		   						from ( 
										select p.patient_id, date_add(max(o.value_datetime), interval 30 day) data_proximo_levantamento  
										from patient p 
								     		inner join encounter e on p.patient_id = e.patient_id 
								        		inner join obs o on o.encounter_id = e.encounter_id 
								        		inner join obs obsLevantou on obsLevantou.encounter_id=e.encounter_id 
								    		where e.voided = 0 and p.voided = 0 and o.value_datetime <=:endDate and o.voided = 0 
								     		and obsLevantou.voided=0 and obsLevantou.concept_id=23865 and obsLevantou.value_coded = 1065 
								        		and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
								        		group by p.patient_id 
								  		union 
								     
								    		select p.patient_id, max(o.value_datetime) data_proximo_levantamento                                                                                            
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id= p.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id                                                                                        
										where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
											and e.location_id= :location and e.encounter_datetime <= :endDate                                                                               
											group by p.patient_id 
								)proximo_levantamento group by proximo_levantamento.patient_id
		   	   			)abandono where date_add(abandono.data_proximo_levantamento, INTERVAL 28 DAY) < :endDate 
	        		) abandono 
			     left join
			     ( 
						select transferido_para.patient_id 
						from(
							select transferido_para.patient_id --,data_transferencia  
					    		from ( 
						    			select patient_id,max(data_transferencia) data_transferencia 
						    			from ( 
						    					select maxEstado.patient_id,maxEstado.data_transferencia 
						    					from ( 
						    							select pg.patient_id,max(ps.start_date) data_transferencia 
						    							from patient p 
													    inner join patient_program pg on p.patient_id=pg.patient_id 
													    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date between :startDate and :endDate and pg.location_id=:location 
						    								group by p.patient_id 
						    					) maxEstado 
						    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
						    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
						    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
						
						    					union

						    					select p.patient_id,max(e.encounter_datetime) data_transferidopara from patient p 																		
				            	           		inner join encounter e on p.patient_id=e.patient_id 																								
				            	            		inner join obs o on o.encounter_id=e.encounter_id 																								
				            	            	where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=21 and o.concept_id=2016 and o.value_coded in (1706,23863) 						
				            	            		and e.encounter_datetime between :startDate and :endDate and e.location_id=:location group by p.patient_id 

				            	            		union
						    					 
											select p.patient_id,max(e.encounter_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											   	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate  
												and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
											  	group by p.patient_id 
						   					
						   					union 
											
											select p.patient_id,max(o.obs_datetime) data_transferencia 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
											    	inner join obs o on o.encounter_id=e.encounter_id 
											where e.voided=0 and p.voided=0 and o.obs_datetime between :startDate and :endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
												group by p.patient_id 
											    	
						    		) 
						    	   transferido group by patient_id 
						      ) 
						     transferido_para 
						     left join
							(
								select ultimo_fila.patient_id, ultimo_fila.max_date
								from(
									select p.patient_id,max(encounter_datetime) max_date                                                                                                
									from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
										group by p.patient_id 
								) ultimo_fila
								inner join(
								select patient_id , data_ultimo_levantamento    
								from(  	
						       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						               from(
						         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
											from patient p                                                                                                                                   
												inner join encounter e on e.patient_id= p.patient_id 
												inner join obs o on o.encounter_id = e.encounter_id                                                                                        
											where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
												and e.location_id= :location and e.encounter_datetime between :startDate and :endDate                                                                              
												group by p.patient_id 
						         
						         				union
						         
						              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						              			from patient p                                                                                                                                   
						               			inner join person pe on pe.person_id = p.patient_id                                                                                         
						                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
						                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime between :startDate and :endDate                                                                                        
						              				group by p.patient_id
						               ) ultimo_levantamento group by patient_id
						      	) ultimo_levantamento
						     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
						     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
							
							) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
									where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
						) transferido_para
						inner join
						(
							select patient_id , data_ultimo_levantamento    
							from(  	
						  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
						          from(
						
									select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
									from patient p                                                                                                                                   
										inner join encounter e on e.patient_id= p.patient_id 
										inner join obs o on o.encounter_id = e.encounter_id                                                                                        
									where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
										and e.location_id= :location and e.encounter_datetime between :startDate and :endDate                                                                                
										group by p.patient_id 
						
									union
						
									select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						       		from patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
							     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
							     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
						               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime between :startDate and :endDate                                                                                         
										group by p.patient_id
						          ) ultimo_levantamento group by patient_id
						 	) ultimo_levantamento
							where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
						)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
						where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
	
						union
						
						select patient_id 
						from(
							select suspenso1.patient_id, data_suspencao  
							from( 
								select patient_id,max(data_suspencao) data_suspencao 
								from( 
									select maxEstado.patient_id,maxEstado.data_suspencao 
									from( 
										select pg.patient_id,max(ps.start_date) data_suspencao 
										from patient p 
									    		inner join patient_program pg on p.patient_id=pg.patient_id 
									        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
									    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
									     	group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
										inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
									where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
										and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
									
									union 
							        
							        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
							        	from patient p 
							        		inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
							        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
							        		group by p.patient_id 
									
									union 
							        
							        	select p.patient_id,max(o.obs_datetime) data_suspencao 
							        	from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
							        		inner join obs o on o.encounter_id=e.encounter_id 
							        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
							        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
							        		group by p.patient_id 
							        
							      ) suspenso group by patient_id) suspenso1 
								inner join 
							   	( 
							   		select patient_id,max(encounter_datetime) encounter_datetime 
							   		from( 
							   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
							   			from patient p 
							   				inner join encounter e on e.patient_id=p.patient_id 
							   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
							   				group by p.patient_id 
							   		) consultaLev group by patient_id
							   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
							   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
									
						) suspensos
	
						union
						
						select patient_id
						from(
							select obito.patient_id 
							from ( 
								select patient_id, max(data_obito) data_obito 
								from ( 
							 		select maxEstado.patient_id,maxEstado.data_obito 
							 		from ( 
							 			select pg.patient_id, max(ps.start_date) data_obito 
							 			from patient p 
									 		inner join patient_program pg on p.patient_id = pg.patient_id 
									 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
									 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
									  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =:location 
									 		group by p.patient_id 
									) maxEstado 
										inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
									 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
									where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =:location and ps2.state = 10 
							        
							        	union 
							 		
							 		select p.patient_id, max(o.obs_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
							 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =:location 
							 			group by p.patient_id 
							 		
							 		union
							 		 
							 		select p.patient_id, max(e.encounter_datetime) data_obito 
							 		from patient p 
							 			inner join encounter e on p.patient_id = e.patient_id 
							 			inner join obs o on o.encounter_id = e.encounter_id 
							 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
							 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =:location 
							 			group by p.patient_id 
							 		
							 		union 
								 	
								 	select person_id, death_date 
								 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
								) obito group by patient_id
							) obito 
							inner join 
							( 
								select patient_id, max(encounter_datetime) encounter_datetime 
								from( 
							 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
							 		from patient p 
							 			inner join encounter e on e.patient_id = p.patient_id 
							 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
							 			and e.location_id =:location 
							 			group by p.patient_id 
								) consultaLev group by patient_id 
							) 
							consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
							 where consultaOuARV.encounter_datetime <= obito.data_obito 
						) obitos
			    ) exclusoes on abandono.patient_id = exclusoes.patient_id where exclusoes.patient_id is null
		   )abandonos

		   		   union

		   select *
		   from(
				select obito.patient_id, obito.data_obito, 2 as RespostaEstadoPermanencia 
				from ( 
					select patient_id, max(data_obito) data_obito 
					from ( 
				 		select maxEstado.patient_id,maxEstado.data_obito 
				 		from ( 
				 			select pg.patient_id, max(ps.start_date) data_obito 
				 			from patient p 
						 		inner join patient_program pg on p.patient_id = pg.patient_id 
						 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
						 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
						  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =:location 
						 		group by p.patient_id 
						) maxEstado 
							inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
						 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
						where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =:location and ps2.state = 10 
				        
				        	union 
				 		
				 		select p.patient_id, max(o.obs_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
				 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =:location 
				 			group by p.patient_id 
				 		
				 		union
				 		 
				 		select p.patient_id, max(e.encounter_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
				 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =:location 
				 			group by p.patient_id 
				 		
				 		union 
					 	
					 	select person_id, death_date 
					 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
					) obito group by patient_id
				) obito 
				inner join 
				( 
					select patient_id, max(encounter_datetime) encounter_datetime 
					from( 
				 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
				 		from patient p 
				 			inner join encounter e on e.patient_id = p.patient_id 
				 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
				 			and e.location_id =:location 
				 			group by p.patient_id 
					) consultaLev group by patient_id 
				) 
				consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
				 where consultaOuARV.encounter_datetime <= obito.data_obito 
		   )obitos


		union


				select transferido_para.patient_id, data_transferencia, transferido_para.RespostaEstadoPermanencia from (
			 	select transferido_para.patient_id, data_transferencia, RespostaEstadoPermanencia
	    	from ( 
		    	 select transferido_para.patient_id,data_transferencia, 3 as RespostaEstadoPermanencia   
		    	 from ( 
		    			select patient_id,max(data_transferencia) data_transferencia 
		    			from ( 
		    					select maxEstado.patient_id,maxEstado.data_transferencia 
		    					from ( 
		    							select pg.patient_id,max(ps.start_date) data_transferencia 
		    							from patient p 
									    inner join patient_program pg on p.patient_id=pg.patient_id 
									    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
		    							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date between :startDate and :endDate and pg.location_id=:location 
		    								group by p.patient_id 
		    					) maxEstado 
		    						inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
		    						inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
		    					where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
		    						and ps2.start_date=maxEstado.data_transferencia and pg2.location_id=:location and ps2.state=7 
		
		    					union

		    					select p.patient_id,max(e.encounter_datetime) data_transferidopara from patient p 																		
            	           		inner join encounter e on p.patient_id=e.patient_id 																								
            	            		inner join obs o on o.encounter_id=e.encounter_id 																								
            	            	where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=21 and o.concept_id=2016 and o.value_coded in (1706,23863) 						
            	            		and e.encounter_datetime between :startDate and :endDate and e.location_id=:location group by p.patient_id 	

            	            		union
		    					 
							select p.patient_id,max(e.encounter_datetime) data_transferencia 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							   	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and e.encounter_datetime between :startDate and :endDate  
								and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and e.location_id=:location 
							  	group by p.patient_id 
		   					
		   					union 
							
							select p.patient_id,max(o.obs_datetime) data_transferencia 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
							    	inner join obs o on o.encounter_id=e.encounter_id 
							where e.voided=0 and p.voided=0 and o.obs_datetime between :startDate and :endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and e.location_id=:location 
								group by p.patient_id 
							    	
		    		) 
		    	   transferido group by patient_id 
		      ) 
		     transferido_para 
		     left join
			(
				select ultimo_fila.patient_id, ultimo_fila.max_date
				from(
					select p.patient_id,max(encounter_datetime) max_date                                                                                                
					from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
						inner join encounter e on e.patient_id=p.patient_id                                                                                         
					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
						and e.location_id=:location  and e.encounter_datetime <=:endDate                                                                             
						group by p.patient_id 
				) ultimo_fila
				inner join(
				select patient_id , data_ultimo_levantamento    
				from(  	
		       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		               from(
		         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
							from patient p                                                                                                                                   
								inner join encounter e on e.patient_id= p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id                                                                                        
							where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
								and e.location_id= :location and e.encounter_datetime <= :endDate                                                                               
								group by p.patient_id 
		         
		         				union
		         
		              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
		              			from patient p                                                                                                                                   
		               			inner join person pe on pe.person_id = p.patient_id                                                                                         
		                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
		                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
		              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
		                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime <= :endDate                                                                                        
		              				group by p.patient_id
		               ) ultimo_levantamento group by patient_id
		      	) ultimo_levantamento
		     		where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
		     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
			
			) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
					where transferido_para.data_transferencia >= ultimo_fila.max_date
	   )
	   transferido_para
	   inner join
		(
			select patient_id , data_ultimo_levantamento    
			from(  	
		  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
		          from(
		
					select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
					from patient p                                                                                                                                   
						inner join encounter e on e.patient_id= p.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id                                                                                        
					where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
						and e.location_id= :location and e.encounter_datetime between :startDate and :endDate                                                                                
						group by p.patient_id 
		
					union
		
					select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
		       		from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
			     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
			     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
		       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
		               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime between :startDate and :endDate                                                                                         
						group by p.patient_id
		          ) ultimo_levantamento group by patient_id
		 	) ultimo_levantamento
			where ultimo_levantamento.data_ultimo_levantamento <= :endDate  
		)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
		where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento  <= :endDate )
		) transferido_para left join
		(
				select obito.patient_id, obito.data_obito, 2 as RespostaEstadoPermanencia 
				from ( 
					select patient_id, max(data_obito) data_obito 
					from ( 
				 		select maxEstado.patient_id,maxEstado.data_obito 
				 		from ( 
				 			select pg.patient_id, max(ps.start_date) data_obito 
				 			from patient p 
						 		inner join patient_program pg on p.patient_id = pg.patient_id 
						 		inner join patient_state ps on pg.patient_program_id = ps.patient_program_id 
						 	where pg.voided = 0 and ps.voided = 0 and p.voided = 0  
						  		and pg.program_id = 2 and DATE(ps.start_date) <= :endDate and pg.location_id =:location 
						 		group by p.patient_id 
						) maxEstado 
							inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id 
						 	inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id 
						where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2  and ps2.start_date = maxEstado.data_obito and pg2.location_id =:location and ps2.state = 10 
				        
				        	union 
				 		
				 		select p.patient_id, max(o.obs_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(o.obs_datetime) <= :endDate  
				 			and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1366 and e.encounter_type = 53 and  e.location_id =:location 
				 			group by p.patient_id 
				 		
				 		union
				 		 
				 		select p.patient_id, max(e.encounter_datetime) data_obito 
				 		from patient p 
				 			inner join encounter e on p.patient_id = e.patient_id 
				 			inner join obs o on o.encounter_id = e.encounter_id 
				 		where e.voided = 0 and p.voided = 0 and DATE(e.encounter_datetime) <= :endDate 
				 			and o.voided = 0 and o.concept_id = 6273 and o.value_coded = 1366 and e.encounter_type = 6 and  e.location_id =:location 
				 			group by p.patient_id 
				 		
				 		union 
					 	
					 	select person_id, death_date 
					 	from person p where p.dead = 1 and date(p.death_date) <= :endDate
					) obito group by patient_id
				) obito 
				inner join 
				( 
					select patient_id, max(encounter_datetime) encounter_datetime 
					from( 
				 		select p.patient_id, max(e.encounter_datetime) encounter_datetime 
				 		from patient p 
				 			inner join encounter e on e.patient_id = p.patient_id 
				 		where p.voided = 0 and e.voided = 0 and e.encounter_type in (18,6,9)  and date(e.encounter_datetime) <= :endDate 
				 			and e.location_id =:location 
				 			group by p.patient_id 
					) consultaLev group by patient_id 
				) 
				consultaOuARV on obito.patient_id = consultaOuARV.patient_id 
				 where consultaOuARV.encounter_datetime <= obito.data_obito 
		)obitos on obitos.patient_id = transferido_para.patient_id
		where obitos.patient_id is null

		union

		select *
		   from(
		   		select suspenso1.patient_id, data_suspencao, 4 as RespostaEstadoPermanencia  
				from( 
					select patient_id,max(data_suspencao) data_suspencao 
					from( 
						select maxEstado.patient_id,maxEstado.data_suspencao 
						from( 
							select pg.patient_id,max(ps.start_date) data_suspencao 
							from patient p 
						    		inner join patient_program pg on p.patient_id=pg.patient_id 
						        	inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						    	where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
						     	group by p.patient_id 
						) maxEstado 
							inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
							inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
						where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2  
							and ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 
						
						union 
				        
				        	select p.patient_id,max(e.encounter_datetime) data_suspencao 
				        	from patient p 
				        		inner join encounter e on p.patient_id=e.patient_id 
				        		inner join obs o on o.encounter_id=e.encounter_id 
				        	where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 
				        		and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location 
				        		group by p.patient_id 
						
						union 
				        
				        	select p.patient_id,max(o.obs_datetime) data_suspencao 
				        	from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
				        		inner join obs o on o.encounter_id=e.encounter_id 
				        	where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 
				        		and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location 
				        		group by p.patient_id 
				        
				      ) suspenso group by patient_id) suspenso1 
					inner join 
				   	( 
				   		select patient_id,max(encounter_datetime) encounter_datetime 
				   		from( 
				   			select p.patient_id,max(e.encounter_datetime) encounter_datetime 
				   			from patient p 
				   				inner join encounter e on e.patient_id=p.patient_id 
				   			where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type=18 
				   				group by p.patient_id 
				   		) consultaLev group by patient_id
				   	) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id 
				   	where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao 
		   ) suspensos
            ) estadoPermanencia on estadoPermanencia.patient_id = marcadoVisita.patient_id 
            left join 
            (	select pid1.* 
            	from patient_identifier pid1 
            	inner join 
            		( 
            			select patient_id,min(patient_identifier_id) id 
            			from patient_identifier 
            			where voided=0 
            			group by patient_id 
            		) pid2 
            	where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
            ) pid on pid.patient_id=marcadoVisita.patient_id 
            left join 
            (	select pn1.* 
            	from person_name pn1 
            	inner join 
            		( 
            			select person_id,min(person_name_id) id 
            			from person_name 
            			where voided=0 
            			group by person_id 
            		) pn2 
            	where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
            ) pn on pn.person_id=marcadoVisita.patient_id 
            inner join person per on per.person_id=marcadoVisita.patient_id 
            where marcadoVisita.data_proxima_consulta between :startDate and :endDate and (marcadoVisita.data_carga is not null or ultimaCarga.data_carga is null) 