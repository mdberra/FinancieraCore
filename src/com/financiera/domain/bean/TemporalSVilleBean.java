package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.Persistencia;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;


public class TemporalSVilleBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	private static int ii;
	
	private Long	id;
	private String	tipoDeNovedad;			// 1   Debito  Total
	private String	cuitEmpresa;			// 11
	private String	sector;					// 3   001
	private String	prestacion;				// 10  CUOTA
	private String	fechaVencimiento;		// 8   fecha del disparo ddmmaaaa
	private String	cbu1;					// 8
	private String	cbuRelleno;				// 3   000
	private String	cbu2;					// 14
	private String	idCliente;				// 22  dni con formato 9(8)
	private String	vtoDebOri;				// 8   00000000
	private String  refDebito;				// 15  COBRANZAS
	private String	importe;				// 10  8 enteros 2 decimales
	private String	tipoMoneda;				// 2   80 pesos
	private String	relleno;				// 36  000000000000000000000000000000000000
	private String	pagador;				// 22  vacio
	private String	codRechazo;				// 3   vacio
	private String	nroOrden;				// 10  0000000000
	private String	nroMovimiento;			// 10  0000000000
	private String	relleno1;				// 54  vacio
	
	private String  cantRegistroTotal;		// 10  0000000000
	private String  cantRegistroMonetario;	// 7   0000000
	private String  cantRegistroNoMonetario;// 7   siempre 0000000
	private String  fechaProceso;			// 8   fecha del disparo ddmmaaaa
	private String	relleno70;				// 70  vacio
	private String	totalImportes;			// 10  8 enteros 2 decimales
	private String	relleno137;				// 137  vacio

	public TemporalSVilleBean() {
		this.setSector("001");
		this.setPrestacion("CUOTA     ");
		this.setCbuRelleno("000");
		this.setVtoDebOri("00000000");
		if(ii>8)
			ii=0;
		else
			ii++;
		this.setRefDebito("COBRANZAS" + ii + "     ");
		this.setTipoMoneda("80");
		this.setRelleno("000000000000000000000000000000000000");
		this.setPagador("                      ");
		this.setCodRechazo("   ");
		this.setNroOrden("0000000000");
		this.setNroMovimiento("0000000000");
		this.setRelleno1("                                                      ");
		this.setCantRegistroNoMonetario("0000000");
		this.setFechaProceso(DateTimeUtil.formatDateDDMMAAAA(DateTimeUtil.getDate()));
		this.setRelleno70("                                                                      ");
		this.setRelleno137("                                                                                                                                         ");
	}

	public void setTipoNovedadDebito() {
		this.setTipoDeNovedad("D");
	}
	public void setTipoNovedadTotal() {
		this.setTipoDeNovedad("T");
	}
	public boolean esDebito() {
		if(this.getTipoDeNovedad().compareTo("D")==0)
			return true;
		else
			return false;
	}
	public void setearDetalle(ParametroBean parametro,MovimientoBean m) {
//		 pic 9(11)
		this.setCuitEmpresa(Util.formateo(Util.NUMERICO, 11, parametro.getCuitEmpresa(), false));
		this.setFechaVencimiento(DateTimeUtil.formatDateDDMMAAAA(m.getFecha()));

		String cbu = m.getCliente().getCbu();
		this.setCbu1(cbu.substring(0, 8));
		this.setCbu2(cbu.substring(8, 22));

//		 pic 9(8) + pic x(14) total 22
		String s = Util.formateo(Util.NUMERICO, 8, m.getCliente().getNroDoc(), false);
		s = Util.formateo(Util.TEXTO_NUM, 22, s, false);
		this.setIdCliente(s);
		
//		pic 9(10)
		this.setImporte(Util.formateo(Util.NUMERICO, 10, m.getImporte(), true));
	}
	public String toStringDetalle() {
		StringBuffer sb = new StringBuffer();
		sb.append(tipoDeNovedad);
		sb.append(cuitEmpresa);
		sb.append(sector);
		sb.append(prestacion);
		sb.append(fechaVencimiento);
		sb.append(cbu1);
		sb.append(cbuRelleno);
		sb.append(cbu2);
		sb.append(idCliente);
		sb.append(vtoDebOri);
		sb.append(refDebito);
		sb.append(importe);
		sb.append(tipoMoneda);
		sb.append(relleno);
		sb.append(pagador);
		sb.append(codRechazo);
		sb.append(nroOrden);
		sb.append(nroMovimiento);
		sb.append(relleno1);
		return sb.toString();
	}

	public void setearTotal(int svilleCantRegistros, double svilleImporteTotal) {
		this.setCantRegistroTotal(Util.formateo(Util.NUMERICO, 10, svilleCantRegistros, false));
		this.setCantRegistroMonetario(Util.formateo(Util.NUMERICO, 7, svilleCantRegistros, false));
		this.setTotalImportes(Util.formateo(Util.NUMERICO, 10, svilleImporteTotal, true));
	}
	public String toStringTotal() {
		StringBuffer sb = new StringBuffer();
		sb.append(tipoDeNovedad);
		sb.append(cantRegistroTotal);
		sb.append(cantRegistroMonetario);
		sb.append(cantRegistroNoMonetario);
		sb.append(fechaProceso);
		sb.append(relleno70);
		sb.append(totalImportes);
		sb.append(relleno137);
		return sb.toString();
	}

	public void setTemporalSuperVilleBean(String linea) {
		this.tipoDeNovedad		= linea.substring(0, 1);
		this.cuitEmpresa		= linea.substring(1, 12);
		this.sector				= linea.substring(12, 15);
		this.prestacion			= linea.substring(15, 25);
		this.fechaVencimiento	= linea.substring(25, 33);
		this.cbu1				= linea.substring(33, 41);
		this.cbuRelleno			= linea.substring(41, 44);
		this.cbu2				= linea.substring(44, 58);
		this.idCliente			= linea.substring(58, 80);
		this.vtoDebOri			= linea.substring(80, 88);
		this.refDebito			= linea.substring(88, 103);
		this.importe			= linea.substring(103, 113);
		this.tipoMoneda			= linea.substring(113, 115);
		this.relleno			= linea.substring(115, 151);
		this.pagador			= linea.substring(151, 173);
		this.codRechazo			= linea.substring(173, 176);
		this.nroOrden			= linea.substring(176, 186);
		this.nroMovimiento		= linea.substring(186, 196);
		this.relleno1			= linea.substring(196, 250);
	}
	
	public static int clearTabla(Session session) {
		int deleteados = 0;
		Transaction transaction = session.beginTransaction();
		try {
			transaction.begin();
			Query q = session.getNamedQuery("TemporalSVilleBean.getAllOrderCliente");
			Iterator tI = q.list().iterator();
			while(tI.hasNext()) {
				session.delete((TemporalSVilleBean)tI.next());
				deleteados++;
			}
			session.flush();
			session.clear();
			Persistencia.clearSession();
		} catch (Exception ex) {
			System.out.println("Error al Deletear TemporalSVilleBean: " + ex.getMessage());
		} finally {
			transaction.commit();
		}
		return deleteados;
	}

	public String getCbu1() {
		return cbu1;
	}
	public void setCbu1(String cbu1) {
		this.cbu1 = cbu1;
	}
	public String getCbu2() {
		return cbu2;
	}
	public void setCbu2(String cbu2) {
		this.cbu2 = cbu2;
	}
	public String getCbuRelleno() {
		return cbuRelleno;
	}
	public void setCbuRelleno(String cbuRelleno) {
		this.cbuRelleno = cbuRelleno;
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
	public String getNroMovimiento() {
		return nroMovimiento;
	}
	public void setNroMovimiento(String nroMovimiento) {
		this.nroMovimiento = nroMovimiento;
	}
	public String getNroOrden() {
		return nroOrden;
	}
	public void setNroOrden(String nroOrden) {
		this.nroOrden = nroOrden;
	}
	public String getPagador() {
		return pagador;
	}
	public void setPagador(String pagador) {
		this.pagador = pagador;
	}
	public String getPrestacion() {
		return prestacion;
	}
	public void setPrestacion(String prestacion) {
		this.prestacion = prestacion;
	}
	public String getRefDebito() {
		return refDebito;
	}
	public void setRefDebito(String refDebito) {
		this.refDebito = refDebito;
	}
	public String getRelleno() {
		return relleno;
	}
	public void setRelleno(String relleno) {
		this.relleno = relleno;
	}
	public String getRelleno1() {
		return relleno1;
	}
	public void setRelleno1(String relleno1) {
		this.relleno1 = relleno1;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getTipoDeNovedad() {
		return tipoDeNovedad;
	}
	public void setTipoDeNovedad(String tipoDeNovedad) {
		this.tipoDeNovedad = tipoDeNovedad;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getVtoDebOri() {
		return vtoDebOri;
	}
	public void setVtoDebOri(String vtoDebOri) {
		this.vtoDebOri = vtoDebOri;
	}
	public String getCantRegistroMonetario() {
		return cantRegistroMonetario;
	}
	public void setCantRegistroMonetario(String cantRegistroMonetario) {
		this.cantRegistroMonetario = cantRegistroMonetario;
	}
	public String getCantRegistroNoMonetario() {
		return cantRegistroNoMonetario;
	}
	public void setCantRegistroNoMonetario(String cantRegistroNoMonetario) {
		this.cantRegistroNoMonetario = cantRegistroNoMonetario;
	}
	public String getCantRegistroTotal() {
		return cantRegistroTotal;
	}
	public void setCantRegistroTotal(String cantRegistroTotal) {
		this.cantRegistroTotal = cantRegistroTotal;
	}
	public String getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getRelleno137() {
		return relleno137;
	}
	public void setRelleno137(String relleno137) {
		this.relleno137 = relleno137;
	}
	public String getRelleno70() {
		return relleno70;
	}
	public void setRelleno70(String relleno70) {
		this.relleno70 = relleno70;
	}
	public String getTotalImportes() {
		return totalImportes;
	}
	public void setTotalImportes(String totalImportes) {
		this.totalImportes = totalImportes;
	}
}