select 	inicio3hp_TPT_INI_FR4.patient_id
	--	inicio3hp_TPT_INI_FR4.data_inicio_3HP,
	--	final3hp.data_final_3HP ResumoOrClinica,
	--	final3hpFiltTrimestral.data_final_3HP FiltTrimestral,
	--	final3hpDTFichaClinica.data_final_3hp ClinicaDTINH
from 
	(
		select patient_id,min(data_inicio_3HP) data_inicio_3HP
		from 
		(
			/*
				Patients who have Última profilaxia TPT with value “3HP” and Data Inicio da Profilaxia TPT registered in 
				Ficha Resumo - Mastercard (3HP Start Date) during the reporting period
				*/
				select 	p.patient_id,min(obs3hpStart.value_datetime) data_inicio_3HP  																		
				from 	patient p  																													
						inner join encounter e on p.patient_id=e.patient_id  																			
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
				where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
						obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
						e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			
				group by p.patient_id 

				union 
			
				/*
					Patients who have Profilaxia TPT with the value “3HP” and Estado da Profilaxia with the value “Inicio (I)” marked on 
					Ficha Clínica – Mastercard (3HP Start Date) during the reporting period 
				*/
				select 	p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
						and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256 							
				group by p.patient_id
				
				union 
				
				/*
					Patients who have Outras Prescrições with the value “3HP” marked on Ficha Clínica - Mastercard during 
					the reporting period (3HP Start Date) and no other 3HP prescriptions (Outras Prescrições = “3HP”) marked 
					on Ficha Clínica in the 4 months prior to this consultation date and no other pick-ups with Regime de 
					TPT = 3HP or 3HP + Piridoxina marked on FILT in the 4 months prior to the 3HP Start Date 
				*/
			
				select inicio.patient_id,inicio.data_inicio_3HP  																							
				from   																																		
					(	select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
						from 	patient p  																													
								inner join encounter e on p.patient_id=e.patient_id  																			
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
								o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  
								e.location_id=:location 			
						group by p.patient_id  																										
					)  																																	
					inicio   																																
					left join  																															
					(  																																	
						select 	p.patient_id,e.encounter_datetime data_inicio_3HP  																		
						from 	patient p  																												
								inner join encounter e on p.patient_id=e.patient_id  																		
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  						
								and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) 
								and  e.location_id=:location
								
					   union 
					   
						select 	p.patient_id, e.encounter_datetime data_inicio_3HP																		
						from	patient p																												 	
								inner join encounter e on p.patient_id=e.patient_id																			 	
								inner join obs o on o.encounter_id=e.encounter_id 																				
						where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
								and	e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and e.location_id= :location		 	 			
					)  																																		
				   inicioAnterior on inicio.patient_id=inicioAnterior.patient_id  																			
					and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null 
				
				/*
					Patients who have Outras Prescrições with the value “DT-3HP” marked on 
					Ficha Clínica - Mastercard during the reporting period (3HP Start Date)
				*/	
				
				union
				
				select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p  																												
						inner join encounter e on p.patient_id=e.patient_id  																		
						inner join obs o on o.encounter_id=e.encounter_id  																				
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  						
						and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6,9) 
						and  e.location_id=:location 
				group by p.patient_id
				
				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and 
					“Seguimento de tratamento TPT” = (‘Inicio’ or ‘Re-Inicio’) marked on Ficha de Levantamento de TPT (FILT) 
					during the reporting period
				*/
				union 
					
				select p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs o on o.encounter_id=e.encounter_id		 																				
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
				group by p.patient_id
				
				union 

				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and “Seguimento de Tratamento TPT” 
					with values “Continua” or “Fim” or no value marked on the first pick-up date on Ficha de Levantamento de TPT 
					(FILT) during the reporting period (FILT 3HP Start Date) and:
						•	No other Regime de TPT with the values “3HP or 3HP + Piridoxina” marked on FILT in the 
							4 months prior to this FILT 3HP Start Date and
						•	No other 3HP Start Dates marked on Ficha Clinica ((Profilaxia TPT with the value “3HP” and 
							Estado da Profilaxia with the value “Inicio (I)”) or (Outras Prescrições with the value “3HP”/“DT-3HP”)) 
							in the 4 months prior to this FILT 3HP Start Date and 
						•	No other 3HP Start Dates marked on Ficha Resumo (Última profilaxia TPT with value “3HP” and 
							Data Inicio da Profilaxia TPT) in the 4 months prior to this FILT 3HP Start Date:

				
				*/		
					
				 select inicio.patient_id,inicio.data_inicio_3HP  																							
				 from 																																		
					(	
					
						Select firstFilt.patient_id,firstFilt.dataFirstFilt data_inicio_3HP
						from 
						(	select 	p.patient_id,min(e.encounter_datetime) dataFirstFilt  																	
							from 	patient p  																												
									inner join encounter e on p.patient_id=e.patient_id	 												
							where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  										
									and e.encounter_type=60 and  e.location_id=:location   					
							group by p.patient_id
						) firstFilt
						inner join encounter e on e.patient_id=firstFilt.patient_id
						inner join obs obsTPT on obsTPT.encounter_id=e.encounter_id
						left join obs seguimentoTPT on (seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided=0 and 
															seguimentoTPT.concept_id=23987)
						where 	firstFilt.dataFirstFilt=e.encounter_datetime and 
								e.encounter_type=60 and obsTPT.voided=0 and obsTPT.concept_id=23985 and obsTPT.value_coded in (23954,23984) and 
								e.location_id=:location and (seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)																													
					) inicio   																																	
				left join   																															
				(  																																		
					select p.patient_id,e.encounter_datetime data_inicio_3HP 
					from 	patient p  															
							inner join encounter e on p.patient_id=e.patient_id  																				
							inner join obs o on o.encounter_id=e.encounter_id  																				
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) 	
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location 	
					
					union
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
					from	patient p																												 		
							inner join encounter e on p.patient_id=e.patient_id																			 		
							inner join obs o on o.encounter_id=e.encounter_id																			 		
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded in (23954,165307) 		 		
							and e.encounter_datetime  between (:startDate - interval 10 month) and (:endDate - interval 6 month)  and e.location_id=:location 

					union 
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP  																		
					from 	patient p														 			  															
							inner join encounter e on p.patient_id=e.patient_id																				 		
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month)	 			  									
							and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
							and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256
					union			
					
					-- Change concept 
					select 	p.patient_id,obs3hpStart.value_datetime data_inicio_3HP  																		
					from 	patient p  																													
							inner join encounter e on p.patient_id=e.patient_id  																			
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
					where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) and  										
							obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
							e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			 
				)  																																			
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id 																				
					and	inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day)  	
				where inicioAnterior.patient_id is null	
		) start3hp
		group by start3hp.patient_id 
	) inicio3hp_TPT_INI_FR4
	left join 
	(
			select patient_id, max(data_final_3HP) data_final_3HP
			from 
			(
				/*
					Patients who have Última profilaxia TPT with value “3HP” and Data de Fim da Profilaxia TPT 
					registered in Ficha Resumo - Mastercard and at least 86  days apart from the 3HP Start Date
				*/
				select 	p.patient_id,max(obs3hpStart.value_datetime) data_final_3HP  																		
				from 	patient p  																													
						inner join encounter e on p.patient_id=e.patient_id  																			
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
				where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime BETWEEN (:startDate - interval 6 month) and :endDate and  										
						obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
						e.location_id=:location and obs3hpStart.concept_id=6129 and obs3hpStart.voided=0			
				group by p.patient_id
				
				union
					
				/*
					Patients who have Profilaxia TPT with the value “3HP” and Estado da Profilaxia with the value “Fim (F)” 
					marked on Ficha Clínica – Mastercard and at least 86 days apart from the 3HP Start Date 
				*/
				select 	p.patient_id, max(e.encounter_datetime) data_final_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime BETWEEN (:startDate - interval 6 month) and :endDate 			  									
						and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
						and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1267 							
				group by p.patient_id
			) final 
			group by final.patient_id				
	) final3hp on final3hp.patient_id=inicio3hp_TPT_INI_FR4.patient_id and final3hp.data_final_3hp>=inicio3hp_TPT_INI_FR4.data_inicio_3HP + interval 86 day 
	left join 
	(
		/*
			At least 1 FILT with 3HP Trimestral (Regime de TPT= 3HP/”3HP + Piridoxina” and Tipo de Dispensa = Trimestral) 
		*/
		select p.patient_id, max(e.encounter_datetime) data_final_3HP  																		
		from 	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id																				 		
				inner join obs o on o.encounter_id=e.encounter_id		 																				
				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
		where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate	 			  									
				and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 
				and  e.location_id=:location	  		
				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23986 and seguimentoTPT.value_coded=23720 							
		group by p.patient_id
	
	) final3hpFiltTrimestral on final3hpFiltTrimestral.patient_id=inicio3hp_TPT_INI_FR4.patient_id and 
							final3hpFiltTrimestral.data_final_3HP>=inicio3hp_TPT_INI_FR4.data_inicio_3HP
	left join 
	(
		/*
			At least 1 consultation registered on Ficha Clínica – Mastercard with DT-3HP 
			(Outras Prescrições=“DT-3HP”) until a 4-month period from the 3HP Start Date (including the 3HP Start Date) 
		*/
		select 	p.patient_id,e.encounter_datetime data_final_3HP  																		
		from 	patient p  																												
				inner join encounter e on p.patient_id=e.patient_id  																		
				inner join obs o on o.encounter_id=e.encounter_id  																				
		where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate  						
				and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6,9) 
				and  e.location_id=:location 
	) final3hpDTFichaClinica on final3hpDTFichaClinica.patient_id=inicio3hp_TPT_INI_FR4.patient_id and 
				final3hpDTFichaClinica.data_final_3HP BETWEEN inicio3hp_TPT_INI_FR4.data_inicio_3HP and (inicio3hp_TPT_INI_FR4.data_inicio_3HP + interval 4 month)
