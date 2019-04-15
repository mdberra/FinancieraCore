package com.dto;

public class Estad4DTO {
	private String	periodo;
	private int 	cantidad;
	private double	importe;
	
	private Parser p = new Parser();
	
	public Estad4DTO() {}
	
	public Estad4DTO(String s) {
		p.parsear(s);
		this.periodo	= p.getTokenString();
		this.cantidad	= p.getTokenInteger();
		this.importe	= p.getTokenDouble();
	}
	public String toString() {
		return new String("Estad4DTO;" + periodo + ";" + cantidad + ";" + importe);
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
}