              select 
                 coorteFinal.patient_id as patient_id,
                 concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
                 pid.identifier as NID, 
                 p.gender as gender, 
                 DATE_FORMAT(DATE(inicio.data_inicio), '%d-%m-%Y')  as data_inicio,
                 floor(datediff(:endDate,p.birthdate)/365) as idade_actual,
                 if(transferedIn.patient_id is not null,'Sim','Não') transfered_in,
                 if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
				 max_fila.data_fila,
				 max_fila.data_proximo_lev,
				 DATE_FORMAT(DATE(maxConsulta.data_seguimento), '%d-%m-%Y')  as data_seguimento,
				 DATE_FORMAT(DATE(max(obs_seguimento.value_datetime)), '%d-%m-%Y')  as data_proximo_seguimento,
				 coorteFinal.criteria,
			     case 
	             when coorteFinal.criteria = 1 then 'C1– CD4 Inicial - Novo Início TARV' 
	             when coorteFinal.criteria = 2 then 'C2– CD4 Inicial - Reinício TARV' 
	             when coorteFinal.criteria = 3 then 'C3– CV Alta' 
	             when coorteFinal.criteria = 4 then 'C4– Estadiamento Clínico III ou IV' 
	             when coorteFinal.criteria = 5 then 'C5– CD4 de Seguimento'
	             when coorteFinal.criteria = 6 then 'C6– Mulher Grávida'
	             end as criterioElegibilidade,
	             DATE_FORMAT(DATE(cd4.data_ultimo_cd4), '%d-%m-%Y')  as data_ultimo_cd4,
                 DATE_FORMAT(DATE(cd4Final.data_resultado_cd4), '%d-%m-%Y')  as data_resultado_cd4,
                 cd4Final.valor_cd4 valor_cd4,
                 DATE_FORMAT(DATE(cd4Final.data_resultado_peneultimo_cd4), '%d-%m-%Y')  as data_resultado_peneultimo_cd4,
	             cd4Final.resultado_peneultimo_cd4 resultado_peneultimo_cd4, 
	             DATE_FORMAT(DATE(estadiamentoClinico.encounter_datetime), '%d-%m-%Y')  as encounter_datetime,
	             case 
		         when estadiamentoClinico.tipoEstadio=3 then 'Estadio III' 
		         when estadiamentoClinico.tipoEstadio=4 then 'Estadio IV' 
		         when ISNULL(estadiamentoClinico.tipoEstadio) then 'N/A' 
		         end as tipo_estadio, 
		         @motivoIndice := 1 + LENGTH(estadiamentoClinico.motivoEstadio) - LENGTH(REPLACE(estadiamentoClinico.motivoEstadio, ',', '')) AS motivoIndice, 
		         IF(ISNULL(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 1)), 'N/A', SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 1)) AS motivoEstadio1, 
		         IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 2), ',', -1), '') = '', 'N/A', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(estadiamentoClinico.motivoEstadio, ',', 2), ',', -1), '')) AS motivoEstadio2,
		         DATE_FORMAT(DATE(finalCV.data_ultimo_resultado_cv), '%d-%m-%Y')  as data_ultimo_resultado_cv,
		         finalCV.resultado_cv resultado_cv,
		         DATE_FORMAT(DATE(finalCV.data_ultimo_resultado_cv_anterior), '%d-%m-%Y')  as data_ultimo_resultado_cv_anterior,
		         finalCV.resultado_cv_anterior resultado_cv_anterior
            from
             (

              SELECT C1.*, 1 criteria FROM  
              (
              SELECT tx_new. patient_id,tx_new.art_start_date FROM 
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
             select final.patient_id from  ( 
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
             SELECT tr.patient_id from  
             (
              SELECT p.patient_id, MIN(obsData.value_datetime) from patient p  
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

            WHERE ((art_start_date BETWEEN :startDate AND :endDate) and (trInProgram.patient_id is null or trInMasterCard.patient_id is null)) and (cd4.patient_id is null)
            )C1
            union 
            
        select C2.patient_id,C2.data_estado,C2.criteria from 
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
               union


          select C3.patient_id,C3.data_resultado, 3 criteria from 
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

                union
                  select C4.patient_id,min(C4.data_estadio),4 criteria
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

                  union

                  select C5.patient_id,C5.data_cd4, 5 criteria from
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

                   union

                  select C6.patient_id,C6.data_gravida, 6 criteria
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
                     
                      )coorteFinal
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
                       )saidas on saidas.patient_id=coorteFinal.patient_id

                      left join
                      (   Select patient_id,min(data_inicio) data_inicio 
		                    from 
		                        (    
		                            Select  p.patient_id,min(e.encounter_datetime) data_inicio 
		                            from    patient p  
		                                    inner join encounter e on p.patient_id=e.patient_id  
		                                    inner join obs o on o.encounter_id=e.encounter_id 
		                            where   e.voided=0 and o.voided=0 and p.voided=0 and  
		                                    e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and  
		                                    e.encounter_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id 
		                            union 
		                            Select  p.patient_id,min(value_datetime) data_inicio 
		                            from    patient p 
		                                    inner join encounter e on p.patient_id=e.patient_id 
		                                    inner join obs o on e.encounter_id=o.encounter_id 
		                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  
		                                    o.concept_id=1190 and o.value_datetime is not null and  
		                                    o.value_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id 
		                            union 
		                            select  pg.patient_id,min(date_enrolled) data_inicio 
		                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id 
		                            where   pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location
		                            group by pg.patient_id 
		                            union 
		                              SELECT    e.patient_id, MIN(e.encounter_datetime) AS data_inicio  
		                              FROM      patient p 
		                                        inner join encounter e on p.patient_id=e.patient_id 
		                              WHERE     p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location
		                              GROUP BY  p.patient_id 
		                            union 
		                            Select  p.patient_id,min(value_datetime) data_inicio 
		                            from    patient p 
		                                    inner join encounter e on p.patient_id=e.patient_id 
		                                    inner join obs o on e.encounter_id=o.encounter_id 
		                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  
		                                    o.concept_id=23866 and o.value_datetime is not null and  
		                                    o.value_datetime<=:endDate and e.location_id=:location
		                            group by p.patient_id    
		                        ) inicio_real 
		                    group by patient_id 
		                 )inicio on inicio.patient_id=coorteFinal.patient_id 
		                 left join
		                 (
		                  SELECT tr.patient_id, tr.data_transferencia from  
				              (
				                SELECT p.patient_id, obsData.value_datetime as data_transferencia from patient p  
				                INNER JOIN encounter e ON p.patient_id=e.patient_id  
				                INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 
				                INNER JOIN obs obsData ON e.encounter_id=obsData.encounter_id AND obsData.voided=0 AND obsData.concept_id=23891 
				                WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 and obsData.value_datetime<=:endDate
				                AND e.location_id=:location 
				                GROUP BY p.patient_id 
				                union
				                select final.patient_id,final.minStateDate  as data_transferencia from  
				                ( 
				                select states.patient_id,states.patient_program_id,min(states.minStateDate) as minStateDate,states.program_id,states.state from  
				                ( 
				                SELECT p.patient_id, pg.patient_program_id, ps.start_date as minStateDate, pg.program_id, ps.state  FROM patient p   
				                inner join patient_program pg on p.patient_id=pg.patient_id  
				                inner join patient_state ps on pg.patient_program_id=ps.patient_program_id  
				                WHERE pg.voided=0 
				                and ps.start_date<=:endDate
				                and ps.voided=0 	
				                and p.voided=0 
				                and pg.program_id=2 
				                and location_id=:location  
				                )states 
				                group by states.patient_id 
				                order by states.minStateDate asc  
				                ) final 
				                inner join patient_state ps on ps.patient_program_id=final.patient_program_id  
				                where ps.start_date=final.minStateDate and ps.state=29 and ps.voided=0 
		                 )tr
		                 )transferedIn on transferedIn.patient_id=coorteFinal.patient_id
	                      inner join person p on p.person_id=coorteFinal.patient_id        
	                      left join  
	                      (   select pad1.* 
	                          from person_address pad1 
	                          inner join  
	                          ( 
	                              select person_id,min(person_address_id) id  
	                              from person_address 
	                              where voided=0 
	                              group by person_id 
	                          ) pad2 
	                          where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id 
	                      ) pad3 on pad3.person_id=coorteFinal.patient_id              
	                      left join            
	                      (   select pn1.* 
	                          from person_name pn1 
	                          inner join  
	                          ( 
	                              select person_id,min(person_name_id) id  
	                              from person_name 
	                              where voided=0 
	                              group by person_id 
	                          ) pn2 
	                          where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
	                      ) pn on pn.person_id=coorteFinal.patient_id          
	                      left join 
	                      (   select pid1.* 
	                          from patient_identifier pid1 
	                          inner join 
	                          ( 
	                              select patient_id,min(patient_identifier_id) id 
	                              from patient_identifier 
	                              where voided=0 
	                              group by patient_id 
	                          ) pid2 
	                          where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
	                      ) pid on pid.patient_id=coorteFinal.patient_id 
	
	                      left join                                                                                                                                                                                                                                                   
	                          (   
	                            select p.patient_id,e.encounter_datetime data_gravida from patient p     
	                              inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
	                                  inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
	                                  inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
	                              where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
	                              and value_coded=1065 and e.encounter_type=6                                                                                                                               
	                              and e.encounter_datetime  between :startDate and :endDate  
	                              and e.location_id=:location  
	                              and pe.gender = 'F' 
	                        ) gravida_real on gravida_real.patient_id=coorteFinal.patient_id
	
	                          left join                                                                                                                                                                                                                                                   
	                          (   
	                            select p.patient_id,e.encounter_datetime data_parto from patient p     
	                              inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
	                                  inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
	                                  inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
	                              where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332
	                              and value_coded=1065 and e.encounter_type=6                                                                                                                               
	                              and e.encounter_datetime  between :startDate and :endDate  
	                              and e.location_id=:location  
	                              and pe.gender = 'F' 
	                          ) lactante_real on lactante_real.patient_id = coorteFinal.patient_id
	                          left join 
                               ( 
                                select patient_id, max(obs_fila.value_datetime) data_proximo_lev, data_fila from ( 
                                select fila.patient_id,fila.data_fila data_fila,e.encounter_id from 
                                 ( 
                                 Select p.patient_id,max(encounter_datetime) data_fila from patient p 
                                 inner join encounter e on e.patient_id=p.patient_id 
                                 where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                 and date(e.encounter_datetime)<=:endDate 
                                  group by p.patient_id 
                                 )fila 
                                inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
                                )maxFila 
                                left join 
                                obs obs_fila on obs_fila.person_id=maxFila.patient_id 
                                and obs_fila.voided=0 
                                and obs_fila.encounter_id=maxFila.encounter_id 
                                and obs_fila.concept_id=5096 
                                and obs_fila.location_id=:location 
                                group by maxFila.patient_id 
                                ) max_fila on coorteFinal.patient_id=max_fila.patient_id
                                left join
                                (
                                 Select p.patient_id,max(encounter_datetime) data_seguimento from patient p 
                                 inner join encounter e on e.patient_id=p.patient_id 
                                 where p.voided=0 
                                 and e.voided=0 
                                 and e.encounter_type in (6,9)
                                 and e.location_id=:location 
                                 and e.encounter_datetime<=:endDate 
                                 group by p.patient_id 
                                )  maxConsulta on maxConsulta.patient_id=coorteFinal.patient_id
                                left join obs obs_seguimento on obs_seguimento.person_id=maxConsulta.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=maxConsulta.data_seguimento 
                                and obs_seguimento.concept_id=1410 
                                and obs_seguimento.location_id=:location 
                                left join
		                      (
		                        select p.patient_id,max(o.obs_datetime) data_ultimo_cd4
		                        from patient p   
		                        inner join encounter e on p.patient_id = e.patient_id   
		                        inner join obs o on o.encounter_id = e.encounter_id   
		                        where p.voided = 0 
		                        and e.voided = 0  
		                        and o.voided = 0     
		                        and  o.concept_id=23722
		                        and o.value_coded=1695
		                        and e.encounter_type=6 
		                        and e.location_id=:location 
		                        group by p.patient_id 
		                      )cd4 on cd4.patient_id=coorteFinal.patient_id
		                      left join
		                      (
		                         select finalCd4.*, o.value_numeric as resultado_peneultimo_cd4  from
			                 (
				                 select ultimoCd4.patient_id,ultimoCd4.data_resultado_cd4,ultimoCd4.valor_cd4,max(peneultimoCd4.data_resultado_peneultimo_cd4) data_resultado_peneultimo_cd4
				                 from
				                 (
				                 select cd4.patient_id,cd4.data_resultado_cd4 data_resultado_cd4,o.value_numeric valor_cd4  from 
				                 (
					              select cd4.patient_id,max(cd4.data_resultado) data_resultado_cd4
					              from (
					                  select p.patient_id,o.obs_datetime data_resultado
					                  from patient p   
					                  inner join encounter e on p.patient_id = e.patient_id   
					                  inner join obs o on o.encounter_id = e.encounter_id   
					                  where p.voided = 0 
					                  and  e.voided = 0  
					                  and  o.voided = 0
					                  and  o.concept_id=1695
					                  and  o.obs_datetime <= :endDate
					                  and  e.encounter_type in(13,6,9,53,51)
					                  and  e.location_id=:location 
					                  )cd4
					                  group by cd4.patient_id
					                  )cd4
					                  left join encounter e on e.patient_id=cd4.patient_id
					                  left join obs o on o.encounter_id=e.encounter_id 
					                  where e.encounter_type in(13,6,9,53,51) and  o.concept_id=1695 and cd4.data_resultado_cd4=o.obs_datetime and e.voided=0 and o.voided=0
				                 )ultimoCd4
				                 left join
				                 (
				 	              select cd4.patient_id,cd4.data_resultado data_resultado_peneultimo_cd4
					              from (
					                  select p.patient_id,o.obs_datetime data_resultado
					                  from patient p   
					                  inner join encounter e on p.patient_id = e.patient_id   
					                  inner join obs o on o.encounter_id = e.encounter_id   
					                  where p.voided = 0 
					                  and e.voided = 0  
					                  and o.voided = 0
					                  and  o.concept_id=1695
					                  and  o.obs_datetime <= :endDate
					                  and e.encounter_type in(13,6,9,53,51)
					                  and e.location_id=:location 
					                  )cd4
					                )peneultimoCd4 on peneultimoCd4.patient_id=ultimoCd4.patient_id
					                where peneultimoCd4.data_resultado_peneultimo_cd4<ultimoCd4.data_resultado_cd4
					                group by ultimoCd4.patient_id
					                )finalCd4
					                left join encounter e on e.patient_id=finalCd4.patient_id
					                left join obs o on o.encounter_id=e.encounter_id 
					                where e.encounter_type in(13,6,9,53,51) and  o.concept_id=1695 and finalCd4.data_resultado_peneultimo_cd4=o.obs_datetime and e.voided=0 and o.voided=0
			                      ) cd4Final on cd4Final.patient_id=coorteFinal.patient_id
			                      left join
			                      ( 
								select patient_id, encounter_datetime, group_concat(motivoEstadioClinico) motivoEstadio, tipoEstadio 
								from( 
									select estadiamentoClinico.patient_id, encounter_datetime encounter_datetime, 
										case estadiamentoClinico.value_coded 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi (SK)' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarreia Crónica' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
											when 14656 then 'Caquexia' 
											when 7180 then 'Toxoplasmose' 
											when 6990 then 'Doença pelo HIV resultando encefalopatia' 
											when 5344 then 'Herpes simples> 1 mês ou viisceral' 
											when 5340 then 'Candidíase esofágica' 
											when 1294 then 'Miningite cryptococal' 
											when 5042 then 'Tuberculose extrapulmonar' 
											when 507 then 'Sarcoma de Kaposi' 
											when 1570 then 'Cancro do colo do útero' 
											when 60 then 'Menigite, NSA' 
											when 5018 then 'Diarréia Crónica > 1 Mês' 
											when 5945 then 'Febre' 
											when 42 then 'Tuberculose Pulmonar' 
											when 3 then 'Anemia' 
											when 43 then 'Pneumonia' 
											when 126 then 'Gengivite' 
											when 6783 then 'Estomatite ulcerativa necrotizante' 
											when 5334 then 'Candidíase oral' 
										end as motivoEstadioClinico, tipoEstadio 
									from( 
								   		select estadio4.patient_id, estadio4.encounter_datetime,o.value_coded, 4 as tipoEstadio 
								   		from( 
								   			select p.patient_id,min(e.encounter_datetime) encounter_datetime 
								   			from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on o.encounter_id=e.encounter_id 
											where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
												and o.obs_datetime <= :endDate 
												group by p.patient_id 
										) estadio4 
											inner join encounter e on e.patient_id = estadio4.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio4.encounter_datetime 
										where e.voided = 0 and o.voided = 0 and o.value_coded in (14656, 7180, 6990, 5344, 5340, 1294, 5042, 507, 1570, 60) 
								
										union 
										
										select estadio3.patient_id, estadio3.encounter_datetime,o.value_coded, 3 as tipoEstadio 
										from( 
								   			select p.patient_id,min(e.encounter_datetime) encounter_datetime 
								   			from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on o.encounter_id=e.encounter_id 
											where e.encounter_type = 6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=1406 and e.location_id=:location 
												and o.obs_datetime <= :endDate 
												group by p.patient_id 
										) estadio3 
											inner join encounter e on e.patient_id = estadio3.patient_id 
											inner join obs o on o.encounter_id = e.encounter_id and o.obs_datetime = estadio3.encounter_datetime 
										where e.voided = 0 and o.voided = 0 and o.value_coded in (5018, 5945, 42, 3, 43, 60, 126, 6783, 5334) 
										)estadiamentoClinico order by estadiamentoClinico.patient_id, estadiamentoClinico.encounter_datetime 
									)estadiamentoClinico group by estadiamentoClinico.patient_id 
								)estadiamentoClinico on estadiamentoClinico.patient_id = coorteFinal.patient_id 
								left join
								(
					            select final.* 
						       from 
						        (
						        select cv.patient_id,max(cv.data_ultimo_resultado_cv) data_ultimo_resultado_cv ,cv.resultado_cv,max(cvAnterior.data_ultimo_resultado_cv_anterior) data_ultimo_resultado_cv_anterior,cvAnterior.resultado_cv_anterior
						        from 
						        (
						        select p.patient_id,o.obs_datetime  data_ultimo_resultado_cv,
						        case o.value_coded 
						              when 23814  then 'CARGA VIRAL INDETECTAVEL'
						              when 165331 then 'MENOR QUE'
						              when 1306   then 'NIVEL BAIXO DE DETECÇÃO'
						              when 1304   then 'MA QUALIDADE DA AMOSTRA'
						              when 23905  then 'MENOR QUE 10 COPIAS/ML'
						              when 23906  then 'MENOR QUE 20 COPIAS/ML'
						              when 23907  then 'MENOR QUE 40 COPIAS/ML'
						              when 23908  then 'MENOR QUE 400 COPIAS/ML'
						              when 23904  then 'MENOR QUE 839 COPIAS/ML'
						              when 165331 then CONCAT('MENOR QUE', ' ',o.comments)
						              else null end as resultado_cv
						              from patient p   
						              inner join encounter e on p.patient_id = e.patient_id   
						              inner join obs o on o.encounter_id = e.encounter_id   
						              where p.voided = 0 
						              and e.voided = 0  
						              and o.voided = 0     
						              and  o.concept_id=1305
						              and e.encounter_type=6 
						              and o.obs_datetime<=:endDate
						              and e.location_id=:location 
						              
						              union
						              
						              select p.patient_id,o.obs_datetime data_ultimo_resultado_cv, o.value_numeric as resultado_cv
						              from patient p   
						              inner join encounter e on p.patient_id = e.patient_id   
						              inner join obs o on o.encounter_id = e.encounter_id   
						              where p.voided = 0 
						              and e.voided = 0  
						              and o.voided = 0
						              and  o.concept_id=856
						              and e.encounter_type=6 
						              and o.obs_datetime<=:endDate
						              and e.location_id=:location 
						              )cv 
						              left join
						              (
							        select p.patient_id,o.obs_datetime  data_ultimo_resultado_cv_anterior,
							        case o.value_coded 
							              when 23814  then 'CARGA VIRAL INDETECTAVEL'
							              when 165331 then 'MENOR QUE'
							              when 1306   then 'NIVEL BAIXO DE DETECÇÃO'
							              when 1304   then 'MA QUALIDADE DA AMOSTRA'
							              when 23905  then 'MENOR QUE 10 COPIAS/ML'
							              when 23906  then 'MENOR QUE 20 COPIAS/ML'
							              when 23907  then 'MENOR QUE 40 COPIAS/ML'
							              when 23908  then 'MENOR QUE 400 COPIAS/ML'
							              when 23904  then 'MENOR QUE 839 COPIAS/ML'
							              when 165331 then CONCAT('MENOR QUE', ' ',o.comments)
							              else null end as resultado_cv_anterior
							              from patient p   
							              inner join encounter e on p.patient_id = e.patient_id   
							              inner join obs o on o.encounter_id = e.encounter_id   
							              where p.voided = 0 
							              and e.voided = 0  
							              and o.voided = 0     
							              and  o.concept_id=1305
							              and e.encounter_type in(13,6,9,53,51) 
							              and o.obs_datetime<=:endDate
							              and e.location_id=:location 
							              
							              union
							              
							              select p.patient_id,o.obs_datetime data_ultimo_resultado_cv_anterior, o.value_numeric as resultado_cv_anterior
							              from patient p   
							              inner join encounter e on p.patient_id = e.patient_id   
							              inner join obs o on o.encounter_id = e.encounter_id   
							              where p.voided = 0 
							              and e.voided = 0  
							              and o.voided = 0
							              and  o.concept_id=856
							              and e.encounter_type in(13,6,9,53,51)
							              and o.obs_datetime<=:endDate
							              and e.location_id=:location 
							              )cvAnterior on cvAnterior.patient_id=cv.patient_id
							              WHERE cvAnterior.data_ultimo_resultado_cv_anterior<cv.data_ultimo_resultado_cv
							              group by cv.patient_id
							              )final
		                                  group by final.patient_id
									  )finalCV on finalCV.patient_id=coorteFinal.patient_id
									  where saidas.patient_id is null
				                           group by coorteFinal.patient_id
				                           order by coorteFinal.patient_id,coorteFinal.criteria