package com.univpm.model;

/**
 * Classe <b>Parametro</b> che rappresenta un query param.
 * @author hog76
 *
 */
public class Parametro {

	 private String chiave="";
	 private String valoreStringa="";
	 private String valorecategoria="";
	 private boolean valorebooelan;
	 private long valorelong;
	 private int indexCall;
	  
	public Parametro(String a){
		this.chiave=a;
	}
	 
	public Parametro(String k,String v){
		this.chiave=k;
		this.valoreStringa=v; 
	}
	 
	public Parametro(String k,boolean b){
		this.chiave=k;
		this.valorebooelan=b; 
	}
	 
	public Parametro(String k,long i){
		this.chiave=k;
		this.valorelong=i; 
	}
	 
	public void setChiave(String k) {
		this.chiave=k;
	}
	
	public void setValueQueryParams(String v) {
		this.valoreStringa=v;
	}
	
	public String getChiave() {
		return this.chiave;
	}
	 	
	public void setCat(String s) {
		this.valorecategoria=s;
	}
	
	public String getCategoria() {
		return this.valorecategoria;
	}
	
	public String getValueQueryParams() {
		return this.valoreStringa;
	}
		
	public boolean getboolean() {
		return this.valorebooelan;
	}
	
	public long getLong() {
		return this.valorelong;
	}
		
	public void setIndex(int i) {
		this.indexCall=i;	
	}
		
	public int getIndexCall() {
		return this.indexCall;
	}
}
