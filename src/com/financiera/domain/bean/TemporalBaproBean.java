package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.Persistencia;


public class TemporalBaproBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long	id;
	private String	codigoInternoBanco;		// 2   BAPRO 71   Otros 51
	private String	fechaVencimiento;		// 8   fecha del disparo ddmmaaaa
	private String	codigoEmpresa;			// 6   siempre 000000
	private String	idCliente;				// 22  dni con formato 9(8)
	private String	tipoMoneda;				// 1   siempre P
	private String	cbu;					// 22
	private String	importe;				// 10  8 enteros 2 decimales
	private String	cuitEmpresa;			// 11
	private String	descripPrestacion;		// 10
	private String	referenciaUnivDebito;	// 15  es el indice
	private String	reservado;				// 15  vacio
	private String	nuevoIdCliente;			// 22  vacio
	private String	codRechazo;				// 3   vacio
	private String	nombreEmpresa;			// 10
	
	public boolean esConocido() {
		if(this.codigoInternoBanco.compareTo("52") == 0) {
			return true;
		}
		if(this.codigoInternoBanco.compareTo("72") == 0) {
			return true;
		}
		if(this.codigoInternoBanco.compareTo("82") == 0) {
			return true;
		}
		if(this.codigoInternoBanco.compareTo("55") == 0) {
			return true;
		}
		return false;
	}
	public boolean esReversion() {
		if(this.codigoInternoBanco.compareTo("55") == 0) {
			return true;
		}
		return false;
	}
	public String toStringBapro() {
		StringBuffer sb = new StringBuffer();
		sb.append(codigoInternoBanco);
		sb.append(fechaVencimiento);
		sb.append(codigoEmpresa);
		sb.append(idCliente);
		sb.append(tipoMoneda);
		sb.append(cbu);
		sb.append(importe);
		sb.append(cuitEmpresa);
		sb.append(descripPrestacion);
		sb.append(referenciaUnivDebito);
		sb.append(reservado);
		sb.append(nuevoIdCliente);
		sb.append(codRechazo);
		sb.append(nombreEmpresa);
		return sb.toString();
	}
	
	public void setTemporalBaproBean(String linea) {
		this.codigoInternoBanco		= linea.substring(0, 2);
		this.fechaVencimiento		= linea.substring(2, 10);
		this.codigoEmpresa			= linea.substring(10, 16);
		this.idCliente				= linea.substring(16, 24);
		this.tipoMoneda				= linea.substring(38, 39);
		this.cbu					= linea.substring(39, 61);
		this.importe				= linea.substring(61, 71);
		this.cuitEmpresa			= linea.substring(71, 82);
		this.descripPrestacion		= linea.substring(82, 92);
		this.referenciaUnivDebito	= linea.substring(92, 107);
		this.reservado				= linea.substring(107, 122);
		this.nuevoIdCliente			= linea.substring(122, 144);
		this.codRechazo				= linea.substring(144, 147);
		this.nombreEmpresa			= linea.substring(147, 163);
	}
	
	public static int clearTabla(Session session) {
		int deleteados = 0;
		Transaction transaction = session.beginTransaction();
		try {
			transaction.begin();
			Query q = session.getNamedQuery("TemporalBaproBean.getAllOrderCliente");
			Iterator tI = q.list().iterator();
			while(tI.hasNext()) {
				session.delete((TemporalBaproBean)tI.next());
				deleteados++;
			}
			session.flush();
			session.clear();
			Persistencia.clearSession();
		} catch (Exception ex) {
			System.out.println("Error al Deletear TemporalBaproBean: " + ex.getMessage());
		} finally {
			transaction.commit();
		}
		return deleteados;
	}
	
	public String getCbu() {
		return cbu;
	}
	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
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
	public String getCuitEmpresa() {
		return cuitEmpresa;
	}
	public void setCuitEmpresa(String cuitEmpresa) {
		this.cuitEmpresa = cuitEmpresa;
	}
	public String getDescripPrestacion() {
		return descripPrestacion;
	}
	public void setDescripPrestacion(String descripPrestacion) {
		this.descripPrestacion = descripPrestacion;
	}
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public String getNuevoIdCliente() {
		return nuevoIdCliente;
	}
	public void setNuevoIdCliente(String nuevoIdCliente) {
		this.nuevoIdCliente = nuevoIdCliente;
	}
	public String getReferenciaUnivDebito() {
		return referenciaUnivDebito;
	}
	public void setReferenciaUnivDebito(String referenciaUnivDebito) {
		this.referenciaUnivDebito = referenciaUnivDebito;
	}
	public String getReservado() {
		return reservado;
	}
	public void setReservado(String reservado) {
		this.reservado = reservado;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
}