where final3hp.patient_id is not null or final3hpFiltTrimestral.patient_id is not null or final3hpDTFichaClinica.patient_id is not null
	
UNION 

select inicio3hp_TPT_INI_FR4.patient_id
	--	inicio3hp_TPT_INI_FR4.data_inicio_3HP,
	--	count(distinct e.encounter_datetime) filts
from 
(
		select patient_id,min(data_inicio_3HP) data_inicio_3HP
		from 
		(
			/*
				Patients who have Última profilaxia TPT with value “3HP” and Data Inicio da Profilaxia TPT registered in 
				Ficha Resumo - Mastercard (3HP Start Date) during the reporting period
				*/
				select 	p.patient_id,min(obs3hpStart.value_datetime) data_inicio_3HP  																		
				from 	patient p  																													
						inner join encounter e on p.patient_id=e.patient_id  																			
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
				where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
						obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
						e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			
				group by p.patient_id 

				union 
			
				/*
					Patients who have Profilaxia TPT with the value “3HP” and Estado da Profilaxia with the value “Inicio (I)” marked on 
					Ficha Clínica – Mastercard (3HP Start Date) during the reporting period 
				*/
				select 	p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
						and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256 							
				group by p.patient_id
				
				union 
				
				/*
					Patients who have Outras Prescrições with the value “3HP” marked on Ficha Clínica - Mastercard during 
					the reporting period (3HP Start Date) and no other 3HP prescriptions (Outras Prescrições = “3HP”) marked 
					on Ficha Clínica in the 4 months prior to this consultation date and no other pick-ups with Regime de 
					TPT = 3HP or 3HP + Piridoxina marked on FILT in the 4 months prior to the 3HP Start Date 
				*/
			
				select inicio.patient_id,inicio.data_inicio_3HP  																							
				from   																																		
					(	select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
						from 	patient p  																													
								inner join encounter e on p.patient_id=e.patient_id  																			
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
								o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  
								e.location_id=:location 			
						group by p.patient_id  																										
					)  																																	
					inicio   																																
					left join  																															
					(  																																	
						select 	p.patient_id,e.encounter_datetime data_inicio_3HP  																		
						from 	patient p  																												
								inner join encounter e on p.patient_id=e.patient_id  																		
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  						
								and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) 
								and  e.location_id=:location
								
					   union 
					   
						select 	p.patient_id, e.encounter_datetime data_inicio_3HP																		
						from	patient p																												 	
								inner join encounter e on p.patient_id=e.patient_id																			 	
								inner join obs o on o.encounter_id=e.encounter_id 																				
						where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
								and	e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and e.location_id= :location		 	 			
					)  																																		
				   inicioAnterior on inicio.patient_id=inicioAnterior.patient_id  																			
					and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null 
				
				/*
					Patients who have Outras Prescrições with the value “DT-3HP” marked on 
					Ficha Clínica - Mastercard during the reporting period (3HP Start Date)
				*/	
				
				union
				
				select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p  																												
						inner join encounter e on p.patient_id=e.patient_id  																		
						inner join obs o on o.encounter_id=e.encounter_id  																				
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  						
						and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6,9) 
						and  e.location_id=:location 
				group by p.patient_id
				
				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and 
					“Seguimento de tratamento TPT” = (‘Inicio’ or ‘Re-Inicio’) marked on Ficha de Levantamento de TPT (FILT) 
					during the reporting period
				*/
				union 
					
				select p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs o on o.encounter_id=e.encounter_id		 																				
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
				group by p.patient_id
				
				union 

				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and “Seguimento de Tratamento TPT” 
					with values “Continua” or “Fim” or no value marked on the first pick-up date on Ficha de Levantamento de TPT 
					(FILT) during the reporting period (FILT 3HP Start Date) and:
						•	No other Regime de TPT with the values “3HP or 3HP + Piridoxina” marked on FILT in the 
							4 months prior to this FILT 3HP Start Date and
						•	No other 3HP Start Dates marked on Ficha Clinica ((Profilaxia TPT with the value “3HP” and 
							Estado da Profilaxia with the value “Inicio (I)”) or (Outras Prescrições with the value “3HP”/“DT-3HP”)) 
							in the 4 months prior to this FILT 3HP Start Date and 
						•	No other 3HP Start Dates marked on Ficha Resumo (Última profilaxia TPT with value “3HP” and 
							Data Inicio da Profilaxia TPT) in the 4 months prior to this FILT 3HP Start Date:

				
				*/		
					
				 select inicio.patient_id,inicio.data_inicio_3HP  																							
				 from 																																		
					(	
					
						Select firstFilt.patient_id,firstFilt.dataFirstFilt data_inicio_3HP
						from 
						(	select 	p.patient_id,min(e.encounter_datetime) dataFirstFilt  																	
							from 	patient p  																												
									inner join encounter e on p.patient_id=e.patient_id	 												
							where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  										
									and e.encounter_type=60 and  e.location_id=:location   					
							group by p.patient_id
						) firstFilt
						inner join encounter e on e.patient_id=firstFilt.patient_id
						inner join obs obsTPT on obsTPT.encounter_id=e.encounter_id
						left join obs seguimentoTPT on (seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided=0 and 
															seguimentoTPT.concept_id=23987)
						where 	firstFilt.dataFirstFilt=e.encounter_datetime and 
								e.encounter_type=60 and obsTPT.voided=0 and obsTPT.concept_id=23985 and obsTPT.value_coded in (23954,23984) and 
								e.location_id=:location and (seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)																													
					) inicio   																																	
				left join   																															
				(  																																		
					select p.patient_id,e.encounter_datetime data_inicio_3HP 
					from 	patient p  															
							inner join encounter e on p.patient_id=e.patient_id  																				
							inner join obs o on o.encounter_id=e.encounter_id  																				
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) 	
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location 	
					
					union
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
					from	patient p																												 		
							inner join encounter e on p.patient_id=e.patient_id																			 		
							inner join obs o on o.encounter_id=e.encounter_id																			 		
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded in (23954,165307) 		 		
							and e.encounter_datetime  between (:startDate - interval 10 month) and (:endDate - interval 6 month)  and e.location_id=:location 

					union 
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP  																		
					from 	patient p														 			  															
							inner join encounter e on p.patient_id=e.patient_id																				 		
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month)	 			  									
							and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
							and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256
					union			
					
					-- Change concept 
					select 	p.patient_id,obs3hpStart.value_datetime data_inicio_3HP  																		
					from 	patient p  																													
							inner join encounter e on p.patient_id=e.patient_id  																			
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
					where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) and  										
							obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
							e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			 
				)  																																			
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id 																				
					and	inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day)  	
				where inicioAnterior.patient_id is null	
		) start3hp
		group by start3hp.patient_id 
	) inicio3hp_TPT_INI_FR4	
	
		/*
			At least 3 FILTs with 3HP Mensal (Regime de TPT= Isoniazida/”Isoniazida + Piridoxina” and
			Tipo de Dispensa = Mensal) until a 4-month period from the 3HP Start Date (including the 3HP Start Date) 
		*/
		inner join encounter e on inicio3hp_TPT_INI_FR4.patient_id=e.patient_id
		inner join obs regimeTPT on regimeTPT.encounter_id=e.encounter_id		 																				
		inner join obs tipoDispensa on tipoDispensa.encounter_id=e.encounter_id																
	where 		e.voided=0 and e.encounter_datetime between inicio3hp_TPT_INI_FR4.data_inicio_3HP and  (inicio3hp_TPT_INI_FR4.data_inicio_3HP + interval 4 month)	 			  									
				and regimeTPT.voided=0 and regimeTPT.concept_id=23985 and regimeTPT.value_coded in (23954,23984) and e.encounter_type=60 
				and  e.location_id=:location	  		
				and tipoDispensa.voided =0 and tipoDispensa.concept_id =23986 and tipoDispensa.value_coded=1098 							
	group by inicio3hp_TPT_INI_FR4.patient_id
	having count(distinct e.encounter_datetime)>=3

