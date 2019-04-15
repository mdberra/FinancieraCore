package com.financiera.domain.bean;

import java.io.Serializable;

import org.hibernate.Session;



public class LocalizacionBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long   id;
	private String calle;
	private String nro;
	private String piso;
	private String depto;
	private String telefLinea;
	private String telefCelular;
	private String codPostal;
	private String barrio;
	private String localidad;
	private String provincia;
	private String pais;

	public static LocalizacionBean getParametro1(Session s) {
		return (LocalizacionBean)s.get(LocalizacionBean.class, ParametroBean.GENERAL);
	}
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("localizacion");	sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append(calle);			sb.append(";");
			sb.append(nro); 			sb.append(";");
			sb.append(piso);			sb.append(";");
			sb.append(depto);			sb.append(";");
			sb.append(telefLinea);		sb.append(";");
			sb.append(telefCelular); 	sb.append(";");
			sb.append(codPostal);		sb.append(";");
			sb.append(barrio);			sb.append(";");
			sb.append(localidad);		sb.append(";");
			sb.append(provincia);		sb.append(";");
			sb.append(pais);			sb.append(";");
		} catch(Exception e) {
			throw new Exception("LocalizacionBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCodPostal() {
		return codPostal;
	}
	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}
	public String getDepto() {
		return depto;
	}
	public void setDepto(String depto) {
		this.depto = depto;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getNro() {
		return nro;
	}
	public void setNro(String nro) {
		this.nro = nro;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTelefCelular() {
		return telefCelular;
	}
	public void setTelefCelular(String telefCelular) {
		this.telefCelular = telefCelular;
	}
	public String getTelefLinea() {
		return telefLinea;
	}
	public void setTelefLinea(String telefLinea) {
		this.telefLinea = telefLinea;
	}
}