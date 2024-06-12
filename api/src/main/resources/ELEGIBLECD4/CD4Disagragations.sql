			select final.patient_id  as patient_id 
			from
			(
			select p.person_id as patient_id,
			if(C1.patient_id is not null,1,
			if(C2.patient_id is not null,2,
			if(C3.patient_id is not null,3,
			if(C4.patient_id is not null,4,
			if(C5.patient_id is not null,5,
			if(C6.patient_id is not null,6,null)))))) criteria
			from person p
			left join
			(
               SELECT C1.patient_id, 1 criteria FROM  
              (
              SELECT tx_new. patient_id,art_start_date,trInProgram.minStateDate,trInMasterCard.value_datetime, cd4.data_cd4 FROM 
              (
              SELECT patient_id, MIN(art_start_date) art_start_date FROM 
              ( 
              SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON o.encounter_id=e.encounter_id 
              WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18) 
              AND e.encounter_datetime<=:endDate AND e.location_id=:location 
              GROUP BY p.patient_id 
              UNION 
              SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p 
              INNER JOIN encounter e ON p.patient_id=e.patient_id 
              INNER JOIN obs o ON e.encounter_id=o.encounter_id 
              WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
              AND o.concept_id=23866 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endDate AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) 
              art_start GROUP BY patient_id 
            ) tx_new 

            left join
            (
             select final.* from  ( 
              select states.patient_id,states.patient_program_id,min(states.minStateDate) as minStateDate,states.program_id,states.state from  
              ( 
              SELECT p.patient_id, pg.patient_program_id, ps.start_date as minStateDate, pg.program_id, ps.state  FROM patient p   
              inner join patient_program pg on p.patient_id=pg.patient_id  
              inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  
              WHERE pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and location_id=:location  
              and ps.start_date <= :endDate 
              )states 
              group by states.patient_id 
              order by states.minStateDate asc  
              ) final 
              inner join patient_state ps on ps.patient_program_id=final.patient_program_id  
              where ps.start_date=final.minStateDate and ps.state=29 and ps.voided=0 
  
            )trInProgram on trInProgram.patient_id=tx_new.patient_id

            left join
            (
             SELECT tr.* from  
             (
              SELECT p.patient_id, MIN(obsData.value_datetime) value_datetime from patient p  
              INNER JOIN encounter e ON p.patient_id=e.patient_id  
              INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 
              INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 
              WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND obsData.value_datetime <= :endDate AND e.location_id=:location 
              GROUP BY p.patient_id 
              ) tr 
              GROUP BY tr.patient_id 
            )trInMasterCard on trInMasterCard.patient_id=tx_new.patient_id
            left join
            (
              select p.patient_id,o.obs_datetime data_cd4
              from patient p   
              inner join encounter e on p.patient_id = e.patient_id   
              inner join obs o on o.encounter_id = e.encounter_id   
              where p.voided = 0 
              and e.voided = 0  
              and o.voided = 0     
              and  o.concept_id in(1695,730) 
              and e.encounter_type=6 
              and e.location_id=:location 
              and o.obs_datetime <=CURDATE()
            )cd4 on cd4.patient_id=tx_new.patient_id
            )C1 
           where (C1.art_start_date BETWEEN :startDate AND :endDate) and (C1.minStateDate is null and C1.value_datetime is null and C1.data_cd4 is null)
           )C1 on C1.patient_id=p.person_id
           left join
           (
           	  select C2.patient_id,C2.criteria from 
        (
              select C2.patient_id,C2.data_estado, 2 criteria 
              from 
              ( 
               select p.patient_id, max(o.obs_datetime) data_estado 
               from patient p 
               inner join encounter e on p.patient_id=e.patient_id  
               inner join obs  o on e.encounter_id=o.encounter_id 
               where e.voided=0 and o.voided=0 
               and p.voided=0 
               and  e.encounter_type in (53,6) 
               and o.concept_id in (6272,6273) 
               and o.value_coded in (1705) 
               and o.obs_datetime BETWEEN :startDate and :endDate 
               and e.location_id=:location 
               group by p.patient_id 
               )C2
               left join
               (
                select p.patient_id,o.obs_datetime data_cd4
                from patient p   
                inner join encounter e on p.patient_id = e.patient_id   
                inner join obs o on o.encounter_id = e.encounter_id   
                where p.voided = 0 
                and e.voided = 0  
                and o.voided = 0     
                and  o.concept_id in(1695,730) 
                and e.encounter_type=6 
                and e.location_id=:location 
                and o.obs_datetime  BETWEEN :startDate and  CURDATE()
              )cd4 on cd4.patient_id=C2.patient_id
               WHERE cd4.patient_id is null
               )C2 
           )C2 on C2.patient_id=p.person_id
           left join
           (
            select C3.patient_id, 3 criteria from 
          (
          select ultimoCV.patient_id,ultimoCV.data_resultado,ultimoCV.resultado,max(cvAnterior.data_resultado) data_resultado_anterior,o.value_numeric resultado_anterior 
          from (
           select cv.patient_id,max(cv.data_resultado) data_resultado ,cv.resultado
            from (
                  select p.patient_id,o.obs_datetime data_resultado, o.value_numeric as resultado
                  from patient p   
                  inner join encounter e on p.patient_id = e.patient_id   
                  inner join obs o on o.encounter_id = e.encounter_id   
                  where p.voided = 0 
                  and e.voided = 0  
                  and o.voided = 0
                  and  o.concept_id=856
                  and  o.obs_datetime BETWEEN date_sub(:endDate, interval 6 month) and :endDate
                  and e.encounter_type=6 
                  and e.location_id=:location 
                  )cv 
                  WHERE cv.resultado>1000
                  group by cv.patient_id
                 )ultimoCV
                 left join 
                 (
                  select p.patient_id,o.obs_datetime data_resultado
                  from patient p   
                  inner join encounter e on p.patient_id = e.patient_id   
                  inner join obs o on o.encounter_id = e.encounter_id   
                  where p.voided = 0 
                  and e.voided = 0  
                  and o.voided = 0
                  and  o.concept_id=856
                  and  o.obs_datetime BETWEEN date_sub(:endDate, interval 6 month) and :endDate
                  and e.encounter_type=6 
                  and e.location_id=:location 
                 ) cvAnterior on ultimoCV.patient_id=cvAnterior.patient_id
                 left join encounter e on cvAnterior.patient_id = e.patient_id   
                 left join obs o on o.encounter_id = e.encounter_id   
                  WHERE e.encounter_type=6 and o.concept_id=856 and  o.value_numeric>1000 and o.obs_datetime=cvAnterior.data_resultado and e.voided=0 and o.voided=0 and cvAnterior.data_resultado<ultimoCV.data_resultado
                 group by cvAnterior.patient_id
                 )C3
                 left join
                (
                 select cv.patient_id,max(cv.data_resultado) data_resultado,cv.resultado,cd4.data_cd4,cd4.encounter_id
              from (
                    select p.patient_id,o.obs_datetime data_resultado, o.value_numeric as resultado
                    from patient p   
                    inner join encounter e on p.patient_id = e.patient_id   
                    inner join obs o on o.encounter_id = e.encounter_id   
                    where p.voided = 0 
                    and e.voided = 0  
                    and o.voided = 0
                    and  o.concept_id=856
                    and  o.obs_datetime BETWEEN date_sub(:endDate, interval 6 month) and :endDate
                    and e.encounter_type=6 
                    and e.location_id=:location 
                    )cv 
                    left join 
                    (
                    select p.patient_id,o.obs_datetime data_cd4,e.encounter_id
                    from patient p   
                    inner join encounter e on p.patient_id = e.patient_id   
                    inner join obs o on o.encounter_id = e.encounter_id   
                    where p.voided = 0 
                    and e.voided = 0  
                    and o.voided = 0     
                    and  o.concept_id in(1695,730) 
                    and e.encounter_type=6 
                    and e.location_id=:location 
                    )cd4 on cd4.patient_id=cv.patient_id
                    where cv.resultado>1000 and cd4.data_cd4 BETWEEN cv.data_resultado and CURDATE()
                    group by cv.patient_id
                
                 )cd4 on cd4.patient_id=C3.patient_id
                WHERE  cd4.patient_id  is null
           )C3 on C3.patient_id=p.person_id
           left join
           (
            select C4.patient_id,4 criteria
                  from
                  (
                  select III.patient_id,III.data_estadio
                  from 
                  ( 
                   select p.patient_id, min(o.obs_datetime) data_estadio 
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0 
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1406 
                   and o.value_coded in (5018,5334,5018,6783,5945,126,60,43,42) 
                   and o.obs_datetime BETWEEN :startDate and :endDate 
                   and e.location_id=:location 
                   group by p.patient_id 
                   )III
           
                  union 

                  select IV.patient_id,IV.data_estadio
                  from 
                  ( 
                   select p.patient_id, min(o.obs_datetime) data_estadio 
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0 
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1406 
                   and o.value_coded in (5340,6990,5344,1294,507,14656,7180,5042,1570) 
                   and o.obs_datetime BETWEEN :startDate and :endDate 
                   and e.location_id=:location 
                   group by p.patient_id 
                   )IV
                   )C4
                  left join

                  (

                  select estadioOMS.patient_id,min(estadioOMS.data_estadio)
                  from
                  (
                  select III.patient_id,III.data_estadio
                  from 
                  ( 
                   select p.patient_id, min(o.obs_datetime) data_estadio 
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0 
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1406 
                   and o.value_coded in (5018,5334,5018,6783,5945,126,60,43,42) 
                   and o.obs_datetime BETWEEN :startDate and :endDate 
                   and e.location_id=:location 
                   group by p.patient_id 
                   )III
           
                  union 

                  select IV.patient_id,IV.data_estadio
                  from 
                  ( 
                   select p.patient_id, min(o.obs_datetime) data_estadio 
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0 
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1406 
                   and o.value_coded in (5340,6990,5344,1294,507,14656,7180,5042,1570) 
                   and o.obs_datetime BETWEEN :startDate and :endDate 
                   and e.location_id=:location 
                   group by p.patient_id 
                   )IV
                   )estadioOMS
                   left join
                   (
                    select p.patient_id,o.obs_datetime data_cd4,e.encounter_id
                      from patient p   
                      inner join encounter e on p.patient_id = e.patient_id   
                      inner join obs o on o.encounter_id = e.encounter_id   
                      where p.voided = 0 
                      and e.voided = 0  
                      and o.voided = 0     
                      and  o.concept_id in(1695,730) 
                      and e.encounter_type=6 
                      and e.location_id=:location 
                      )cd4 on cd4.patient_id=estadioOMS.patient_id
                      where cd4.data_cd4 BETWEEN estadioOMS.data_estadio and CURDATE()
                      group by estadioOMS.patient_id
                 
                  )exclusao on exclusao.patient_id=C4.patient_id
                  where exclusao.patient_id is null
                  group by C4.patient_id
           )C4 on C4.patient_id=p.person_id
           left join
           (
             select C5.patient_id, 5 criteria from
                  (
                  select CD4Absuluto.patient_id,CD4Absuluto.data_cd4,CD4Absuluto.cd4
                  from 
                  ( 
                   select p.patient_id, min(e.encounter_datetime) data_cd4,o.value_numeric cd4
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0  
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1695 
                   and o.obs_datetime BETWEEN date_sub(:endDate, interval 12 month) and :endDate 
                   and e.location_id=:location 
                   and o.value_numeric<350
                   group by p.patient_id 
                   )CD4Absuluto

                   union

                  select CD4Percentual.patient_id,CD4Percentual.data_cd4,CD4Percentual.cd4
                  from 
                  ( 
                   select p.patient_id, min(e.encounter_datetime) data_cd4,o.value_numeric cd4
                   from patient p 
                   inner join encounter e on p.patient_id=e.patient_id  
                   inner join obs  o on e.encounter_id=o.encounter_id 
                   where e.voided=0 and o.voided=0  
                   and p.voided=0 
                   and  e.encounter_type=6 
                   and o.concept_id=1695 
                   and o.obs_datetime BETWEEN date_sub(:endDate, interval 12 month) and :endDate 
                   and e.location_id=:location 
                   and o.value_numeric<30
                   group by p.patient_id 
                   )CD4Percentual
                   )C5
                   left join

                   (
                    select final.patient_id from
                     (
                    select CD4Absuluto.patient_id,CD4Absuluto.data_cd4,CD4Absuluto.cd4
                    from 
                    ( 
                     select p.patient_id, min(e.encounter_datetime) data_cd4,o.value_numeric cd4
                     from patient p 
                     inner join encounter e on p.patient_id=e.patient_id  
                     inner join obs  o on e.encounter_id=o.encounter_id 
                     where e.voided=0 and o.voided=0  
                     and p.voided=0 
                     and  e.encounter_type=6 
                     and o.concept_id=1695 
                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 12 month) and :endDate 
                     and e.location_id=:location 
                     and o.value_numeric<350
                     group by p.patient_id 
                     )CD4Absuluto

                     union
  
                    select CD4Percentual.patient_id,CD4Percentual.data_cd4,CD4Percentual.cd4
                    from 
                    ( 
                     select p.patient_id, min(e.encounter_datetime) data_cd4,o.value_numeric cd4
                     from patient p 
                     inner join encounter e on p.patient_id=e.patient_id  
                     inner join obs  o on e.encounter_id=o.encounter_id 
                     where e.voided=0 and o.voided=0  
                     and p.voided=0 
                     and  e.encounter_type=6 
                     and o.concept_id=1695 
                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 12 month) and :endDate 
                     and e.location_id=:location 
                     and o.value_numeric<30
                     group by p.patient_id 
                     )CD4Percentual
                     )final
                     left join
                     (
                     select p.patient_id,o.obs_datetime data_cd4,o.value_numeric
                        from patient p   
                        inner join encounter e on p.patient_id = e.patient_id   
                        inner join obs o on o.encounter_id = e.encounter_id   
                        where p.voided = 0 
                        and e.voided = 0  
                        and o.voided = 0     
                        and  o.concept_id in(1695,730) 
                        and e.encounter_type=6 
                        and e.location_id=:location 
                      )cd4 on cd4.patient_id=final.patient_id
                      where cd4.data_cd4 BETWEEN date_add(final.data_cd4, interval 1 day) and CURDATE()
                   )exclusion on exclusion.patient_id=C5.patient_id
                   where exclusion.patient_id is null
           )C5 on C5.patient_id=p.person_id
           left join
           (
            select C6.patient_id, 6 criteria
                    from 
                    ( 
                     select p.patient_id, min(e.encounter_datetime) data_gravida
                     from patient p 
                     inner join encounter e on p.patient_id=e.patient_id  
                     inner join obs  o on e.encounter_id=o.encounter_id 
                     where e.voided=0 and o.voided=0  
                     and p.voided=0 
                     and  e.encounter_type=6 
                     and o.concept_id=1982 
                     and o.value_coded=1065
                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 9 month) and :endDate 
                     and e.location_id=:location 
                     group by p.patient_id 
                     )C6
                     left join
                     (
                    select gravida.patient_id,gravida.data_gravida
                    from 
                    ( 
                     select p.patient_id, min(e.encounter_datetime) data_gravida
                     from patient p 
                     inner join encounter e on p.patient_id=e.patient_id  
                     inner join obs  o on e.encounter_id=o.encounter_id 
                     where e.voided=0 and o.voided=0  
                     and p.voided=0 
                     and  e.encounter_type=6 
                     and o.concept_id=1982 
                     and o.value_coded=1065
                     and o.obs_datetime BETWEEN date_sub(:endDate, interval 9 month) and :endDate 
                     and e.location_id=:location 
                     group by p.patient_id 
                     )gravida
                     left join
                     (
                     
                        select p.patient_id,o.obs_datetime data_cd4
                        from patient p   
                        inner join encounter e on p.patient_id = e.patient_id   
                        inner join obs o on o.encounter_id = e.encounter_id   
                        where p.voided = 0 
                        and e.voided = 0  
                        and o.voided = 0     
                        and  o.concept_id in(1695,730) 
                        and e.encounter_type=6 
                        and e.location_id=:location 
                      )cd4 on cd4.patient_id=gravida.patient_id 
                      WHERE cd4.data_cd4  BETWEEN gravida.data_gravida and CURDATE()
                      )exclusion on exclusion.patient_id=C6.patient_id
                      where exclusion.patient_id is null
           )C6 on C6.patient_id=p.person_id
           )final 
           left join
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
                       )saidas on saidas.patient_id=final.patient_id
