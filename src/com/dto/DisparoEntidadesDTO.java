package com.dto;

public class DisparoEntidadesDTO {
	private String  tipo;
	private Long    id;
	private String  datos;
	
	public DisparoEntidadesDTO() {}
	
	public DisparoEntidadesDTO(String pp) {
		Parser p = new Parser();
		p.parsear(pp);

		this.tipo	= p.getTokenString();
		this.id		= p.getTokenLong();
		this.datos	= p.getTokenString();
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("disparoEntidades"); 	sb.append(";");
		sb.append(tipo);		sb.append(";");
		sb.append(id);			sb.append(";");
		sb.append(datos); 		sb.append(";");
		return sb.toString();
	}
	public String getDatos() {
		return datos;
	}
	public void setDatos(String datos) {
		this.datos = datos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}