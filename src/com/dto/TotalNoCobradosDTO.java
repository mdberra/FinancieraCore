package com.dto;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.financiera.core.util.DateTimeUtil;

public class TotalNoCobradosDTO {
	private Hashtable<String, NoCobradosDTO> matrizTotal = new Hashtable<String, NoCobradosDTO>();
	private NoCobradosDTO aux;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		String auxs;
	    Vector v = new Vector(matrizTotal.keySet());
	    Collections.sort(v);

	    for (Enumeration e = v.elements(); e.hasMoreElements();) {
	      String key = (String)e.nextElement();
	      aux = (NoCobradosDTO)matrizTotal.get(key);
//	      System.out.println(aux.toString());
	      sb.append(aux);
	    }
		return sb.toString();
	}
	
	public void incrementar(String periodo, String estado) {
		periodo = DateTimeUtil.getNroPeriodo(periodo);
		String clave = new String(periodo.concat(";").concat(estado));
		
		if(matrizTotal.containsKey(clave)) {
			aux = matrizTotal.get(clave);
		} else {
			aux = new NoCobradosDTO(periodo, estado);
		}
		aux.incrementar();

		matrizTotal.put(clave, aux);
	}
}