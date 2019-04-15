package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.financiera.core.util.DateTimeUtil;


public class UsuarioBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ARCA = "ARCA";
	public static final String SERVINA = "SERVINA";
	
	private Long		id;
	private String		nombre;
	private String		apellido;
	private Collection<GrupoPermisoBean> grupos;
	private Date		fechaDesde;
	private Date		fechaHasta;
	private String		alias;
	private String		password;
	private String		empresa;
	
	public String getNyA() {
		return new String(nombre + " " + apellido);
	}
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("usuario"); 	sb.append(";");
			sb.append(id);			sb.append(";");
			sb.append(nombre);		sb.append(";");
			sb.append(apellido);	sb.append(";");
			String fecha = DateTimeUtil.formatDate(fechaDesde);
			if(fecha == null || fecha.compareTo("") == 0) {
				sb.append(" "); sb.append(";");
			}else{
				sb.append(fecha); sb.append(";");
			}
			fecha = DateTimeUtil.formatDate(fechaHasta);
			if(fecha == null || fecha.compareTo("") == 0) {
				sb.append(" "); sb.append(";");
			}else{
				sb.append(fecha); sb.append(";");
			}
			sb.append(alias);		sb.append(";");
			sb.append(password);	sb.append(";");
			sb.append(empresa);		sb.append(";");
		} catch(Exception e) {
			throw new Exception("UsuarioBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Collection<GrupoPermisoBean> getGrupos() {
		return grupos;
	}
	public void setGrupos(Collection<GrupoPermisoBean> grupos) {
		this.grupos = grupos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		try {
			this.fechaDesde = DateTimeUtil.getDate(fechaDesde);
		} catch(Exception e) { }
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		try {
			this.fechaHasta = DateTimeUtil.getDate(fechaHasta);
		} catch(Exception e) { }
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public boolean esArca() {
		if(this.empresa.compareTo(UsuarioBean.ARCA)==0)
			return true;
		else
			return false;
	}
	public boolean esServina() {
		if(this.empresa.compareTo(UsuarioBean.SERVINA)==0)
			return true;
		else
			return false;
	}
}