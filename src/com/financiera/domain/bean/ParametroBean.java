package com.financiera.domain.bean;

import java.io.Serializable;

import org.hibernate.Session;


public class ParametroBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int diaCorte = 20;
	public static final int diaCorteAltaDelegacion = 5;
	public static final Long GENERAL = 1L;
	public static final String SERVINA = "SERVINA SA";
	
	private Long		id;
	private int			indice;
	private String		cuitEmpresa;
	private String		nombreEmpresa;
	private String		moneda;
	private int			ultimoServicio;

	public boolean esServina() {
		if(this.nombreEmpresa.compareTo(SERVINA)==0)
			return true;
		else
			return false;
	}
	public int getDiaCorte() {
		return diaCorte;
	}
	public int getDiaCorteAltaDelegacion() {
		return diaCorteAltaDelegacion;
	}

	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("param"); 		sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append(indice);			sb.append(";");
			sb.append(cuitEmpresa); 	sb.append(";");
			sb.append(nombreEmpresa);	sb.append(";");
			sb.append(moneda);			sb.append(";");
			sb.append(ultimoServicio);	sb.append(";");
		} catch(Exception e) {
			throw new Exception("ParametroBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public static ParametroBean getParametro1(Session s) {
		return (ParametroBean)s.get(ParametroBean.class, ParametroBean.GENERAL);
	}
	public static void updateParametro(Session s, ParametroBean p) {
		s.update(p);
	}
	public String getCuitEmpresa() {
		return cuitEmpresa;
	}
	public void setCuitEmpresa(String cuitEmpresa) {
		this.cuitEmpresa = cuitEmpresa;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public int dameIndice() {
		return ++this.indice;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public int getUltimoServicio() {
		return ultimoServicio;
	}
	public void setUltimoServicio(int ultimoServicio) {
		this.ultimoServicio = ultimoServicio;
	}
}