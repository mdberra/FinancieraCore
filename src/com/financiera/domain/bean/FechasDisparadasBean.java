package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Date;

import com.financiera.core.util.DateTimeUtil;

public class FechasDisparadasBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	private static int ident = 1;
	
	private Long			id;
	private Long			idDelegacion;
	private DelegacionBean	delegacion;
	private Long			idDisparo;
	private DisparoBean		disparo;
	private Date			fechaDisparo;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("FechasDisparadas");		sb.append(";");
			sb.append(ident++);					sb.append(";");
			sb.append(delegacion.getId());		sb.append(";");
			sb.append(disparo.getId());			sb.append(";");
			sb.append(DateTimeUtil.formatDate(fechaDisparo)); sb.append(";");
		} catch(Exception e) {
			throw new Exception("FechasDisparadasBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public DelegacionBean getDelegacion() {
		return delegacion;
	}
	public void setDelegacion(DelegacionBean delegacion) {
		this.delegacion = delegacion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaDisparo() {
		return fechaDisparo;
	}
	public String getFechaDisparoStr() {
		return DateTimeUtil.formatDate(fechaDisparo);
	}
	public void setFechaDisparo(Date fechaDisparo) {
		this.fechaDisparo = fechaDisparo;
	}
	public void setFechaDisparo(String fechaDisparo) {
		try {
			this.fechaDisparo = DateTimeUtil.getDate(fechaDisparo);
		} catch(Exception e) { }
	}
	public Long getIdDelegacion() {
		return idDelegacion;
	}
	public void setIdDelegacion(Long idDelegacion) {
		this.idDelegacion = idDelegacion;
	}
	public DisparoBean getDisparo() {
		return disparo;
	}
	public void setDisparo(DisparoBean disparo) {
		this.disparo = disparo;
	}
	public Long getIdDisparo() {
		return idDisparo;
	}
	public void setIdDisparo(Long idDisparo) {
		this.idDisparo = idDisparo;
	}
}