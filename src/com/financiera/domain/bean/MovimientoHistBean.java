package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Date;

import com.financiera.core.util.DateTimeUtil;

public class MovimientoHistBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static String VACIO = "VACIO";
	private static int ident = 1;
	
	private Long			id;
	private Long			idCliente;
	private ClienteBean		cliente;
	private Long			idServicio;
	private Date			fecha;
	private double			importe;
	private String			descripcion;
	private int				nroCuota;
	private int				totalCuotas;
	private EstadoMovBean	estado;
	private DisparoBean		disparo;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("movimientoHist"); 			sb.append(";");
			sb.append(ident++);					sb.append(";");
			sb.append(idCliente);				sb.append(";");
			sb.append(idServicio);				sb.append(";");
			sb.append(DateTimeUtil.formatDate(fecha)); sb.append(";");
			sb.append(importe);					sb.append(";");
			sb.append(descripcion);				sb.append(";");
			sb.append(nroCuota);				sb.append(";");
			sb.append(totalCuotas);				sb.append(";");
			sb.append(estado.getId());			sb.append(";");
			sb.append(disparo.getId());			sb.append(";");
		} catch(Exception e) {
			throw new Exception("MovimientoHistBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(id));			sb.append(";");
		sb.append(String.valueOf(idCliente));	sb.append(";");
		sb.append(String.valueOf(idServicio));	sb.append(";");
		if(this.fecha == null) {
			sb.append(" ");						sb.append(";");
		}else{
			sb.append(DateTimeUtil.formatDateDD_MM_AAAA(this.fecha));	sb.append(";");
		}
		sb.append(String.valueOf(importe));		sb.append(";");
		sb.append(descripcion);					sb.append(";");
		sb.append(String.valueOf(nroCuota));	sb.append(";");
		sb.append(String.valueOf(totalCuotas));	sb.append(";");
		
		return sb.toString();
	}

	public String getPeriodo() {
		if(cliente.getDelegacion().getBanco().isNacion())
			return DateTimeUtil.getPeriodoNacion(this.fecha);
		else
			return DateTimeUtil.getPeriodo(this.fecha);
	}
	public ClienteBean getCliente() {
		return cliente;
	}
	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public int getNroCuota() {
		return nroCuota;
	}
	public void setNroCuota(int nroCuota) {
		this.nroCuota = nroCuota;
	}
	public int getTotalCuotas() {
		return totalCuotas;
	}
	public void setTotalCuotas(int totalCuotas) {
		this.totalCuotas = totalCuotas;
	}
	public EstadoMovBean getEstado() {
		return estado;
	}
	public void setEstado(EstadoMovBean estado) {
		this.estado = estado;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public DisparoBean getDisparo() {
		return disparo;
	}
	public void setDisparo(DisparoBean disparo) {
		this.disparo = disparo;
	}
	public Long getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}
}