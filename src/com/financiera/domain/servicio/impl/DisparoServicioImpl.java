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

import com.dto.MovTotTipoDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.bean.TemporalBaproBean;
import com.financiera.domain.bean.TemporalBicaBean;
import com.financiera.domain.bean.TemporalItauBean;
import com.financiera.domain.bean.TemporalNacionBean;
import com.financiera.domain.bean.TemporalSVilleBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.servicio.DisparoServicio;


public class DisparoServicioImpl extends AbstractService implements DisparoServicio {
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	private DelegacionService delegacion = (DelegacionService) ServiceLocator.getInstance().getService(DelegacionService.class);
	private ClienteService    cliente    = (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);
	
	private PrintWriter printerBapro = null;
	private PrintWriter printerSVille = null;
	private PrintWriter printerNacion = null;
	private PrintWriter printerItau = null;
	private boolean itauPriVez = true;
	private PrintWriter printerBica = null;
	private double aux = 0;
	private int baproCantRegistros = 0;
	private double baproImporteTotal = 0;
	private int svilleCantRegistros = 0;
	private double svilleImporteTotal = 0;
	private int itauCantRegistros = 0;
	private double itauImporteTotal = 0;
	private int bicaCantRegistros = 0;
	private double bicaImporteTotal = 0;
	private int nacionCantRegistros = 0;
	private double nacionImporteTotal = 0;
	private DisparoBean disparoBapro;
	private DisparoBean disparoSVille;
	private DisparoBean disparoNacion;
	private DisparoBean disparoItau;
	private DisparoBean disparoBica;
	private boolean bicaPriVez = true;
	private ParametroBean parametro;
	private int nroEnvio;
	private Date fechaGeneracionArchivo;
	
	public DisparoServicioImpl(){
		super();
		this.setDescription("Servicio para Disparos");
		this.setName("DisparoService");
	}

