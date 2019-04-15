package com.dto;


public class ResultadoDTO {
	private Parser p = new Parser();
	private int i = 0;
	
	private int leidos;
	private int yaRespondido;
	private int grabados;
	private int errores;
	private int deleteados;
	private int enviadas;
	private int estadoTransitorio; // No es Aceptado ni Rechazado
	private String[] mensajes = new String[1000]; 

	public ResultadoDTO() {
		leidos = 0;
		yaRespondido = 0;
		grabados = 0;
		errores = 0;
		deleteados = 0;
		enviadas = 0;
		for(int i=0;i<1000;i++) {
			mensajes[i] = new String();
		}
	}
	public ResultadoDTO(String s) {
		p.parsear(s);

		this.leidos		  = p.getTokenInteger();
		this.yaRespondido = p.getTokenInteger();
		this.grabados	  = p.getTokenInteger();
		this.errores	  = p.getTokenInteger();
		this.deleteados	  = p.getTokenInteger();
		this.enviadas	  = p.getTokenInteger();
		this.estadoTransitorio = p.getTokenInteger();
		for(int i=0;i<10;i++) {
			mensajes[i] = p.getTokenString();
		}
	}
	public String toString() {
		return new String("resultado;".
				   concat(String.valueOf(leidos)).concat(";").
				   concat(String.valueOf(yaRespondido)).concat(";").
				   concat(String.valueOf(grabados)).concat(";").
				   concat(String.valueOf(errores)).concat(";").
				   concat(String.valueOf(deleteados)).concat(";").
				   concat(String.valueOf(enviadas)).concat(";").
				   concat(String.valueOf(estadoTransitorio)).concat(";").
				   concat(mensajes[0]).concat(";").concat(mensajes[1]).concat(";").concat(mensajes[2]).concat(";").concat(mensajes[3]).concat(";").
				   concat(mensajes[4]).concat(";").concat(mensajes[5]).concat(";").concat(mensajes[6]).concat(";").concat(mensajes[7]).concat(";").
				   concat(mensajes[8]).concat(";").concat(mensajes[9]).concat(";")
		);
	}
	public void incEstadoTransitorio() {
		this.estadoTransitorio++;
	}
	public int getEstadoTransitorio() {
		return estadoTransitorio;
	}
	public void setEstadoTransitorio(int estadoTransitorio) {
		this.estadoTransitorio = estadoTransitorio;
	}
	public void incEnviadas() {
		this.enviadas++;
	}
	public int getEnviadas() {
		return enviadas;
	}
	public void setEnviadas(int enviadas) {
		this.enviadas = enviadas;
	}
	public int getDeleteados() {
		return deleteados;
	}
	public void incDeleteados(int i) {
		this.deleteados += i;
	}
	public int getErrores() {
		return errores;
	}
	public void incErrores() {
		this.errores++;
	}
	public int getGrabados() {
		return grabados;
	}
	public void incGrabados() {
		this.grabados++;
	}
	public int getLeidos() {
		return leidos;
	}
	public void incLeidos() {
		this.leidos++;
	}
	public void decLeidos() {
		this.leidos--;
	}
	public int getYaRespondido() {
		return yaRespondido;
	}
	public void incYaRespondido() {
		this.yaRespondido++;
	}
	public String[] getMensajes() {
		return mensajes;
	}
	public void setMensajes(String[] mensajes) {
		this.mensajes = mensajes;
	}
	public void incMensajes(String m) {
		this.mensajes[this.i] = m;
		this.i++;
	}
}