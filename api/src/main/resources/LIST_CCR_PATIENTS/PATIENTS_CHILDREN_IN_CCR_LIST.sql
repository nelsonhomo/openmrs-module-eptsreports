       select distinct ccr.patient_id,
                       pid.identifier as NID,
             concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
             p.gender as GENDER, 
             floor(datediff('2023-12-20',birthdate)/365) AGE,
             pad3.address6 as 'localidade',
             pad3.address5 as 'bairro',
             pad3.address1 as 'pontoReferencia', 
             pat.value as CONTACTO,
             ccr.data_inicio,
             @motivoIndice := 1 + LENGTH(motivoDaConsulta.resposta) - LENGTH(REPLACE(motivoDaConsulta.resposta, ',', '')) AS motivoIndice, 
      IF(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 1)='N' OR ISNULL(motivoDaConsulta.resposta), 'N', SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 1)) AS motivoEstadio1, 
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 2), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 2), ',', -1), '')) AS motivoEstadio2,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 3), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 3), ',', -1), '')) AS motivoEstadio3,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 4), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 4), ',', -1), '')) AS motivoEstadio4,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 5), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 5), ',', -1), '')) AS motivoEstadio5,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 6), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 6), ',', -1), '')) AS motivoEstadio6,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 7), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 7), ',', -1), '')) AS motivoEstadio7,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 8), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 8), ',', -1), '')) AS motivoEstadio8,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 9), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 9), ',', -1), '')) AS motivoEstadio9,
      IF(IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 10), ',', -1), '') = 'N' OR ISNULL(motivoDaConsulta.resposta), 'N', IF(@motivoIndice > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(motivoDaConsulta.resposta, ',', 10), ',', -1), '')) AS motivoEstadio10,
      ptvCode.comments as CodigoPTV,
      nomeDaMae.value_text as nidDaMaeFichaCCR,
      nomeMaeRelationSHip.nomeDaMaeSesp,
      nomeMaeRelationSHip.identifier as nid_da_mae,
      IF(ISNULL(aceitaVisita.value_coded) OR aceitaVisita.value_coded = 1066, 'N', 'S') AS aceitaVisitaDomiciliar,
      firstSeguimento.data_seguimento as primeiraConsultaCCR,
      maxCCRSeguimento.data_seguimento as ultimaConsultaCCR,
      maxCCRSeguimento.value_datetime as dataProximaConsultaCCR,
      pcrDateAndResult.encounter_datetime pcrResultDate,
            case 
     when pcrDateAndResult.value_coded= 703 then 'Positivo' 
     when pcrDateAndResult.value_coded = 664 then 'Negativo' 
     when pcrDateAndResult.value_coded = 1138 then 'Indeterminado' 
     end as resultadoUltimoPCR,
               case 
     when tipoAmostraPCR.value_coded= 1002 then 'Plasma' 
     when tipoAmostraPCR.value_coded = 23831 then 'Amostra de sangue seco' 
     when tipoAmostraPCR.value_coded = 165501 then 'Amostra de mancha de plasma seco'
     when tipoAmostraPCR.value_coded = 1000 then 'Sangue total' 
     end as tipoDeAmostraPCR,
             case 
     when penultimoPCR.value_coded= 703 then 'Positivo' 
     when penultimoPCR.value_coded = 664 then 'Negativo' 
     when penultimoPCR.value_coded = 1138 then 'Indeterminado' 
     end as resultadoPenultimoPCR,
     penultimoPCR.penultimoDatePCR,
             case 
     when penultimoTipoDeAmostra.value_coded= 1002 then 'Plasma' 
     when penultimoTipoDeAmostra.value_coded = 23831 then 'Amostra de sangue seco' 
     when penultimoTipoDeAmostra.value_coded = 165501 then 'Amostra de mancha de plasma seco'
     when penultimoTipoDeAmostra.value_coded = 1000 then 'Sangue total' 
     end as penultimoTipoDeAmostraPCR,
            case 
     when trHIVUltimoResultado.value_coded= 703 then 'Positivo' 
     when trHIVUltimoResultado.value_coded = 664 then 'Negativo' 
     when trHIVUltimoResultado.value_coded = 1138 then 'Indeterminado' 
     end as trHIVUltimoResultadoResultado,
     trHIVUltimoResultado.encounter_datetime as dataTRHiv,
                   case
    when programa.state = 11 then 'ACTIVO NO PROGRAMA' 
    when programa.state = 13 then 'CONSULTA MÉDICA' 
    when programa.state = 14 OR programa.state = 1707 then 'ABANDONO' 
    when programa.state = 15 OR programa.state = 1366 then 'OBITOU' 
    when programa.state = 23 then 'CURADO'
    when programa.state = 31 then 'TRANSFERIDO DE'
    when programa.state = 32 OR programa.state = 1706 then 'TRANSFERIDO PARA'
    when programa.state = 165484 then 'TRANSFERIDO PARA CONSULTAS INTEGRADAS'
    when programa.state = 165485 then 'TRANSFERIDO PARA CONSULTA DE CRIANÇA SADIA'
    when programa.state = 165483 then 'TRANSFERIDO PARA SECTOR DE TB'
    end AS state,
     case
    when fichaResumo.state = 1707 then 'ABANDONO' 
    when fichaResumo.state = 1366 then 'OBITOU' 
    when fichaResumo.state = 1706 then 'TRANSFERIDO PARA'
    when fichaResumo.state = 165484 then 'TRANSFERIDO PARA CONSULTAS INTEGRADAS'
    when fichaResumo.state = 165485 then 'TRANSFERIDO PARA CONSULTA DE CRIANÇA SADIA'
    when fichaResumo.state = 165483 then 'TRANSFERIDO PARA SECTOR DE TB'
    end AS altaFichaResumo,
         case
    when fichaSeguimento.state = 1707 then 'ABANDONO' 
    when fichaSeguimento.state = 1366 then 'OBITOU' 
    when fichaSeguimento.state = 1706 then 'TRANSFERIDO PARA'
    when fichaSeguimento.state = 165484 then 'TRANSFERIDO PARA CONSULTAS INTEGRADAS'
    when fichaSeguimento.state = 165485 then 'TRANSFERIDO PARA CONSULTA DE CRIANÇA SADIA'
    when fichaSeguimento.state = 165483 then 'TRANSFERIDO PARA SECTOR DE TB'
    end AS altaFichaSeguimento 
          from (
        select patient_id, min(data_inicio) data_inicio from (
         select  pg.patient_id,min(date_enrolled) data_inicio                                
         from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
         where   pg.voided=0 and p.voided=0 and program_id=6 and date_enrolled between '2022-12-21' and '2023-12-20' and location_id= 277       
         group by pg.patient_id  
        union
        Select  p.patient_id,min(e.encounter_datetime) data_inicio                                
          from  patient p                                                 
              inner join encounter e on p.patient_id=e.patient_id                                                   
          where   p.voided=0 and e.voided=0 and e.encounter_type = 92 and                                      
              e.encounter_datetime between '2020-12-21' and '2023-12-20' and e.location_id= 277                              
          group by p.patient_id 
          ) ccr group by ccr.patient_id
          ) ccr
            inner join person p on p.person_id=ccr.patient_id          
            left join   ( 
            select pad1.*  from person_address pad1  
            inner join   (  
            select person_id,max(person_address_id) id   from person_address  
            where voided=0  
            group by person_id  
            ) pad2  
            where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
            ) pad3 on pad3.person_id=ccr.patient_id 
            left join( 
            select pn1.*  from person_name pn1  
            inner join (  
            select person_id,min(person_name_id) id   from person_name  
            where voided=0  
            group by person_id  
            ) pn2  
            where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
            ) pn on pn.person_id=ccr.patient_id 
            left join  ( 
            select pid1.*  from patient_identifier pid1  
            inner join  (  
            select patient_id,min(patient_identifier_id) id  from patient_identifier  
            where voided=0  
            group by patient_id  
            ) pid2 
            where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
            ) pid on pid.patient_id=ccr.patient_id
            left join person_attribute pat on pat.person_id=ccr.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
            left join (
                select patient_id, group_concat(case when value_coded is null then 'N' else 'S' end) resposta from (  
                select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 1 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1842 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id
                   
                   union
                   
                    select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 2 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 6397 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id
                   
            union
            
                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 3 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 5050 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id
                   
                    union
                    
                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 4 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1844 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union

                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 5 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1586 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union
                   
                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 6 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1847 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union

                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 7 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1845 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union

                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 8 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1846 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union
                   
                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 9 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1843 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id

                   union

                                 select  distinct motivoDaConsulta.patient_id, motivo.value_coded, 10 as motivo from (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   )motivoDaConsulta left join (
                 select  p.patient_id, value_coded                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 6409 and   
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
                   ) motivo on motivo.patient_id = motivoDaConsulta.patient_id
                   ) motivos
            ) motivoDaConsulta on motivoDaConsulta.patient_id = ccr.patient_id
        left join (
                 select  p.patient_id, o.comments                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1874 and o.value_coded = 1586 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
             ) ptvCode on ptvCode.patient_id = ccr.patient_id
                left join (
                 select  p.patient_id, o.value_text                                             
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 1477 and        
                   e.encounter_datetime<='2023-12-20' and e.location_id= 277
             ) nomeDaMae on nomeDaMae.patient_id = ccr.patient_id
            left join (
            select  person_a,person_b, nomeDaMaeSesp,pidMae.identifier from (
            SELECT person_a, person_b, concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as nomeDaMaeSesp FROM relationship r
            inner join person p on p.person_id = r.person_b
            inner join person_name pn on pn.person_id = p.person_id
            where relationship = 14 
            ) nomeDaMaeSesp             
            left join  ( 
                select pid1.*  from patient_identifier pid1  
                inner join  (  
                select patient_id,min(patient_identifier_id) id  from patient_identifier  
                where voided=0  
                group by patient_id  
                ) pid2 
                where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
            ) pidMae on pidMae.patient_id=nomeDaMaeSesp.person_b
            ) nomeMaeRelationSHip on nomeMaeRelationSHip.person_a = ccr.patient_id
            left join (
                             select  p.patient_id, e.encounter_datetime,o.value_coded                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                     
                   inner join obs  o on e.encounter_id=o.encounter_id                                      
               where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 92 and o.concept_id = 2071 and o.value_coded in (1065,1066) and   
                   e.encounter_datetime <=  '2023-12-20' and e.location_id= 277
            ) aceitaVisita on aceitaVisita.patient_id = ccr.patient_id
            left join (
                select ccr.patient_id, min(fichaSeguimento.encounter_datetime) data_seguimento from
                (select patient_id, min(data_inicio) data_inicio from (
         select  pg.patient_id,min(date_enrolled) data_inicio                                
         from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
         where   pg.voided=0 and p.voided=0 and program_id=6 and date_enrolled between '2022-12-21' and '2023-12-20' and location_id= 277       
         group by pg.patient_id  
        union
        Select  p.patient_id,min(e.encounter_datetime) data_inicio                                
          from  patient p                                                 
              inner join encounter e on p.patient_id=e.patient_id                                                   
          where   p.voided=0 and e.voided=0 and e.encounter_type = 92 and                                      
              e.encounter_datetime between '2020-12-21' and '2023-12-20' and e.location_id= 277                              
          group by p.patient_id 
          ) ccr group by ccr.patient_id
          ) ccr left join (
                select  p.patient_id, e.encounter_datetime                                            
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id                                                                       
               where   e.voided=0 and p.voided=0 and                                        
                   e.encounter_type = 93 and e.location_id= 277
            ) fichaSeguimento on fichaSeguimento.patient_id = ccr.patient_id
            where fichaSeguimento.encounter_datetime between ccr.data_inicio and '2023-12-20'
            group by ccr.patient_id
           ) firstSeguimento on firstSeguimento.patient_id = ccr.patient_id
           left join (
                select ccr.patient_id, max(fichaSeguimento.encounter_datetime) data_seguimento, fichaSeguimento.value_datetime from
                (select patient_id, min(data_inicio) data_inicio from (
         select  pg.patient_id,min(date_enrolled) data_inicio                                
         from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
         where   pg.voided=0 and p.voided=0 and program_id=6 and date_enrolled between '2022-12-21' and '2023-12-20' and location_id= 277       
         group by pg.patient_id  
        union
        Select  p.patient_id,min(e.encounter_datetime) data_inicio                                
          from  patient p                                                 
              inner join encounter e on p.patient_id=e.patient_id                                                   
          where   p.voided=0 and e.voided=0 and e.encounter_type = 92 and                                      
              e.encounter_datetime between '2020-12-21' and '2023-12-20' and e.location_id= 277                              
          group by p.patient_id 
          ) ccr group by ccr.patient_id
          ) ccr left join (
                select  p.patient_id, e.encounter_datetime, o.value_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1410                                      
                  and e.encounter_type = 93 and e.location_id= 277
            ) fichaSeguimento on fichaSeguimento.patient_id = ccr.patient_id
            where fichaSeguimento.encounter_datetime between ccr.data_inicio and '2023-12-20'
            group by ccr.patient_id
           )maxCCRSeguimento on maxCCRSeguimento.patient_id = ccr.patient_id
            left join (
                select pcr.patient_id, pcr.encounter_datetime, o.value_coded from 
                (select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1030                                      
                  and e.encounter_type in (93,13) and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                  ) pcr 
                  inner join encounter e on e.patient_id = pcr.patient_id
                  inner join obs o on o.encounter_id = e.encounter_id
                  where o.concept_id = 1030 and o.value_coded in (703,664,1138)
                  and e.encounter_datetime = pcr.encounter_datetime
            )pcrDateAndResult on pcrDateAndResult.patient_id = ccr.patient_id
            left join (
                select pcr.patient_id, pcr.encounter_datetime, o.value_coded from 
                (select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 23832                                      
                  and e.encounter_type = 13 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                  ) pcr 
                  inner join encounter e on e.patient_id = pcr.patient_id
                  inner join obs o on o.encounter_id = e.encounter_id
                  where o.concept_id = 23832 and o.value_coded in (1002,23831,165501,1000)
                  and e.encounter_datetime = pcr.encounter_datetime
            )tipoAmostraPCR on tipoAmostraPCR.patient_id = ccr.patient_id
            left join (
                select ultimoPCR.patient_id, max(penultimoPCR.encounter_datetime) penultimoDatePCR, penultimoPCR.value_coded from (
                    select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1030                                      
                  and e.encounter_type in (93,13) and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                ) ultimoPCR
                left join(
                select  p.patient_id, e.encounter_datetime, o.value_coded                                       
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1030                                      
                  and e.encounter_type in (93,13) and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                ) penultimoPCR on penultimoPCR.patient_id = ultimoPCR.patient_id
                where penultimoPCR.encounter_datetime < ultimoPCR.encounter_datetime
            )penultimoPCR on penultimoPCR.patient_id = ccr.patient_id
            left join(
                select ultimoTipoDeAmostra.patient_id, max(penultimoTipoDeAmostra.encounter_datetime) encounter_datetime, penultimoTipoDeAmostra.value_coded from (
                    select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 23832                                      
                  and e.encounter_type = 13 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                ) ultimoTipoDeAmostra left join (
                            select  p.patient_id, encounter_datetime, o.value_coded                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 23832                                      
                  and e.encounter_type = 13 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                ) penultimoTipoDeAmostra on penultimoTipoDeAmostra.patient_id = ultimoTipoDeAmostra.patient_id
                where penultimoTipoDeAmostra.encounter_datetime < ultimoTipoDeAmostra.encounter_datetime
                group by penultimoTipoDeAmostra.patient_id
            ) penultimoTipoDeAmostra on penultimoTipoDeAmostra.patient_id = ccr.patient_id
            left join (
            select pcr.patient_id, pcr.encounter_datetime, o.value_coded from 
                (select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1040                                      
                  and e.encounter_type = 93 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                  ) pcr 
                  inner join encounter e on e.patient_id = pcr.patient_id
                  inner join obs o on o.encounter_id = e.encounter_id
                  where o.concept_id = 1040 and o.value_coded in (703,664,1138)
                  and e.encounter_datetime = pcr.encounter_datetime
            ) trHIVUltimoResultado on trHIVUltimoResultado.patient_id = ccr.patient_id
            left join (
                    select pg.patient_id,max(ps.start_date) data_estado, ps.state
                                        from patient p 
                                        inner join patient_program pg on p.patient_id=pg.patient_id 
                                        inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                                        where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=6 and ps.start_date<='2023-12-20' and pg.location_id=277 
                                        group by  pg.patient_id
            )programa on programa.patient_id = ccr.patient_id
            left join(
                    select fichaResumo.patient_id, o.value_coded state from (
                    select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1873                                      
                  and e.encounter_type = 93 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                  ) fichaResumo 
                  inner join encounter e on e.patient_id = fichaResumo.patient_id
                  inner join obs o on o.encounter_id = e.encounter_id
                  where o.voided = 0 and o.concept_id = 1873 and o.value_coded in (165484,1706,165485,165483,1707,1366)
                  and e.encounter_datetime = fichaResumo.encounter_datetime
            )fichaResumo on fichaResumo.patient_id = ccr.patient_id
            left join (
			  select fichaSeguimento.patient_id, o.value_coded state from (
                    select  p.patient_id, max(e.encounter_datetime) encounter_datetime                                           
               from  patient p                                                         
                   inner join encounter e on p.patient_id=e.patient_id
                   inner join obs o on o.encounter_id = e.encounter_id
               where   e.voided=0 and p.voided=0 and o.voided = 0 and o.concept_id = 1873                                      
                  and e.encounter_type = 92 and e.location_id= 277 and e.encounter_datetime <= '2023-12-20'
                  group by p.patient_id
                  ) fichaSeguimento 
                  inner join encounter e on e.patient_id = fichaSeguimento.patient_id
                  inner join obs o on o.encounter_id = e.encounter_id
                  where o.voided = 0 and o.concept_id = 1873 and o.value_coded in (165484,1706,165485,165483,1707,1366)
                  and e.encounter_datetime = fichaSeguimento.encounter_datetime
            )fichaSeguimento on fichaSeguimento.patient_id = ccr.patient_id