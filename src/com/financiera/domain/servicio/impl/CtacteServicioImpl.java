package com.financiera.domain.servicio.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.RespuestaDTO;
import com.dto.ResultadoDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.service.ArchivoService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.MovimientoHistBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.bean.TemporalBaproBean;
import com.financiera.domain.bean.TemporalBicaBean;
import com.financiera.domain.bean.TemporalItauBean;
import com.financiera.domain.bean.TemporalNacionBean;
import com.financiera.domain.bean.TemporalSVilleBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.servicio.CtacteServicio;

public class CtacteServicioImpl extends AbstractService implements CtacteServicio {
	private ArchivoService  archivo  = (ArchivoService)    ServiceLocator.getInstance().getService(ArchivoService.class);
	private ClienteService  cliente  = (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);
	private ServicioService servicio = (ServicioService) ServiceLocator.getInstance().getService(ServicioService.class);
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	
	public CtacteServicioImpl(){
		super();
		this.setDescription("Servicio para Ctacte");
		this.setName("CtacteServicio");
	}

	public Collection<String> generarCtacteCuota(String fechaMesDisparo, String usuario) {
		Collection<String> salida = new ArrayList<String>();
		ServicioBean servicioBean = null;
		
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		
		ParametroBean parametro = ParametroBean.getParametro1(s); 
		EstadoMovBean estado = movimiento.getEstadoMovEstado(EstadoMovBean.E0_MES_CUOTA, s);
		try{
			transaction.begin();
			DisparoBean disparo = (DisparoBean)s.get(DisparoBean.class, new Long(1));
			Date fecha1 = null;
			try {
				fecha1 = DateTimeUtil.getFechaDia1(DateTimeUtil.getDateAAAAMMDD(fechaMesDisparo));
			} catch(Exception e) {
				salida.add("Error al obtener la fecha " + e.getMessage());
			}
			int nro = parametro.getUltimoServicio();

			boolean leer = true;
			while(leer) {
				nro++;
				servicioBean = servicio.getServicioById(new Long(nro), s);
				if(servicioBean == null) {
					leer = false;
					salida.add("Servicio no encontrado " + nro);
					nro--;
				} else {
					salida.addAll(this.generarCtacte(s, servicioBean, estado, disparo, fecha1));
				}
			}
			parametro.setUltimoServicio(nro);
			ParametroBean.updateParametro(s, parametro);
			
			transaction.commit();
			s.flush();
		} catch(Exception e) {
			salida.add(e.getStackTrace().toString());
		}
		return salida;
	}
	
