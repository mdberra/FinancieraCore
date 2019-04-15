package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.Session;

import com.financiera.core.util.DateTimeUtil;

public class DisparoBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long		id;
	private Date		fecha;
	private int			cantRegistros;
	private double		importe;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("disparo");		sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append(DateTimeUtil.formatDate(fecha));	sb.append(";");
			sb.append(cantRegistros); 	sb.append(";");
			sb.append(importe);			sb.append(";");
		} catch(Exception e) {
			throw new Exception("DisparoBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public void getNewDisparo(Session s) {
		try{
			this.setFecha(DateTimeUtil.getDate());
			this.setAccion(Persistible.INSERT);
			super.persistir(s, this);
		} catch (Exception ex) {
			System.out.println("DisparoBean - No se agrega el nuevo disparo : " + ex.getMessage());
		}
	}
	public void updateDisparo(Session s) {
		try {
			this.setAccion(Persistible.UPDATE);
			super.persistir(s, this);
		} catch (Exception ex) {
			System.out.println("DisparoBean - No se actualiza el disparo : " + ex.getMessage());
		}
	}
	
	public int getCantRegistros() {
		return cantRegistros;
	}
	public void setCantRegistros(int cantRegistros) {
		this.cantRegistros = cantRegistros;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setFecha(String fecha) {
		try {
			this.fecha = DateTimeUtil.getDate(fecha);
		} catch(Exception e) { }
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
}