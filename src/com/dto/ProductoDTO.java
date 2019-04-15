package com.dto;


public class ProductoDTO {
	private Long	id;
	private String	codigo;
	private String	descripcion;
	private boolean estado;
	
	public ProductoDTO() {}
	
	public ProductoDTO(String pp) {
		Parser p = new Parser();
		p.parsear(pp);

		this.id				= p.getTokenLong();
		this.codigo			= p.getTokenString();
		this.descripcion	= p.getTokenString();
		if(p.getTokenString().compareTo("true")==0)
			this.estado = true;
		else
			this.estado = false;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("producto"); 	sb.append(";");
		sb.append(id);			sb.append(";");
		sb.append(codigo);		sb.append(";");
		sb.append(descripcion); sb.append(";");
		sb.append(estado); 		sb.append(";");
	
		return sb.toString();
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}