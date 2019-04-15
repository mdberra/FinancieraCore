package com.dto;

public class MovTotTipoDTO {
	private int    cantMesesSinCobro;
	private int	   cantCuotasCobradas;	
	private double importeCobrado;
	private double importeDevengado;
	

	public void addCantMesesSinCobro(int cantMesesSinCobro) {
		this.cantMesesSinCobro += cantMesesSinCobro;
	}
	public void addCantCuotasCobradas(int cantCuotasCobradas) {
		this.cantCuotasCobradas += cantCuotasCobradas;
	}
	public void addImporteCobrado(double importeCobrado) {
		this.importeCobrado+= importeCobrado;
	}
	public void addImporteDevengado(double importeDevengado) {
		this.importeDevengado+= importeDevengado;
	}
	
	public int getCantMesesSinCobro() {
		return cantMesesSinCobro;
	}
	public void setCantMesesSinCobro(int cantMesesSinCobro) {
		this.cantMesesSinCobro = cantMesesSinCobro;
	}
	public double getImporteCobrado() {
		return importeCobrado;
	}
	public void setImporteCobrado(double importeCobrado) {
		this.importeCobrado = importeCobrado;
	}
	public double getImporteDevengado() {
		return importeDevengado;
	}
	public void setImporteDevengado(double importeDevengado) {
		this.importeDevengado = importeDevengado;
	}
	public int getCantCuotasCobradas() {
		return cantCuotasCobradas;
	}
	public void setCantCuotasCobradas(int cantCuotasCobradas) {
		this.cantCuotasCobradas = cantCuotasCobradas;
	}
}