	public void generarCuotasDelegacion(Long idDelegacion, String fechaMesDisparo, String usuario) throws Exception {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Collection<DelegacionBean> delegaciones= new ArrayList<DelegacionBean>();
		delegaciones.add(delegacion.getDelegacion(idDelegacion, sesion));
		
		Date fechaReferencia = DateTimeUtil.getDate(fechaMesDisparo);

		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();		
// Obtengo todos los clientes de las delegaciones
			List<ClienteBean> clientes = cliente.getClienteFindDelegacion(delegaciones, sesion);
		
// solo tomo los clientes en estado ACTIVO
			List<ClienteBean> clientesAFact = new ArrayList<ClienteBean>();
			Iterator j = clientes.iterator();
			while(j.hasNext()) {
				ClienteBean c = (ClienteBean) j.next();
				if(c.getEstado() == ClienteBean.ACTIVO) {
					clientesAFact.add(c);
				}
			}
			this.generarMovimientoMensual(clientesAFact, fechaReferencia, movimiento, sesion);
			
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}
	private void generarMovimientoMensual(List<ClienteBean> clientes, Date fechaReferencia, MovimientoService movimiento, Session sesion) {
		int cantClientes = 0;
		Transaction transaction = sesion.beginTransaction();
		EstadoMovBean estadoAdisparar = (EstadoMovBean)sesion.get(EstadoMovBean.class, new Long(1)); // A_DISPARAR;
		try{
			transaction.begin();
// Recorro las delegaciones que se eligieron
			Iterator i = clientes.iterator();
			while(i.hasNext()) {
				ClienteBean cliente = (ClienteBean)i.next();
				cantClientes++;
// la idea es para todos los clientes busco en la ctacte y despliego los movimientos que hay
				movimiento.generarMovimiento(cliente, fechaReferencia, DateTimeUtil.getPeriodo(fechaReferencia), sesion, estadoAdisparar);
				if(cantClientes % 50 == 0) {

					transaction.commit();
					
					transaction = sesion.beginTransaction();
					transaction.begin();
				}
			}
		} catch(Exception e) {
			System.out.println("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}
	
	public Collection<String> generarMovimiento(List<ClienteBean> clientes, Date fechaReferencia, MovimientoService movimiento, Session sesion) {
		Collection<String> salida = new ArrayList<String>();
		int cantClientes = 0;
		Transaction transaction = sesion.beginTransaction();
		EstadoMovBean estadoAdisparar = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion);
		try{
			transaction.begin();
// Recorro las delegaciones que se eligieron
			Iterator i = clientes.iterator();
			while(i.hasNext()) {
				ClienteBean cliente = (ClienteBean)i.next();
				cantClientes++;
// la idea es para todos los clientes busco en la ctacte y despliego los movimientos que hay
				movimiento.generarMovimiento(cliente, fechaReferencia, DateTimeUtil.getPeriodo(fechaReferencia), sesion, estadoAdisparar);
				if(cantClientes % 50 == 0) {

					transaction.commit();
					
					transaction = sesion.beginTransaction();
					transaction.begin();
				}
			}
		} catch(Exception e) {
			salida.add("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		salida.add("Clientes: " + cantClientes);

		return salida;
	}
	public Collection<String> generarMovimientoDisparo(List<ServicioBean> servicioAFact, Date fechaReferencia, MovimientoService movimiento, boolean validarMovExistente, Session sesion) {
		Collection<String> salida = new ArrayList<String>();
		int cantClientes = 0;
		Transaction transaction = sesion.beginTransaction();
		EstadoMovBean estadoCuota     = movimiento.getEstadoMovEstado(EstadoMovBean.E0_MES_CUOTA, sesion);
		EstadoMovBean estadoAdisparar = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion);
		EstadoMovBean estadoDividido  = movimiento.getEstadoMovEstado(EstadoMovBean.E0_DIVIDIDO, sesion);
		try{
			transaction.begin();
// Recorro las delegaciones que se eligieron
			Iterator i = servicioAFact.iterator();
			while(i.hasNext()) {
				ServicioBean s = (ServicioBean)i.next();
				cantClientes++;
//				System.out.println(cantClientes + " " + s.getCliente().getId() + " " + s.getId() + s.getCliente().getNyA());
//				if(s.getCliente().getNroDoc() == 4505212) {
//					System.out.println("abcd");
//				}
// la idea es para todos los clientes busco en la ctacte y despliego los movimientos que hay
				movimiento.generarMovimiento(fechaReferencia, s, DateTimeUtil.getPeriodo(fechaReferencia), sesion,
											 estadoAdisparar, validarMovExistente, estadoCuota.getId(),
											 estadoAdisparar.getId(), estadoDividido.getId());
				if(cantClientes % 2 == 0) {

					transaction.commit();
					
					transaction = sesion.beginTransaction();
					transaction.begin();
				}
			}
			transaction.commit();
			sesion.flush();
		} catch(Exception e) {
			salida.add("MovimientoService" + e.getMessage());
		}
		salida.add("Clientes: " + cantClientes);

		return salida;
	}
	public Collection<String> generarMovimientoDisparoDupInteres(List<ServicioBean> servicioAFact, Date fechaReferencia, MovimientoService movimiento, Session sesion) {
		Collection<String> salida = new ArrayList<String>();
		double importeTotal = 0;
		int cantClientes = 0;
		MovTotTipoDTO mttDto = null;
		Transaction transaction = sesion.beginTransaction();
		EstadoMovBean estadoAdisparar = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion);
		try{
			transaction.begin();
// Recorro las delegaciones que se eligieron
			Iterator i = servicioAFact.iterator();
			while(i.hasNext()) {
				ServicioBean s = (ServicioBean)i.next();
				importeTotal = s.getImporteCuota() * s.getCantCuota();
				if(s.getCliente().getNroDoc() == 4505212) {
					System.out.println("aa");
				}
				mttDto = movimiento.getMovimientoByServicio(s.getId(), sesion);
				
				if(mttDto.getCantCuotasCobradas() < (s.getUltCuotaDebitada() - 1) && mttDto.getCantMesesSinCobro() > 0 &&
				   mttDto.getImporteCobrado() < importeTotal) {
					
					cantClientes++;
//					System.out.println(cantClientes + " " + s.getCliente().getId() + " " + s.getId() + s.getCliente().getNyA());
// Agrego la cuota de interes
					MovimientoBean m = new MovimientoBean();
					m.setFecha(DateTimeUtil.getDateAAAAbMMbDD((DateTimeUtil.getFecha1_Periodo(DateTimeUtil.getPeriodo(fechaReferencia)))));
					movimiento.generarMovPorDiaDisparo(s, m, estadoAdisparar, sesion, false, new Double(0.0));
					if(cantClientes % 50 == 0) {
						transaction.commit();
						
						transaction = sesion.beginTransaction();
						transaction.begin();
					}
				}
			}
		} catch(Exception e) {
			salida.add("MovimientoService" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		salida.add("Clientes: " + cantClientes);

		return salida;
	}	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public Collection<String> generarDisparoBaproSVilleItauBica(List<ServicioBean> servicios, String periodo, String periodoAnterior, Date fechaGeneracionArchivo, PrintWriter printerBapro, PrintWriter printerSVille, PrintWriter printerItau, PrintWriter printerBica, Session sesion) {
		Collection<String> salida = new ArrayList<String>();

		this.printerBapro = printerBapro;
		this.printerSVille = printerSVille;
		this.printerItau = printerItau;
		this.printerBica = printerBica;
		
		salida.add("");
		salida.add("TemporalBaproBean eliminados: " + TemporalBaproBean.clearTabla(sesion));
		salida.add("TemporalSVilleBean eliminados: " + TemporalSVilleBean.clearTabla(sesion));

		baproCantRegistros = 0;
		baproImporteTotal = 0;
		svilleCantRegistros = 0;
		svilleImporteTotal = 0;
		itauCantRegistros = 0;
		itauImporteTotal = 0;
		bicaCantRegistros = 0;
		bicaImporteTotal = 0;
		
		EstadoMovBean estadoAdisparar = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion);
		EstadoMovBean estadoDisparado = movimiento.getEstadoMovEstado(EstadoMovBean.E1_DISPARADO, sesion);
		
		disparoBapro = new DisparoBean();
		disparoBapro.getNewDisparo(sesion);
		
		disparoSVille = new DisparoBean();
		disparoSVille.getNewDisparo(sesion);

		disparoItau = new DisparoBean();
		disparoItau.getNewDisparo(sesion);

		disparoBica = new DisparoBean();
		disparoBica.getNewDisparo(sesion);

		nroEnvio = disparoItau.getId().intValue();
		
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			parametro = ParametroBean.getParametro1(sesion);
			this.fechaGeneracionArchivo = fechaGeneracionArchivo;		
			
			Query q = sesion.getNamedQuery("MovimientoBean.findByPendDisparo");
			q.setLong("estado", estadoAdisparar.getId());
			salida.add("Movimientos generados provenientes de ingresos Manuales y de Servicios : " +
					this.generarMovimientos(servicios, q, periodo, periodoAnterior, parametro, estadoDisparado, sesion));
			
			this.generarTotalSVille();
			if(!itauPriVez) {
				this.generarTotalItau(parametro, nroEnvio, fechaGeneracionArchivo);
			}
		} catch (Exception ex) {
			salida.add("GenerarDisparo - Error : " + ex.getMessage());
		}			

		transaction = sesion.beginTransaction();
		try {
			transaction.begin();

			System.out.println("importe total Bapro " + baproImporteTotal);
			aux = Math.round(baproImporteTotal * 100.0)/100.0;
			baproImporteTotal = aux;
			disparoBapro.setCantRegistros(baproCantRegistros);
			disparoBapro.setImporte(baproImporteTotal);
			disparoBapro.updateDisparo(sesion);

			disparoSVille.setCantRegistros(svilleCantRegistros);
			disparoSVille.setImporte(svilleImporteTotal);
			disparoSVille.updateDisparo(sesion);

			disparoItau.setCantRegistros(itauCantRegistros);
			disparoItau.setImporte(itauImporteTotal);
			disparoItau.updateDisparo(sesion);
			
			disparoBica.setCantRegistros(bicaCantRegistros);
			disparoBica.setImporte(bicaImporteTotal);
			disparoBica.updateDisparo(sesion);

			salida.add("Bapro : Cant registros: " + baproCantRegistros  + " Importe total: " + baproImporteTotal);
			salida.add("SVille: Cant registros: " + svilleCantRegistros + " Importe total: " + svilleImporteTotal);
			salida.add("Itau  : Cant registros: " + itauCantRegistros   + " Importe total: " + itauImporteTotal);
			salida.add("Bica  : Cant registros: " + bicaCantRegistros   + " Importe total: " + bicaImporteTotal);

			transaction.commit();

		} catch (Exception ex) {
			salida.add("GenerarDisparo - Error : " + ex.getMessage());
		} finally {			
			sesion.flush();

			printerBapro.flush();
			printerBapro.close();
			printerSVille.flush();
			printerSVille.close();
			printerItau.flush();
			printerItau.close();
			printerBica.flush();
			printerBica.close();
		}
		return salida;
	}
	public Collection<String> generarDisparoNacion(String mes, String nroDisquette, String fechaTope,
											List<ServicioBean> servicios, String periodo, String periodoAnterior,
											PrintWriter printerNacion, Session sesion) {
		Collection<String> salida = new ArrayList<String>();

		this.printerNacion = printerNacion;
		
		salida.add("");
		salida.add("TemporalNacionBean eliminados: " + TemporalNacionBean.clearTabla(sesion));

		nacionCantRegistros = 0;
		nacionImporteTotal = 0;
		
		EstadoMovBean estadoAdisparar = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR, sesion);
		EstadoMovBean estadoDisparado = movimiento.getEstadoMovEstado(EstadoMovBean.E1_DISPARADO, sesion);
		
		disparoNacion = new DisparoBean();
		disparoNacion.getNewDisparo(sesion);

		Transaction transaction = sesion.beginTransaction();
		try {
			this.generarHeadNacion(mes, nroDisquette, DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate(fechaTope)));
			
			transaction.begin();
			ParametroBean parametro = ParametroBean.getParametro1(sesion);
			Query q = sesion.getNamedQuery("MovimientoBean.findByPendDisparo");
			q.setLong("estado", estadoAdisparar.getId());
			salida.add("Movimientos generados provenientes de ingresos Manuales y de Servicios : " +
			this.generarMovimientos(servicios, q, periodo, periodoAnterior, parametro, estadoDisparado, sesion));
			
			this.generarTotalNacion();
			
			disparoNacion.setCantRegistros(nacionCantRegistros);
			disparoNacion.setImporte(nacionImporteTotal);
			sesion.update(disparoNacion);

			salida.add("Nacion: Cant registros: " + nacionCantRegistros + " Importe total: " + nacionImporteTotal);
		} catch (Exception ex) {
			salida.add("GenerarDisparo - Error : " + ex.getMessage());
		} finally {
			try {
				transaction.commit();
			} catch(Exception ex ){ }
			sesion.flush();
			printerNacion.flush();
			printerNacion.close();
		}
		return salida;
	}
//******************************************************************************************************************
	private int generarMovimientos(List<ServicioBean> servicios, Query q, String periodo, String periodoAnterior, ParametroBean parametro, EstadoMovBean estadoDisparado, Session sesion) {
		CacheManager.getInstance().resetearDisparoEntidades(sesion);
		int cant = 0;
		Collection mov = q.list();
		Iterator mI = mov.iterator();
		try {
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
	
				boolean enc = false;
				Iterator ci = servicios.iterator();
				while(ci.hasNext() && !enc) {
					ServicioBean serv = (ServicioBean)ci.next();
					if(serv.getCliente().getId().compareTo(m.getCliente().getId()) == 0) {
						enc = true;
					}
				}
				if(enc) {
					String mes = DateTimeUtil.formatPeriodoAAAAMMDD(m.getFecha());
					
					DisparoEntidadesBean deBean = CacheManager.getInstance().getDisparoEntidades(m.getIdCliente(), DisparoEntidadesBean.CLIENTE);
					if(deBean == null) {
	//	Busco por Id de delegacion
						deBean = CacheManager.getInstance().getDisparoEntidades(m.getCliente().getDelegacion().getId(), DisparoEntidadesBean.DELEGACION);					
					}
					if(m.getCliente().esquemaItau()) {
						m.setDisparo(disparoItau);
						this.generarDetItau(m);
						if(deBean != null) {
							deBean.setDisparo(disparoItau);
						}
					} else {
						if(m.getCliente().esquemaBica()) {
							m.setDisparo(disparoBica);
							this.generarDetBica(m);
							if(deBean != null) {
								deBean.setDisparo(disparoBica);
							}
						} else {
							if(m.getCliente().esquemaNacion()) {
								this.generarDetNacion(m, parametro, sesion);
								m.setDisparo(disparoNacion);
								if(deBean != null) {
									deBean.setDisparo(disparoNacion);
								}
							} else {
								this.generarSeqBapro(m, parametro, sesion);
								m.setDisparo(disparoBapro);
								if(deBean != null) {
									deBean.setDisparo(disparoBapro);
								}
								if(m.getCliente().esquemaSVille()) {
									this.generarSeqSuperVille(m, parametro, sesion);
									m.setDisparo(disparoSVille);
									if(deBean != null) {
										deBean.setDisparo(disparoSVille);
									}
								}
							}
						}
					}
					m.setEstado(estadoDisparado);
					sesion.update(m);
					cant++;
					if(deBean != null) {
						delegacion.persistirDisparoEntidadesSesion(deBean, sesion);					
					}
				}
			}
		}
		catch (Exception e) {	
			System.out.println("Error DisparoServicioImpl.generarMovimientos" + e.getMessage());
		} 
		sesion.update(parametro);
		return cant;
	}
	private void generarSeqBapro(MovimientoBean m, ParametroBean parametro, Session sesion) {
		baproCantRegistros++;
		baproImporteTotal += m.getImporte();
	
		TemporalBaproBean t = new TemporalBaproBean();
		BancoBean b = m.getCliente().getDelegacion().getBanco();
		
		t.setCodigoInternoBanco(String.valueOf(b.getCodigoDebito())); // pic 99
		t.setFechaVencimiento(DateTimeUtil.formatDateDDMMAAAA(m.getFecha()));
		t.setCodigoEmpresa(new String("000000"));

// pic 9(8) + pic x(14) total 22
		String s = Util.formateo(Util.NUMERICO, 8, m.getCliente().getNroDoc(), false);
		s = Util.formateo(Util.TEXTO, 22, s, false);
		t.setIdCliente(s);

		t.setTipoMoneda(parametro.getMoneda());
// pic 9(22)
		t.setCbu(Util.formateo(Util.TEXTO, 22, m.getCliente().getCbu(), false));
		
		// pic 9(10)
		t.setImporte(Util.formateo(Util.NUMERICO, 10, m.getImporte(), true));
// pic 9(11)
		t.setCuitEmpresa(Util.formateo(Util.NUMERICO, 11, parametro.getCuitEmpresa(), false));
// pic x(10)		
		t.setDescripPrestacion(Util.formateo(Util.TEXTO, 10, b.getDescripPrestacion(), false));
		
// formato pic 9(15)
		t.setReferenciaUnivDebito(Util.formateo(Util.NUMERICO, 15, parametro.dameIndice(), false));

		t.setReservado("               "); // pic x(15)
		t.setNuevoIdCliente("                      "); // pic x(22)
		t.setCodRechazo("   "); // pic x(3)
// pic x(16)
		t.setNombreEmpresa(Util.formateo(Util.TEXTO, 16, parametro.getNombreEmpresa(), false));

		sesion.save(t);
		printerBapro.println(t.toStringBapro());
	}
	
	private void generarSeqSuperVille(MovimientoBean m, ParametroBean parametro, Session sesion) {
		svilleCantRegistros++;
		svilleImporteTotal += m.getImporte();

		TemporalSVilleBean t = new TemporalSVilleBean();

		t.setTipoNovedadDebito();
		
		t.setearDetalle(parametro, m);

		sesion.save(t);
		printerSVille.println(t.toStringDetalle());
	}
	private void generarTotalSVille() {
		TemporalSVilleBean t = new TemporalSVilleBean();
		t.setTipoNovedadTotal();
		aux = Math.round(svilleImporteTotal * 100.0)/100.0;
		svilleImporteTotal = aux;
		t.setearTotal(svilleCantRegistros, svilleImporteTotal);
		printerSVille.println(t.toStringTotal());
	}
