package com.univpm.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.univpm.exception.WrongPeriodException;
import com.univpm.model.Anno;
import com.univpm.model.Classificazione;
import com.univpm.model.Country;
import com.univpm.model.Mese;
import com.univpm.model.Parametro;
import com.univpm.model.Periodo;
import com.univpm.util.APICall;
import com.univpm.util.ApiKeyScanner;
import com.univpm.util.EndPoint;
import com.univpm.util.EndPointApiKey;

/**
 * Questa classe implementa il <b>Service</b> dell'applicazione.
 * @author Gerardo
 * @author Sara
 *
 */
@Service("setupModel&response")
public class StatsImpl implements I_Stats {

	//SETTING DELL'END POINT CHE STIAMO UTILIZZANDO
	String endpoint = "https://app.ticketmaster.com/discovery/v2/events.json";
//	String endpoint = "https://app.ticketmaster.com/discovery/v2";
	
	ApiKeyScanner scanner = new ApiKeyScanner(endpoint);
	Parametro apikey = new Parametro("apikey", scanner.readApiKey());
	EndPointApiKey source = new EndPointApiKey(endpoint, apikey);
//	Parametro apikey=new Parametro ("apikey","GV7AaEfUWGgyb0QXwOhDquUh5gmpbRMV");
//	EndPointApiKey source = new EndPointApiKey("https://app.ticketmaster.com/discovery/v2",apikey);

	Country canada=new Country("CA");
	APICall chiamante=new APICall();
	ArrayList<String> list_state= canada.getDataString();	
	int numCategorie=0;
	JSONArray stati=new JSONArray();
	JSONArray categories=new JSONArray();
	String msg="pattern di chiamata compromesso";
	JSONObject err=new JSONObject();
	
	/***************************************************************************************************************************/
	/**************************************************** STATISTICHE **********************************************************/
	/***************************************************************************************************************************/
	
	/**
	 * Questo metodo permette di calcolare il numero di eventi attraverso chiamate all'End Point. 
	 * Chiamate che sono parametrizzate a seconda della scelta dell'utente.
	 * @param countryCode indica  la nazione
	 * @param stateCode indica lo stato o gli stati
	 * @param nameCat indica le classificazioni o le classificazioni
	 * @param startDate indica la data inizio del periodo
	 * @param endDate indica la dara fine del periodo
	 * @return <code>JSONObject</code>
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getNumberOfEvents(String countryCode ,String stateCode,String nameCat,String startDate,String endDate) {
		Country ca=new Country("CA");
		
		ArrayList<String> list_stati=ca.getDataString();
		JSONObject resp=new JSONObject();
		err.put("errore",msg);
		ArrayList <Parametro> list_state=new ArrayList<Parametro>();
		
		/**********tutti gli eventi per countrycode************/
		resp=withCountrycode(countryCode);
		if(resp.containsKey("messaggio")) {
			
			return resp;
		}
		/*******************************************/
		
