package com.financiera.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.CtacteDTO;
import com.dto.MovTotTipoDTO;
import com.dto.RespuestaDTO;
import com.dto.ResultadoDTO;
import com.dto.ServicioDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DiasCobroBean;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.MovimientoHistBean;
import com.financiera.domain.bean.Persistible;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;


public class MovimientoServiceImpl extends AbstractService implements MovimientoService {
	private int inserts = 0;
	private int deleteados = 0;
	private Long auxIdServicio = null;
		
	public MovimientoServiceImpl(){
		super();
		this.setDescription("Servicio para Movimiento");
		this.setName("MovimientoService");
	}

	public void agregar(MovimientoBean m, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			s.save(m);
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agregar(Collection<Object> m, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Iterator<Object> i = m.iterator();
			while(i.hasNext()) {
				s.save((MovimientoBean)i.next());
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
//		CacheManager.getInstance().restart(usuario);
	}
	public void agregarHist(Collection<Object> m, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Iterator<Object> i = m.iterator();
			while(i.hasNext()) {
				s.save((MovimientoHistBean)i.next());
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
//		CacheManager.getInstance().restart(usuario);
	}
	public void actMovimiento(Long id, Date fecha, double importe, String codRechazo, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			m.setFecha(fecha);
			m.setImporte(importe);
			if(Util.isBlank(codRechazo))
				m.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(3)));
			else
				m.setEstado(this.getEstadoCodRechazo(codRechazo, s));
				
			s.update(m);
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrDevolucion(Long id, Date fecha, double importe, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Devolucion");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(6))); // devuelto
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrRechazo(Long id, Date fecha, double importe, String codRechazo, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Rechazo");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado(this.getEstadoCodRechazo(codRechazo, s));
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);
			
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrDebito(Long id, Date fecha, double importe, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Debito");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(3))); //DEBITADO));
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);
			
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrReversion(Long id, Date fecha, double importe, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Reversion");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(5))); //REVERSION
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);
			
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrADisparar(Long id, Date fecha, double importe, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("A Disparar Manual");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(41)));
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);
			
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void agrDisparo(Long id, Date fecha, double importe, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);
	
			MovimientoBean m1 = new MovimientoBean();
			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Disparo Manual");
			m1.setFecha(fecha);
			m1.setImporte(importe);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(2))); //DISPARADO;
			m1.setDisparo((DisparoBean)s.get(DisparoBean.class, new Long(1)));
			s.save(m1);

		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void delMovimiento(Long id, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			MovimientoBean m = (MovimientoBean)s.get(MovimientoBean.class, id);

			s.delete(m);

		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void delMovimiento(Collection<MovimientoBean> m, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Iterator<MovimientoBean> i = m.iterator();
			while(i.hasNext()) {
				s.delete((MovimientoBean)i.next());
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void dividirMovimiento(Long id, String usuario) throws Exception {
		MovimientoBean m  = null;
		MovimientoBean m1 = new MovimientoBean();
		
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			m = (MovimientoBean)s.get(MovimientoBean.class, id);

			Double d = m.getImporte() / 2;
			m.setImporte(d);
			m.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(40))); //Dividido

			m1.setIdCliente(m.getIdCliente());
			m1.setCliente(m.getCliente());
			m1.setIdServicio(m.getIdServicio());
			m1.setDescripcion("Disparo Manual");
			m1.setFecha(m.getFecha());
			m1.setImporte(d);
			m1.setEstado((EstadoMovBean)s.get(EstadoMovBean.class, new Long(40))); //Dividido
			m1.setDisparo(m.getDisparo());

			s.update(m);
			s.save(m1);

		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void delMovimientoADisparar(ClienteBean cliente, String usuario) throws Exception {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("MovimientoBean.findByPendClienteEstado");
			q.setLong("idCliente", cliente.getId());
			q.setLong("estado", EstadoMovBean.E0_MES_CUOTA);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				s.delete(m);
			}
			q = s.getNamedQuery("MovimientoBean.findByPendClienteEstado");
			q.setLong("idCliente", cliente.getId());
			q.setLong("estado", new Long(38));
			mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				s.delete(m);
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public MovimientoBean getMovimiento(ClienteBean cliente, Date fecha, double importe, String codigoInternoBanco, Session sesion) throws Exception {
		if(codigoInternoBanco == null) {
			codigoInternoBanco = new String();
		}
		MovimientoBean movimientoBean = null;
		boolean encontre;
		Date d;
//		Date dItau = DateTimeUtil.getDateDDMMAAAA("01082018");
//		Date dBica = DateTimeUtil.getDateDDMMAAAA("01082018");
		
		try {
			Query q = sesion.getNamedQuery("MovimientoBean.findByClienteDisparado");
			q.setLong("idCliente", cliente.getId());
			q.setLong("estado", new Long(EstadoMovBean.E1_DISPARADO));
			Iterator mI = q.list().iterator();
			encontre = false;
			while(mI.hasNext() && !encontre) {
				movimientoBean = (MovimientoBean)mI.next();
//				if(movimientoBean.getIdCliente() == 5408) {
//					System.out.println(movimientoBean.getIdCliente());
//				}
				if(movimientoBean.getImporte() == importe) {
					String f = DateTimeUtil.formatDateDDMMAAAA(movimientoBean.getFecha());
					d = DateTimeUtil.getDateDDMMAAAA(f);
					if(d.compareTo(fecha) == 0) {   // BAPRO
						encontre = true;
					}
				}
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		}
		if(encontre)
			return movimientoBean;
		else
			System.out.println("No encuentro mov para " + cliente.getNroDoc() + " " + fecha.toString()+ " " + importe);
			throw new Exception("No encuentro movimiento para " + cliente.getId() + " " + fecha.toString());
	}
	private MovimientoBean actMovimientoItauBica(ClienteBean cliente, Date fecha, double importe, String codigoInternoBanco, Session s) throws Exception {
		if(cliente.getId() == 2181) {
			System.out.println();
		}
		MovimientoBean movimientoBean = null;
		boolean encontre;
		Date d;
		try {
			Query q = s.getNamedQuery("MovimientoBean.findByMovClienteRespondido");
			q.setLong("idCliente", cliente.getId());
			Iterator mI = q.list().iterator();
			encontre = false;
			while(mI.hasNext() && !encontre) {
				movimientoBean = (MovimientoBean)mI.next();

				if(movimientoBean.getDescripcion().compareTo(codigoInternoBanco) == 0) {
					if(movimientoBean.getImporte() == importe) {
						auxIdServicio = movimientoBean.getIdServicio();
							if(movimientoBean.getFecha().compareTo(fecha) == 0) {
							encontre = true;
						}
					}
				}
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService actMovimientoItauBica " + e.getMessage());
		}
		if(encontre) {
			movimientoBean.setAccion(Persistible.NADA);
		} else {
			try {
				Query q = s.getNamedQuery("MovimientoBean.findByClienteDisparado");
				q.setLong("idCliente", cliente.getId());
				q.setLong("estado", new Long(EstadoMovBean.E1_DISPARADO));
				Iterator mI = q.list().iterator();
				encontre = false;
				while(mI.hasNext() && !encontre) {
					movimientoBean = (MovimientoBean)mI.next();
		//				if(movimientoBean.getIdCliente() == 5408) {
		//					System.out.println(movimientoBean.getIdCliente());
		//				} aaa
					if(movimientoBean.getDescripcion().compareTo(codigoInternoBanco) == 0) {
						if(movimientoBean.getImporte() == importe) {
							auxIdServicio = movimientoBean.getIdServicio();
							if(movimientoBean.getFecha().compareTo(fecha) == 0) {
								encontre = true;
							}
						}
					}
				}
				if(!encontre) {
					mI = q.list().iterator();
					encontre = false;
					while(mI.hasNext() && !encontre) {
						movimientoBean = (MovimientoBean)mI.next();
		
						if(movimientoBean.getDescripcion().compareTo(codigoInternoBanco) == 0) {
							if(movimientoBean.getImporte() == importe) {
								if(movimientoBean.getFecha().compareTo(fecha) < 0) {
									encontre = true;
								}
							}
						}
					}
				}
			} catch(Exception e) {
				throw new Exception("MovimientoService actMovimientoItauBica " + e.getMessage());
			}
			if(encontre) {
				movimientoBean.setAccion(Persistible.UPDATE);
			} else {
				movimientoBean.setAccion(Persistible.INSERT);
			}
		}
		return movimientoBean;
	}
	public void delMovimientoDisparadoFechaHasta(ClienteBean cliente, Session s) throws Exception {
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("MovimientoBean.findByPendClienteEstado");
			q.setLong("idCliente", cliente.getId());
			q.setLong("estado", EstadoMovBean.E1_DISPARADO);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				
				s.delete(m);
			}
			q = s.getNamedQuery("MovimientoBean.findByPendClienteEstado");
			q.setLong("idCliente", cliente.getId());
			q.setLong("estado", new Long(38));
			mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				s.delete(m);
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			s.flush();
		}
	}
	public MovimientoBean getMovimiento(Long idServicio, Date fecha, Session sesion) throws Exception {
		MovimientoBean movimientoBean = null;
		boolean encontre;
		Date d;
		try{
			Query q = sesion.getNamedQuery("MovimientoBean.findByServicio");
			q.setLong("idServicio", idServicio);
			Iterator mI = q.list().iterator();
			encontre = false;
			while(mI.hasNext() && !encontre) {
				movimientoBean = (MovimientoBean)mI.next();
				if(movimientoBean.getEstado().getEstado() == EstadoMovBean.E0_A_DISPARAR ||
				   movimientoBean.getEstado().getEstado() == EstadoMovBean.E0_MES_CUOTA) {
					continue;
				} else {
					String f = DateTimeUtil.formatDateDDMMAAAA(movimientoBean.getFecha());
					d = DateTimeUtil.getDateDDMMAAAA(f);
					if(d.compareTo(fecha) == 0) {
						encontre = true;
					}
				}
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		}
		if(encontre)
			return movimientoBean;
		else
			throw new Exception("No encuentro movimiento para " + idServicio + " " + fecha.toString());
	}
	
	public List<MovimientoBean> getMovimientoByCliente(Long cliente, String usuario, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
			Vector<Long> v = CacheManager.getInstance().getServiciosVisibles(cliente, esArca, sesion);
			int i = 0;
			try{
				Query q = sesion.getNamedQuery("MovimientoBean.getAllbyCliente");
				q.setLong("idCliente", cliente);
				Iterator mI = q.list().iterator();
				while(mI.hasNext()) {
					MovimientoBean mb = (MovimientoBean)mI.next();
					if(v.contains(mb.getIdServicio())) {
						movimientos.add(i++, mb);
//						if(i>140) {
//							System.out.println(mb);
//						}
					}
				}
			} catch(Exception e) {
				System.out.println("MovimientoService : " + e.getMessage());
			}
		}
		return movimientos;
	}
	public List<MovimientoBean> getMovimientoHistByCliente(Long cliente, String usuario, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
			MovimientoBean m = null;
			MovimientoHistBean mh = null;
			int i = 0;
			try{
				Query q = sesion.getNamedQuery("MovimientoHistBean.getAllbyCliente");
				q.setLong("idCliente", cliente);
				Iterator mI = q.list().iterator();
				while(mI.hasNext()) {
					mh = (MovimientoHistBean)mI.next();
					
					m = new MovimientoBean();
					m.setId(mh.getId());
					m.setIdCliente(mh.getIdCliente());
					m.setCliente(mh.getCliente());
					m.setIdServicio(mh.getIdServicio());
					m.setFecha(mh.getFecha());
					m.setImporte(mh.getImporte());
					m.setDescripcion(mh.getDescripcion());
					m.setNroCuota(mh.getNroCuota());
					m.setTotalCuotas(mh.getTotalCuotas());
					m.setEstado(mh.getEstado());
					m.setDisparo(mh.getDisparo());
					movimientos.add(i++, m);
				}
			} catch(Exception e) {
				System.out.println("MovimientoService : " + e.getMessage());
			}
		}
		return movimientos;
	}

	public MovTotTipoDTO getMovimientoByServicio(Long servicio, Session sesion) throws Exception {
		MovTotTipoDTO mttDto = new MovTotTipoDTO();
		MovimientoBean m = null;
		String p = new String();
		boolean seCobro = true;
		boolean procese = false;
//		Session session = Persistencia.getSession();
//		Transaction transaction = session.beginTransaction();
		try{
//			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByServicio");
			q.setLong("idServicio", servicio);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				m = (MovimientoBean) (mI.next());
				if(m.getEstado().esProcesableEstad()) {
					procese = true;
					if(m.getPeriodo().compareTo(p) == 0) {
						if(m.getEstado().esDebitado()) {
							mttDto.addCantCuotasCobradas(1);
							mttDto.addImporteCobrado(m.getImporte());
							seCobro = true;
						}
					} else {
						if(!seCobro) {
							mttDto.addCantMesesSinCobro(1);
							mttDto.addImporteDevengado(m.getImporte());
						}
						
						p = m.getPeriodo();
						seCobro = false;
						if(m.getEstado().esDebitado()) {
							mttDto.addCantCuotasCobradas(1);
							mttDto.addImporteCobrado(m.getImporte());
							seCobro = true;
						}
					}
				}
			}
			if(procese)
				if(!seCobro) {
					mttDto.addCantMesesSinCobro(1);
					mttDto.addImporteDevengado(m.getImporte());
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return mttDto;
	}
	public String[] getMovimientoByClienteStr(Long cliente, String usuario) {
		String[] aux = null;
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
//		Session sesion = Persistencia.getSession();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
				aux = this.getMovimientoByClienteStrPriv(cliente, true, usuario, sesion);
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return aux;
		
	}
	public String[] getMovimientoByClienteStr(Long cliente, String usuario, Session sesion) throws Exception {
		String[] aux = null;
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		try{
			if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
				aux = this.getMovimientoByClienteStrPriv(cliente, true, usuario, sesion);
			}
		} catch(Exception e) {
			throw new Exception("MovimientoService" + e.getMessage());
		}
		return aux;
	}
	public String[] getMovimientoHistByClienteStr(Long cliente, String usuario) {
		String[] aux = null;
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
//		Session sesion = Persistencia.getSession();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
				aux = this.getMovimientoByClienteStrPriv(cliente, false, usuario, sesion);
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return aux;
	}

	private String[] getMovimientoByClienteStrPriv(Long cliente, boolean actual, String usuario, Session sesion) {
		List<MovimientoBean> movimientos = null;
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
			if(actual) {
				movimientos = this.getMovimientoByCliente(cliente, usuario, sesion);
			} else {
				movimientos = this.getMovimientoHistByCliente(cliente, usuario, sesion);
			}
		}		
		return procesarMovimientos(movimientos);
	}	
	private String[] procesarMovimientos(List<MovimientoBean> movimientos) {
		Date fechaInic = new Date();
		boolean privez = true;
// busco el periodo mas antiguo
		Iterator<MovimientoBean> mI = movimientos.iterator();
		while(mI.hasNext()) {
			MovimientoBean movimiento = (MovimientoBean)mI.next();
			
			Date fechaAux = movimiento.getFecha();
			if(privez) {
				privez = false;
				fechaInic = fechaAux;
			} else {
				if(fechaAux.before(fechaInic)) {
					fechaInic = fechaAux;
				}
			}
		}
		int anio = fechaInic.getYear();
		int mes  = fechaInic.getMonth();
		int dia  = fechaInic.getDate();
		if(dia > 28) {
			dia = dia - 3;
		}
		fechaInic = new Date(anio, mes, dia);
//
		String[] mov = new String[movimientos.size()+500];
		String codRechazo = null;
		int i = 0;
		Long idServicio = 0l;
		mI = movimientos.iterator();
		while(mI.hasNext()) {
			MovimientoBean movimiento = (MovimientoBean)mI.next();
//			if(movimiento.getCliente().getNroDoc() == 16243418) {
//				System.out.println("ACA");
//			}
			try {
//				System.out.println(movimiento.getCliente().getNroDoc() + " " + movimiento.getImporte() + " " + movimiento.getPeriodo());
				
				EstadoMovBean estado = movimiento.getEstado();
				String periodo = movimiento.getPeriodo();
				String periodoSort = movimiento.getPeriodoSort();
				if(estado.getCodRechazo() == null)
					codRechazo = "   ";
				else
					codRechazo = estado.getCodRechazo();
				if(movimiento.getIdServicio().compareTo(idServicio) !=0) {
					idServicio = movimiento.getIdServicio(); 
					Date fecha = fechaInic;
					
					MovimientoBean m = new MovimientoBean();
					m.setCliente(movimiento.getCliente());
					m.setFecha(fecha);
					
					while(m.getPeriodo().compareTo(movimiento.getPeriodo()) != 0) {
						 
						m.setId(movimiento.getId());
						m.setIdCliente(movimiento.getIdCliente());
						m.setCliente(movimiento.getCliente());
						m.setIdServicio(movimiento.getIdServicio());
						m.setImporte(0.0);
						m.setDescripcion(MovimientoBean.VACIO);
						m.setNroCuota(0);
						m.setTotalCuotas(0);
						
						mov[i++] = new String(m.toString().concat("1; ;").concat(m.getPeriodo()).concat(";"));
					
						try {
							fecha = DateTimeUtil.add1Mes(m.getFecha());
						} catch(Exception e) {
							System.out.println("error en fecha");
						}
						m = new MovimientoBean();
						m.setCliente(movimiento.getCliente());
						m.setFecha(fecha);						
					}
				}
				mov[i++] = new String(movimiento.toString().concat(String.valueOf(estado.getColor())).concat(";").concat(codRechazo).concat(";").concat(periodo).concat(";").concat(periodoSort).concat(";"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return mov;
	}
	
	public List<MovimientoBean> getMovimientoByClientePeriodo(Long cliente, String periodo, String usuario, Session sesion) {
		List<MovimientoBean> mov = null;
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		if(CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
			List<MovimientoBean> movAll = this.getMovimientoByCliente(cliente, usuario, sesion);
			mov = new ArrayList<MovimientoBean>();
			Iterator<MovimientoBean> mI = movAll.iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				if(m.getPeriodo().compareTo(periodo) == 0) {
					mov.add(m);
				}
			}
		}
		return mov;
	}
	public void generarMovimiento(ClienteBean cliente, Date fecha, String periodo, Session session, EstadoMovBean estadoAdisparar) throws Exception {
// Si aparece algun movimiento pendiente en el mismo periodo los elimina		
		MovimientoBean movimientoBean;
		Query q = session.getNamedQuery("MovimientoBean.findByPendClienteEstado");
		q.setLong("idCliente", cliente.getId());
		q.setLong("estado", EstadoMovBean.E0_MES_CUOTA);
		Iterator mI = q.list().iterator();
		while(mI.hasNext()) {
			movimientoBean = (MovimientoBean)mI.next();
			if(DateTimeUtil.getPeriodo(movimientoBean.getFecha()).compareTo(periodo) == 0) {

				session.delete(movimientoBean); deleteados++;
			}
		}
// Agrego el movimiento
		DisparoBean disparoBean = (DisparoBean)session.get(DisparoBean.class, new Long(1));
		Iterator<ServicioBean> sI = cliente.getServicios().iterator();
		while(sI.hasNext()) {
			ServicioBean s = (ServicioBean)sI.next();
// para todos los servicios porque solo vienen los clientes ACTIVOS			
//			if(s.servicioDisponible()) {
				MovimientoBean m = new MovimientoBean();
				m.setFecha(fecha);
				m.setCliente(cliente);
				m.setIdCliente(cliente.getId());
				m.setIdServicio(s.getId());
				m.setImporte(s.getImporteCuota());
				m.setDescripcion(cliente.getDelegacion().getBanco().getDescripcion());
				m.setEstado(estadoAdisparar);
				m.setDisparo(disparoBean);
				session.save(m);
//			}
		}
	}

	
	public Collection<String> generarMovimiento(Date maxFecha, ServicioBean servicio, String periodo, Session session, EstadoMovBean estadoAdisparar, boolean validarMovExistente, long estado1, long estado2, long estado3) throws Exception { 
		Collection<String> salida = new ArrayList<String>();
		double importe = 0.0;
/**
 *		Eliminamos los movimientos de referencia del mes
 */
		MovimientoBean movimientoBean;
		ClienteBean cliente = servicio.getCliente();
		Query q = session.getNamedQuery("MovimientoBean.findByPendDisparoEstado");
		q.setLong("idCliente", cliente.getId());
		q.setLong("idServicio", servicio.getId());
		q.setLong("estado1", estado1);
		q.setLong("estado2", estado2);
		q.setLong("estado3", estado3);
		q.setDate("fecha", maxFecha);
		boolean hayMov = false;
		Iterator mI = q.list().iterator();
		while(mI.hasNext()) {
			movimientoBean = (MovimientoBean)mI.next();
			if(DateTimeUtil.getPeriodo(movimientoBean.getFecha()).compareTo(periodo) == 0) {

				if(movimientoBean.getEstado().getEstado() == EstadoMovBean.E0_DIVIDIDO) {
					importe = movimientoBean.getImporte()/2;
				} else {
					importe = 0.0;
				}
//				session.delete(movimientoBean); deleteados++;

// 					genero uno por cada fecha
				hayMov = true;
				if(movimientoBean.getEstado().getEstado() == EstadoMovBean.E0_DIVIDIDO) {
					this.generarMovPorDiaDisparo(servicio, movimientoBean, estadoAdisparar, session, false, importe);
// hacerlo 2 veces
					this.generarMovPorDiaDisparo(servicio, movimientoBean, estadoAdisparar, session, false, importe);
//					registro que el movimiento esta dividido para que el mensual tambien salga dividido y
//					luego volver al estado 
//					servicio.setEstado(ServicioDTO.DIVIDIDO);
//					session.update(servicio);
					
				} else {
					this.generarMovPorDiaDisparo(servicio, movimientoBean, estadoAdisparar, session, validarMovExistente, importe);
				}
			}
		}
		if(!hayMov) {
//  				debo continuar generando disparo
			MovimientoBean m = new MovimientoBean();
			m.setFecha(DateTimeUtil.getDateAAAAbMMbDD((DateTimeUtil.getFecha1_Periodo(periodo))));
//			m.setFecha(DateTimeUtil.getDate());
			this.generarMovPorDiaDisparo(servicio, m, estadoAdisparar, session, validarMovExistente, new Double(0.0));
		}
		return salida;
	}
	public void generarMovPorDiaDisparo(ServicioBean servicio, MovimientoBean movimientoBean, EstadoMovBean estadoAdisparar, Session session, boolean validarMovExistente, double importe) {
		try {
			Iterator<DiasCobroBean> i = servicio.getCliente().getDelegacion().getDiasCobro(session).iterator();
			Date fecha1=null;
			Double valorCuota = null;
			try {
	//			fecha1 = DateTimeUtil.getFechaDia1(DateTimeUtil.add1Mes(movimientoBean.getFecha()));
				fecha1 = DateTimeUtil.getFechaDia1(movimientoBean.getFecha());
			} catch(Exception e) {
				
			}
			if(i.hasNext()) {
				while(i.hasNext()) {
					DiasCobroBean d = (DiasCobroBean) i.next();
		//valido que para este cliente/servicio/fecha no exista un movimiento. De esta forma no se repiten los disparos
					try {
						if(validarMovExistente) {
							this.getMovimiento(servicio.getId(), d.getFechaDisparo(), session);
						} else {
							throw new Exception();
						}
					} catch (Exception e) {
		//si no se encontro es porque no hay y lo puedo generar						
						MovimientoBean m = new MovimientoBean();
						
						m.setFecha(d.getFechaDisparo());
						
						m.setCliente(servicio.getCliente());
						m.setIdCliente(servicio.getCliente().getId());
						m.setIdServicio(servicio.getId());
						if(importe == 0.0) {
							m.setImporte(servicio.getImporteCuota());
						} else {
							m.setImporte(importe);
						}
						try {
//							if(servicio.getCliente().getDelegacion().getBanco().getId() < 22) {
								m.setDescripcion(servicio.getCliente().getDelegacion().getBanco().getDescripcion());
//							} else {
//								m.setDescripcion("Banco Supervielle");
//							}
						} catch(Exception ex) {
							m.setDescripcion(new String("Sin banco: " + servicio.getCliente().getDelegacion().getDescripcion()));
						}
		//					m.setNroCuota(servicio.getUltCuotaDebitada());
		//					m.setTotalCuotas(servicio.getCantCuota());
						m.setEstado(estadoAdisparar);
		
						session.save(m); inserts++;
					}
				}
			} else {
				try {
					if(validarMovExistente) {
						this.getMovimiento(servicio.getId(), fecha1, session);
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
	//si no se encontro es porque no hay y lo puedo generar						
					MovimientoBean m = new MovimientoBean();
					
					m.setFecha(fecha1);
					
					m.setCliente(servicio.getCliente());
					m.setIdCliente(servicio.getCliente().getId());
					m.setIdServicio(servicio.getId());
					try {
						m.setDescripcion(servicio.getCliente().getDelegacion().getBanco().getDescripcion());
					} catch (Exception ex) {
						m.setDescripcion("banco");
					}
	//				m.setNroCuota(servicio.getUltCuotaDebitada());
	//				m.setTotalCuotas(servicio.getCantCuota());
					m.setEstado(estadoAdisparar);
				
					if(importe == 0.0) {
						if(servicio.getEstado().compareTo(ServicioDTO.DIVIDIDO) == 0) {
							valorCuota = servicio.getImporteCuota() / 2;
							m.setImporte(valorCuota);
							
							MovimientoBean m1 = new MovimientoBean();
							m1.setFecha(m.getFecha());
							m1.setCliente(m.getCliente());
							m1.setIdCliente(m.getIdCliente());
							m1.setIdServicio(m.getIdServicio());
							m1.setDescripcion(m.getDescripcion());
							m1.setEstado(m.getEstado());
							m1.setImporte(valorCuota);
							session.save(m1); inserts++;
						} else {
							m.setImporte(servicio.getImporteCuota());
						}
					} else {
						m.setImporte(importe);
					}
					session.save(m); inserts++;
				}
			}
		}
		catch (Exception e) {
			System.out.println("generarMovPorDiaDisparo " + servicio.toString());
		}
	}
	public void actualizarSvc(ClienteBean c, Session s) {
		Iterator<ServicioBean> i = c.getServicios().iterator();
		while(i.hasNext()) {
			ServicioBean svcb = (ServicioBean)i.next();
			this.actualizarSvc(svcb, s);
		}
	}
	public void actualizarSvc(ServicioBean svcb, Session session) {
		int k = 0;
		String periodo = "0";
		MovimientoBean mov = null;
		
		Iterator<MovimientoBean> mi = this.getMovimientoByCliente(svcb.getCliente().getId(), new String(" "), session).iterator();
		while(mi.hasNext()) {
			mov = (MovimientoBean) mi.next();

			if(svcb.getId().equals(mov.getIdServicio())) {
				if(mov.getEstado() != null)
					if(periodo.compareTo(mov.getPeriodo()) != 0 && mov.getEstado().getEstado() != 1) {
						periodo = mov.getPeriodo();
						k++;
	       		}
			}
		}
		if(k>0) {
			svcb.setUltCuotaDebitada(k);
			svcb.setUltFechaConMovimientos(mov.getFecha());
			session.update(svcb);
		}
	}
	public void generarCuotaInteresAll(ClienteService cliente, String usuario) {
		ClienteBean c;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			DisparoBean disparoBean = (DisparoBean)sesion.get(DisparoBean.class, new Long(1));
			Iterator<ClienteBean> ri = cliente.getClientes(new String(" "), sesion).iterator();
			while(ri.hasNext()) {
				c = (ClienteBean)ri.next();
				System.out.println("id : " + c.getId());
				Date fecha = DateTimeUtil.getDateAAAAMMDD("20070101");
				for(int i=0; i<11; i++) {
					fecha = DateTimeUtil.add1Mes(fecha);
					this.generarCuotaInteres(sesion, c, fecha, disparoBean);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}

	public String actualizarRespuesta(ClienteService cliente, ServicioService servicioService, Collection<RespuestaDTO> respuestas, String resDTO, String usuario) {		
		ResultadoDTO res = new ResultadoDTO(resDTO);
		ClienteBean clienteBean;
		RespuestaDTO r;
		String m;

		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		MovimientoBean movBean;
		
		try {
			transaction.begin();
			DisparoBean disparoBean = (DisparoBean)sesion.get(DisparoBean.class, new Long(1));
			Iterator<RespuestaDTO> ri = respuestas.iterator();
			while(ri.hasNext()) {
				r = (RespuestaDTO)ri.next();
//				System.out.println("Resp " + r);
				if(r.getDni() == 16664623) {
					System.out.println("Resp " + r);
				}
				clienteBean = cliente.getClienteByNroDoc(r.getDni(), sesion);
				if(clienteBean == null) {
					m = new String("No existe el cliente. DNI:" + r.getDni() + " CBU:" + r.getCbu() + " CA:" + r.getSucursalCA() + r.getNroCA());
					System.out.println(m);
					res.incMensajes(m);
					res.incErrores();
					continue;
				}
				this.actualizarSvc(clienteBean, sesion);
				if(!r.isConocido()) {
					res.incErrores();
					m = new String("Codigo desconocido " + r.getCodigoInternoBanco() + " Cliente :" + clienteBean.getId() + " " + r.getDni() + " " + r.getFecha() + " " + r.getImporte() + " " + r.getCodRechazo());
					System.out.println(m);
					res.incMensajes(m);
					res.incErrores();
					continue;
				} else {
					if(r.isReversion()) {
						try {
							movBean = this.getMovimiento(clienteBean, r.getFecha(), r.getImporte(), r.getCodigoInternoBanco(), sesion);

// si encuentra un movimiento disparado quiere decir que hizo reversion sin haber pagado algo
							movBean.setEstado((EstadoMovBean)sesion.get(EstadoMovBean.class, new Long(5))); //REVERSION
							movBean.setDescripcion("Reversion");
							sesion.update(movBean);
							res.incGrabados();
							
						} catch(Exception e) {
							movBean = new MovimientoBean();
							movBean.setIdCliente(clienteBean.getId());
							movBean.setCliente(clienteBean);
							Iterator<ServicioBean> it = clienteBean.getServicios().iterator();
							while(it.hasNext()) {
								ServicioBean serv = (ServicioBean)it.next();
								if(serv.getImporteCuota() == r.getImporte() && serv.isAprobado()) {
									movBean.setIdServicio(serv.getId());
									movBean.setNroCuota(0);
									movBean.setTotalCuotas(serv.getCantCuota());
								}
							}
							movBean.setFecha(r.getFecha());
							movBean.setImporte(r.getImporte());
							movBean.setDescripcion("Reversion");
							
							movBean.setEstado((EstadoMovBean)sesion.get(EstadoMovBean.class, new Long(5))); //REVERSION
							movBean.setDisparo(disparoBean);
							
							sesion.save(movBean);
							
							res.incGrabados();
						}
					} else {
						if(r.getCodigoInternoBanco().compareTo(new String(BancoBean.ITAU)) == 0 || r.getCodigoInternoBanco().compareTo(new String(BancoBean.BICA)) == 0) {
							try {
								movBean = this.actMovimientoItauBica(clienteBean, r.getFecha(), r.getImporte(), r.getCodigoInternoBanco(), sesion);
								switch(movBean.getAccion()) {
									case Persistible.UPDATE: {
										if(r.getFechaCobroReal() != null) {
											movBean.setFecha(r.getFechaCobroReal());
										} else {
											movBean.setFecha(r.getFecha());
										}
										movBean.setEstado(this.getEstadoCodRechazo(r.getCodRechazo(), sesion));
										sesion.update(movBean);
										res.incGrabados();
										break;
									}
									case Persistible.INSERT: {
										MovimientoBean movBean1 = new MovimientoBean();
										movBean1.setIdCliente(clienteBean.getId());
										movBean1.setCliente(clienteBean);
										movBean1.setImporte(r.getImporte());										
										movBean1.setIdServicio(fixIdservicio(servicioService, movBean1, sesion));
										if(movBean1.getIdServicio() == null) {
											movBean1.setIdServicio(auxIdServicio);
										}
										if(r.getFechaCobroReal() != null) {
											movBean1.setFecha(r.getFechaCobroReal());
										} else {
											movBean1.setFecha(r.getFecha());
										}
										movBean1.setDescripcion(r.getCodigoInternoBanco());
										movBean1.setEstado(this.getEstadoCodRechazo(r.getCodRechazo(), sesion));
										movBean1.setDisparo(movBean.getDisparo());
										sesion.save(movBean1);
										res.incGrabados();
										break;
									}
									case Persistible.NADA: {
										res.incYaRespondido();
										break;
									}
									default: {
										break;
									}
								}
							} catch(Exception e) {
								continue;
							}							
						} else {
							try {
								movBean = this.getMovimiento(clienteBean, r.getFecha(), r.getImporte(), r.getCodigoInternoBanco(), sesion);
							} catch(Exception e) {
								m = new String("Ya respondido " + clienteBean.getNroDoc() + "  " + DateTimeUtil.formatDateDD_MM_AAAA(r.getFecha()) + "  " + r.getImporte() + "  " + r.getCodRechazo());
		//							System.out.println(m);
		// Dejo de mostrar los YA RESPONDIDOS a pedido de Maxi	res.incMensajes(m);
								res.incYaRespondido();
								continue;
							}
							if(r.getFechaCobroReal() != null) {
								movBean.setFecha(r.getFechaCobroReal());
							}
							movBean.setEstado(this.getEstadoCodRechazo(r.getCodRechazo(), sesion));
							
							sesion.update(movBean);
		
							res.incGrabados();
						}
					}
//					this.generarCuotaInteres(session, clienteBean, r.getFecha(), disparoBean);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
			CacheManager.getInstance().restart(usuario);
		}
		return res.toString();
	}
	public Long fixIdservicio(ServicioService servicio, MovimientoBean m, Session sesion) {
		Long auxIdServicio = null;
		List<ServicioBean> serv = servicio.getServicios(m.getIdCliente(), sesion);
		Iterator its = serv.iterator();
		while(its.hasNext()) {
			ServicioBean s = (ServicioBean) its.next();
			if(s.isAprobado())
				if(s.getImporteCuota() == m.getImporte()) {
						auxIdServicio = s.getId();
				}
		}
		return auxIdServicio;
	}
	private void generarCuotaInteres(Session session, ClienteBean c, Date fecha, DisparoBean disparoBean) throws Exception {
		Long idServicio = 0l;
		int nroCuota = 0;
		double importe = 0;
		boolean pendiente = false;		
		
		Iterator<MovimientoBean> ii = this.getMovimientoByClientePeriodo(c.getId(), DateTimeUtil.getPeriodo(fecha), new String(" "), session).iterator();
// recorro los movimientos del cliente en orden idServicio / fecha
		while(ii.hasNext() && !pendiente) {
			MovimientoBean m = (MovimientoBean)ii.next();
			if(m.getIdServicio().compareTo(idServicio) != 0) {
				this.corte(pendiente, idServicio, c, importe, session);
				idServicio = m.getIdServicio();
				nroCuota = 0;
				importe = 0;
				pendiente = false;
			}
			if(nroCuota == 0) {
				nroCuota = m.getNroCuota();
			}
			switch(m.getEstado().analisisEstado()) {
				case EstadoMovBean.PENDIENTE: {
					pendiente = true; break;
				}
				case EstadoMovBean.DEBITO: {
					importe += m.getImporte(); break;
				}
				case EstadoMovBean.RECHAZO:	{
					break;
				}
				case EstadoMovBean.REVERSION: {
					importe -= m.getImporte(); break;
				}
			}
		}
		this.corte(pendiente, idServicio, c, importe, session);
	}
	private void corte(boolean pendiente, Long idServicio, ClienteBean c, double importe, Session session) throws Exception {
		boolean encontre;
		ServicioBean s = null;
		
		if(!pendiente   &&   idServicio.compareTo(new Long(0)) > 0) {
			Iterator<ServicioBean> jj = c.getServicios().iterator();
//busco el servicio para obtener el importe de la cuota
			encontre = false;
			while(jj.hasNext() && !encontre) {
				s = (ServicioBean)jj.next();
				if(s.getId().compareTo(idServicio) == 0) {
					encontre = true;
				}
			}
			if(s.servicioDisponible())
				if(importe < s.getImporteCuota()) {
//si el cliente pago menos de lo que le corresponde se le genera una cuota al final por lo que debe
					double debe = s.getImporteCuota() - importe; 
					this.generarCuotaInteres(session, s, debe);
				}
		}
	}
	private void generarCuotaInteres(Session session, ServicioBean s, double importeCuota) throws Exception {
		DisparoBean disparoBean = (DisparoBean)session.get(DisparoBean.class, new Long(1));
		
		for(int i=0; i<2; i++) {
			MovimientoBean movBean = new MovimientoBean();
	
			movBean.setIdCliente(s.getCliente().getId());
			movBean.setCliente(s.getCliente());
			movBean.setIdServicio(s.getId());
			Date fecha = DateTimeUtil.add1Mes(s.getUltFechaConMovimientos());
			movBean.setFecha(fecha);
			movBean.setImporte(importeCuota);
			movBean.setDescripcion("Cuota Interes");
			movBean.setNroCuota(0);
			movBean.setTotalCuotas(s.getCantCuota());
			movBean.setEstado(this.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, session));
			movBean.setDisparo(disparoBean);
			
			session.save(movBean);
			
			s.setUltFechaConMovimientos(fecha);
		}		
		session.update(s);
	}
	public String limpiarRespuestas(String resDTO, String usuario) {
		ResultadoDTO res = new ResultadoDTO(resDTO);

		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		
		ArrayList<MovimientoBean> mList = new ArrayList<MovimientoBean>();
		Long idCliente = 0l;
		Long idServicio = 0l;
		String periodo = null;
		
		try {
			Date fecha1 = DateTimeUtil.getNMesesAtras(5);
			Date fecha2 = DateTimeUtil.getNMesesAtras(3);
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByMovConRespuesta");
			q.setDate("fecha1", fecha1);
			q.setDate("fecha2", fecha2);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				
				if(m.getIdCliente().equals(idCliente)   &&   m.getIdServicio().equals(idServicio)) {
					if(m.getPeriodo().compareTo(periodo) != 0) {
						res.incDeleteados(this.procesarPeriodo(sesion, mList));
						periodo = m.getPeriodo();
					}
				} else {
					res.incDeleteados(this.procesarPeriodo(sesion, mList));

					idCliente = m.getIdCliente();
					idServicio = m.getIdServicio();
					periodo = m.getPeriodo();
				}
				mList.add(m);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		return res.toString();
	}

//----------------------------------------------------------------------------------------------------------------		
	private int procesarPeriodo(Session s, ArrayList<MovimientoBean> mList) {
		int deleteados = 0;
		boolean hayDebito = false;
		boolean primero = true;
		
		Iterator<MovimientoBean> i = mList.iterator();
		while(i.hasNext()) {
			MovimientoBean m = (MovimientoBean)i.next();
			if(m.getEstado().getEstado() == EstadoMovBean.E2_DEBITADO) {
				hayDebito = true;
			}
		}
		i = mList.iterator();
		while(i.hasNext()) {
			MovimientoBean m = (MovimientoBean)i.next();
			if(hayDebito) {
				if(m.getEstado().eliminable()) {
					s.delete(m);
					deleteados++;
				}
			} else {
				if(m.getEstado().eliminable()) {
					if(primero) {
						primero = false;
					} else {
						s.delete(m);
						deleteados++;
					}
				}
			}
		}
		mList.clear();
		return deleteados;
	}
	public EstadoMovBean getEstadoMovId(Long id, Session s) {
		EstadoMovBean e = null;
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("EstadoMovBean.findById");
			q.setLong("id", id);
//			Collection<EstadoMovBean> est = q.list();
			Iterator<EstadoMovBean> eI = q.list().iterator();
			if(eI.hasNext())
				e = (EstadoMovBean)eI.next();
		} catch (Exception x) {
			System.out.println("EstadoMovBean - No se encuentra el id : " + id);
		}
		return e;
	}
	
	public EstadoMovBean getEstadoMovEstado(int estado, Session s) {
		EstadoMovBean e = null;
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("EstadoMovBean.findByEstado");
			q.setLong("estado", estado);
			Collection<EstadoMovBean> est = q.list();
			Iterator<EstadoMovBean> eI = est.iterator();
			if(eI.hasNext())
				e = (EstadoMovBean)eI.next();
		} catch (Exception x) {
			System.out.println("EstadoMovBean - No se encuentra el estado : " + estado);
		}
		return e;
	}
	public EstadoMovBean getEstadoMovColor(int color, Session s) {
		EstadoMovBean e = null;
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("EstadoMovBean.findByColor");
			q.setLong("color", color);
			Collection<EstadoMovBean> col = q.list();
			Iterator<EstadoMovBean> eI = col.iterator();
			if(eI.hasNext())
				e = (EstadoMovBean)eI.next();
		} catch (Exception x) {
			System.out.println("EstadoMovBean - No se encuentra el color : " + color);
		}
		return e;
	}
	public EstadoMovBean getEstadoCodRechazo(String codRechazo, Session s) {
		EstadoMovBean e = null;
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q;
			if(Util.isBlank(codRechazo) || codRechazo.contains("Deb")) {
				q = s.getNamedQuery("EstadoMovBean.findByEstado");
				q.setInteger("estado", 3);
			} else {
				if(codRechazo.contains("Rev")) {
					q = s.getNamedQuery("EstadoMovBean.findByEstado");
					q.setInteger("estado", 5);					
				} else {
					q = s.getNamedQuery("EstadoMovBean.findByCodRechazo");
					q.setString("codRechazo", codRechazo);
				}
			}
			Collection<EstadoMovBean> col = q.list();
			Iterator<EstadoMovBean> eI = col.iterator();
			if(eI.hasNext())
				e = (EstadoMovBean)eI.next();
		} catch (Exception x) {
			System.out.println("EstadoMovBean - No se encuentra el codRechazo : " + codRechazo);
		}
		if(e==null) {
			System.out.println(codRechazo);
		}
		return e;
	}
	public Collection<EstadoMovBean> getAllCodRechazos(Session s) {
		Collection<EstadoMovBean> e = new ArrayList<EstadoMovBean>();
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("EstadoMovBean.getAllRechazos");
			Collection<EstadoMovBean> col = q.list();
			Iterator<EstadoMovBean> eI = col.iterator();
			while(eI.hasNext())
				e.add((EstadoMovBean)eI.next());
		} catch (Exception x) {
			System.out.println("EstadoMovBean - No se cargar los EstadoMovBean");
		}
		return e;
	}
	public String[] getAllCodRechazosStr(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Collection<EstadoMovBean> e = getAllCodRechazos(sesion);
		String[] est = new String[e.size()];
		int i = 0;
		Iterator<EstadoMovBean> ii = e.iterator();
		while(ii.hasNext()) {
			est[i++] = ((EstadoMovBean)ii.next()).toString();
		}
		return est;
	}
	public List<MovimientoBean> getMovimientoByFecha(Date fDesde, Date fHasta, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByFecha");
			q.setDate("fDesde", fDesde);
			q.setDate("fHasta", fHasta);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				movimientos.add(i++, (MovimientoBean)mI.next());
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return movimientos;
	}
	public List<MovimientoBean> findEstadoyRangoFecha(Date fDesde, Date fHasta, int estado, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findEstadoyRangoFecha");
			q.setDate("fDesde", fDesde);
			q.setDate("fHasta", fHasta);
			q.setLong("estado", estado);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				movimientos.add(i++, (MovimientoBean)mI.next());
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return movimientos;
	}
	public String[] getMovimientoByClienteVertStr(Long cliente, String usuario) {
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		int j = 0;
		int k = 1;
		Map<String, String> fil = new HashMap<String, String>();

		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			if(!CacheManager.getInstance().veoTodosLosDatos(cliente, esArca, sesion)) {
				return new String[0];
			}
			Map<String, String> servicios = new HashMap<String, String>();
			
			CtacteDTO mov = null;
			String[] movimientos = this.getMovimientoByClienteStr(cliente, usuario, sesion);
			for(int i = 0; i < movimientos.length; i++) {
				if(movimientos[i] == null) {
					continue;
				}
		       	mov = new CtacteDTO(movimientos[i]);
	       		if(mov.getDescripcion().compareTo("VACIO") == 0) {
	       			continue;
	       		}
				String periodo    = mov.getPeriodoSort();
	       		String idServicio = String.valueOf(mov.getIdServicio());
	       		String idServicioOrig = idServicio;
	       		ServicioBean sb = (ServicioBean)sesion.get(ServicioBean.class, new Long(idServicio));
	       		if(sb.getContId() > 0) {
	           		idServicio = String.valueOf(sb.getContId());
	       		}
	       		if(servicios.containsKey(idServicio)) {
	       			idServicio = servicios.get(idServicio);
	       		} else {
	       			servicios.put(idServicio, String.valueOf(k));
	       			idServicio = String.valueOf(k++);
	       		}
	       		String clave = periodo + "|" + idServicio;
	
	       		if(fil.containsKey(clave)) {
	       			String str1 = (String)fil.get(clave);
	       			fil.put(clave, str1 + "|" + mov.toString());
	       		} else {
	       			j++;
	       			fil.put(clave, idServicioOrig + "|" + mov.toString());
				}
	      	}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String[] salida = new String[j];
		j=0;
		Iterator<String> it = Util.sortByKey(fil).iterator();
		while (it.hasNext()) {
			String s = (String)it.next();
			StringTokenizer st = new StringTokenizer(s,"|");
			
			try {
				String per = DateTimeUtil.getPeriodo(DateTimeUtil.getDateAAAAMMDD(st.nextToken() + "01"));
				String serv = st.nextToken();
				
				String s1 = fil.get(s).toString();
				salida[j++]= new String(per + "|" + serv + "|" + s1);
			} catch(Exception e) {
			}
		}
		return salida;
	}
	public List<MovimientoBean> getMovimientoFindEstado(int estado1, int estado2, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.getMovimientoFindEstado");
			q.setLong("estado1", new Long(estado1));
			q.setLong("estado2", new Long(estado2));
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				movimientos.add(m);
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return movimientos;
	}
	public List<MovimientoBean> getMovimientoByDescripcion(String descripcion, Session sesion) {
		List<MovimientoBean> movimientos = new ArrayList<MovimientoBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.getMovimientoByDescripcion");
			q.setString("descripcion", descripcion);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				movimientos.add(m);
			}
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return movimientos;
	}

}