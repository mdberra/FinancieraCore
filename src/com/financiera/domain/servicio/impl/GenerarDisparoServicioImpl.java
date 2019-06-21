package com.financiera.domain.servicio.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.server.ServiceNegocioLocator;
import com.financiera.core.service.ArchivoService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.servicio.CtacteServicio;
import com.financiera.domain.servicio.DisparoServicio;
import com.financiera.domain.servicio.GenerarDisparoServicio;

public class GenerarDisparoServicioImpl extends AbstractService implements GenerarDisparoServicio {
	private ClienteService    cliente    = (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	private ArchivoService    archivo    = (ArchivoService)    ServiceLocator.getInstance().getService(ArchivoService.class);
	private DelegacionService delegacion = (DelegacionService) ServiceLocator.getInstance().getService(DelegacionService.class);
	private ServicioService   servicios  = (ServicioService)   ServiceLocator.getInstance().getService(ServicioService.class);
/**
 * El cliente y el servicio deben estar ACTIVOS
 * Solo pueden venir las delegaciones que se disparan por el Bapro o SVille
 * 
 * fechaMesDisparo ddmmAAAA
 */
	public String[] dispararBaproSVille(String fechaMesDisparo, boolean validarMovExistente, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		String[] salida = this.guardianClienteSinServicio(sesion);
		if(salida.length > 0) {
			return salida;
		}
		
//		CtacteServicio ctacte = (CtacteServicio)ServiceNegocioLocator.getInstance().getService(CtacteServicio.class);
//		try {
//			ctacte.generarCtacteCuota(DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate(fechaMesDisparo)));
//			ctacte.guardian38();
//		}
//		catch(Exception e) {
//			return null;
//		}
		Collection<Long> cDelegacion = new ArrayList<Long>();
		Collection<Long> cCliente = new ArrayList<Long>();
		DisparoEntidadesBean deBean;
		Iterator it = delegacion.getDisparoEntidadesBaproSVille(sesion).iterator();
		while(it.hasNext()) {
			deBean = (DisparoEntidadesBean)it.next();
			if(deBean.esDelegacion()) {
				cDelegacion.add(deBean.getIdentificador());
			}
			if(deBean.esCliente()) {
				cCliente.add(deBean.getIdentificador());
			}
		}
		return this.dispararBaproSVille(fechaMesDisparo, cDelegacion, cCliente, validarMovExistente, sesion, usuario);
	}
/**
 * Cliente y Servicio ACTIVO
 * Aquellos clientes que tengan la delegacion
 * cuyo banco tenga el ente recaudador NACION
 * y  tengan Caja de ahorro deben salir por aca
 * Si no tiene Caja de ahorro no sale por ningun lado.
 * 
 * fechaMesDisparo dd/mm/AAAA
 * Periodo
 * fechaTope
 * NroDisquette
 */
	public String[] dispararNacion(String fechaMesDisparo, String fechaTope, String nroDisquette, boolean validarMovExistente, String usuario) {
		System.out.println("dispararNacion - validarMovExistente: " + validarMovExistente);
		
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		String[] salida = this.guardianClienteSinServicio(sesion);
		if(salida.length > 0) {
			return salida;
		}
		CtacteServicio ctacte = (CtacteServicio)ServiceNegocioLocator.getInstance().getService(CtacteServicio.class);
		Collection<Long> cDelegacion = new ArrayList<Long>();
		Collection<Long> cCliente = new ArrayList<Long>();
		DisparoEntidadesBean deBean;
		String mes;
		List<ServicioBean> serviciosAFact = null;
		try {
			ctacte.guardian38(sesion);
			String fecha = DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate(fechaMesDisparo));
			ctacte.generarCtacteCuota(fecha, usuario);
			fecha = DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate(fechaTope));
			mes = new String(fecha.substring(4, 6));
			Iterator it = delegacion.getDisparoEntidadesNacion(sesion).iterator();
			while(it.hasNext()) {
				deBean = (DisparoEntidadesBean)it.next();
				if(deBean.esDelegacion()) {
					cDelegacion.add(deBean.getIdentificador());
				}
				if(deBean.esCliente()) {
					cCliente.add(deBean.getIdentificador());
				}
			}
			serviciosAFact = this.armarDelegaciones(cDelegacion, cCliente, sesion);
		}
		catch(Exception e) {
			return null;
		}				
		return this.dispararNacion(mes ,nroDisquette, fechaTope, fechaMesDisparo, serviciosAFact, validarMovExistente, sesion, usuario);
	}
	public String[] guardianClienteSinServicio(Session sesion) {
		Collection<String> salida = new ArrayList<String>();
		ClienteBean c = null;
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ClienteBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				c = (ClienteBean)cI.next();
				if(c.isActivo()) {
					Iterator sI = c.getServicios().iterator();
					if(!sI.hasNext()) {
						salida.add("Cliente activo sin Servicios: " + c.getNyA() + " DNI: " + c.getNroDoc());
					}
				}
			}
		} catch(Exception e) {
			System.out.println("GuardianServicio : " + e.getMessage());
		}
		
		String[] ss = new String[salida.size()];
		int i = 0;
		Iterator<String> it = salida.iterator();
		while(it.hasNext()) {
			ss[i++] = (String)it.next();
		}
		return ss;
	}
