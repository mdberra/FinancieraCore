package com.dto;

public class Estad1DTO {
	private String		fecha;
	private int			cobrosCant;
	private double		cobrosImporte;
	private int			reversionCant;
	private double		reversionImporte;
	
	private Parser p = new Parser();
	
	public Estad1DTO() {}
	
	public Estad1DTO(String s) {
		p.parsear(s);
		this.fecha				= p.getTokenString();
		this.cobrosCant			= p.getTokenInteger();
		this.cobrosImporte		= p.getTokenDouble();
		this.reversionCant		= p.getTokenInteger();
		this.reversionImporte	= p.getTokenDouble();
	}
	public String toString() {
		return new String("Estad1DTO;" + fecha + ";" + cobrosCant
		+ ";" + cobrosImporte  + ";" +  reversionCant + ";" +  reversionImporte);
	}
	public String getFechaDisp() {
		return new String(fecha.substring(8, 10) + "-" + fecha.substring(5, 7) + "-" + fecha.substring(0, 4));
	}
	public int getCobrosCant() {
		return cobrosCant;
	}
	public void setCobrosCant(int cobrosCant) {
		this.cobrosCant = cobrosCant;
	}
	public double getCobrosImporte() {
		return cobrosImporte;
	}
	public void setCobrosImporte(double cobrosImporte) {
		this.cobrosImporte = cobrosImporte;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getReversionCant() {
		return reversionCant;
	}
	public void setReversionCant(int reversionCant) {
		this.reversionCant = reversionCant;
	}
	public double getReversionImporte() {
		return reversionImporte;
	}
	public void setReversionImporte(double reversionImporte) {
		this.reversionImporte = reversionImporte;
	}
	
}