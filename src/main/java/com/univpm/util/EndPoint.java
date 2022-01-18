package com.univpm.util;

import java.util.ArrayList;

import com.univpm.model.Parametro;


/**
 * Questa classe rappresenta un End Point generico per costruire la request URL.
 * 
 * @author Gerardo
 *
 */
public class EndPoint {

	ArrayList<Parametro> listaParametri= new ArrayList<Parametro>();
	ArrayList<Parametro> ordineParametri_queryString = new ArrayList<Parametro>();
	
	String domain;
	String i_path;
	String QueryString = ""; 
	String addQueryString = "";
	int indiceDiChiamta;
	
	/**
	 * Costruttore che prende come parametro il path dell'api che si vuole utilizzare.
	 * @param url indica il path dell'api
	 */
	public EndPoint(String url) {
		this.domain = url;
	}
	
	/**
	 * Costruttore vuoto
	 */
	public EndPoint() {
	}
 
	public void setDomain(String d) {
		this.domain=d;
	}
	
	public String getDomain() {
		return domain;
	}
 
	public void setPath(String path) {
		this.i_path=path + ".json"; //CHIEDERE A GE
	}

	public String getPath() {
		return  this.i_path;
	}
	
	int indexParametro=0;//setChiaveValore se aggiunge un nuovo parametro incrementa questo valore
	
	/**
	 * Questo metodo serve per aggiungere un parametro nel request URL.
	 * 
	 * @param chiave
	 * @param Valore
	 */
	public void setChiaveValore (String chiave,String Valore) {

		boolean continua=avvisaIncasoDiParametriIncoerenti(chiave, Valore);
		System.out.println("continua ---> " + continua);
		if(continua) {	
			boolean trovato=false;	 
			if(listaParametri.size()==0) {
				Parametro p=new Parametro(chiave,Valore);
				p.setIndex(indexParametro);
				listaParametri.add(p);
				indexParametro++;
			}
			else {
				if(listaParametri.size()>0) {
					for(int i =0;i<listaParametri.size();i++) {
			
						String tmp=listaParametri.get(i).getChiave();
						if(listaParametri.get(i).getChiave().equals(chiave)) {
				
							if(!Valore.equals("") && !Valore.equals("-")) {
								listaParametri.get(i).setChiave(chiave);
								listaParametri.get(i).setValueQueryParams(Valore);
								listaParametri.get(i).setIndex(indexParametro);
								indexParametro++;
								trovato=true;
								break;
							}
						}
						else {
							trovato=false;
						}	
					}
					if(!trovato){
						Parametro p=new Parametro(chiave,Valore);
						int index=indexParametro;
						p.setIndex(index);
						listaParametri.add(p);
						indexParametro++;
					}
				}
			}
		}
	}

	/**
	 * Questo metodo costuisce la request URL attraverso la lista parametri.
	 * @return <code>boolean</code>
	 */
	public boolean costruisciApi () {
		int count = listaParametri.size();
		boolean chiamataConParams = false;
		
		for(int i=0; i<listaParametri.size(); i++) {//itero la lista dei parametri di un endpoint
			if(!listaParametri.get(i).getValueQueryParams().equals("")) {
				chiamataConParams=true;
			}
		}
				
		if(chiamataConParams) {//ha trovato un valore
			boolean primovalore = false;
			
			int indiceDelPrimoParametro = 0;
			for(int i=0; i<count; i++) {
				if(!listaParametri.get(i).getValueQueryParams().equals("") && !listaParametri.get(i).getChiave().equals("") ) {
					int compare=listaParametri.get(i).getIndexCall();
					if(compare<indiceDelPrimoParametro) {
						indiceDelPrimoParametro=compare;
					}
				}
			}
			for(int j=0;j<count;j++) {
				if(listaParametri.get(j).getIndexCall()==indiceDelPrimoParametro && !listaParametri.get(j).getValueQueryParams().equals("") ) {
					QueryString+= "?" + listaParametri.get(j).getChiave()+"="+listaParametri.get(j).getValueQueryParams();	
				}
				else {
					if(!listaParametri.get(j).getValueQueryParams().equals("") ) {
					addQueryString+= "&" + listaParametri.get(j).getChiave()+"="+listaParametri.get(j).getValueQueryParams();
					}
				}
			}
		}
		return chiamataConParams;
	}
	
	/**
	 * Getter della request URL.
	 * @return <code>String</code>
	 */
	public String getApi() {
		costruisciApi(); //mi aggiorna con nuovi parametri	 
		pulisciqueryString();
		indexParametro=0; //riporto a zero l indice
		return "";
	}	
 	
	/**
	 * Questo metodo pulisce la query String ovvero cancella i query Params dalla request URL.
	 */
	public void pulisciqueryString() {
		
		QueryString="";
		addQueryString="";
		System.out.println("pulisci");
		for(int i= 0;i<listaParametri.size();i++) {
			listaParametri.get(i).setValueQueryParams("");
			listaParametri.get(i).setIndex(0);
		}
		this.i_path="";
	}

	/**
	 * Questo metodo pulisce il valore della chiave passata come parametro della lista parametri.
	 * @param update
	 */
	public void setValueForParams(String update) {
		for(int i= 0;i<listaParametri.size();i++) {
			listaParametri.get(i).setValueQueryParams("");;
		}
	}
 
	/**
	 * Getter della lista parametri.
	 * @return
	 */
	public ArrayList <Parametro> getParametri(){
		return listaParametri;
	}
	
	/**
	 * Questo metodo serve per controllare se i parametri inseriti dall'utente sono incoerenti.
	 * @param k
	 * @param v
	 * @return
	 */
	public boolean avvisaIncasoDiParametriIncoerenti(String k,String v) {
		int codiceerrore=0;
		boolean responce=true;
		String msg = "intercettato un  parametro ma hai effettuato una chiamata";		
		if(k.equals("")) {
			//incremento di 1
			codiceerrore++;
			responce=false; 
		}
		if( v.equals("") || v.equals("-")) { //MODIFICA SARA
			//incfremento di 2
			codiceerrore++;
			codiceerrore++;	
			responce=false; 
		}
		switch(codiceerrore) {
		 	case 1:
		 		msg = msg  + " senza chiave per il valore : "+ v;   
		 		break;
		 	case 2:
		 		msg = msg  + " senza valore per la chiave : "+ k;
		 		break;
		 	case 3:
		 		msg=msg + "  senza valorizzare chiave e valore ";
		}
		if(!responce) {
		      msg=msg + " \n il parametro quindi non è stato aggiunto alla queryString ";
		      System.out.println(msg);
		}
		return responce;	 
	 }	
}
