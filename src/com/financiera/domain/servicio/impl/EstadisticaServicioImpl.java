package com.financiera.domain.servicio.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ClienteDTO;
import com.dto.Estad1DTO;
import com.dto.Estad2DTO;
import com.dto.Estad3DTO;
import com.dto.Estad4DTO;
import com.dto.ServicioDTO;
import com.dto.TotalesDesgloseDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.service.ArchivoService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.servicio.EstadisticaServicio;

public class EstadisticaServicioImpl extends AbstractService implements EstadisticaServicio {
	private ArchivoService  archivo  = (ArchivoService)    ServiceLocator.getInstance().getService(ArchivoService.class);
	private ClienteService  cliente  = (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);
	private ServicioService servicio = (ServicioService) ServiceLocator.getInstance().getService(ServicioService.class);
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	
	private Long idCliente = 0l;
	private Long idServicio = 0l;
	private ClienteBean c = null;
	private MovimientoBean m = null;
	private ServicioBean servicioBean = null;
	
	public EstadisticaServicioImpl(){
		super();
		this.setDescription("Servicio para Estadistica");
		this.setName("EstadisticaServicio");
	}
	public Collection<String> rankingCobros(String cantCuotasCobradas, String idDelegacion, String usuario) {
		Estad3DTO eDTO = new Estad3DTO();
		Collection<String> salida = new ArrayList<String>();
		Collection<MovimientoBean> mc = new ArrayList<MovimientoBean>();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			Hashtable<Long, ServicioBean> v  = servicio.getServiciosAprobados(sesion);
			Date fecha1 = DateTimeUtil.getDateAAAAbMMbDD("2000-01-01");
			Date fecha2 = DateTimeUtil.getDate();
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByMovConRespuesta");
			q.setDate("fecha1", fecha1);
			q.setDate("fecha2", fecha2);
			Iterator<MovimientoBean> mI = q.list().iterator();
			while(mI.hasNext()) {
				m = (MovimientoBean)mI.next();
				if(v.containsKey(m.getIdServicio())) {
					c = m.getCliente();
					if(c.isActivo()) {
						if(idDelegacion.compareTo("0")==0) {
							mc.add(m);
						} else {
							if(c.getDelegacion().getId().compareTo(new Long(idDelegacion))==0) {
								mc.add(m);
							}
						}
					}
				}
			}
			Vector<Long> serviciosArca = null;
			boolean esArca = false;
			if(usuario.compareTo("Emiliano Arca")== 0) {
				esArca = true;
			}
			serviciosArca = servicio.getServiciosArcaId(sesion);

			mI = mc.iterator();
			while(mI.hasNext()) {
				m = (MovimientoBean)mI.next();
//				if(m.getCliente().getNroDoc() == 30541339) {
//					System.out.println(m.getCliente().getNroDoc());
//				}
				if(esArca) {
					if(!serviciosArca.contains(m.getIdServicio()))
						continue;
				}else{
					if(serviciosArca.contains(m.getIdServicio()))
						continue;
				}
				if(m.getIdCliente().equals(idCliente)   &&   m.getIdServicio().equals(idServicio)) {
				} else {
					idCliente = m.getIdCliente();
					idServicio = m.getIdServicio();
					c = m.getCliente();
					eDTO.calcularTotal();
					if(eDTO.getTotal() > Integer.parseInt(cantCuotasCobradas)) {
						salida.add(eDTO.toString());
					}
					eDTO = new Estad3DTO();
					eDTO.setDelegacion(c.getDelegacion().getDescripcion());
					eDTO.setCliente(c.getNyA());
					servicioBean = v.get(idServicio);
					eDTO.setImporteTotal(servicioBean.getImporteTotal());
					eDTO.setNrodoc(c.getNroDoc());
					eDTO.setEstado(c.getEstadoString());
					eDTO.setTelefLineaParticular(c.getLocParticular().getTelefLinea());
					eDTO.setTelefCelularParticular(c.getLocParticular().getTelefCelular());
					eDTO.setTelefLineaLaboral(c.getLocLaboral().getTelefLinea());
					eDTO.setTelefCelularLaboral(c.getLocLaboral().getTelefCelular());
				}
				this.acumular3DTO(m, eDTO);
			}
			eDTO.calcularTotal();
			if(eDTO.getTotal() > Integer.parseInt(cantCuotasCobradas)) {
				salida.add(eDTO.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return salida;
	}
	
	
	public Collection<String> generarCobrosReversion(String fechaDesde, String fechaHasta, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Date fDesde = null, fHasta = null;
		int cant1 = 0;
		int cant2 = 0;
		double importe1 = 0.0;
		double importe2 = 0.0;
		try {
			fDesde = DateTimeUtil.getDate(fechaDesde);
			fHasta = DateTimeUtil.getDate(fechaHasta);
		} catch(Exception e) {
			System.out.println("Error al obtener la fecha " + e.getMessage());
		}
		Collection<String> salida = new ArrayList<String>();
		Estad1DTO estad1 = null;
		Date fecha  = new Date();
		boolean entrar = false;
		Iterator i = movimiento.getMovimientoByFecha(fDesde, fHasta, sesion).iterator();
		while(i.hasNext()) {
			MovimientoBean m = (MovimientoBean)i.next();
			
			if(m.getFecha().compareTo(fecha) != 0) {
				fecha = m.getFecha();
				if(entrar) {
					this.acumular(estad1, salida, cant1, importe1, cant2, importe2);
				}
				estad1 = new Estad1DTO();
				estad1.setFecha(m.getFecha().toString());
			}
			this.incrementar(estad1, m);
			entrar = true;
		}
		this.acumular(estad1, salida, cant1, importe1, cant2, importe2);
		
		estad1 = new Estad1DTO();
		estad1.setFecha("TOTAL");
		estad1.setCobrosCant(cant1);
		estad1.setCobrosImporte(importe1);
		estad1.setReversionCant(cant2);
		estad1.setReversionImporte(importe2);
		this.acumular(estad1, salida, cant1, importe1, cant2, importe2);
		
		return salida;
	}
	private void incrementar(Estad1DTO e, MovimientoBean m) {
		if(m.getEstado().esDebitado()) {
			e.setCobrosCant(e.getCobrosCant() + 1);
			e.setCobrosImporte(e.getCobrosImporte() + m.getImporte());
		}
		if(m.getEstado().esReversion()) {
			e.setReversionCant(e.getReversionCant() + 1);
			e.setReversionImporte(e.getReversionImporte() + m.getImporte());
		}
	}
	private void acumular(Estad1DTO e, Collection<String> salida, int cant1, double importe1, int cant2, double importe2) {
		if(e != null) {
			e.setCobrosImporte(Util.redondear(e.getCobrosImporte(),2));
			e.setReversionImporte(Util.redondear(e.getReversionImporte(),2));
			salida.add(e.toString());
			
       		cant1    += e.getCobrosCant();
       		importe1 += e.getCobrosImporte();
       		cant2    += e.getReversionCant();
       		importe2 += e.getReversionImporte();
		}
	}
	public Collection<String> ventas(String fechaDesde, String fechaHasta, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Date fDesde = null, fHasta = null;
		int cant1 = 0;
		double importe1 = 0.0;
		try {
			fDesde = DateTimeUtil.getDate(fechaDesde);
			fHasta = DateTimeUtil.getDate(fechaHasta);
		} catch(Exception e) {
			System.out.println("Error al obtener la fecha " + e.getMessage());
		}
		Collection<String> salida = new ArrayList<String>();
		Estad2DTO estad2 = null;
		String[] s = null;
		ServicioDTO servicioDTO;
		ClienteBean clienteBean;
		try {
			s = servicio.getServicioByFechaVentaOrderFecha(fDesde, fHasta, usuario);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			for(int i=0; i< s.length; i++) {
				servicioDTO = new ServicioDTO(s[i]);
				clienteBean = cliente.getClienteById(servicioDTO.getIdCliente(), sesion);
				estad2 = new Estad2DTO();
				estad2.setFecha(servicioDTO.getFechaVenta().toString());
				estad2.setCliente(clienteBean.getNyA());
				estad2.setImporte(servicioDTO.getImporteTotal());
				cant1 += 1;
				importe1 += estad2.getImporte();
				salida.add(estad2.toString());
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		estad2 = new Estad2DTO();
		estad2.setFecha("TOTAL");
		estad2.setCliente(String.valueOf(cant1));
		estad2.setImporte(Util.redondear(importe1,2));
		salida.add(estad2.toString());
		
		return salida;
	}
	public Collection<String> clientesEmbargar(String meses, String usuario) {
		Collection<String> salida = new ArrayList<String>();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		Long idCliente = 0l;
		Long idServicio = 0l;
		Estad3DTO eDTO = new Estad3DTO();
		ClienteBean c = null;
		ServicioBean servicioBean = null;
		try {
			Date fecha = DateTimeUtil.getNMesesAtras(Integer.parseInt(meses));
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByMovFechaMayor");
			q.setDate("fecha", fecha);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				c = m.getCliente();
				if(m.getIdCliente().equals(idCliente)   &&   m.getIdServicio().equals(idServicio)) {
				} else {
					this.totalizar(salida, eDTO);
					
					idCliente = m.getIdCliente();
					idServicio = m.getIdServicio();
					servicioBean = servicio.getServicioById(idServicio, sesion);
					eDTO = new Estad3DTO();
					eDTO.setDelegacion(c.getDelegacion().getDescripcion());
					eDTO.setCliente(c.getNyA());
					eDTO.setImporteTotal(servicioBean.getImporteTotal());
					eDTO.setNrodoc(c.getNroDoc());
					eDTO.setEstado(c.getEstadoString());
					eDTO.setTelefLineaParticular(c.getLocParticular().getTelefLinea());
					eDTO.setTelefCelularParticular(c.getLocParticular().getTelefCelular());
					eDTO.setTelefLineaLaboral(c.getLocLaboral().getTelefLinea());
					eDTO.setTelefCelularLaboral(c.getLocLaboral().getTelefCelular());
				}
				this.acumular3DTO(m, eDTO);
			}
			this.totalizar(salida, eDTO);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return salida;
	}
	private void totalizar(Collection<String> salida, Estad3DTO eDTO) {
		eDTO.calcularTotal();
		if(eDTO.getCliente() != null && eDTO.getTotal() <= 0) {
			if(eDTO.getRechazos() > 0)
				salida.add(eDTO.toString());
			else
				if(eDTO.getDebitos() > 0)
					salida.add(eDTO.toString());
				else
					if(eDTO.getDevolucion() > 0)
						salida.add(eDTO.toString());
		}	
	}
	private void acumular3DTO(MovimientoBean m, Estad3DTO eDTO) {
		if(m.getCliente().isActivo())
			if(m.getEstado().esRechazo())
				eDTO.incRechazos();
			else
				if(m.getEstado().esDebitado())
					eDTO.incDebitos();
				else
					if(m.getEstado().esReversion())
						eDTO.incReversion();
					else
						if(m.getEstado().esDevolucion())
							eDTO.incDevolucion();
	}
	public Collection<String> clientesEstado(int estado, String usuario) {
		Collection<String> salida = new ArrayList<String>();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		Estad3DTO eDTO = null;
		ClienteBean c = null;
		ServicioBean s = null;
		try {
			transaction.begin();
			Query q = sesion.getNamedQuery("ClienteBean.clientesEstado");
			q.setInteger("estado", estado);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				c = (ClienteBean)mI.next();
				Iterator<ServicioBean> sI = c.getServicios().iterator();
				while(sI.hasNext()) {
					s = (ServicioBean)sI.next();
					eDTO = new Estad3DTO();
					eDTO.setNrodoc(c.getNroDoc());
					eDTO.setCliente(c.getNyA());
					eDTO.setDelegacion(c.getDelegacion().getDescripcion());
					eDTO.setImporteTotal(s.getImporteTotal());
					salida.add(eDTO.toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return salida;
	}
	public Collection<String> obtMovimientosEstado(String fechaDesde, String fechaHasta, int estado, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Date fDesde = null, fHasta = null;
		try {
			fDesde = DateTimeUtil.getDate(fechaDesde);
			fHasta = DateTimeUtil.getDate(fechaHasta);
		} catch(Exception e) {
			System.out.println("Error al obtener la fecha " + e.getMessage());
		}
		Collection<String> salida = new ArrayList<String>();
		Estad3DTO eDTO = new Estad3DTO();
		ClienteBean c = null;
		Iterator<MovimientoBean> i = movimiento.findEstadoyRangoFecha(fDesde, fHasta, estado, sesion).iterator();
		while(i.hasNext()) {
			MovimientoBean m = (MovimientoBean)i.next();

			c = m.getCliente();
			eDTO = new Estad3DTO();
			eDTO.setCliente(c.getNyA());
			eDTO.setDelegacion(c.getDelegacion().getDescripcion());
			eDTO.setNrodoc(c.getNroDoc());
			eDTO.setEstado(c.getEstadoString());
			eDTO.setTelefLineaParticular(c.getLocParticular().getTelefLinea());
			eDTO.setTelefCelularParticular(c.getLocParticular().getTelefCelular());
			eDTO.setTelefLineaLaboral(c.getLocLaboral().getTelefLinea());
			eDTO.setTelefCelularLaboral(c.getLocLaboral().getTelefCelular());
			eDTO.setImporte(m.getImporte());
			eDTO.setFecha(DateTimeUtil.formatDate(m.getFecha()));
			salida.add(eDTO.toString());
		}
		return salida;
	}
	
//	fecha >='2010-10-21' and fecha <= '2010-11-20' and id_estado > 1;
	public Collection<String> cantMovPer(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Collection<MovimientoBean> ccc = new ArrayList<MovimientoBean>();
		try {
			Date fDesde = DateTimeUtil.getDateAAAAMMDD("20110321");
			Date fHasta = DateTimeUtil.getDateAAAAMMDD("20130820");
			Iterator<MovimientoBean> i = movimiento.getMovimientoByFecha(fDesde, fHasta, sesion).iterator();
			while(i.hasNext()) {
				MovimientoBean m = (MovimientoBean)i.next();
				if(m.getEstado().getId() > 2)
					if(m.getEstado().esDebitado() ||
					   m.getEstado().esReversion() || m.getEstado().esDevolucion()) {
							boolean es = false;
							Iterator<ServicioBean> mi = m.getCliente().getServicios().iterator();
							while(mi.hasNext()) {
								ServicioBean ss = (ServicioBean)mi.next();
								if(ss.getVendedor().vendedorArca()) {
									es = true;
								}
							}
							if(es) {
								ccc.add(m);
							}
				}
			}
		} catch (Exception e) {
			
		}
		return procesarCantMovPer(ccc);
	}
	private Collection<String> procesarCantMovPer(Collection<MovimientoBean> ccc) {
		Collection<String> salida = new ArrayList<String>();
		
		Hashtable<String, Estad4DTO> matrizTotal = new Hashtable<String, Estad4DTO>();
		Estad4DTO eDTO = null;
		
		Iterator<MovimientoBean> ci = ccc.iterator();
		while(ci.hasNext()) {
			MovimientoBean m = (MovimientoBean)ci.next();
//			if(m.getPeriodo().compareTo("Mayo-2011")==0){
//				try{
//				System.out.println(m.getCliente().getNroDoc() + ";" + m.getFecha() + ";" + m.getImporte() + ";" + m.getEstado().getEstado());
//				} catch (Exception e) {}
//			}
			if(matrizTotal.containsKey(m.getPeriodoSort())) {
				eDTO = matrizTotal.get(m.getPeriodoSort());
			} else {
				eDTO = new Estad4DTO();
			}
			eDTO.setPeriodo(m.getPeriodo());
			eDTO.setCantidad(eDTO.getCantidad() + 1);
			if(m.getEstado().esDebitado()) {
				eDTO.setImporte(eDTO.getImporte() + m.getImporte());
			}
			if(m.getEstado().esReversion() || m.getEstado().esDevolucion()) {
				eDTO.setImporte(eDTO.getImporte() - m.getImporte());
			}
			eDTO.setImporte(Util.redondear(eDTO.getImporte(), 2));
			matrizTotal.put(m.getPeriodoSort(), eDTO);
		}
		Vector<String> v = new Vector<String>(matrizTotal.keySet());
	    Collections.sort(v);
	    for (Enumeration<String> e = v.elements(); e.hasMoreElements();) {
	        String key = (String)e.nextElement();
	        if(key.endsWith("null")) {
	        	continue;
	        }
	        eDTO = (Estad4DTO)matrizTotal.get(key);
	        salida.add(eDTO.toString());
	    }
		return salida;
	}
}