//**********************************************************************************************
	private void generarHeadNacion(String mes, String nroDisquette, String fechaTope) {
		TemporalNacionBean t = new TemporalNacionBean();
		t.setCajaAhorroEmpresa("3200104740082578");
		t.setMes(mes);
		t.setNroDisquette(nroDisquette);
		t.setFechaTope(fechaTope);
		
		printerNacion.println(t.toStringHeader());
	}
	private void generarDetNacion(MovimientoBean m, ParametroBean parametro, Session sesion) {
		try {
			nacionCantRegistros++;
			nacionImporteTotal += m.getImporte();
	
			TemporalNacionBean t = new TemporalNacionBean();
			t.setSucursalCA(m.getCliente().getSucursalCA());
			t.setNroCA(m.getCliente().getNroCA());
			t.setImporte(Util.formateo(Util.NUMERICO, 15, m.getImporte(), true));
	
			sesion.save(t);
			printerNacion.println(t.toStringDetalle());
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void generarTotalNacion() {
		TemporalNacionBean t = new TemporalNacionBean();
		aux = Math.round(nacionImporteTotal * 100.0)/100.0;
		nacionImporteTotal = aux;
		t.setImporte(Util.formateo(Util.NUMERICO, 15, nacionImporteTotal, true));
		t.setCantRegistros(Util.formateo(Util.NUMERICO, 6, nacionCantRegistros, false));
		
		printerNacion.println(t.toStringTotal());
	}
//*********************************************************************************************
	private void generarHeadItau(MovimientoBean m) {
		TemporalItauBean t = new TemporalItauBean();
		t.setCuitEmpresa(parametro.getCuitEmpresa());
		t.setNroEnvio(nroEnvio);
		t.setFechaGeneracionArchivo(fechaGeneracionArchivo);
		t.setSecuencial(m.getDisparo().getId());
		printerItau.println(t.toStringHeader());
	}
	private void generarDetItau(MovimientoBean m) {
		if(itauPriVez) {
			itauPriVez = false;
			this.generarHeadItau(m);
		}
		try {
			itauCantRegistros++;
			itauImporteTotal += m.getImporte();
	
			TemporalItauBean t = new TemporalItauBean();

			printerItau.println(t.toStringDetalle(m));
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void generarTotalItau(ParametroBean parametro, int nroEnvio, Date fechaGeneracionArchivo) {
		TemporalItauBean t = new TemporalItauBean();
		t.setCuitEmpresa(parametro.getCuitEmpresa());
		t.setNroEnvio(nroEnvio);
		t.setFechaGeneracionArchivo(fechaGeneracionArchivo);

		aux = Math.round(itauImporteTotal * 100.0)/100.0;
		itauImporteTotal = aux;
		
		printerItau.println(t.toStringTotal(itauImporteTotal, itauCantRegistros));
	}
//*********************************************************************************************
	private void generarDetBica(MovimientoBean m) {
		try {
			bicaCantRegistros++;
			bicaImporteTotal += m.getImporte();
	
			TemporalBicaBean t = new TemporalBicaBean();
			if(bicaPriVez)
				bicaPriVez = false;
			else
				printerBica.println();
			
			printerBica.print(t.toStringDetalle(m));
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}