package com.dto;


public class CtacteDTO {
	private Long			id;
	private Long			idCliente;
	private Long			idServicio;
	private String			fecha;
	private double			importe;
	private String			descripcion;
	private int				nroCuota;
	private int				totalCuotas;
	private int				estadoColor;
	private String			codRechazo;
	private String			periodo;
	private String			periodoSort;
	
	public CtacteDTO(String ctacte) {
		String c = new String("movimiento;").concat(ctacte);
		Parser p = new Parser();
		p.parsear(c);

		this.id				= p.getTokenLong();
		this.idCliente		= p.getTokenLong();
		this.idServicio		= p.getTokenLong();
		this.fecha			= p.getTokenString();
		this.importe		= p.getTokenDouble();
		this.descripcion	= p.getTokenString();
		this.nroCuota		= p.getTokenInteger();
		this.totalCuotas	= p.getTokenInteger();
		this.estadoColor	= p.getTokenInteger();
		this.codRechazo		= p.getTokenString();
		this.periodo		= p.getTokenString();
		this.periodoSort	= p.getTokenString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(id));			sb.append(";");
		sb.append(String.valueOf(idCliente));	sb.append(";");
		sb.append(String.valueOf(idServicio));	sb.append(";");
		if(this.fecha == null) {
			sb.append(" ");						sb.append(";");
		}else{
			sb.append(this.fecha.toString());	sb.append(";");
		}
		sb.append(String.valueOf(importe));		sb.append(";");
		sb.append(descripcion);					sb.append(";");
		sb.append(String.valueOf(nroCuota));	sb.append(";");
		sb.append(String.valueOf(totalCuotas));	sb.append(";");
		sb.append(String.valueOf(estadoColor));	sb.append(";");
		sb.append(codRechazo);					sb.append(";");
		sb.append(periodo);						sb.append(";");
		sb.append(periodoSort);					sb.append(";");
		
		return sb.toString();
	}

	public String getCodRechazo() {
		return codRechazo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public int getEstadoColor() {
		return estadoColor;
	}
	public String getFecha() {
		return fecha;
	}
	public Long getId() {
		return id;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public double getImporte() {
		return importe;
	}
	public int getNroCuota() {
		return nroCuota;
	}
	public int getTotalCuotas() {
		return totalCuotas;
	}
	public String getPeriodo() {
		return periodo;
	}
	public Long getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}
	public String getPeriodoSort() {
		return periodoSort;
	}
	public void setPeriodoSort(String periodoSort) {
		this.periodoSort = periodoSort;
	}
}