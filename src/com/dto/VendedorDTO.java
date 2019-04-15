package com.dto;


public class VendedorDTO  {
	public static final String ARCA = "PABLO ARC.";
	public static final String ARCA1 = "PABLO ARC.1";
	public static final String ARCA2 = "PABLO ARC.2";
	
	private Long		id;
	private String		codigo;
	private String		nombre;
	private int			utilizar;
	
	public VendedorDTO() {}
	
	public VendedorDTO(String vendedor) {
		Parser p = new Parser();
		p.parsear(vendedor);

		if(p.esNull())
			p.getTokenString();
		else
			this.id	= p.getTokenLong();

		if(p.esNull())
			p.getTokenString();
		else
			this.codigo = p.getTokenString();
		
		if(p.esNull())
			p.getTokenString();
		else
			this.nombre = p.getTokenString();
		
		this.utilizar = p.getTokenInteger();
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("vendedor"); 	sb.append(";");
		sb.append(id);			sb.append(";");
		sb.append(codigo);		sb.append(";");
		sb.append(nombre); 		sb.append(";");
		sb.append(utilizar); 	sb.append(";");
		
		return sb.toString();
	}
	public boolean esArca() {
		if(this.getCodigo().startsWith(VendedorDTO.ARCA)  ||
		   this.getCodigo().startsWith(VendedorDTO.ARCA1) ||
		   this.getCodigo().startsWith(VendedorDTO.ARCA2))
			return true;
		else
			return false;
	}
	public boolean esServina() {
		if(this.getCodigo().startsWith(VendedorDTO.ARCA)  ||
		   this.getCodigo().startsWith(VendedorDTO.ARCA1) ||
		   this.getCodigo().startsWith(VendedorDTO.ARCA2))
			return false;
		else
			return true;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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