/****************************************************************************************/
	private String[] dispararBaproSVille(String fechaMesDisparo, Collection<Long> delegacionesId, Collection<Long> clientesId, boolean validarMovExistente, Session sesion, String usuario) {
		Collection<String> salida = new ArrayList<String>();
		DisparoServicio disparo = (DisparoServicio)ServiceNegocioLocator.getInstance().getService(DisparoServicio.class);
		try {
			sesion = CacheManager.getInstance().restart(usuario);

			salida.add(DateTimeUtil.getStringFormatDateTime() + " Comenzar - Buscar Servicios");
			List<ServicioBean> serviciosAFact = this.armarDelegaciones(delegacionesId, clientesId, sesion);
			
			Date fechaReferencia = DateTimeUtil.getDate(fechaMesDisparo);
			String periodo = DateTimeUtil.getPeriodoDD_MM_AAAA(fechaReferencia);
			String periodoAnterior = DateTimeUtil.Sub1Periodo(periodo);

			String diaHoy = DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate());
			String diaMesHoy = DateTimeUtil.formatDateDDMM(DateTimeUtil.getDate());
			String baproFileName = "C:\\Servina\\Disparos\\EBT_SERVINA_" + diaHoy + ".TXT";
			String superVilleFileName = "C:\\Servina\\Disparos\\DSERV" + diaMesHoy + ".001";
			
			String itauFileName = "C:\\Servina\\Disparos\\ITAU_" + diaHoy + ".TXT";
//			String itauFileName = "//home//marce//Liberdina//Servina//Disparos//ITAU_" + diaHoy + ".TXT";

			String bicaFileName = "C:\\Servina\\Disparos\\BICA_" + diaHoy + ".TXT";
			PrintWriter printerBapro = archivo.abrirFileSalida(baproFileName);
			PrintWriter printerSVille = archivo.abrirFileSalida(superVilleFileName);
			PrintWriter printerItau = archivo.abrirFileSalida(itauFileName);
			PrintWriter printerBica = archivo.abrirFileSalida(bicaFileName);
						
// fechaReferencia ultimo dia del mes que se usa como fecha tope para filtrar los movimientos
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Servicios: " + serviciosAFact.size() + " Generar Movimientos");
			salida.addAll(disparo.generarMovimientoDisparo(serviciosAFact, fechaReferencia, movimiento, validarMovExistente, sesion));
			
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Generar Disparo");
			salida.addAll(disparo.generarDisparoBaproSVilleItauBica(serviciosAFact, periodo, periodoAnterior, DateTimeUtil.getDate(), printerBapro, printerSVille, printerItau, printerBica, sesion));
			
			archivo.eliminarFile(superVilleFileName);
			
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Terminar");
		} catch(Exception e) {
			salida.add("Error al calcular el periodo anterior");
		}
		String[] ss = new String[salida.size()];
		int i = 0;
		Iterator<String> it = salida.iterator();
		while(it.hasNext()) {
			ss[i++] = (String)it.next();
		}
		return ss;
	}
	private List<ServicioBean> armarDelegaciones(Collection<Long> delegacionesId, Collection<Long> clientesId, Session sesion) throws Exception {
		List<ServicioBean> serviciosAFact = new ArrayList<ServicioBean>();
		
		Collection<DelegacionBean> delegaciones= new ArrayList<DelegacionBean>();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Iterator<Long> i = delegacionesId.iterator();
			while(i.hasNext()) {
				Long id = (Long)i.next();
				delegaciones.add((DelegacionBean)sesion.get(DelegacionBean.class, id));
			}
		} catch(Exception e) {
			throw new Exception("La delegacion no pudo ingresarse para el disparo");
		}