		if(!stateCode.equals("0")) {//getstats invoca il metodo con staecode!= "0"
			String[] splitState=stateCode.split("-");//caso in cui 
			long tot=0;
			long tmp=0;
			stati.clear();//su piu chiamate devo pulire l array che inserisco nella risposta
			
			for(String s:splitState) {
				long j=validazione("stateCode", s);
				if(j  == -1) {
					return getErrori();//problemi di connessione
				}
				if(j>0) {
					
					
					tot+=j;
					Parametro p=new Parametro(s,true);
					list_state.add(p);
					JSONObject stato=new JSONObject();
					stato.put("stateCode", s);
					stato.put("numero eventi", j);
					stati.add(stato);
					resp.put("stati", stati);
					resp.put("numero eventi",tot );	
		 		}
				else {
					JSONObject stato=new JSONObject();
			 		Parametro p=new Parametro(s,false);
			 		list_state.add(p);
			 		if( list_stati.contains(s)) {
			 			stato.put("stateCode", s );//mettere il model country con letterer piccole
			 		
			 		}
			 		else {
			 			stato.put("stateCode", s +" parametro non valido");
			 		}		 		
			 		stato.put("numero eventi", j);
			 		stati.add(stato);
			 		resp.put("stati", stati);
			 		++tmp;
					}
			 		
		 		
			}
			if(tmp==splitState.length) {//stessi giri nell esle eventi complessivi=0
				resp.put("numero eventi", "0");
			}
		}
		long result=0;
		if(!nameCat.equals("0")) {
			stati.clear();
			categories.clear();
			JSONObject new_resp=new JSONObject();
			new_resp.put("contryCode", countryCode);
			
			String[] splitCat=nameCat.split("-");
			new_resp.put("categorie", categories);
			for(String c:splitCat) {
			
				JSONObject categoria=new JSONObject();
				JSONArray stati2=new JSONArray();
				JSONArray stati_f=new JSONArray();
				categoria.put("categoria", c);
				long v3=validazione("segmentName", c);
				if(v3 == -1) {
					
					return getErrori();
				}
				if(v3>0) {
				
					for(Parametro s:list_state) {
						JSONObject stato1=new JSONObject();//x ogni stato creo un oggetto
						if(s.getboolean()) {
							source.setChiaveValore("countryCode", countryCode);
							source.setChiaveValore("stateCode", s.getChiave());
							source.setChiaveValore("segmentName", c);
							if(!startDate.equals("0") && !endDate.equals("0")) {
								String start=getDate(startDate);//mi ritorna la stringa
								String end=getDate(endDate);
								
								//GESTIONE PARAMETRO NON VALIDO
								if(start != null && end != null) {
									String[] start_g = start.split("T");
									String[] end_g = end.split("T");
									String init = start_g[0];
									String fin = end_g[0];
									try {
										GregorianCalendar date_init = new GregorianCalendar(Integer.parseInt(init.split("-")[0]), Integer.parseInt(init.split("-")[1]), Integer.parseInt(init.split("-")[2]));
										GregorianCalendar date_fin = new GregorianCalendar(Integer.parseInt(fin.split("-")[0]), Integer.parseInt(fin.split("-")[1]), Integer.parseInt(fin.split("-")[2]));
										if (date_init.after(date_fin)) {
											JSONObject er = new JSONObject();
											er.put("errore", "startDate superiore a endDate");
											return er;
										}
									}
									catch(Exception e) {
										System.out.println("DENTRO CATCH");
										start = null;
										end = null;
									}
								}
								
								if(start== null || end== null) {
									System.out.println("DOPO ECCEZIONE");
									JSONObject err1 = new JSONObject();
									err1.put("Errore", "Periodo non valido");
									err1.put("Messaggio", "Pattern di chiamata compromesso");
						
									chiamante.getData(source.getApi());
									return err1;
								}
								
									String [] split_s=startDate.split("-");
								String [] split_e=endDate.split("-");
								
								result =cacolaMedia(split_s,split_e);
								
								source.setChiaveValore("startDateTime", start);
								source.setChiaveValore("endDateTime", end);
							}
							JSONObject risposta = chiamante.getData(source.getApi());
							JSONObject resp1=new JSONObject();
							resp1= (JSONObject) risposta.get("page");
							long num=(long) resp1.get("totalElements");
							stato1.put("stateCode", s.getChiave());
							stato1.put("numero eventi", num);
							
							if(result!=0 && result<= 31) {
								String t=formatMedia(num,result);
								stato1.put("media giornaliera",t );
							}
							if(result!=0 && result> 31 && result<61) {
								String t=formatMedia(num,result);
								stato1.put("media giornaliera eventi",t );
							}
							if(result!=0 && result> 60) {
								result=result/30;
								String t=formatMedia(num,result);
								stato1.put("media mensile eventi",t );
							}
						}
						else {
							if( list_stati.contains(s.getChiave())) {
				 				stato1.put("stateCode", s.getChiave() );//mettere il model country con letterer piccole
							}
							else {
				 				stato1.put("stateCode", s.getChiave()+" parametro non valido");
				 			}
					 		stato1.put("numero eventi", "0");
						}
						stati2.add(stato1);
					}
					categoria.put("stati",stati2);	
				}
				else {
					
					
						categoria.put("stati",stati_f);
						categoria.put("categoria", c+ " paramentro non valido");
						categoria.put("numero eventi", "0");
					}
				
				categories.add(categoria);		
			}
			resp=new_resp;
		}
		list_state.clear();
		return resp;	
	}
	
	/**
	 * Questo metodo permette di calcolare le statistiche a seconda dei valori inseriti dall'utente.
	 * @param countryCode indica la nazione
	 * @param stateCode indice lo stato o gli stati
	 * @param nameCat indica la classificazione o le classificazioni
	 * @return <code>JSONObject</code>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getstatsNumberOfEvents(String countryCode,String stateCode,String nameCat) {
	
		JSONObject resp=new JSONObject();
	
		
		if(countryCode.equals("ca") || countryCode.equals("CA")) {

			ArrayList<Parametro> perCalcolare=new ArrayList<Parametro>();
			JSONObject  calcola=new JSONObject();
			
				//ITERO GLI STATI
				for(String x:list_state) {//list_state codici di stecode nel model country
					if(nameCat.equals("0")) {//chiamaa senza nameCat
			
						calcola=getNumberOfEvents(countryCode,x, "0","0","0");//se sbaglio a settare model
						if(calcola.containsKey("messaggio")) {
							return calcola;
						}
				

						else {	
							long n=Long.parseLong((String.valueOf(calcola.get("numero eventi"))));
							Parametro valori =new Parametro(x,n);
							perCalcolare.add(valori);
						}
					}
					else {
						
						
						String[] splitCat=nameCat.split("-");
						numCategorie=splitCat.length;
						for(String c:splitCat) {
							calcola=getNumberOfEvents(countryCode,x, c,"0","0");
						
				 
							JSONArray categorie=new JSONArray();
							if(Objects.nonNull(calcola.get("categorie"))) {
								categorie=(JSONArray) calcola.get("categorie");
					
								for(int i =0;i<categorie.size();i++) {
									JSONObject obj=new JSONObject();
									JSONArray stati=new JSONArray();
									obj=(JSONObject) categorie.get(i);
									stati=(JSONArray) obj.get("stati");
									if(Objects.nonNull(stati) && stati.size()>0 ) {
										for(int j =0;j<stati.size();j++) {
											JSONObject obj1=new JSONObject();
											obj1=(JSONObject) stati.get(j);
											long l=Long.parseLong((String.valueOf(obj1.get("numero eventi"))));

											String chiave=(String) obj1.get("stateCode"); 
											Parametro valori = new Parametro(chiave,l);
											valori.setCat(c);
											perCalcolare.add(valori);
										}	
									}//stati
								}
							}
						}
					}
				}
		
				JSONObject respost=new JSONObject();
				String[] splitCat=nameCat.split("-");

				JSONArray a5=new JSONArray();
				if(splitCat.length>0 && !splitCat[0].equals("0")) {
					//ho piou categorie
					for(int i =0;i<splitCat.length;i++) {
									
						JSONArray a_max=new JSONArray();
						JSONArray a_min=new JSONArray();
						JSONObject obj5=new JSONObject();
						long totale=0;
						long media=0;
						long max=0;
						long min=0;
						int indexInitialMin=0;
						
						for(int v=0;v<perCalcolare.size();v++) {
							if(perCalcolare.get(v).getCategoria().equals(splitCat[i])) {//a-b
								indexInitialMin=v;//primo valore dell array dei valori rispetto a categoria
								break;
							}	
						}
						if(perCalcolare.size()>0) {
							min=perCalcolare.get(indexInitialMin).getLong();//inizializzo	
						}
						boolean trovato=false;
						for(int l=0;l<perCalcolare.size();l++) {
					
							if(perCalcolare.get(l).getCategoria().equals(splitCat[i])) {
								trovato=true;//LA CATEOGIRA HA VALORI IN PERCALCOLA
							}
						}	
						
						if(trovato) {
							for(int h=0;h<perCalcolare.size();h++) {//MASSIMO E MINIMO
								if(perCalcolare.get(h).getCategoria().equals(splitCat[i])) {
									System.out.println("max  "+ max+ "  min  "+ min);
									totale += perCalcolare.get(h).getLong();
									if(perCalcolare.get(h).getLong()>max) {
										max=perCalcolare.get(h).getLong();
									}		
									if(perCalcolare.get(h).getLong()<min) {
										min=perCalcolare.get(h).getLong();
									}
								}
							}
											
							for(int w=0;w<perCalcolare.size();w++) {	
								if(perCalcolare.get(w).getCategoria().equals(splitCat[i])) {
									if(perCalcolare.get(w).getLong()==max) {
										JSONObject r1=new JSONObject();
										r1.put("stato  ",perCalcolare.get(w).getChiave());
										r1.put("num eventi", perCalcolare.get(w).getLong());
										a_max.add(r1);
									}
									if(perCalcolare.get(w).getLong()==min) {
										JSONObject r1=new JSONObject();
										r1.put("stato  ",perCalcolare.get(w).getChiave());
										r1.put("num eventi", perCalcolare.get(w).getLong());
										a_min.add(r1);
									}
							
									media=totale/12;
												
									JSONArray a_stats=new JSONArray();
									JSONObject st_min= new JSONObject();
									JSONObject st_max= new JSONObject();
									st_max.put("statistiche stati con numero maggiore di eventi", a_max);
									a_stats.add(st_max);
									st_min.put("statistiche stati con numero minore  di eventi", a_min);
									a_stats.add(st_min);
									obj5.put("categoria", splitCat[i]);
									obj5.put("totale eventi organizzati di "+splitCat[i], totale);
									obj5.put("media eventi per stato  "+splitCat[i], media);
									obj5.put("statistiche", a_stats);
								}
							}
									
							a5.add(obj5);
						}
						else {
							JSONArray a_stats=new JSONArray();
							JSONObject st_min= new JSONObject();
							JSONObject st_max= new JSONObject();
							obj5.put("categoria", splitCat[i]+ " parametro nn valido");
							obj5.put("totale eventi organizzati di "+splitCat[i], totale);
							obj5.put("media eventi per stato  "+splitCat[i], media);
							obj5.put("statistiche", a_stats);
							a5.add(obj5);
						}
						respost.put("stats",a5);	
						resp=respost;	
					}
				
				}
				else {
					JSONArray a_stats=new JSONArray();
					JSONArray a_max=new JSONArray();
					JSONArray a_min=new JSONArray();
					JSONObject obj5=new JSONObject();
					long totale=0;
					long media=0;
					long max=0;
					long min=perCalcolare.get(0).getLong();
					for(int h=0;h<perCalcolare.size();h++) {//MASSIMO E MINIMO
			
						System.out.println("max  "+ max+ "  min  "+ min);
						totale += perCalcolare.get(h).getLong();
						if(perCalcolare.get(h).getLong()>max) {
							max=perCalcolare.get(h).getLong();
						}						
						if(perCalcolare.get(h).getLong()<min) {
							min=perCalcolare.get(h).getLong();
						}
					}
			
					for(int w=0;w<perCalcolare.size();w++) {	
						obj5.put("paese", "ca");							
						if(perCalcolare.get(w).getLong()==max) {
							JSONObject r1=new JSONObject();
							r1.put("stato  ",perCalcolare.get(w).getChiave());
							r1.put("num eventi", perCalcolare.get(w).getLong());
							a_max.add(r1);
						}
						if(perCalcolare.get(w).getLong()==min) {
							JSONObject r1=new JSONObject();
							r1.put("stato  ",perCalcolare.get(w).getChiave());
							r1.put("num eventi", perCalcolare.get(w).getLong());
							a_min.add(r1);
						}
					}
					media=totale/12;
									
					JSONObject st_min= new JSONObject();
					JSONObject st_max= new JSONObject();
					st_max.put("statistiche stati con numero maggiore di eventi", a_max);
					a_stats.add(st_max);
					st_min.put("statistiche stati con numero minore  di eventi", a_min);
					a_stats.add(st_min);
				
					obj5.put("totale eventi organizzati di ", totale);
					obj5.put("media eventi nazione ", media);
					obj5.put("statistiche", a_stats);
					
					respost.put("Stats",obj5);	
					resp = respost;
				}
			}
	
		
		else {
			JSONObject respost=new JSONObject();
			respost.put("messaggio", "statistiche disponibili solo per il Canada");
			respost.put("codice ammesso", "countryCode: ca/CA");
			resp=respost;
		}
		return resp;
	}

	/**
	 * Questo metodo permette di calcolare le <b>Statistiche Annuali</b> di un determinato stato.
	 * @param countryCode indica la nazione
	 * @param stateCode indica lo stato
	 * @param anno indica l'anno 
	 * @return <code>JSONObject</code>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getStatsAnnuali(String countryCode, String stateCode, String anno) {	
		JSONObject response = new JSONObject();
		JSONObject obj;
		JSONArray arr = new JSONArray();
		Anno year = new Anno(anno);
		HashMap<Integer,Mese> mesi = year.getMesi();
			
		long tot = 0;
		long min = 0;
		int indice_min = 0;
		long max = 0;
		int indice_max = 0;
			
		//GESTIONE VALIDAZIONE PARAMETRO ANNO 
		int anno_corrente = Calendar.getInstance().get(Calendar.YEAR);
		if(Long.parseLong(anno)<anno_corrente || Long.parseLong(anno) > 2100 ) {
			//GESTIONE ERRORE
			JSONObject err_anno = new JSONObject();
			err_anno.put("messaggio", "anno non valido");
			err_anno.put("errore", "pattern di chiamata compromesso");
			return err_anno;
		}
		
		response.put("Anno", anno);
			
		//VALIDAZIONE PARAMETRO COUNTRYCODE
		response = withCountrycode(countryCode);
		if(response.containsKey("messaggio")) {

		return response;
		}
		
		response.put("Country", countryCode);
		
		//VALIDAZIONE PARAMETRO STATECODE
		long valStateCode = this.validazione("stateCode", stateCode);
		
		//ERRORE CONNESSIONE
		if(valStateCode == -1) {
			return getErrori();
		}
		if(!list_state.contains(stateCode)) {
			JSONObject errore = new JSONObject();
			errore.put("errore", "stateCode non valido");
			return errore;
		}
		response.put("State", stateCode);
			
		Periodo p1; //RAPPRESENTA IL PERIODO DELL'ANNO
		//GESTISCO L'ECCEZIONE CHE MI GENERA LA CLASSE PERIODO
		try { p1 = new Periodo(1,1, Integer.parseInt(anno), 31,12,Integer.parseInt(anno)); } 
		catch (WrongPeriodException e) { p1 = null; } 
		
		if (p1 == null) {
			err.put("Errore", "Periodo non valido");
			err.put("Messaggio", "Pattern di chiamata compromesso");
			return err;
		}
		
		JSONArray arr_obj = new JSONArray();
		JSONArray arr_class = this.getClassificationEvents(); //HO LE CLASSIFICAZIONI
		for (int i=0; i<arr_class.size(); i++) {
			JSONObject c = (JSONObject) arr_class.get(i);
			String nome = (String) c.get("name");
			if(nome.contains("&")) nome = nome.replace("&", "%");
			JSONObject dato = setupModel(countryCode,stateCode,nome,p1);
			//GESTICO L'ECCEZIONE CHE PUO' GENERARMI IL METODO getData
			if(dato == null) {
				err.put("errore", "Connessione Fallita");
				err.put("Messaggio", "Riprovare");
				return err;
			}
			long num = this.numeroElementi(dato);
			obj = new JSONObject();
			obj.put("Nome", nome);
			obj.put("Numero Eventi", num);
			arr_obj.add(obj);
		}
		response.put("Stats Classificazioni", arr_obj);
		
		//GESTIONE STATISTICHE MENSILI OVVERO CALCOLO DEGLI EVENTI PER MESE
		for (int i=1; i<=12; i++) {
			obj = new JSONObject();
			Periodo periodo = new Periodo(mesi.get(i).getData_inizio(), mesi.get(i).getData_fine());
			JSONObject dato = setupModel(countryCode,stateCode, periodo);
			//GESTICO L'ECCEZIONE CHE PUO' GENERARMI IL METODO getData
			if(dato == null) {
				err.put("errore", "Connessione Fallita");
				err.put("Messaggio", "Riprovare");
				return err;
			}
			long num = this.numeroElementi(dato);
			tot+=num; //CONTEGGIO TOTALI EVENTI
				
			//CALCOLO STATISTICHE
			if(i==1) {
				min = num;
				indice_min = i;
				max = num;
				indice_max = i;	
			}
			else {
				if(num<min) {
					min = num;
					indice_min = i;
				}
				if(num>max) {
					max = num;
					indice_max = i;
				}
			}
				
			String nome_mese = mesi.get(i).getNome_mese();
			obj.put(nome_mese, num);
			arr.add(obj);
		}
			
		//INSERIMENTO DELLE STATISTICHE NELLA RISPOSTA
		JSONArray stats_minmaxmed = new JSONArray();
		JSONObject sing = new JSONObject();
		sing.put("Media Mensile", tot/12);
		sing.put("Mese con numero minimo di eventi", mesi.get(indice_min).getNome_mese() + "(" + min + ")");
		sing.put("Mese con numero massimo di eventi" , mesi.get(indice_max).getNome_mese() + "(" + max + ")");
		stats_minmaxmed.add(sing);
			
		response.put("MIN,MAX,MEDIA", stats_minmaxmed);
		response.put("TOT EVENTI", tot);
		response.put("Stats Mensili", arr);
		return response;
	}
	
	/***************************************************************************************************************************/
	/**************************************************** METODI UTILI *********************************************************/
	/***************************************************************************************************************************/
	
	/**
	 * Questo metodo serve a cambiare il formato decimale della media che ottengo.
	 * @param num 
	 * @param result
	 * @return media con solo una cifra dopo la virgola
	 */
	private String formatMedia (long num, long result) {
		double media= ((double)num)/result;
		String pattern = "#.#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String formattedDouble = decimalFormat.format(media);
		return formattedDouble;
	}

	/**
	 * Questo metodo serve per calcolare i giorni di differenza tra due date.
	 * @param split_s indica la data splittata nel giorno, mese e anno
	 * @param split_e indica la data splittata nel giorno, mese e anno
	 * @return differenza
	 */
	private long  cacolaMedia(String[] split_s, String[] split_e) {
		LocalDate startDate = LocalDate.of(Integer.parseInt(split_s[2]),Integer.parseInt(split_s[1]) , Integer.parseInt(split_s[0]));
		LocalDate endDate = LocalDate.of(Integer.parseInt(split_e[2]),Integer.parseInt(split_e[1]) , Integer.parseInt(split_e[0]));

		long n = ChronoUnit.DAYS.between(startDate, endDate);
		return n;
	}

	private String getDate(String date) {
		
		String[] arrayDate=date.split("-");
		
		if(arrayDate.length>1 && arrayDate[0].length()<2) {
			String correggi="0";
			correggi+=arrayDate[0];
			arrayDate[0]=correggi;
		}
		if(arrayDate.length>1 && arrayDate[1].length()<2) {
			String correggi="0";
			correggi+=arrayDate[1];
			arrayDate[1]=correggi;
		}
		
		
		
		try {
			if (arrayDate.length==3) {
				String giorno=arrayDate[0];
				String mese=arrayDate[1];
				String anno=arrayDate[2];
			Periodo p=new Periodo(giorno,mese,anno);
			return p.getStartDate();
			}else
				return null;
		}
		catch(WrongPeriodException e) {
			return null;
		}
		
	}

	private long validazione(String name, String valore) {
		source.setChiaveValore(name, valore);
		JSONObject risposta = chiamante.getData(source.getApi());
		
		if(risposta == null) return -1;
		return numeroElementi(risposta);
	}
	
	public long numeroElementi(JSONObject obj) {
		JSONObject resp1=new JSONObject();
		
		if(Objects.nonNull(obj)) {
			resp1= (JSONObject) obj.get("page");
		  long num=(long) resp1.get("totalElements");
		  return num;}
		else return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getClassificationEvents() {

		EndPointApiKey source=new EndPointApiKey("https://app.ticketmaster.com/discovery/v2",apikey);
		source.setPath("/classifications.json");

		JSONObject tmp= chiamante.getData(source.getApi());
		JSONObject p1=(JSONObject) tmp.get("_embedded");
		JSONArray p2=(JSONArray) p1.get("classifications");//quindi p2 Ã¨ un json array
		JSONArray arrResponse = new JSONArray();
		
		for(int i=0;i<p2.size();i++) {
			JSONObject x=(JSONObject) p2.get(i);
			JSONObject y=(JSONObject) x.get("segment");
			
			if(Objects.nonNull(y)) {
				String id= (String) y.get("id");
				String s= (String) y.get("name");
				JSONObject response=new JSONObject();
			
				response.put("id", id);
				response.put("name", s);
				arrResponse.add(response);
			}//è se è null nn è gestito
		}
		return arrResponse;
	}
	
	//FA LA VALIDAZIONE DEL PARAMETRO
	private boolean validazione2(String name, String valore) {
		source.setChiaveValore(name, valore);
		JSONObject obj = chiamante.getData(source.getApi());
		long num = this.numeroElementi(obj);
		if (num != 0) return true;
		else return false;
	}	
	
	/***************************************************************************************************************************/
	/****************************************** PRENDE I DATI DA TICKETMASTER **************************************************/
	/***************************************************************************************************************************/
	
	//CHIAMATA TICKETMASTER CON COUNTRY, STATE, CLASSIFICAZIONE E PERIODO
	public JSONObject setupModel(String countryCode, String stateCode, String nameClass, Periodo periodo) {
		source.setChiaveValore("countryCode", countryCode);
		source.setChiaveValore("stateCode", stateCode);
		source.setChiaveValore("segmentName", nameClass);
		source.setChiaveValore("startDateTime", periodo.getStartDate());
		source.setChiaveValore("endDateTime", periodo.getEndDate());
		return chiamante.getData(source.getApi());
	}

	//CHIAMATA TICKETMASTER CON COUNTRY, STATE E PERIODO
	public JSONObject setupModel(String countryCode, String stateCode, Periodo periodo) {
		source.setChiaveValore("countryCode", countryCode);
		source.setChiaveValore("stateCode", stateCode);
		source.setChiaveValore("startDateTime", periodo.getStartDate());
		source.setChiaveValore("endDateTime", periodo.getEndDate());
		return chiamante.getData(source.getApi());
	}
	public JSONObject getErrori () {

		JSONArray errArr=new JSONArray();
		JSONObject errore=new JSONObject();
		JSONObject errore2=new JSONObject();
		errore.put("errore ", "problema interno al software"
				+ " controlla apikey ed endpoint");
		errArr.add(errore);
		errore2.put("errore", "problemi con la risposta dal server");
		errArr.add(errore2);
		err.put("messaggio", "connessione fallita");
		err.put("lista possibili errori", errArr);
	return err;	
	}
	public JSONObject withCountrycode (String countryCode) {
		JSONObject responce=new JSONObject();
		long co=validazione("countryCode", countryCode);
		if(co  == -1) {
		
		return getErrori();
		}
		//fine validazione canada
		if(co >0) {
		
			responce.put("contryCode", countryCode);
			responce.put("numero eventi", co);
		}
		else {
				err.put("messaggio", "errore");
				err.put("countryCode", countryCode +" parametro non valido");
				err.put("numero eventi", co);
			
				return err;
			
		}
	return responce;	
	}
	
}