	public void guardian38(Session sesion) {
		MovimientoBean m = new MovimientoBean();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findEstado38");
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				m = (MovimientoBean)mI.next();
				if(DateTimeUtil.esDia01(m.getFecha())) {
					m.setEstado(movimiento.getEstadoMovEstado(EstadoMovBean.E0_MES_CUOTA, sesion));
					sesion.update(m);
					System.out.println("Estado 38 con dia 1" + m.getCliente().getNyA() + m.getPeriodo());
				}
			}
			transaction.commit();
			sesion.flush();
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
	}
	
	private Collection<String> generarCtacte(Session session, ServicioBean svcb, EstadoMovBean estado, DisparoBean disparo, Date fecha) {
		Collection<String> salida = new ArrayList<String>();
		int u = svcb.getUltCuotaDebitada();
		for(int i=u; i<12; i++) {
			MovimientoBean m = new MovimientoBean();
			
			m.setCliente(svcb.getCliente());
			m.setIdCliente(m.getCliente().getId());
			m.setIdServicio(svcb.getId());
			m.setImporte(svcb.getImporteCuota());
			m.setDescripcion(svcb.getCliente().getDelegacion().getBanco().getDescripcion());
			m.setNroCuota(0);
			m.setTotalCuotas(svcb.getCantCuota());
			m.setEstado(estado);
			m.setFecha(fecha);
			m.setDisparo(disparo);

			session.save(m);
	
			try {
				fecha = DateTimeUtil.add1Mes(fecha);
			} catch(Exception e) {
				salida.add("Error al obtener la fecha " + e.getMessage());
			}
		}
		return salida;
	}
	
	public String procesarRespuesta(String tipo, String inputFileName, String usuario) {
		ResultadoDTO res = new ResultadoDTO();
		Collection<RespuestaDTO> r = new ArrayList<RespuestaDTO>();
		RespuestaDTO rDTO;
		r.clear();
		Date fechaMov = null;
		boolean privez = true; 
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();

			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator<String> ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea != null && linea.length() == 0) {
					continue;
				}
				if(linea.contains("CBU")) {
					continue;
				}
				res.incLeidos();

				if(tipo.compareTo(CtacteServicio.ESQ_BAPRO)==0) {
					TemporalBaproBean t = new TemporalBaproBean();
					t.setTemporalBaproBean(linea);
					
					rDTO = new RespuestaDTO();
					rDTO.setDni(Integer.parseInt(t.getIdCliente()));
					rDTO.setCbu(t.getCbu());
					rDTO.setFecha(DateTimeUtil.getDateDDMMAAAA(t.getFechaVencimiento()));
					rDTO.setImporte(Double.parseDouble(t.getImporte()) / 100);
					rDTO.setCodRechazo(t.getCodRechazo());
					rDTO.setConocido(t.esConocido());
					rDTO.setReversion(t.esReversion());
					rDTO.setCodigoInternoBanco(t.getCodigoInternoBanco());
					
					r.add(rDTO);
				} else
				if(tipo.compareTo(CtacteServicio.ESQ_SUPERVILLE)==0) {
					TemporalSVilleBean t = new TemporalSVilleBean();
					t.setTemporalSuperVilleBean(linea);
					if(t.esDebito()) {
						rDTO = new RespuestaDTO();
						
 						rDTO.setDni(Integer.parseInt(new String(t.getIdCliente()).substring(0, 8)));
						rDTO.setFecha(DateTimeUtil.getDateDDMMAAAA(t.getFechaVencimiento()));
						rDTO.setImporte(Double.parseDouble(t.getImporte()) / 100);
						if(t.getCodRechazo().compareTo("ACE")== 0) {
							t.setCodRechazo("   ");
						}
						rDTO.setCodRechazo(t.getCodRechazo());
						rDTO.setConocido(true);
						rDTO.setReversion(false);
						rDTO.setCodigoInternoBanco(null);
						
						r.add(rDTO);
					}
				} else
				if(tipo.compareTo(CtacteServicio.ESQ_NACION)==0) {
					TemporalNacionBean t = new TemporalNacionBean();
					if(linea.startsWith("1")) {
						fechaMov = DateTimeUtil.getFechaDia1(DateTimeUtil.getDateAAAAMMDD(t.getFechaTope(linea)));
					}
					if(linea.startsWith("2")) {
						if(linea.startsWith("21910CA027974175750000000000191202012110")) {
							System.out.println(linea);							
						}
						t.setTemporalNacionBean(linea);
						ClienteBean c = cliente.getClienteBySucNroCA(t.getSucursalCA(), t.getNroCA(), sesion);
						if(c == null) {
							System.out.println("no se encontro al cliente para " + t.getSucursalCA() + " "+ t.getNroCA());
							continue;
						}
						rDTO = new RespuestaDTO();
						rDTO.setDni(c.getNroDoc());
						rDTO.setSucursalCA(t.getSucursalCA());
						rDTO.setNroCA(t.getNroCA());
// Envio el dia 1 del mes del cobro para que encuentre el movimiento enviado
						rDTO.setFecha(fechaMov);
// Envio la fecha verdadera del cobro para que actualiza el sistema
						Date d = DateTimeUtil.getDateAAAAMMDD(t.getFechaCobro());
						if(d.compareTo(fechaMov) < 0 )
							rDTO.setFechaCobroReal(fechaMov);
						else
							rDTO.setFechaCobroReal(d);

						rDTO.setImporte(Double.parseDouble(t.getImporte()) / 100);
						
						rDTO.setCodRechazo(t.obtCodRechazo());
						rDTO.setConocido(true);
						rDTO.setReversion(false);
						rDTO.setCodigoInternoBanco(null);
						rDTO.setTextoError(t.getTextoError());
						
						r.add(rDTO);
					}
				}else
				if(tipo.compareTo(CtacteServicio.ESQ_ITAU)==0) {
					TemporalItauBean t = new TemporalItauBean();
					if(privez) {
						res.decLeidos();
						privez = false;
						t.setTemporalItauBeanHeader(linea);
						if(t.esTipoFileNO() && t.esTipoRegH()) {
							continue;
						} else {
							System.out.println("Tipo de archivo incorrecto.  Primer registro debe ser H y NO");
							break;
						}
					} else {
						t.setTemporalItauBean(linea);
						if(t.esTipoRegI()) {
							if(t.isAceptado() || t.isRechazado()) {
								rDTO = new RespuestaDTO();
	
								rDTO.setDni(Integer.parseInt(t.getDni()));
								rDTO.setFecha(DateTimeUtil.getDateAAAAMMDD(t.getFecha()));
								rDTO.setImporte(Double.parseDouble(t.getImporte()) / 100);
								rDTO.setConocido(true);
								rDTO.setCodRechazo(t.getCodRechazo());
								rDTO.setReversion(false);
								rDTO.setCodigoInternoBanco(new String(BancoBean.ITAU));
								
								r.add(rDTO);
							} else {
								res.incEstadoTransitorio();
							}
						} else {
							if(t.esTipoRegT()) {
								res.decLeidos();
								continue;
							} else {
								System.out.println("Tipo de registro incorrecto. Debe ser I");
							}
						}
					}
				}else
				if(tipo.compareTo(CtacteServicio.ESQ_BICA)==0) {
					TemporalBicaBean t = new TemporalBicaBean();
					t.setTemporalBicaBean(linea);
					rDTO = new RespuestaDTO();
					
					rDTO.setDni(Integer.parseInt(t.getDni()));
					rDTO.setFecha(DateTimeUtil.getDate(t.getFecha()));
					rDTO.setImporte(Double.parseDouble(t.getImporte())/100);
					rDTO.setConocido(true);
					rDTO.setCodRechazo(t.getCodRechazo().substring(0, 3));
					rDTO.setReversion(false);
					rDTO.setReversion(t.esReversion());
					rDTO.setCodigoInternoBanco(new String(BancoBean.BICA));
					if(rDTO.getCodRechazo().compareTo("Env")==0) {
						res.incEnviadas();
					} else {
						r.add(rDTO);
					}
				}
			} 