//		 Obtengo todos los clientes de las delegaciones
		List<ClienteBean> clientes = cliente.getClienteFindDelegacion(delegaciones, sesion);
		List<ClienteBean> clientesAFact = new ArrayList<ClienteBean>();
		
		ClienteBean c;
//solo tomo los clientes en estado ACTIVO
		
		Iterator i = clientes.iterator();
		while(i.hasNext()) {
			c = (ClienteBean) i.next();
			if(c.getEstado() == ClienteBean.ACTIVO) {
				clientesAFact.add(c);
			}
		}
//agrego los clientes
		if(clientesId != null) {
			i = clientesId.iterator();
			while(i.hasNext()) {
				Long id = (Long)i.next();
				c = cliente.getClienteById(id, sesion);
				if(!clientesAFact.contains(c)) {
					clientesAFact.add(c);
				}
			}
		}
//filtro los servicios APROBADOS
		
		i = clientesAFact.iterator();
		while(i.hasNext()) {
			c = (ClienteBean) i.next();
			Iterator j = c.getServicios().iterator();
			while(j.hasNext()) {
				ServicioBean serv = (ServicioBean) j.next();
				if(serv.isAprobado()) {
					serviciosAFact.add(serv);
				}
			}
		}
		return serviciosAFact;
	}
	
	private String[] dispararNacion(String mes, String nroDisquette, String fechaTope, String fechaMesDisparo, List<ServicioBean> serviciosAFact, boolean validarMovExistente, Session sesion, String usuario) {
		Collection<String> salida = new ArrayList<String>();
		
		DisparoServicio disparo = (DisparoServicio)ServiceNegocioLocator.getInstance().getService(DisparoServicio.class);
		try {
			sesion = CacheManager.getInstance().restart(usuario);
			Date fechaReferencia = DateTimeUtil.getDate(fechaMesDisparo);
			String periodo = DateTimeUtil.getPeriodoDD_MM_AAAA(fechaReferencia);
			String periodoAnterior = DateTimeUtil.Sub1Periodo(periodo);

			String diaHoy = DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate());
			String diaMesHoy = DateTimeUtil.formatDateDDMM(DateTimeUtil.getDate());
			String nacionFileName = "C:\\Servina\\Disparos\\SERVNA1E.txt";// + diaHoy;
			PrintWriter printerNacion = archivo.abrirFileSalida(nacionFileName);
			
//	fechaReferencia ultimo dia del mes que se usa como fecha tope para filtrar los movimientos
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Servicios: " + serviciosAFact.size() + " Generar Movimientos");
			salida.addAll(disparo.generarMovimientoDisparo(serviciosAFact, fechaReferencia, movimiento, validarMovExistente, sesion));
			
			sesion = CacheManager.getInstance().restart(usuario);
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Generar Movimientos por Interes");
			salida.addAll(disparo.generarMovimientoDisparoDupInteres(serviciosAFact, fechaReferencia, movimiento, sesion));
			
			sesion = CacheManager.getInstance().restart(usuario);
			servicios.actualizarDivididos(sesion);

			salida.add(DateTimeUtil.getStringFormatDateTime() + " Generar disparo");
			salida.addAll(disparo.generarDisparoNacion(mes, nroDisquette, fechaTope, 
									serviciosAFact, periodo, periodoAnterior, printerNacion, sesion));
			salida.add(DateTimeUtil.getStringFormatDateTime() + " Terminar");
		} catch(Exception e) {
			salida.add("Error al calcular el periodo anterior");
		}
		String[] ss = new String[salida.size()];
		int i = 0;
		Iterator it = salida.iterator();
		while(it.hasNext()) {
			ss[i++] = (String)it.next();
		}
		return ss;
	}
}