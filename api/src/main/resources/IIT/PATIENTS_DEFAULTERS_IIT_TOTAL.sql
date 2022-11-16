          select inicio.patient_id from                                                                                                
             (                                                                                                                                      												
               select art_started.patient_id, art_started .art_start_date from                                                                      												
               (                                                                                                                                    												
                   select patient_id, art_start_date from                                                                                           												
                   (                                                                                                                                												
                       select patient_id, min(art_start_date) art_start_date from                                                                   												
                       (                                                                                                                            												
                         select p.patient_id, min(e.encounter_datetime) art_start_date from patient p                                               												
                           inner join encounter e on p.patient_id=e.patient_id                                                                      												
                           inner join obs o on o.encounter_id=e.encounter_id                                                                        												
                         where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9)                                            												
                           and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location group by p.patient_id 									
                         union                                                                                                                      												
                         select p.patient_id, min(value_datetime) art_start_date from patient p inner join encounter e on p.patient_id=e.patient_id 												
                           inner join obs o on e.encounter_id=o.encounter_id where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type IN (18,6,9,53) 									
                           and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id 								
                         union                                                                                                                      												
                         select pg.patient_id, min(date_enrolled) art_start_date from patient p                                                     												
                           inner join patient_program pg on p.patient_id=pg.patient_id                                                              												
                         where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location group by pg.patient_id 											
                         union                                                                                                                      												
                         select e.patient_id, min(e.encounter_datetime) AS art_start_date from patient p                                            												
                           inner join encounter e on p.patient_id=e.patient_id                                                                      												
                         where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location group by p.patient_id 							
                         union                                                                                                                      												
                         select p.patient_id, min(value_datetime) art_start_date from patient p                                                     												
                           inner join encounter e on p.patient_id=e.patient_id                                                                      												
                           inner join obs o on e.encounter_id=o.encounter_id                                                                        												
                         where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                     												
                           and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id 								
                       ) art_start group by patient_id                                                                                              												
                 ) tx_new where art_start_date  <= :endDate                                                                                         												
                   union                                                                                                                            												
                   select state_transferred_in.patient_id,art_start_date from                                                                       												
                   (                                                                                                                                												
                       select p.patient_id, pg.patient_program_id, ps.start_date as art_start_date  from patient p                                  												
                           inner join patient_program pg on p.patient_id=pg.patient_id                                                              												
                             inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                             												
                       where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  and ps.start_date <= :endDate 											
                       group by pg.patient_program_id                                                                                               												
                   ) state_transferred_in                                                                                                           												
                     inner join patient_state ps on ps.patient_program_id=state_transferred_in.patient_program_id                                   												
                   where ps.start_date=state_transferred_in.art_start_date and ps.state=29 and ps.voided=0                                          												
                   union                                                                                                                            												
                 select state_transferred_in.patient_id, art_start_date from                                                                        												
                   (                                                                                                                                												
                       select p.patient_id, obsdata.value_datetime art_start_date from patient p                                                    												
                         inner join encounter e on p.patient_id=e.patient_id                                                                        												
                         inner join obs obstrans on e.encounter_id=obstrans.encounter_id and obstrans.voided=0 and obstrans.concept_id=1369 and obstrans.value_coded=1065 						
                         inner join obs obstarv on e.encounter_id=obstarv.encounter_id and obstarv.voided=0 and obstarv.concept_id=6300 and obstarv.value_coded=6276  							
                         inner join obs obsdata on e.encounter_id=obsdata.encounter_id and obsdata.voided=0 and obsdata.concept_id=23891             											
                     where p.voided=0 and e.voided=0 and e.encounter_type=53 and obsdata.value_datetime <= :endDate  and e.location_id=:location group by p.patient_id 							
                   ) state_transferred_in group by state_transferred_in.patient_id                                                                  												
               ) art_started                                                                                                                        												
               left join                                                                                                                            												
               (                                                                                                                                    												
                 select all_saidas.patient_id , all_saidas.data_estado from                                                                         												
                 (                                                                                                                                  												
                   select patient_id,max(data_estado) data_estado from                                                                              												
                   (                                                                                                                                												
            					select distinct max_estado.patient_id,                               																															
            							max_estado.data_estado		                               																								
            							from ( 																																					
            								select pg.patient_id,                               																								
            									max(ps.start_date) data_estado                               																					
            								from patient p                                																										
            									inner join patient_program pg on p.patient_id=pg.patient_id                               														
            									inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                               										
            								where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2   																				
            									and pg.location_id=:location and ps.start_date<= curdate() group by pg.patient_id 																	
            								) 																																					
            							max_estado                               																												
            								inner join patient_program pp on pp.patient_id = max_estado.patient_id															 					
            								inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado	        					
            							where pp.program_id = 2 and ps.state in (7,8,10) and pp.voided = 0 and ps.voided = 0 and ps.start_date<= curdate() and pp.location_id = :location     	
                           union                                                                                                                    												
                           select p.patient_id, max(o.obs_datetime) data_estado from patient p                                                      												
                               inner join encounter e on p.patient_id=e.patient_id                                                                  												
                               inner join obs  o on e.encounter_id=o.encounter_id                                                                   												
                           where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) 					
                             and o.obs_datetime<= curdate() and e.location_id=:location group by p.patient_id                                         												
                           union                                                                                                                    												
                           select person_id as patient_id,death_date as data_estado from person                                                     												
                           where dead=1 and death_date is not null and death_date<= curdate()                                                         												
                           union                                                                                                                    												
                           select  p.patient_id,max(obsObito.obs_datetime) data_estado from  patient p                                              												
                               inner join encounter e on p.patient_id=e.patient_id                                                                  												
                               inner join obs obsObito on e.encounter_id=obsObito.encounter_id                                                      												
                           where e.voided=0 and p.voided=0 and obsObito.voided=0 and e.encounter_type in (21,36,37) and  e.encounter_datetime<= curdate() and e.location_id=:location 				
                             and obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366 group by p.patient_id                      												
                           union                                                                                                                    												
                           select  p.patient_id,max(e.encounter_datetime) data_estado from  patient p                                               												
                               inner join encounter e on p.patient_id=e.patient_id                                                                  												
                               inner join obs o on o.encounter_id=e.encounter_id                                                                    												
                           where e.voided=0 and p.voided=0 and e.encounter_datetime<= curdate() and o.voided=0 and o.concept_id=2016                  												
                             and o.value_coded in (1706,23863) and e.encounter_type in (21,36,37) and  e.location_id=:location group by p.patient_id 											
                       ) all_saidas  group by patient_id                                                                                            												
                     ) all_saidas                                                                                                                   												
                     inner join                                                                                                                     												
                     (                                                                                                                              												
                       select patient_id,max(encounter_datetime) encounter_datetime from                                                            												
                       (                                                                                                                            												
                           select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p                                         												
                             inner join encounter e on e.patient_id=p.patient_id                                                                    												
                           where p.voided=0 and e.voided=0 and e.encounter_datetime<= curdate() and e.location_id=:location and e.encounter_type in (18,6,9) group by p.patient_id 				
                           union                                                                                                                    												
                           select p.patient_id,max(value_datetime) encounter_datetime from  patient p                                               												
                             inner join encounter e on p.patient_id=e.patient_id                                                                    												
                             inner join obs o on e.encounter_id=o.encounter_id                                                                      												
                           where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null 										
                             and o.value_datetime<= curdate() and e.location_id=:location group by p.patient_id                                       												
                       ) consulta_levantamento group by patient_id                                                                                  												
                   ) all_consulta_levantamento on all_consulta_levantamento.patient_id = all_saidas.patient_id                                      												
                 where all_consulta_levantamento.encounter_datetime <= all_saidas.data_estado and all_saidas.data_estado <= curdate()                												
                                                                                                                                                    												
               ) saidas on saidas.patient_id = art_started.patient_id                                                                               												
               where saidas.data_estado is null or( saidas.data_estado is not null and saidas.data_estado < art_started.art_start_date)             												
             ) inicio                                                                                                                               												
             inner join                                                                                                                             												
             (                                                                                                                                      												
               select patient_id, max(encounter_datetime) encounter_datetime from                                                                   												
               (                                                                                                                                    												
                 select patient_id, max(encounter_datetime) encounter_datetime from                                                                 												
                 (                                                                                                                                  												
                     select last_fila.patient_id, next_fila.value_datetime encounter_datetime from                                                  												
                     (                                                                                                                              												
                         select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p                                            												
                           inner join encounter e on e.patient_id=p.patient_id                                                                      												
                         where p.voided=0 and e.voided=0 and e.encounter_type = 18 and e.encounter_datetime<=:endDate and e.location_id=:location   												
                           group by p.patient_id                                                                                                    												
                     ) last_fila                                                                                                                    												
                         inner join encounter e on e.patient_id = last_fila.patient_id and e.encounter_datetime = last_fila.encounter_datetime      												
                         left join obs next_fila on next_fila.encounter_id = e.encounter_id                                                          											
                     where  e.voided = 0 and next_fila.voided = 0 and e.encounter_type =18 and e.encounter_datetime = last_fila.encounter_datetime  												
                         and next_fila.concept_id = 5096 and next_fila.value_datetime is not null                                                   												
                       union                                                                                                                        												
                     select levantamento_master_card.patient_id, DATE_ADD(levantamento_master_card.encounter_datetime, INTERVAL 30 DAY) encounter_datetime  from 								
                     (                                                                                                                              												
                         select p.patient_id,max(data_levantamento_arv.value_datetime) encounter_datetime from patient p                            												
                             inner join encounter e on p.patient_id=e.patient_id                                                                    												
                             left join obs data_levantamento_arv on data_levantamento_arv.encounter_id = e.encounter_id                            												
                           where p.voided=0 and e.voided=0 and data_levantamento_arv.voided=0 and e.encounter_type=52 and data_levantamento_arv.concept_id=23866 								
                             and data_levantamento_arv.value_datetime is not null and e.location_id=:location                                       												
                             and data_levantamento_arv.value_datetime <= :endDate group by p.patient_id                                             												
                     ) levantamento_master_card                                                                                                     												
                 ) all_levantamentos  group by all_levantamentos.patient_id                                                                         												
               )all_levantamentos                                                                                                                   												
               where (TIMESTAMPDIFF(DAY,all_levantamentos.encounter_datetime,:endDate)) BETWEEN :minDelayDays AND :maxDelayDays group by all_levantamentos.patient_id 							
             ) iit_patients on iit_patients.patient_id = inicio.patient_id                                                                             											;
