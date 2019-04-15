package com.financiera.domain.bean;

import java.io.Serializable;

public class DisparoEntidadesBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DELEGACION = "Delegacion";
	public static final String CLIENTE = "Cliente";
	public static final String DELEGACION_NACION = "DelegNacion";
	
	private Long		id;
	private Long		identificador;
	private String		tipo;
	private DisparoBean	disparo;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("disparEntidades");	sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append(identificador);	sb.append(";");
			sb.append(tipo); 			sb.append(";");
			if(disparo != null)
				sb.append(disparo.getId()); sb.append(";");
		} catch(Exception e) {
			throw new Exception("DisparoEntidadesBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public boolean esCliente() {
		if(tipo.compareTo(DisparoEntidadesBean.CLIENTE)==0)
			return true;
		else
			return false;
	}
	public boolean esDelegacion() {
		if(tipo.compareTo(DisparoEntidadesBean.DELEGACION)==0 || tipo.compareTo(DisparoEntidadesBean.DELEGACION_NACION)==0)
			return true;
		else
			return false;
	}
	public boolean esDelegacionNacion() {
		if(tipo.compareTo(DisparoEntidadesBean.DELEGACION_NACION)==0)
			return true;
		else
			return false;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Long identificador) {
		this.identificador = identificador;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public DisparoBean getDisparo() {
		return disparo;
	}
	public void setDisparo(DisparoBean disparo) {
		this.disparo = disparo;
	}
}