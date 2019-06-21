package com.financiera.domain.bean;

import java.util.Date;

import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;


public class TemporalItauBean {

	public static Long secuencial = 0L;

	public static final String ACEPTADO = "AC";
	public static final String RECHAZADO = "RC";
	
	public static final String FALTA_FONDOS = "10";
	public static final String VUELTA_ATRAS_CAMARA = "31";
	public static final String DIA_NO_LABORABLE = "93";
	
	private String	cuitEmpresa;
	private int 	nroEnvio;
	private Date 	fechaGeneracionArchivo;
	
	private String  tipoDoc;
	private String  dni;					// 8
	private String	fecha;					// 8   fecha del disparo ddmmaaaa
	private String	importe;				// 10  8 enteros 2 decimales
	private String	estado;
	private String	codRechazo;				// 3   vacio

	private String tipoReg;
	private String tipoFile;
	
	public void setSecuencial(Long sec) {
		this.secuencial = sec * 10000;
	}
	public boolean isAceptado() {
		if(this.estado.compareTo(ACEPTADO)==0)
			return true;
		else
			return false;
	}
	public boolean isRechazado() {
		if(this.estado.compareTo(RECHAZADO)==0)
			return true;
		else
			return false;
	}
	public String toStringHeader() {
		StringBuffer sb = new StringBuffer();
		sb.append("H");
		sb.append(Util.formateo(Util.NUMERICO, 11, cuitEmpresa, false));
		sb.append("300");
		sb.append("000001");
		sb.append(Util.formateo(Util.NUMERICO, 5, nroEnvio, false));
		sb.append(DateTimeUtil.formatDateAAAAMMDD(fechaGeneracionArchivo));
		sb.append(" BD");
		sb.append(Util.formateo(Util.TEXTO, 763, new String(" "), false));
		return sb.toString();
	}
	public String toStringDetalle(MovimientoBean m) {
		StringBuffer sb = new StringBuffer();
		sb.append("DAFC");
		sb.append(Util.formateo(Util.TEXTO, 7, new String(" "), false));
		sb.append(Util.formateo(Util.NUMERICO, 8, secuencial++, false));
		sb.append(Util.formateo(Util.TEXTO, 7, new String(" "), false));
		sb.append("000");
		sb.append(Util.formateo(Util.TEXTO, 14, new String(" "), false)); 
		sb.append(Util.formateo(Util.NUMERICO, 8, m.getCliente().getNroDoc(), false));    // DNI
		sb.append(Util.formateo(Util.TEXTO, 60, m.getCliente().getNyA(), false));
		sb.append("DU"); // siempre DNI
		sb.append(Util.formateo(Util.NUMERICO, 11, m.getCliente().getNroDoc(), false));   // DNI
		sb.append(Util.formateo(Util.TEXTO, 41, new String(" "), false));
		sb.append(Util.formateo(Util.NUMERICO, 49, new String("0"), false));
		sb.append(DateTimeUtil.formatDateAAAAMMDD(m.getFecha()));                         // Fecha
		sb.append(Util.formateo(Util.NUMERICO, 17, m.getImporte(), true));                // Importe
		sb.append(Util.formateo(Util.NUMERICO, 96, new String("0"), false)); // fecha2Venc importe Porc, fecha3Venc importe Porc,
		sb.append(Util.formateo(Util.TEXTO, 24, new String(" "), false));
		sb.append(Util.formateo(Util.NUMERICO, 7, new String("0"), false));
		sb.append(Util.formateo(Util.TEXTO, 22, m.getCliente().getCbu(), false));         // CBU
		sb.append(Util.formateo(Util.NUMERICO, 52, new String("0"), false));
		sb.append(Util.formateo(Util.TEXTO, 168, new String(" "), false));
		sb.append(Util.formateo(Util.NUMERICO, 6, new String("0"), false)); // Numero
		sb.append(Util.formateo(Util.TEXTO, 12, new String(" "), false));
		
		//CP de 8
		sb.append(Util.formateo(Util.TEXTO, 1, new String(" "), false));
		sb.append(Util.formateo(Util.NUMERICO, 4, new String("0"), false));
		sb.append(Util.formateo(Util.TEXTO, 3, new String(" "), false));
		
		sb.append(Util.formateo(Util.TEXTO, 166, new String(" "), false));
		return sb.toString();
	}

	public String toStringTotal(double itauImporteTotal, int itauCantRegistros) {
		StringBuffer sb = new StringBuffer();
		sb.append("T");
		sb.append(Util.formateo(Util.NUMERICO, 11, cuitEmpresa, false));
		sb.append("300");
		sb.append("000001");
		sb.append(Util.formateo(Util.NUMERICO, 5, nroEnvio, false));
		sb.append(DateTimeUtil.formatDateAAAAMMDD(fechaGeneracionArchivo));
		sb.append(Util.formateo(Util.NUMERICO, 5, new String("0"), false));
		sb.append(Util.formateo(Util.NUMERICO, 17, itauImporteTotal, true));
		sb.append(Util.formateo(Util.NUMERICO, 9, itauCantRegistros, false));
		sb.append(Util.formateo(Util.TEXTO, 735, new String(" "), false));
		return sb.toString();
	}
	public void setTemporalItauBeanHeader(String linea) {
		this.tipoReg	= linea.substring(39, 40);
		this.tipoFile	= linea.substring(40, 42);
	}
	public boolean esTipoRegH() {
		if(this.tipoReg.compareTo("H") == 0)
			return true;
		else
			return false;
	}
	public boolean esTipoRegI() {
		if(this.tipoReg.compareTo("I") == 0)
			return true;
		else
			return false;
	}
	public boolean esTipoRegT() {
		if(this.tipoReg.compareTo("T") == 0)
			return true;
		else
			return false;
	}
	public boolean esTipoFileNO() {
		if(this.tipoFile.compareTo("NO") == 0)
			return true;
		else
			return false;
	}
	public void setTemporalItauBean(String linea) {
		this.tipoReg	= linea.substring(39, 40);
		this.tipoDoc	= linea.substring(62, 64);
		this.dni		= linea.substring(67, 75);
		this.fecha		= linea.substring(86, 94);
		this.importe	= linea.substring(149, 166);
		this.estado		= linea.substring(255, 257);
		String a = linea.substring(277, 279);
		if(a.compareTo(new String("  "))==0)
			this.codRechazo	= a;
		else
			this.codRechazo	= new String("R" + linea.substring(277, 279));
	}
	public String getCuitEmpresa() {
		return cuitEmpresa;
	}
	public void setCuitEmpresa(String cuitEmpresa) {
		this.cuitEmpresa = cuitEmpresa;
	}
	public int getNroEnvio() {
		return nroEnvio;
	}
	public void setNroEnvio(int nroEnvio) {
		this.nroEnvio = nroEnvio;
	}
	public Date getFechaGeneracionArchivo() {
		return fechaGeneracionArchivo;
	}
	public void setFechaGeneracionArchivo(Date fechaGeneracionArchivo) {
		this.fechaGeneracionArchivo = fechaGeneracionArchivo;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getCodRechazo() {
		return codRechazo;
	}
	public void setCodRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoReg() {
		return tipoReg;
	}
	public void setTipoReg(String tipoReg) {
		this.tipoReg = tipoReg;
	}
	public String getTipoFile() {
		return tipoFile;
	}
	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}
}