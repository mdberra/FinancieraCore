package com.dto;

import java.util.Date;

public class RespuestaDTO {
	private int		idServicio;
	private int		dni;
	private String	cbu;
	private String  sucursalCA;
	private String  nroCA;
	private Date	fecha;
	private double	importe;
	private String	codRechazo;
	private boolean	conocido;
	private boolean reversion;
	private String	codigoInternoBanco;
	private Date 	fechaCobroReal;
	private String  textoError;
	
	public String toString(){
		return dni + " " + fecha + " " + importe + " " +  codRechazo + " " + textoError;
	}
	public String getCodigoInternoBanco() {
		return codigoInternoBanco;
	}
	public void setCodigoInternoBanco(String codigoInternoBanco) {
		this.codigoInternoBanco = codigoInternoBanco;
	}
	public String getCodRechazo() {
		return codRechazo;
	}
	public void setCodRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}	
	public int getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(int idServicio) {
		this.idServicio = idServicio;
	}
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		this.dni = dni;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	public boolean isConocido() {
		return conocido;
	}
	public void setConocido(boolean conocido) {
		this.conocido = conocido;
	}
	public boolean isReversion() {
		return reversion;
	}
	public void setReversion(boolean reversion) {
		this.reversion = reversion;
	}
	public Date getFechaCobroReal() {
		return fechaCobroReal;
	}
	public void setFechaCobroReal(Date fechaCobroReal) {
		this.fechaCobroReal = fechaCobroReal;
	}
	public String getTextoError() {
		return textoError;
	}
	public void setTextoError(String textoError) {
		this.textoError = textoError;
	}
	public String getCbu() {
		return cbu;
	}
	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	public String getSucursalCA() {
		return sucursalCA;
	}
	public void setSucursalCA(String sucursalCA) {
		this.sucursalCA = sucursalCA;
	}
	public String getNroCA() {
		return nroCA;
	}
	public void setNroCA(String nroCA) {
		this.nroCA = nroCA;
	}
}