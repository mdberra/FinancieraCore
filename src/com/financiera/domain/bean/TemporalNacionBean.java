package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.Persistencia;


public class TemporalNacionBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String CUENTA_BLOQUEADA = "CUENTA BLOQUEADA              ";
	public static final String MONTO_SUP_DISP   = "MONTO SUP. AL DISPONIBLE      "; 
	public static final String CUENTA_CERRADA   = "CUENTA CERRADA                ";
	public static final String CUENTA_INACTIVA  = "CUENTA INACTIVA               ";
	public static final String CLIENTE_BAJA     = "CLIENTE DADO DE BAJA          ";
	
	private Long	id;
	private String	relleno;
	
	private String	cajaAhorroEmpresa;		// 16
	private String	mes;					// 2  
	private String  nroDisquette;			// 2
	private String	fechaTope;				// 8  fecha hasta cuando ejecuta los debitos aaaammdd
	private String  sucursalCA;				// 4
	private String  nroCA;					// 10 
	private String  importe;				// 15
	private String  cantRegistros;			// 6
	private String  codRetorno;
	private String	textoError;
	private String  fechaCobro;

	public String toStringHeader() {
		StringBuffer sb = new StringBuffer();
		sb.append("1");
		sb.append(cajaAhorroEmpresa);
		sb.append("PE");
		sb.append(mes);
		sb.append(nroDisquette);
		sb.append(fechaTope);
		sb.append("REE");
		relleno = new String("          " + "          " + "          " +
				"          " + "          " + "          " +
				"          " + "          " + "          " + "    ");
		sb.append(relleno); //94
		return sb.toString();
	}
	public String getFechaTope(String linea) {
		return new String(linea.substring(23, 31));
	}
	public String toStringDetalle() {
		StringBuffer sb = new StringBuffer();
		sb.append("2");
		sb.append(sucursalCA);
		sb.append("CA0");
		sb.append(nroCA);
		sb.append(importe);
		sb.append("000000000");
		relleno = new String("          " + "          " + "          " +
				"          " + "          " + "          " +
				"          " + "          " + "      ");
		sb.append(relleno); //86
		return sb.toString();
	}

	public String toStringTotal() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("3");
		sb.append(importe);
		sb.append(cantRegistros);
		sb.append("000000000000000000000");
		relleno = new String("          " + "          " + "          " +
				"          " + "          " + "          " +
				"          " + "          " + "     ");
		sb.append(relleno); //85
		
		return sb.toString();
	}
	public void setTemporalNacionBean(String linea) {
		this.sucursalCA		= linea.substring(1, 5);
		this.nroCA		    = linea.substring(8, 18);
		this.importe		= linea.substring(18, 33);
		this.fechaCobro		= linea.substring(33, 41);
		this.codRetorno		= linea.substring(41, 42);
		this.textoError		= linea.substring(42, 72);
	}
	public static int clearTabla(Session session) {
		int deleteados = 0;
		Transaction transaction = session.beginTransaction();
		try {
			transaction.begin();
			Query q = session.getNamedQuery("TemporalNacionBean.getAllOrderCliente");
			Iterator tI = q.list().iterator();
			while(tI.hasNext()) {
				session.delete((TemporalNacionBean)tI.next());
				deleteados++;
			}
			session.flush();
			session.clear();
			Persistencia.clearSession();
		} catch (Exception ex) {
			System.out.println("Error al Deletear TemporalNacionBean: " + ex.getMessage());
		} finally {
			transaction.commit();
		}
		return deleteados;
	}
	public String obtCodRechazo() {
		String ret = null;

// fue cobrado
		if(this.codRetorno.compareTo("0") == 0) {
			ret = new String();
		} else {
			if(this.getTextoError().compareTo(TemporalNacionBean.CUENTA_BLOQUEADA)==0)
				ret = new String("R08");
			else
			if(this.getTextoError().compareTo(TemporalNacionBean.MONTO_SUP_DISP)==0)
				ret = new String("R10");
			else
			if(this.getTextoError().compareTo(TemporalNacionBean.CUENTA_CERRADA)==0)
				ret = new String("R15");
			else
			if(this.getTextoError().compareTo(TemporalNacionBean.CUENTA_INACTIVA)==0)
				ret = new String("R15");
			else
			if(this.getTextoError().compareTo(TemporalNacionBean.CLIENTE_BAJA)==0)
				ret = new String("R15");
			else
				ret = new String("R99");
		}
		return ret;
	}

	
	public String getCajaAhorroEmpresa() {
		return cajaAhorroEmpresa;
	}
	public void setCajaAhorroEmpresa(String cajaAhorroEmpresa) {
		this.cajaAhorroEmpresa = cajaAhorroEmpresa;
	}
	public String getCantRegistros() {
		return cantRegistros;
	}
	public void setCantRegistros(String cantRegistros) {
		this.cantRegistros = cantRegistros;
	}
	public String getFechaTope() {
		return fechaTope;
	}
	public void setFechaTope(String fechaTope) {
		this.fechaTope = fechaTope;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getNroCA() {
		return nroCA;
	}
	public void setNroCA(String nroCA) {
		this.nroCA = nroCA;
	}
	public String getNroDisquette() {
		return nroDisquette;
	}
	public void setNroDisquette(String nroDisquette) {
		this.nroDisquette = nroDisquette;
	}
	public String getRelleno() {
		return relleno;
	}
	public void setRelleno(String relleno) {
		this.relleno = relleno;
	}
	public String getSucursalCA() {
		return sucursalCA;
	}
	public void setSucursalCA(String sucursalCA) {
		this.sucursalCA = sucursalCA;
	}
	public String getCodRetorno() {
		return codRetorno;
	}
	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}
	public String getFechaCobro() {
		return fechaCobro;
	}
	public void setFechaCobro(String fechaCobro) {
		this.fechaCobro = fechaCobro;
	}
	public String getTextoError() {
		return textoError;
	}
	public void setTextoError(String textoError) {
		this.textoError = textoError;
	}
}