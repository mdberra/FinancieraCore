package com.financiera.domain.bean;

import java.util.StringTokenizer;

import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;


public class TemporalBicaBean {
	private String  idCliente;
	private String  idServicio;
	private String  dni;					// 8
	private String	fecha;					// 8   fecha del disparo ddmmaaaa
	private String	importe;				// 10  8 enteros 2 decimales
	private String	codRechazo;				// 3   vacio
	private String  debitada;				// debitada
	
	public String toStringDetalle(MovimientoBean m) {
		StringBuffer sb = new StringBuffer();
		sb.append(Util.formateo(Util.TEXTO, 22, m.getCliente().getCbu(), false));
		sb.append(";");
		sb.append(DateTimeUtil.formatDate(m.getFecha()));
		sb.append(";");
		sb.append(Util.formateo(Util.NUMERICO_SIN_ESP_CON_PUNTO, 0, m.getImporte(), true));
		sb.append(";");
		sb.append(Util.formateo(Util.NUMERICO, 11, m.getCliente().getNroDoc(), false));
		sb.append(";");
		sb.append(Util.formateo(Util.NUMERICO, 8, m.getIdServicio(), false));

		return sb.toString();
	}
	public void setTemporalBicaBean(String linea) {
		StringTokenizer tokenizer = new StringTokenizer(linea, ";");
	    while (tokenizer.hasMoreElements()) {
	        String t = tokenizer.nextToken();  		// CBU
	        this.importe = tokenizer.nextToken();  	// importe
	        this.idServicio = tokenizer.nextToken(); // id_servicio cambio de id_cliente
	        this.dni = tokenizer.nextToken();  		// DNI
	        this.fecha = tokenizer.nextToken();  	// Fecha Original
	        t = tokenizer.nextToken();  			// Fecha Compensacion
	        this.codRechazo = tokenizer.nextToken(); // Rechazo
	        this.debitada = tokenizer.nextToken();   // Debitada
	        if(this.debitada.compareTo("Debitado")==0) {
	        	this.codRechazo = "Deb";
	        }
	        if(this.debitada.compareTo("Enviada")==0) {
	        	this.codRechazo = "Env";
	        }
	        break;
	    }
	}
	
	public boolean esReversion() {
        if(this.debitada.compareTo("Reversa")==0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getDebitada() {
		return debitada;
	}
	public void setDebitada(String debitada) {
		this.debitada = debitada;
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
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(String idServicio) {
		this.idServicio = idServicio;
	}
}