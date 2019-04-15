package com.financiera.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Util {
	public static final int NUMERICO = 0;
	public static final int TEXTO = 1;
	public static final int TEXTO_NUM = 2;
	public static final int NUMERICO_SIN_ESP = 3;
	public static final int NUMERICO_SIN_ESP_CON_PUNTO = 4;
/**
 * Redondear
 */
	public static double redondear(double numero, int decimales ) {
		return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
	}
/**
 * Devuelve true si el String es null o ""
 */
	public static boolean isBlank(String text) {
		return text == null || text.trim().equals("");
	}

/**
 * Devuelve true si el (Integer)String es >= 0
 */
	public static boolean isPositiveInteger(String text) {
    	boolean isPositiveInteger = true;
    	
    	try {
    		int textInt = Integer.parseInt(text);
    		
    		if (textInt < 0) {
    			isPositiveInteger = false;
    		}
    	}
    	catch(Exception e) {
    		isPositiveInteger = false;
    	}
    	
    	return isPositiveInteger ;
    }
/**
 * Formatea con la longitud
 * NUMERICO  Si el tieneDecimales = true significa que tiene 2 decimales  
 * 			 el numero se alinea a la derecha y se rellena con 0
 *           
 * TEXTO el texto se alinea a la izquierda y se rellena con blancos.
 */
	public static String formateo(int tipo, int longitud, Object o, boolean tieneDecimales) {
		StringBuffer sb = new StringBuffer();
		double numd = 0;
		String tex = new String();
		int l = 0; 
		
		switch(tipo) {
		case Util.NUMERICO :
			if(tieneDecimales) {
				numd = (Double)o;

				StringBuffer sb1 = new StringBuffer();
				Formatter f = new Formatter(sb1, Locale.US);
				f.format("%.0f", numd*100);
				
				tex = sb1.toString();
			} else {
				tex = String.valueOf(o);
			}
			l = tex.length();
			if(l<=longitud) {
				l = longitud - l;
				for(int i=0; i<l; i++) {
					sb.append("0");
				}
				sb.append(tex);
			}
			break;
		case Util.NUMERICO_SIN_ESP :
			if(tieneDecimales) {
				numd = (Double)o;

				StringBuffer sb1 = new StringBuffer();
				Formatter f = new Formatter(sb1, Locale.US);
				f.format("%.0f", numd*100);
				
				tex = sb1.toString();
			} else {
				tex = String.valueOf(o);
			}
			sb.append(tex);
			break;
		case Util.NUMERICO_SIN_ESP_CON_PUNTO :
			if(tieneDecimales) {
				numd = (Double)o;
				
				StringBuffer sb1 = new StringBuffer();
				Formatter f = new Formatter(sb1, Locale.US);
				f.format("%1.2f", numd);
				tex = sb1.toString();
			} else {
				tex = String.valueOf(o);
			}
			sb.append(tex);
			break;
		case Util.TEXTO :
			tex = (String)o;
			l = tex.length();
			if(l<=longitud) {
				sb.append(tex);
				l = longitud - l;
				for(int i=0; i<l; i++) {
					sb.append(" ");
				}
			}
			break;
		case Util.TEXTO_NUM :
			tex = (String)o;
			l = tex.length();
			if(l<=longitud) {
				sb.append(tex);
				l = longitud - l;
				for(int i=0; i<l; i++) {
					sb.append("0");
				}
			}
			break;
		}
		
		return sb.toString();
	}
	
// Ordena la clave

	public static ArrayList<String> sortByKey(Map<String, String> fil) { 
		ArrayList<String> lista = new ArrayList<String>();
		Iterator<String> keys = fil.keySet().iterator();
		while(keys.hasNext()) {
			String periodo = (String)keys.next();
			
			lista.add(periodo);
		}
		
		Collections.sort(lista); 
	 
	    return lista;
	}
}