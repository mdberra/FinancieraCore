package com.dto;

public class EstadoMovDTO {
	private Long	id;
	private int		estado;
	private int  	color;
	private String  codRechazo;
	
	public EstadoMovDTO() {}
	
	public EstadoMovDTO(String pp) {
		Parser p = new Parser();
		p.parsear(pp);

		this.id			= p.getTokenLong();
		this.estado		= p.getTokenInteger();
		this.color		= p.getTokenInteger();
		this.codRechazo	= p.getTokenString();
		
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("estadoMov"); sb.append(";");
		sb.append(id);			sb.append(";");
		sb.append(estado);		sb.append(";");
		sb.append(color);		sb.append(";");
		sb.append(codRechazo); 	sb.append(";");
	
		return sb.toString();
	}
	public String getCodRechazo() {
		return codRechazo;
	}
	public void setCodRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}