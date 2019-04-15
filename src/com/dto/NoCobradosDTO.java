package com.dto;

import java.util.StringTokenizer;

public class NoCobradosDTO {
	private String periodo;
	private String estado;
	private int cantidad;
	
	public NoCobradosDTO(String periodo, String estado) {
		this.periodo = periodo;
		this.estado  = estado;
	}
	public String toString() {
		return this.periodo.concat(";").concat(this.estado).concat(";").concat(String.valueOf(this.cantidad)).concat(";");
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public void incrementar() {
		this.cantidad++;
	}
	public void desarmar(String key) {
		StringTokenizer tk =  new StringTokenizer(key, ";");
		while (tk.hasMoreTokens()) {
			this.periodo = tk.nextToken();
			this.estado  = tk.nextToken();
		}
	}
}