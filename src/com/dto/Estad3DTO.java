package com.dto;

public class Estad3DTO {
	private String	cliente;
	private String  delegacion;
	private double	importeTotal;
	private int		nrodoc;
	private String	estado;
	private String	telefLineaParticular;
	private String	telefCelularParticular;
	private String	telefLineaLaboral;
	private String	telefCelularLaboral;
	private int 	debitos;
	private int 	reversion;
	private int 	devolucion;
	private int		rechazos;
	private int 	total;
	private double	importe;
	private String	fecha;
	
	private Parser p = new Parser();
	
	public Estad3DTO() {}
	
	public Estad3DTO(String s) {
		p.parsear(s);
		this.cliente	= p.getTokenString();
		this.delegacion = p.getTokenString();
		this.importeTotal = p.getTokenDouble();
		this.nrodoc		= p.getTokenInteger();
		this.estado		= p.getTokenString();
		this.telefLineaParticular	= p.getTokenString();
		this.telefCelularParticular	= p.getTokenString();
		this.telefLineaLaboral		= p.getTokenString();
		this.telefCelularLaboral	= p.getTokenString();
		this.debitos	= p.getTokenInteger();
		this.reversion	= p.getTokenInteger();
		this.devolucion	= p.getTokenInteger();
		this.rechazos	= p.getTokenInteger();
		this.total		= p.getTokenInteger();
		this.importe	= p.getTokenDouble();
		this.fecha		= p.getTokenString();
	}
	public String toString() {
		return new String("Estad3DTO;" + cliente + ";" + delegacion + ";" + importeTotal + ";" + nrodoc + ";" + estado + ";" + 
				telefLineaParticular + ";" + telefCelularParticular + ";" + telefLineaLaboral + ";" + telefCelularLaboral + ";" +
				debitos + ";" + reversion + ";" + devolucion + ";" + rechazos + ";" + total + ";" + importe + ";" + fecha);
	}
	public void calcularTotal() {
		this.total = this.debitos - this.reversion - this.devolucion;
	}
	public String getCliente() {
		return cliente;
	}
	public void incDebitos() {
		this.debitos += 1;
	}
	public void incReversion() {
		this.reversion += 1;
	}
	public void incDevolucion() {
		this.devolucion += 1;
	}
	public void incRechazos() {
		this.rechazos += 1;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getDebitos() {
		return debitos;
	}
	public void setDebitos(int debitos) {
		this.debitos = debitos;
	}
	public int getReversion() {
		return reversion;
	}
	public void setReversion(int reversion) {
		this.reversion = reversion;
	}
	public int getDevolucion() {
		return devolucion;
	}
	public void setDevolucion(int devolucion) {
		this.devolucion = devolucion;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getNrodoc() {
		return nrodoc;
	}
	public void setNrodoc(int nrodoc) {
		this.nrodoc = nrodoc;
	}
	public int getRechazos() {
		return rechazos;
	}
	public void setRechazos(int rechazos) {
		this.rechazos = rechazos;
	}
	public String getTelefLineaParticular() {
		return telefLineaParticular;
	}
	public void setTelefLineaParticular(String telefLineaParticular) {
		this.telefLineaParticular = telefLineaParticular;
	}
	public String getTelefCelularParticular() {
		return telefCelularParticular;
	}
	public void setTelefCelularParticular(String telefCelularParticular) {
		this.telefCelularParticular = telefCelularParticular;
	}
	public String getTelefLineaLaboral() {
		return telefLineaLaboral;
	}
	public void setTelefLineaLaboral(String telefLineaLaboral) {
		this.telefLineaLaboral = telefLineaLaboral;
	}
	public String getTelefCelularLaboral() {
		return telefCelularLaboral;
	}
	public void setTelefCelularLaboral(String telefCelularLaboral) {
		this.telefCelularLaboral = telefCelularLaboral;
	}
	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public String getDelegacion() {
		return delegacion;
	}
	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}