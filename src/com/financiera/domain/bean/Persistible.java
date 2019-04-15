package com.financiera.domain.bean;

import org.hibernate.Session;


public class Persistible {
	public static final int NADA = -1;
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;
	
	private int accion;
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}
	public void persistir(Session s, Persistible p) throws Exception {
		switch(p.getAccion()) {
		case Persistible.INSERT : s.save(p); break;
		case Persistible.UPDATE : s.update(p); break;
		case Persistible.DELETE : s.delete(p); break;
		default :
			throw new Exception("Falta definir Accion");
		}
	}
	public String toString() {
		String ret = null;
		try {
			ret = this.backupBajar();
		} catch (Exception e) {}
		return ret;
	}
// para sobrescribir
	public String backupBajar() throws Exception {
		return null;
	}
// cuando no tiene valor agregar un " "
	public String agregarBlancoANull(String input) {
		char separador = ';';
		StringBuffer sb = new StringBuffer();
		
		String salida = "";
		char[] chars = input.toCharArray();
		for(int i=0; i< chars.length; i++) {
			if(chars[i] == separador) {
				this.cambio(salida, sb);
				salida = "";
			} else {
				salida += chars[i];
			}
		}
		return sb.toString();
	}
	private void cambio(String salida, StringBuffer sb) {
		if(salida == null || salida.compareTo("null") == 0 || salida.compareTo("") == 0) {
			sb.append(" "); sb.append(";");
		} else {
			sb.append(salida); sb.append(";");
		}
	}
}
