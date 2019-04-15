package com.dto;

public class Estad2DTO {
	private String		fecha;
	private String		cliente;
	private double		importe;
	
	private Parser p = new Parser();
	
	public Estad2DTO() {}
	
	public Estad2DTO(String s) {
		p.parsear(s);
		this.fecha		= p.getTokenString();
		this.cliente	= p.getTokenString();
		this.importe	= p.getTokenDouble();
	}
	public String toString() {
		return new String("Estad2DTO;" + fecha + ";" + cliente + ";" + importe);
	}
	public String getFechaDisp() {
		return new String(fecha.substring(8, 10) + "-" + fecha.substring(5, 7) + "-" + fecha.substring(0, 4));
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
}