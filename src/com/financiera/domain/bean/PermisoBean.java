package com.financiera.domain.bean;

import java.io.Serializable;

public class PermisoBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long		id;
	private String		codigo;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(""); 				sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append("");				sb.append(";");
			sb.append(""); 				sb.append(";");
			sb.append("");				sb.append(";");
			sb.append("");				sb.append(";");
			sb.append("");				sb.append(";");
		} catch(Exception e) {
			throw new Exception("PermisoBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
