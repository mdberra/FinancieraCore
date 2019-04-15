package com.financiera.domain.bean;

import java.io.Serializable;

import org.hibernate.Session;


public class ProductoBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long	id;
	private String	codigo;
	private String	descripcion;
	private boolean estado;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("producto"); 	sb.append(";");
			sb.append(id);			sb.append(";");
			sb.append(codigo);		sb.append(";");
			sb.append(descripcion); sb.append(";");
			sb.append(estado); 		sb.append(";");
		} catch(Exception e) {
			throw new Exception("ProductoBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public static ProductoBean getProducto(Session s, Long id) {
		return (ProductoBean)s.get(ProductoBean.class, id);
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