UNION

select inicio3hp_TPT_INI_FR4.patient_id
	--	inicio3hp_TPT_INI_FR4.data_inicio_3HP,
	--	count(distinct final3hpClinica.encounter_id) filts
from 
(
		select patient_id,min(data_inicio_3HP) data_inicio_3HP
		from 
		(
			/*
				Patients who have Última profilaxia TPT with value “3HP” and Data Inicio da Profilaxia TPT registered in 
				Ficha Resumo - Mastercard (3HP Start Date) during the reporting period
				*/
				select 	p.patient_id,min(obs3hpStart.value_datetime) data_inicio_3HP  																		
				from 	patient p  																													
						inner join encounter e on p.patient_id=e.patient_id  																			
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
				where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
						obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
						e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			
				group by p.patient_id 

				union 
			
				/*
					Patients who have Profilaxia TPT with the value “3HP” and Estado da Profilaxia with the value “Inicio (I)” marked on 
					Ficha Clínica – Mastercard (3HP Start Date) during the reporting period 
				*/
				select 	p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
						inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
						and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256 							
				group by p.patient_id
				
				union 
				
				/*
					Patients who have Outras Prescrições with the value “3HP” marked on Ficha Clínica - Mastercard during 
					the reporting period (3HP Start Date) and no other 3HP prescriptions (Outras Prescrições = “3HP”) marked 
					on Ficha Clínica in the 4 months prior to this consultation date and no other pick-ups with Regime de 
					TPT = 3HP or 3HP + Piridoxina marked on FILT in the 4 months prior to the 3HP Start Date 
				*/
			
				select inicio.patient_id,inicio.data_inicio_3HP  																							
				from   																																		
					(	select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
						from 	patient p  																													
								inner join encounter e on p.patient_id=e.patient_id  																			
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and  										
								o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  
								e.location_id=:location 			
						group by p.patient_id  																										
					)  																																	
					inicio   																																
					left join  																															
					(  																																	
						select 	p.patient_id,e.encounter_datetime data_inicio_3HP  																		
						from 	patient p  																												
								inner join encounter e on p.patient_id=e.patient_id  																		
								inner join obs o on o.encounter_id=e.encounter_id  																				
						where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  						
								and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) 
								and  e.location_id=:location
								
					   union 
					   
						select 	p.patient_id, e.encounter_datetime data_inicio_3HP																		
						from	patient p																												 	
								inner join encounter e on p.patient_id=e.patient_id																			 	
								inner join obs o on o.encounter_id=e.encounter_id 																				
						where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
								and	e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and e.location_id= :location		 	 			
					)  																																		
				   inicioAnterior on inicio.patient_id=inicioAnterior.patient_id  																			
					and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null 
				
				/*
					Patients who have Outras Prescrições with the value “DT-3HP” marked on 
					Ficha Clínica - Mastercard during the reporting period (3HP Start Date)
				*/	
				
				union
				
				select 	p.patient_id,min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p  																												
						inner join encounter e on p.patient_id=e.patient_id  																		
						inner join obs o on o.encounter_id=e.encounter_id  																				
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  						
						and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6,9) 
						and  e.location_id=:location 
				group by p.patient_id
				
				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and 
					“Seguimento de tratamento TPT” = (‘Inicio’ or ‘Re-Inicio’) marked on Ficha de Levantamento de TPT (FILT) 
					during the reporting period
				*/
				union 
					
				select p.patient_id, min(e.encounter_datetime) data_inicio_3HP  																		
				from 	patient p														 			  															
						inner join encounter e on p.patient_id=e.patient_id																				 		
						inner join obs o on o.encounter_id=e.encounter_id		 																				
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
				where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			  									
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
				group by p.patient_id
				
				union 

				/*
					Patients who have Regime de TPT with the values “3HP or 3HP + Piridoxina” and “Seguimento de Tratamento TPT” 
					with values “Continua” or “Fim” or no value marked on the first pick-up date on Ficha de Levantamento de TPT 
					(FILT) during the reporting period (FILT 3HP Start Date) and:
						•	No other Regime de TPT with the values “3HP or 3HP + Piridoxina” marked on FILT in the 
							4 months prior to this FILT 3HP Start Date and
						•	No other 3HP Start Dates marked on Ficha Clinica ((Profilaxia TPT with the value “3HP” and 
							Estado da Profilaxia with the value “Inicio (I)”) or (Outras Prescrições with the value “3HP”/“DT-3HP”)) 
							in the 4 months prior to this FILT 3HP Start Date and 
						•	No other 3HP Start Dates marked on Ficha Resumo (Última profilaxia TPT with value “3HP” and 
							Data Inicio da Profilaxia TPT) in the 4 months prior to this FILT 3HP Start Date:

				
				*/		
					
				 select inicio.patient_id,inicio.data_inicio_3HP  																							
				 from 																																		
					(	
					
						Select firstFilt.patient_id,firstFilt.dataFirstFilt data_inicio_3HP
						from 
						(	select 	p.patient_id,min(e.encounter_datetime) dataFirstFilt  																	
							from 	patient p  																												
									inner join encounter e on p.patient_id=e.patient_id	 												
							where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  										
									and e.encounter_type=60 and  e.location_id=:location   					
							group by p.patient_id
						) firstFilt
						inner join encounter e on e.patient_id=firstFilt.patient_id
						inner join obs obsTPT on obsTPT.encounter_id=e.encounter_id
						left join obs seguimentoTPT on (seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided=0 and 
															seguimentoTPT.concept_id=23987)
						where 	firstFilt.dataFirstFilt=e.encounter_datetime and 
								e.encounter_type=60 and obsTPT.voided=0 and obsTPT.concept_id=23985 and obsTPT.value_coded in (23954,23984) and 
								e.location_id=:location and (seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)																													
					) inicio   																																	
				left join   																															
				(  																																		
					select p.patient_id,e.encounter_datetime data_inicio_3HP 
					from 	patient p  															
							inner join encounter e on p.patient_id=e.patient_id  																				
							inner join obs o on o.encounter_id=e.encounter_id  																				
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) 	
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location 	
					
					union
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
					from	patient p																												 		
							inner join encounter e on p.patient_id=e.patient_id																			 		
							inner join obs o on o.encounter_id=e.encounter_id																			 		
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded in (23954,165307) 		 		
							and e.encounter_datetime  between (:startDate - interval 10 month) and (:endDate - interval 6 month)  and e.location_id=:location 

					union 
					
					select 	p.patient_id, e.encounter_datetime data_inicio_3HP  																		
					from 	patient p														 			  															
							inner join encounter e on p.patient_id=e.patient_id																				 		
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
					where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month)	 			  									
							and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
							and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded=1256
					union			
					
					-- Change concept 
					select 	p.patient_id,obs3hpStart.value_datetime data_inicio_3HP  																		
					from 	patient p  																													
							inner join encounter e on p.patient_id=e.patient_id  																			
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
							inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id 
					where 	e.voided=0 and p.voided=0 and obs3hpStart.value_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month) and  										
							obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=53 and  
							e.location_id=:location and obs3hpStart.concept_id=6128 and obs3hpStart.voided=0			 
				)  																																			
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id 																				
					and	inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day)  	
				where inicioAnterior.patient_id is null	
		) start3hp
		group by start3hp.patient_id 
	) inicio3hp_TPT_INI_FR4	
	inner join 
	(
	
		/*
			At least 3 consultations registered on Ficha Clínica – Mastercard with Outras Prescricoes 3HP (Outras Prescrições= “3HP”) 
			or  
		*/
		select 	p.patient_id,e.encounter_id,e.encounter_datetime data_final_3hp  																		
		from 	patient p  																													
				inner join encounter e on p.patient_id=e.patient_id  																			
				inner join obs o on o.encounter_id=e.encounter_id  																				
		where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate and  										
				o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  
				e.location_id=:location 			
		
		union 
		
		/*
			Profilaxia 3HP  (Profilaxia TPT=”3HP” and Estado da Profilaxia=”Inicio(I)/Continua(C)”)  until a 4-month period 
			from the 3HP Start Date (including the 3HP Start Date) 
		*/
		select 	p.patient_id, e.encounter_id,e.encounter_datetime data_final_3hp  																		
		from 	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id																				 		
				inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id		 																				
				inner join obs obs3hpStart on obs3hpStart.encounter_id=e.encounter_id																
		where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate	 			  									
				and obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type=6 and  e.location_id=:location	  		
				and obs3hpStart.voided =0 and obs3hpStart.concept_id =165308 and obs3hpStart.value_coded in (1256,1257)		
	
	) final3hpClinica on final3hpClinica.patient_id=inicio3hp_TPT_INI_FR4.patient_id
	where final3hpClinica.data_final_3hp between inicio3hp_TPT_INI_FR4.data_inicio_3HP and  (inicio3hp_TPT_INI_FR4.data_inicio_3HP + interval 4 month)
	group by inicio3hp_TPT_INI_FR4.patient_id
	having count(distinct final3hpClinica.encounter_id)>=3
		