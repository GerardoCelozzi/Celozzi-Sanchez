package com.univpm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * Questa classe gestisce le risposte delle richieste all'end point scelto.
 * @author Sara
 *
 */
public class APICall {
	
	/**
	 * Questo metodo fa la chiamata all'end point passato come parametro e ritorna la risposta in formato JSONObject.
	 * Se la chiamata all'end point non va a buon fine ritorno null.
	 * @param url indica la request URL
	 * @return <code>JSONObject</code>
	 */
	public JSONObject getData(String url) {
		
		JSONObject jo = null;
		String data_filter ="";
		String line = "";
		try {
			
			URLConnection openConnection = new URL(url).openConnection();
			InputStream in = openConnection.getInputStream();

			InputStreamReader input = new InputStreamReader( in );
			BufferedReader buf = new BufferedReader( input );
			
			while ( ( line = buf.readLine() ) != null ) {
				data_filter += line;
			}
			
			in.close();
			jo = (JSONObject) JSONValue.parseWithException(data_filter);
			return jo;
		}
		catch (Exception e ) {
			return null;
		}
	}
	
}