//			this.generarCarta(r);
			res = new ResultadoDTO(movimiento.actualizarRespuesta(cliente, servicio, r, res.toString(), usuario));
			if(res.getGrabados() > 0 ) {
				res = new ResultadoDTO(movimiento.limpiarRespuestas(res.toString(), usuario));
			}
			
		} catch (Exception e) {
			System.out.println("No se procesaron las respuestas " + e.getMessage());
		}
		return res.toString();
	}
	public void delMovimientoADisparar(Long idCliente, String usuario) throws Exception {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();

			ClienteBean c = cliente.getClienteById(idCliente, sesion);
			
			movimiento.delMovimientoADisparar(c, usuario);
			
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}
	public Collection<String> guardianes(String fDesde, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Collection<String> salida = new ArrayList<String>();
		
		salida.addAll(this.guardianImporteMovimiento(sesion));
		salida.addAll(this.guardianCuotasMensuales(fDesde, sesion));
		return salida;
	}
	public Collection<String> guardianImporteMovimiento(Session sesion) {
		Collection<String> salida = new ArrayList<String>();
		MovimientoBean movimientoBean = null;
		double importeServicio = 0.0;
		double importeMovimiento = 0.0;
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findByPendDisparo");
			q.setLong("estado", EstadoMovBean.E0_MES_CUOTA);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				movimientoBean = (MovimientoBean)mI.next();
				importeMovimiento = movimientoBean.getImporte();
				
				importeServicio = servicio.getServicioById(new Long(movimientoBean.getIdServicio()), sesion).getImporteCuota();
				
				if(importeMovimiento != importeServicio) {
					String s = new String("Dif.Cuota --> Cliente:" + movimientoBean.getCliente().getNyA() +
							" Servicio:" + importeServicio + " Movimiento:" + importeMovimiento);
					salida.add(s);
//					System.out.println(s);
					
					movimientoBean.setImporte(importeServicio);
					sesion.update(movimientoBean);
				}
			}
			sesion.flush();
			transaction.commit();
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return salida;
	}
	public Collection<String> guardianCuotasMensuales(String fechaDesde, Session sesion) {
/**
 * Guardian que esten todas las cuotas mensuales
 * Guardian que no haya mas de una cuota mensual
 */
		Collection<String> salida = new ArrayList<String>();
		MovimientoBean movimientoBean = null;
		Long servicioId = new Long(0);
		Date fecha = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			Date fDesde = DateTimeUtil.getDateAAAAMMDD(fechaDesde);
			Date fPrimerCuota = DateTimeUtil.add1Mes(fDesde); 
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.findOrderServicio");
			q.setDate("fDesde", fDesde);
			q.setLong("estado1", movimiento.getEstadoMovEstado(EstadoMovBean.E0_MES_CUOTA, sesion).getId()); // 1
			q.setLong("estado2", movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion).getId()); // 38
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				movimientoBean = (MovimientoBean)mI.next();
				if(movimientoBean.getId() == 11556) {
					System.out.println(movimientoBean.getId());
				}
				if(servicioId.compareTo(movimientoBean.getIdServicio()) != 0 ||
						DateTimeUtil.getPeriodo(movimientoBean.getFecha()).compareTo(DateTimeUtil.getPeriodo(fecha)) !=0) {
					servicioId = movimientoBean.getIdServicio();
					fecha = movimientoBean.getFecha();
				} else {
					if(DateTimeUtil.getPeriodo(movimientoBean.getFecha()).compareTo(DateTimeUtil.getPeriodo(fecha)) == 0) {
						String s = new String("Se elimina cuota mensual porque sobra - Cliente:" + movimientoBean.getCliente().getNyA() +
								" Periodo:" + DateTimeUtil.formatDateDD_MM_AAAA(movimientoBean.getFecha()));
						System.out.println(s);
						sesion.delete(movimientoBean);
						fecha = DateTimeUtil.sub1Mes(fecha);
					}
				}
			}
			sesion.flush();
			transaction.commit();
		} catch(Exception e) {
			System.out.println("MovimientoService : " + e.getMessage());
		}
		return salida;
	}
	private void agregarCuota(MovimientoBean movimientoBean, Date fecha, Session session) {
		MovimientoBean m = new MovimientoBean();
		m.setIdCliente(movimientoBean.getIdCliente());
		m.setCliente(movimientoBean.getCliente());
		m.setIdServicio(movimientoBean.getIdServicio());
		m.setFecha(fecha);
		m.setImporte(movimientoBean.getImporte());
		m.setDescripcion(movimientoBean.getDescripcion());
		m.setNroCuota(movimientoBean.getNroCuota());
		m.setTotalCuotas(movimientoBean.getTotalCuotas());
		m.setEstado(movimientoBean.getEstado());
		m.setDisparo(movimientoBean.getDisparo());
		session.save(m);
	}
	private void generarCarta(Collection<RespuestaDTO> r) {
		Iterator<RespuestaDTO> i = r.iterator();
		while(i.hasNext()) {
			RespuestaDTO rDTO = i.next();
			
			if(rDTO.getCodRechazo().compareTo("R15") == 0) {
			
			}
		}
	}
	public void actualizarHistorico(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			Collection<ServicioBean> s = servicio.getServiciosHist(sesion);
			transaction.begin();
			Iterator<ServicioBean> iS = s.iterator();
			while(iS.hasNext()) {
				ServicioBean sb = (ServicioBean) iS.next();

				this.pasarCtacteaHist(sesion, sb);
			}
			sesion.flush();
			transaction.commit();
		}catch(Exception e) {
			System.out.println("CtacteServicioImpl : " + e.getMessage());
		}
	}
	public void actualizarHistorico(ServicioBean sb, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();

			this.pasarCtacteaHist(sesion, sb);

			sesion.flush();
			transaction.commit();
		}catch(Exception e) {
			System.out.println("CtacteServicioImpl : " + e.getMessage());
		}
	}
	private void pasarCtacteaHist(Session session, ServicioBean sb) {
		MovimientoBean movimientoBean = null;
		MovimientoHistBean movimientoHistBean = null;

		Query q = session.getNamedQuery("MovimientoBean.findByServicio");
		q.setLong("idServicio", sb.getId());
		Iterator<MovimientoBean> mI = q.list().iterator();
		while(mI.hasNext()) {
			movimientoBean = (MovimientoBean)mI.next();
	
			movimientoHistBean = new MovimientoHistBean();
			movimientoHistBean.setId(movimientoBean.getId());
			movimientoHistBean.setIdCliente(movimientoBean.getIdCliente());
			movimientoHistBean.setCliente(movimientoBean.getCliente());
			movimientoHistBean.setIdServicio(movimientoBean.getIdServicio());
			movimientoHistBean.setFecha(movimientoBean.getFecha());
			movimientoHistBean.setImporte(movimientoBean.getImporte());
			movimientoHistBean.setDescripcion(movimientoBean.getDescripcion());
			movimientoHistBean.setNroCuota(movimientoBean.getNroCuota());
			movimientoHistBean.setTotalCuotas(movimientoBean.getTotalCuotas());
			movimientoHistBean.setEstado(movimientoBean.getEstado());
			movimientoHistBean.setDisparo(movimientoBean.getDisparo());
			
			session.save(movimientoHistBean);
			session.delete(movimientoBean);
		}
	}
	public void depurarHistorico(String usuario, String fecha) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);

		Collection<ServicioBean> serv = new ArrayList<ServicioBean>();				
		int cantServHist = 0;
		int cantServHistFMenor = 0;
		Transaction transaction = sesion.beginTransaction();
		try {
			Date fHasta = DateTimeUtil.getDateAAAAMMDD(fecha);
			String fileName = "C:\\Servina\\MovimientosHist" + fecha + ".txt";
			PrintWriter fileNameII = archivo.abrirFileSalida(fileName);				
						
			Collection<ServicioBean> s = servicio.getServiciosHist(sesion);
			transaction.begin();
			Iterator<ServicioBean> iS = s.iterator();
			while(iS.hasNext()) {
				ServicioBean sb = (ServicioBean) iS.next();
				cantServHist++;
				if(sb.getFechaVenta() == null || sb.getFechaVenta().before(fHasta)) {
					if(this.obtCtacteHist(sesion,sb,fHasta)) {
						serv.add(sb);
//						System.out.println(sb);
						cantServHistFMenor++;
					}
				}
			}
			System.out.println("CantServHist       : " + cantServHist);
			System.out.println("CantServHistFMenor : " + cantServHistFMenor);
		
			Iterator<ServicioBean> servi = serv.iterator();
			while(servi.hasNext()) {
				ServicioBean sb = (ServicioBean) servi.next();
				Query q = sesion.getNamedQuery("MovimientoHistBean.findByServicio");
				q.setLong("idServicio", sb.getId());
				Iterator<MovimientoHistBean> mI = q.list().iterator();
				while(mI.hasNext()) {
					MovimientoHistBean mhBean = (MovimientoHistBean)mI.next();
					sesion.delete(mhBean);
					fileNameII.println(mhBean.backupBajar());
				}
			}
			sesion.flush();
			fileNameII.close();
			transaction.commit();
		}catch(Exception e) {
			System.out.println("CtacteServicioImpl : " + e.getMessage());
		}
	}
	private boolean obtCtacteHist(Session session, ServicioBean sb, Date fHasta) {
		boolean aux = true;
		MovimientoHistBean mhBean = null;

		Query q = session.getNamedQuery("MovimientoHistBean.findByServicio");
		q.setLong("idServicio", sb.getId());
		Iterator<MovimientoHistBean> mI = q.list().iterator();
		while(mI.hasNext()) {
			mhBean = (MovimientoHistBean)mI.next();
			if(mhBean.getFecha().after(fHasta)) {
				aux = false;
//				System.out.println(mhBean);
			}
		}
		return aux;
	}
	private void deleteCtacteHist(Session sesion, ServicioBean sb, PrintWriter fileNameII) {

	}

}