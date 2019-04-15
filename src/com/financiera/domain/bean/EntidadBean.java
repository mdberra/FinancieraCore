package com.financiera.domain.bean;

import java.io.Serializable;

import org.hibernate.Session;

public class EntidadBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long	id;
	private String	codigo;
	private String	descripcion;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("entidad"); 	sb.append(";");
			sb.append(id);			sb.append(";");
			sb.append(codigo);		sb.append(";");
			sb.append(descripcion); sb.append(";");
		} catch(Exception e) {
			throw new Exception("EntidadBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public static EntidadBean getParametro1(Session s) {
		return (EntidadBean)s.get(EntidadBean.class, ParametroBean.GENERAL);
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
