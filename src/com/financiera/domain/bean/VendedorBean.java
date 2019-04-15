package com.financiera.domain.bean;

import java.io.Serializable;

import com.dto.VendedorDTO;


public class VendedorBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int K_UTILIZAR = 0;
	public static final int K_NO_UTILIZAR = 1;

	private Long		id;
	private String		codigo;
	private String		nombre;
	private int			utilizar;
	
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("vendedor"); 	sb.append(";");
			sb.append(id);			sb.append(";");
			sb.append(codigo);		sb.append(";");
			sb.append(nombre); 		sb.append(";");
			sb.append(utilizar);	sb.append(";");
		} catch(Exception e) {
			throw new Exception("VendedorBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public int getVendedorArca() {
		if(this.getCodigo().compareTo(VendedorDTO.ARCA)==0) {
			return 0;
		} else {
			if(this.getCodigo().compareTo(VendedorDTO.ARCA1)==0) {
				return 1;
			} else {
				if(this.getCodigo().compareTo(VendedorDTO.ARCA2)==0) {
					return 2;
				} else {
					return 9;
				}
			}
		}
	}
	public boolean vendedorArca() {
		if(this.getVendedorArca() == 9)
			return false;
		else
			return true;
	}
	public boolean usar() {
		boolean usar = true;
		
		switch(this.getUtilizar()) {
		case K_UTILIZAR:    usar = true; break;
		case K_NO_UTILIZAR: usar = false; break;
		}
		return usar;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getUtilizar() {
		return utilizar;
	}
	public void setUtilizar(int utilizar) {
		this.utilizar = utilizar;
	}
}