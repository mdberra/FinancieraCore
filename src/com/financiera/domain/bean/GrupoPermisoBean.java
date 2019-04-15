package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Collection;


public class GrupoPermisoBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Collection<PermisoBean> permisos;
	
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
			throw new Exception("GrupoPermisoBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Collection<PermisoBean> getPermisos() {
		return permisos;
	}
	public void setPermisos(Collection<PermisoBean> permisos) {
		this.permisos = permisos;
	}
}
