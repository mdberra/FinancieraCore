package com.financiera.domain.servicio.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ClienteDTO;
import com.dto.ServicioDTO;
import com.dto.VendedorDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.PersistenceService;
import com.financiera.core.server.Persistencia;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.service.ArchivoService;
import com.financiera.core.service.PersistenciaService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DiasCobroBean;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.EntidadBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.MovimientoHistBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.bean.Persistible;
import com.financiera.domain.bean.ProductoBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.bean.UsuarioBean;
import com.financiera.domain.bean.VendedorBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.service.UsuarioService;
import com.financiera.domain.servicio.OperacionesServicio;

public class OperacionesServicioImpl extends AbstractService implements OperacionesServicio {
	private ArchivoService archivo = (ArchivoService)ServiceLocator.getInstance().getService(ArchivoService.class);
	private Collection<Object> objectosPersistir = new ArrayList<Object>();
	private PersistenciaService persistencia = (PersistenciaService) ServiceLocator.getInstance().getService(PersistenciaService.class);
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	private ClienteService  cliente  = (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);
	private ServicioService servicio = (ServicioService) ServiceLocator.getInstance().getService(ServicioService.class);	private ServicioService  servicioServ = (ServicioService)    ServiceLocator.getInstance().getService(ServicioService.class);
	private long indice = 0l;
	private Collection<String> informe = new ArrayList<String>();
	
	private void clearInforme() {
		informe.clear();
	}
	public String[] Backup(String usuario) {
		this.clearInforme();
		
		String fechaHoy = DateTimeUtil.formatDateTime1(DateTimeUtil.getDate());
		String outputFileName = new String("C:\\Servina\\Backups\\bajada_" + fechaHoy + ".txt");
//		String outputFileName = new String("//home//marce//Desktop//Liberdina//Servina//Backups//bajada_" + fechaHoy + ".txt");

		ServiceLocator.getInstance().removeService(persistencia);
		ServiceLocator.getInstance().addServervice(PersistenceService.class, new PersistenceService());
		
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		PrintWriter p = null;
		try{
			p = archivo.abrirFileSalida(outputFileName);
			transaction.begin();

			Query q = s.getNamedQuery("UsuarioBean.getAll");
			q.setReadOnly(true);
			this.comun(q, p);
			q = s.getNamedQuery("LocalizacionBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("ParametroBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("BancoBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DisparoBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DisparoEntidadesBean.getBackup");
			this.comun(q, p);
			q = s.getNamedQuery("VendedorBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("EntidadBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DelegacionBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DiasCobroBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("ProductoBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("ClienteBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("ServicioBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("MovimientoBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("MovimientoHistBean.getAll");
			this.comun(q, p);
			
			//estadoMov grupopermiso permiso usuario

		} catch(Exception e) {
			informe.add(e.getMessage());
		} finally {
			p.flush();
			p.close();
		}
		return this.informeToString();
	}
	public String[] BackupArca(String usuario) {
		this.clearInforme();
		
		String fechaHoy = DateTimeUtil.formatDateTime1(DateTimeUtil.getDate());
		String outputFileName = new String("C:\\Servina\\Backups\\bajada_Arca_" + fechaHoy + ".txt");
		
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		PrintWriter p = null;
		try{
			p = archivo.abrirFileSalida(outputFileName);
			transaction.begin();
			Query q = s.getNamedQuery("ServicioBean.getAll");
			this.armarServicioArca(q, p, s);

		} catch(Exception e) {
			informe.add(e.getMessage());
		} finally {
			p.flush();
			p.close();
		}
		return this.informeToString();
	}
	private void armarServicioArca(Query q, PrintWriter p, Session s) {
		try {
			Iterator<ServicioBean> aI = q.list().iterator();
			while(aI.hasNext()) {
				ServicioBean svc = (ServicioBean)aI.next();
				ServicioDTO sd = new ServicioDTO(svc.toString());
				if(sd.getVendedor().esArca()) {
//					if(svc.getCliente().getId().compareTo(new Long(1873))==0) {
//						System.out.println(svc.getCliente().getId());
//					}
					Query q1 = s.getNamedQuery("MovimientoBean.getAllbyServicio");
					q1.setLong("idServicio", svc.getId());
					Iterator<MovimientoBean> mI = q1.list().iterator();
					while(mI.hasNext()) {
						MovimientoBean mb = (MovimientoBean)mI.next();
						
						if(mb.getIdServicio().compareTo(svc.getId())==0) {
							mb.setDescripcion(String.valueOf(svc.getCliente().getNroDoc()));
							mb.setNroCuota(svc.getVendedor().getVendedorArca());
							p.println(mb.backupBajar());
						}
					}
				}
			}
		} catch(Exception e) {
			informe.add(e.getMessage());
		}
	}
	public String[] RestoreArca(String inputFileName, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		this.clearInforme();
		objectosPersistir.clear();
		int i = 0;
		try {
			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator<String> ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("movimiento;")) {
					this.movimientoArca(linea, sesion);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(e.getMessage());
		}
		return this.informeToString();
	}
	
	private String[] informeToString() {
		int j = 0;
		String[] s = new String[informe.size()];
		Iterator<String> i = informe.iterator();
		while(i.hasNext()) {
			s[j++] = (String)i.next();
		}
		return s;
	}
	private void comun(Query q, PrintWriter p) throws Exception {
		Collection a = q.list();
		Iterator aI = a.iterator();
		while(aI.hasNext()) {
			try {
				p.println(((Persistible)aI.next()).backupBajar());
			} catch(Exception e) {
				informe.add(e.getMessage());
			}
		}
	}
//-------------------------------------------------------------------------------------	
	public String[] Restore(String inputFileName, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		this.clearInforme();
		informe.add(new String("Primera Pasada"));
		objectosPersistir.clear();
		Collection<String> clinea = null;
		try {
			clinea = archivo.getData(inputFileName);
			transaction.begin();
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("usuario")) {
					this.usuario(linea);
				}
				if(linea.startsWith("localizacion")) {
					this.localizacion(linea);
				}
				if(linea.startsWith("param")) {
					this.parametro(linea);
				}
				if(linea.startsWith("producto")) {
					this.producto(linea);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}

		informe.add(new String("Segunda Pasada"));
		objectosPersistir.clear();
		try {
//			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("banco")) {
					this.banco(linea, sesion);
				}
				if(linea.startsWith("vendedor")) {
					this.vendedor(linea);
				}
				if(linea.startsWith("entidad")) {
					this.entidad(linea);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}

		informe.add(new String("Tercera Pasada"));
		objectosPersistir.clear();
		try {
			//Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("delegacion")) {
					this.delegacion(linea, sesion);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}
		objectosPersistir.clear();
		try {
			this.estadoMov();
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}
//------------------------------------------------------------------------------

		informe.add(new String("Cuarta Pasada"));
		objectosPersistir.clear();
		try {
//			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("disparo")) {
					this.disparo(linea);
				}
				if(linea.startsWith("diacobro")) {
					this.diacobro(linea, sesion);
				}
				if(linea.startsWith("cliente")) {
					while(!linea.endsWith(";")) {
						String l1 = (String)ilinea.next();
						linea = linea.concat("\n").concat(l1);
					}
					this.cliente(linea, sesion);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}
				
		informe.add(new String("Quinta Pasada"));
		objectosPersistir.clear();		
		try {
//			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("servicio")) {
					this.servicio(linea, sesion);
				}
				if(linea.startsWith("disparEntidades")) {
					this.disparoEntidades(linea, sesion);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);
		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		}

		informe.add(new String("Sexta Pasada"));
		objectosPersistir.clear();		
		try {
//			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("movimiento;")) {
					this.movimiento(linea, sesion);
				}
				if(objectosPersistir.size() == 1000) {
					movimiento.agregar(objectosPersistir, usuario);
					objectosPersistir.clear();
				}
			}
			movimiento.agregar(objectosPersistir, usuario);
		} catch (Exception e) {
			informe.add(e.getMessage());
		}

		informe.add(new String("Septima Pasada"));
		objectosPersistir.clear();		
		try {
//			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				if(linea.startsWith("movimientoHist")) {
					this.movimientoHist(linea, sesion);
				}
				if(objectosPersistir.size() == 1000) {
					movimiento.agregarHist(objectosPersistir, usuario);
					objectosPersistir.clear();
				} 
			}
			movimiento.agregarHist(objectosPersistir, usuario);
		} catch (Exception e) {
			informe.add(e.getMessage());
		}

		return this.informeToString();
	}
	private void estadoMov() throws Exception {
		EstadoMovBean e = new EstadoMovBean();
		try {
			for(int i=1; i<=6; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(i);
				if(i==4) {
					e.setCodRechazo("R10");
				}
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=7; i<=11; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				switch(i) {
					case 7:
						e.setCodRechazo("R02");
						break;
					case 8:
						e.setCodRechazo("R15");
						break;
					case 9:
						e.setCodRechazo("R03");
						break;
					case 10:
						e.setCodRechazo("R93");
						break;
					case 11:
						e.setCodRechazo("R08");
						break;
					case 12:
						e.setCodRechazo("R04");
						break;
					case 13:
						e.setCodRechazo("R07");
						break;
					case 14:
						e.setCodRechazo("R13");
						break;
					case 15:
						e.setCodRechazo("R14");
						break;
				}
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=17; i<=20; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=23; i<=29; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=31; i<=31; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=75; i<=80; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=86; i<=91; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			for(int i=95; i<=99; i++) {
				e = new EstadoMovBean();
				e.setId(new Long(i));
				e.setEstado(i);
				e.setColor(4);
				e.setCodRechazo(new String("R" + String.valueOf(i)));
				e.setAccion(Persistible.INSERT);
				objectosPersistir.add(e);
			}
			e = new EstadoMovBean();
			e.setEstado(100);
			e.setColor(1);
			e.setAccion(Persistible.INSERT);
			objectosPersistir.add(e);
			
			e = new EstadoMovBean();
			e.setEstado(101);
			e.setColor(4);
			e.setCodRechazo(new String("R04"));
			e.setAccion(Persistible.INSERT);
			objectosPersistir.add(e);
			
			e = new EstadoMovBean();
			e.setEstado(102);
			e.setColor(1);
			e.setAccion(Persistible.INSERT);
			objectosPersistir.add(e);

		} catch (Exception ex) { throw new Exception("EstadoMov con error: " + e.getId() + " :" + ex.getMessage()); }
	}
	private void disparo(String linea) throws Exception {
		DisparoBean d = new DisparoBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
			
			d.setId(new Long((String)tokens.nextToken()));
			d.setFecha(DateTimeUtil.getDate((String)tokens.nextToken()));
			d.setCantRegistros(Integer.parseInt((String)tokens.nextToken()));
			d.setImporte(Double.parseDouble((String)tokens.nextToken()));
			
			d.setAccion(Persistible.INSERT);
			objectosPersistir.add(d);
		} catch (Exception e) { throw new Exception("Disparo con error: " + d.getId() + " :" + e.getMessage()); }
	}
	private void usuario(String linea) throws Exception {
		UsuarioBean u = new UsuarioBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			u.setId(new Long((String)tokens.nextToken()));
			u.setNombre((String)tokens.nextToken());
			u.setApellido((String)tokens.nextToken());
			String fecha = (String)tokens.nextToken();
			if(!Util.isBlank(fecha)) {
				u.setFechaDesde(DateTimeUtil.getDate(fecha));
			}
			fecha = (String)tokens.nextToken();
			if(!Util.isBlank(fecha)) {
				u.setFechaHasta(DateTimeUtil.getDate(fecha));
			}
			u.setAlias((String)tokens.nextToken());
			u.setPassword((String)tokens.nextToken());
			u.setEmpresa((String)tokens.nextToken());
			
			u.setAccion(Persistible.INSERT);
			objectosPersistir.add(u);
		} catch (Exception e) { throw new Exception("Usuario con error: " + u.getId() + " :" + e.getMessage()); }
		
	}
	private void localizacion(String linea) throws Exception {
		LocalizacionBean l = new LocalizacionBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			l.setId(new Long((String)tokens.nextToken()));
			l.setCalle((String)tokens.nextToken());
			l.setNro((String)tokens.nextToken());
			l.setPiso((String)tokens.nextToken());
			l.setDepto((String)tokens.nextToken());
			l.setTelefLinea((String)tokens.nextToken());
			l.setTelefCelular((String)tokens.nextToken());
			l.setCodPostal((String)tokens.nextToken());
			l.setBarrio((String)tokens.nextToken());
			l.setLocalidad((String)tokens.nextToken());
			l.setProvincia((String)tokens.nextToken());
			l.setPais((String)tokens.nextToken());
	
			l.setAccion(Persistible.INSERT);
			objectosPersistir.add(l);
		} catch (Exception e) { throw new Exception("Localizacion con error: " + l.getId() + " :" + e.getMessage()); }
	}
	private void parametro(String linea) throws Exception {
		ParametroBean p = new ParametroBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			p.setId(new Long((String)tokens.nextToken()));
			p.setIndice(Integer.parseInt((String)tokens.nextToken()));
			p.setCuitEmpresa((String)tokens.nextToken());
			p.setNombreEmpresa((String)tokens.nextToken());
			p.setMoneda((String)tokens.nextToken());
			p.setUltimoServicio(Integer.parseInt((String)tokens.nextToken()));
	
			p.setAccion(Persistible.INSERT);
			objectosPersistir.add(p);
		} catch (Exception e) { throw new Exception("Parametro con error: " + p.getId() + " :" + e.getMessage()); }
	}
	private void banco(String linea, Session sesion) throws Exception {
		BancoBean b = new BancoBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			b.setId(new Long((String)tokens.nextToken()));
			b.setCodigo((String)tokens.nextToken());
			b.setDescripcion((String)tokens.nextToken());
			b.setLocalizacion((LocalizacionBean) this.leer("localizacion", (String)tokens.nextToken(), sesion));
			b.setCodigoDebito(Integer.parseInt((String)tokens.nextToken()));
			b.setDescripPrestacion((String)tokens.nextToken());
			b.setBancoRecaudador((String)tokens.nextToken());
			
			b.setAccion(Persistible.INSERT);
			objectosPersistir.add(b);
		} catch (Exception e) { throw new Exception("Banco con error: " + b.getId() + " :" + e.getMessage()); }
	}
	private void producto(String linea) throws Exception {
		ProductoBean p = new ProductoBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			p.setId(new Long((String)tokens.nextToken()));
			p.setCodigo((String)tokens.nextToken());
			p.setDescripcion((String)tokens.nextToken());
			if(((String)tokens.nextToken()).compareTo("true")==0)
				p.setEstado(true);
			else
				p.setEstado(false);
			
			p.setAccion(Persistible.INSERT);
			objectosPersistir.add(p);
		} catch (Exception e) { throw new Exception("Producto con error: " + p.getId() + " :" + e.getMessage()); }
	}
	private void vendedor(String linea) throws Exception {
		VendedorBean v = new VendedorBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			v.setId(new Long((String)tokens.nextToken()));
			v.setCodigo((String)tokens.nextToken());
			v.setNombre((String)tokens.nextToken());
			v.setUtilizar(Integer.parseInt((String)tokens.nextToken()));
			
			v.setAccion(Persistible.INSERT);
			objectosPersistir.add(v);
		} catch (Exception e) { throw new Exception("Vendedor con error: " + v.getId() + " :" + e.getMessage()); }
	}
	private void entidad(String linea) throws Exception {
		EntidadBean e = new EntidadBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			e.setId(new Long((String)tokens.nextToken()));
			e.setCodigo((String)tokens.nextToken());
			e.setDescripcion((String)tokens.nextToken());		
			
			e.setAccion(Persistible.INSERT);
			objectosPersistir.add(e);
		} catch (Exception ex) { throw new Exception("Entidad con error: " + e.getId() + " :" + ex.getMessage()); }
	}
	private void delegacion(String linea, Session sesion) throws Exception {
		DelegacionBean d = new DelegacionBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			d.setId(new Long((String)tokens.nextToken()));
			d.setCodigo((String)tokens.nextToken());
			d.setDescripcion((String)tokens.nextToken());
			d.setF_alta(DateTimeUtil.getDate((String)tokens.nextToken()));
			d.setLocalizacion((LocalizacionBean) this.leer("localizacion", (String)tokens.nextToken(), sesion));
			d.setEntidad((EntidadBean) this.leer("entidad", (String)tokens.nextToken(), sesion));
			d.setBanco((BancoBean) this.leer("banco", (String)tokens.nextToken(), sesion));
			d.setUtilizar(Integer.parseInt((String)tokens.nextToken()));
	//		LocalizacionBean l = d.getBanco().getLocalizacion();
			
			d.setAccion(Persistible.INSERT);
			objectosPersistir.add(d);
		} catch (Exception e) { throw new Exception("Delegacion con error: " + d.getId() + " :" + e.getMessage()); }
	}
	private void diacobro(String linea, Session sesion) throws Exception {
		DiasCobroBean d = new DiasCobroBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			d.setId(new Long((String)tokens.nextToken()));
			d.setDelegacion((DelegacionBean) this.leer("delegacion", (String)tokens.nextToken(), sesion));
			d.setIdDelegacion(d.getDelegacion().getId());
			d.setFechaDisparo(DateTimeUtil.getDate((String)tokens.nextToken()));
			d.setAccion(Persistible.INSERT);
			objectosPersistir.add(d);
		} catch (Exception e) { throw new Exception("DiaCobro con error: " + d.getId() + " :" + e.getMessage()); }
	}

	private void cliente(String linea, Session sesion) throws Exception {
		ClienteBean c = new ClienteBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			c.setId(new Long((String)tokens.nextToken()));
			
			c.setTipoDoc((String)tokens.nextToken());
			c.setNroDoc(Integer.parseInt((String)tokens.nextToken()));
			c.setCbu((String)tokens.nextToken());
			c.setSucursalCA((String)tokens.nextToken());
			c.setNroCA((String)tokens.nextToken());
			c.setNombre((String)tokens.nextToken());
			c.setApellido((String)tokens.nextToken());
			String fecha = (String)tokens.nextToken();
			if(!Util.isBlank(fecha)) {
				c.setFechaIngreso(DateTimeUtil.getDate(fecha));
			}
			c.setComentarios((String)tokens.nextToken());
			c.setLocParticular((LocalizacionBean) this.leer("localizacion", (String)tokens.nextToken(), sesion));
			c.setLocLaboral((LocalizacionBean) this.leer("localizacion", (String)tokens.nextToken(), sesion));
			c.setLocInformado((LocalizacionBean) this.leer("localizacion", (String)tokens.nextToken(), sesion));
			
			c.setEstado(Integer.parseInt((String)tokens.nextToken()));
			String fechaEstado = (String)tokens.nextToken();
			if(!Util.isBlank(fechaEstado)) {
				c.setFechaEstado(DateTimeUtil.getDate(fechaEstado));
			}
			c.setEstadoAnterior(Integer.parseInt((String)tokens.nextToken()));
			
			c.setDelegacion((DelegacionBean) this.leer("delegacion", (String)tokens.nextToken(), sesion));
			c.setIdDelegacion(c.getDelegacion().getId());
			
			c.setAccion(Persistible.INSERT);
			objectosPersistir.add(c);
		} catch (Exception e) { throw new Exception("Cliente con error: " + c.getId() + " :" + e.getMessage()); }
	}

	private void servicio(String linea, Session sesion) throws Exception {
		ServicioBean s = new ServicioBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			
			String token = (String)tokens.nextToken();
	
			s.setId(new Long((String)tokens.nextToken()));
			s.setCliente((ClienteBean)	 this.leer("cliente",	(String)tokens.nextToken(), sesion));
			s.setProducto((ProductoBean) this.leer("producto", (String)tokens.nextToken(), sesion));
			s.setImporteTotal(Double.parseDouble((String)tokens.nextToken()));
			s.setCantCuota(Integer.parseInt((String)tokens.nextToken()));
			s.setImporteCuota(Double.parseDouble((String)tokens.nextToken()));
			s.setVendedor((VendedorBean) this.leer("vendedor",	(String)tokens.nextToken(), sesion));
			
			s.setComision(Double.parseDouble((String)tokens.nextToken()));
			
			token = (String)tokens.nextToken();
			if(token == null || token.compareTo("")== 0)
				s.setFechaVenta(" ");
			else {
				try {
					s.setFechaVenta(DateTimeUtil.getDate(token));
				} catch(Exception e) {
					throw new Exception("Error al subir la fechaVenta para " + s.toString());
				}
			}
			token = (String)tokens.nextToken();
			if(token == null || token.compareTo("")== 0)
				s.setPerPrimerDisparo(0);
			else
				s.setPerPrimerDisparo(Integer.parseInt(token));
			s.setUltCuotaDebitada(Integer.parseInt((String)tokens.nextToken()));
			
			s.setEstado((String)tokens.nextToken());
			s.setMotivo((String)tokens.nextToken());
			
			token = (String)tokens.nextToken();
			if(token == null || token.compareTo("")== 0) {
				s.setFechaIngreso(" ");
			} else {
				try {
					s.setFechaIngreso(DateTimeUtil.getDate(token));
				} catch(Exception e) {
					throw new Exception("Error al subir la fechaIngreso para " + s.toString());
				}
			}
			token = (String)tokens.nextToken();
			if(token == null || token.compareTo("")== 0) {
				s.setUltFechaConMovimientos(" ");
			} else {
				try {
					s.setUltFechaConMovimientos(token);
				} catch(Exception e) {
					throw new Exception("Error al subir la UltFechaConMovimientos para " + s.toString());
				}
			}
			s.setContId(Long.valueOf(tokens.nextToken()));
			
			s.setAccion(Persistible.INSERT);
			objectosPersistir.add(s);
		} catch (Exception e) { throw new Exception("Servicio con error: " + s.getId() + " :" + e.getMessage()); }
	}
	
	private void disparoEntidades(String linea, Session sesion) throws Exception {
		DisparoEntidadesBean de = new DisparoEntidadesBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
	
			de.setId(new Long((String)tokens.nextToken()));
			de.setIdentificador(new Long((String)tokens.nextToken()));
			de.setTipo((String)tokens.nextToken());
			try {
				String disparo = (String)tokens.nextToken();
				de.setDisparo((DisparoBean) this.leer("disparo", disparo, sesion));
			} catch(Exception e) {}
			
			de.setAccion(Persistible.INSERT);
			objectosPersistir.add(de);
		} catch (Exception e) { throw new Exception("DisparoEntidades con error: " + de.getId() + " :" + e.getMessage()); }
	}
	
	private void movimiento(String linea, Session sesion) throws Exception {
		MovimientoBean m = new MovimientoBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
			
			m.setId(new Long((String)tokens.nextToken()));
			m.setCliente((ClienteBean) this.leer("cliente", (String)tokens.nextToken(), sesion));
			m.setIdCliente(m.getCliente().getId());
			m.setIdServicio(Long.parseLong(tokens.nextToken()));
			
				m.setFecha(DateTimeUtil.getDate((String)tokens.nextToken()));
			
			m.setImporte(Double.parseDouble((String)tokens.nextToken()));
			m.setDescripcion((String)tokens.nextToken());
			m.setNroCuota(Integer.parseInt((String)tokens.nextToken()));
			m.setTotalCuotas(Integer.parseInt((String)tokens.nextToken()));
			String eee = (String)tokens.nextToken();

//			EstadoMovBean estado = movimiento.getEstadoMovId(new Long(eee), sesion);
			EstadoMovBean estado = (EstadoMovBean) this.leer("estadoMov", eee, sesion);		

			m.setEstado(estado);
			DisparoBean disparo = (DisparoBean)this.leer("disparo", (String)tokens.nextToken(), sesion);
			m.setDisparo(disparo);
			
			m.setAccion(Persistible.INSERT);
			objectosPersistir.add(m);
		} catch (Exception e) { throw new Exception("Movimiento con error: " + m.getId() + " :" + e.getMessage()); }
	}

	private void movimientoArca(String linea, Session sesion) throws Exception {
		MovimientoBean m = new MovimientoBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
			
			tokens.nextToken();
			tokens.nextToken();
			tokens.nextToken();
			m.setFecha(DateTimeUtil.getDate((String)tokens.nextToken()));
			m.setImporte(Double.parseDouble((String)tokens.nextToken()));

			int dni = Integer.parseInt(tokens.nextToken());
//			if(dni == 16535295) {
//				System.out.println(dni);
//			}
			int indArca = Integer.parseInt((String)tokens.nextToken());
			m.setNroCuota(0);
			m.setTotalCuotas(Integer.parseInt((String)tokens.nextToken()));

			ClienteBean c = cliente.getClienteByNroDocActivo(dni, sesion);
			
			m.setDescripcion("ingresado desde Servina");

			m.setId(indice++);
			
			m.setCliente(c);
			m.setIdCliente(c.getId());

//			Iterator<ServicioBean> it = servicioServ.getServicios(c.getId()).iterator();
			Iterator<ServicioBean> it = c.getServicios().iterator();
			while(it.hasNext()) {
				ServicioBean s = (ServicioBean)it.next();
				if(s.isAprobado())
					if(s.getImporteCuota() == m.getImporte()) {
						if(s.getVendedor().getCodigo().compareTo(VendedorDTO.ARCA)==0 &&
						   indArca == 0) {
							m.setIdServicio(s.getId());
						} else {
						if(s.getVendedor().getCodigo().compareTo(VendedorDTO.ARCA1)==0 &&
							indArca == 1) {
							m.setIdServicio(s.getId());
						} else {
						if(s.getVendedor().getCodigo().compareTo(VendedorDTO.ARCA2)==0 &&
							indArca == 2) {
							m.setIdServicio(s.getId());
						}}}
					}
			}
			String eee = (String)tokens.nextToken();

//			EstadoMovBean estado = movimiento.getEstadoMovId(new Long(eee), sesion);
			EstadoMovBean estado = (EstadoMovBean) this.leer("estadoMov", eee, sesion);		

			m.setEstado(estado);
			DisparoBean disparo = (DisparoBean)this.leer("disparo", "1", sesion);
			m.setDisparo(disparo);

			
			m.setAccion(Persistible.INSERT);
			objectosPersistir.add(m);
		} catch (Exception e) { throw new Exception("Movimiento con error: " + m.getId() + " :" + e.getMessage()); }
	}

	private void movimientoHist(String linea, Session sesion) throws Exception {
		MovimientoHistBean m = new MovimientoHistBean();
		try {
			StringTokenizer tokens = new StringTokenizer(linea, ";");
			tokens.nextToken();
			
			m.setId(new Long((String)tokens.nextToken()));
			m.setCliente((ClienteBean) this.leer("cliente", (String)tokens.nextToken(), sesion));
			m.setIdCliente(m.getCliente().getId());
			m.setIdServicio(Long.parseLong(tokens.nextToken()));
			
				m.setFecha(DateTimeUtil.getDate((String)tokens.nextToken()));
			
			m.setImporte(Double.parseDouble((String)tokens.nextToken()));
			m.setDescripcion((String)tokens.nextToken());
			m.setNroCuota(Integer.parseInt((String)tokens.nextToken()));
			m.setTotalCuotas(Integer.parseInt((String)tokens.nextToken()));
			String eee = (String)tokens.nextToken();

//			EstadoMovBean estado = movimiento.getEstadoMovId(new Long(eee), sesion);
			EstadoMovBean estado = (EstadoMovBean) this.leer("estadoMov", eee, sesion);		

			m.setEstado(estado);
			DisparoBean disparo = (DisparoBean)this.leer("disparo", (String)tokens.nextToken(), sesion);
			m.setDisparo(disparo);
			
			m.setAccion(Persistible.INSERT);
			objectosPersistir.add(m);
		} catch (Exception e) { throw new Exception("MovimientoHist con error: " + m.getId() + " :" + e.getMessage()); }
	}

	private Object leer(String tipo, String sclave, Session s) {
		Long clave = new Long(sclave); 
		Object o = null;
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			if(tipo.compareTo("localizacion") == 0) { 
				o = (LocalizacionBean)s.get(LocalizacionBean.class, clave);
			}
			if(tipo.compareTo("delegacion") == 0) { 
				o = (DelegacionBean)s.get(DelegacionBean.class, clave);
			}
			if(tipo.compareTo("entidad") == 0) { 
				o = (EntidadBean)s.get(EntidadBean.class, clave);
			}
			if(tipo.compareTo("banco") == 0) { 
				o = (BancoBean)s.get(BancoBean.class, clave);
			}
			if(tipo.compareTo("cliente") == 0) { 
				o = (ClienteBean)s.get(ClienteBean.class, clave);
			}
			if(tipo.compareTo("vendedor") == 0) { 
				o = (VendedorBean)s.get(VendedorBean.class, clave);
			}
			if(tipo.compareTo("disparo") == 0) { 
				o = (DisparoBean)s.get(DisparoBean.class, clave);
			}
			if(tipo.compareTo("producto") == 0) { 
				o = (ProductoBean)s.get(ProductoBean.class, clave);
			}
			if(tipo.compareTo("cliente") == 0) { 
				o = (ClienteBean)s.get(ClienteBean.class, clave);
			}
			if(tipo.compareTo("estadoMov") == 0) {
				o = (EstadoMovBean)s.get(EstadoMovBean.class, clave);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return o;
	}
	/**
	@SuppressWarnings("unchecked")
	public String[] subirLocalizacion(String inputFileName) {
		Session session = Persistencia.getSession();
		Transaction transaction = session.beginTransaction();
		this.clearInforme();

		try {
			transaction.begin();
			Collection<String> clinea = archivo.getData(inputFileName);
			Iterator ilinea = clinea.iterator();
			while(ilinea.hasNext()) {
				String linea = (String)ilinea.next();
				StringTokenizer tokens = new StringTokenizer(linea,";");  
				while (tokens.hasMoreTokens()){
					LocalizacionBean l = new LocalizacionBean();
					String dni = new String((tokens.nextToken()));
					l.setCalle((tokens.nextToken()));
					l.setNro((tokens.nextToken()));
					l.setPiso((tokens.nextToken()));
					l.setDepto((tokens.nextToken()));
					l.setTelefLinea((tokens.nextToken()));
					l.setTelefCelular((tokens.nextToken()));
					l.setCodPostal((tokens.nextToken()));
					l.setBarrio((tokens.nextToken()));
					l.setLocalidad((tokens.nextToken()));
					l.setProvincia((tokens.nextToken()));
					l.setPais("Argentina");

					session.save(l);
					
					ClienteBean c = cliente.getClienteByNroDoc(Integer.parseInt(dni), session);
					c.setLocInformado(l);
					session.update(c);
				}
			}
			session.flush();
			session.clear();
		
			Persistencia.clearSession();
		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
		finally {
			transaction.commit();
		}
		return this.informeToString();
	}
	
	public String[] BackupCompletoArca() {
		String fechaHoy = DateTimeUtil.formatDateTime1(DateTimeUtil.getDate());
		String outputFileName = new String("C:\\Servina\\backup_Arca_" + fechaHoy + ".txt");
		PrintWriter p = null;

		Collection<ClienteBean> listaCli = new ArrayList<ClienteBean>();
		Session s = Persistencia.getSession();
		Transaction transaction = s.beginTransaction();
		try{
			p = archivo.abrirFileSalida(outputFileName);
			transaction.begin();
			
			Query q = s.getNamedQuery("UsuarioBean.getAll");
			q.setReadOnly(true);
			this.comun(q, p);
//			q = s.getNamedQuery("LocalizacionBean.getAll");
//			this.comun(q, p);
			q = s.getNamedQuery("ParametroBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("BancoBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DisparoBean.getAll");
			this.comun(q, p);
//			q = s.getNamedQuery("DisparoEntidadesBean.getBackup");
//			this.comun(q, p);
			q = s.getNamedQuery("VendedorBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("EntidadBean.getAll");
			this.comun(q, p);
			q = s.getNamedQuery("DelegacionBean.getAll");
			this.comun(q, p);
//			q = s.getNamedQuery("DiasCobroBean.getAll");
//			this.comun(q, p);
			q = s.getNamedQuery("ProductoBean.getAll");
			this.comun(q, p);

			q = s.getNamedQuery("ServicioBean.getAll");
			Iterator<ServicioBean> aI = q.list().iterator();
			while(aI.hasNext()) {
				ServicioBean svc = (ServicioBean)aI.next();
				ServicioDTO sd = new ServicioDTO(svc.toString());
				if(sd.getVendedor().esArca()) {
					if(!listaCli.contains(svc.getCliente()))
						listaCli.add(svc.getCliente());
				}
			}
			Iterator<ClienteBean> itCli = listaCli.iterator();
			long iCli = 0, iSvc = 0, iMb = 0, aa = 0, bb = 0, cc = 0, d1 = 0
						, d2 = 0 , d3 = 0;
			
			boolean priVez = true;
			while(itCli.hasNext()) {
				ClienteBean cli = (ClienteBean)itCli.next();
				priVez = true;
				Iterator<ServicioBean> is = cli.getServicios().iterator();
				while(is.hasNext()) {
					ServicioBean svc = (ServicioBean)is.next();
					ServicioDTO sd = new ServicioDTO(svc.toString());
					if(sd.getVendedor().esArca()) {
						Query q1 = s.getNamedQuery("MovimientoBean.getAllbyServicio");
						q1.setLong("idServicio", svc.getId());
						Iterator<MovimientoBean> mI = q1.list().iterator();
						if(priVez) {
							priVez = false;
							aa = cli.getId();
							cli.setId(++iCli);
							p.println(((Persistible)cli).backupBajar());
						}
						bb = svc.getId();
						svc.setId(++iSvc);
						svc.getCliente().setId(iCli);
						p.println(((Persistible)svc).backupBajar());
						
						cli.setId(aa);
						svc.setId(bb);
						
						while(mI.hasNext()) {
							MovimientoBean mb = (MovimientoBean)mI.next();
							cc = mb.getId();
							mb.setId(++iMb);
							mb.setIdCliente(iCli);
							mb.setIdServicio(iSvc);
							p.println(((Persistible)mb).backupBajar());
							mb.setId(cc);
						}
					}
				}
			}
			p.flush();
			p.close();
		} catch(Exception e) {
			informe.add(e.getMessage());
		}
		return this.informeToString();
	}
	
	public String[] eliminarNoArca(String usuario) {
		Collection<Long> listaCli = new ArrayList<Long>();
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			LocalizacionBean ll = (LocalizacionBean) this.leer("localizacion","3529", s);
			
			Query q = s.getNamedQuery("BancoBean.getAll");
			Iterator<BancoBean> bI = q.list().iterator();
			while(bI.hasNext()) {
				BancoBean bb = (BancoBean)bI.next();
				bb.setLocalizacion(ll);
				s.update(bb);
			}
			q = s.getNamedQuery("DelegacionBean.getAll");
			Iterator<DelegacionBean> dI = q.list().iterator();
			while(dI.hasNext()) {
				DelegacionBean dd = (DelegacionBean)dI.next();
				dd.setLocalizacion(ll);
				s.update(dd);
			}
			s.flush();
			s.clear();

			transaction.commit();

			transaction = s.beginTransaction();
			transaction.begin();
			q = s.getNamedQuery("ServicioBean.getAll");
			Iterator<ServicioBean> aI = q.list().iterator();
			while(aI.hasNext()) {
				ServicioBean svc = (ServicioBean)aI.next();
				ServicioDTO sd = new ServicioDTO(svc.toString());
				if(!sd.getVendedor().esArca()) {
					if(!listaCli.contains(svc.getCliente())) {
						listaCli.add(svc.getCliente().getId());
						ClienteBean cli = svc.getCliente();
						cli.setApellido("-");
						cli.setNombre("-");
						cli.setCbu("-");
						cli.setComentarios("-");
						cli.setLocInformado((LocalizacionBean)s.get(LocalizacionBean.class, new Long(3529)));
						cli.setLocLaboral((LocalizacionBean)s.get(LocalizacionBean.class, new Long(3529)));
						cli.setLocParticular((LocalizacionBean)s.get(LocalizacionBean.class, new Long(3529)));
						cli.setNroCA("-");
						cli.setSucursalCA("-");
						s.update(cli);
					}
					Query q1 = s.getNamedQuery("MovimientoBean.getAllbyServicio");
					q1.setLong("idServicio", svc.getId());
					Iterator<MovimientoBean> mI = q1.list().iterator();
					while(mI.hasNext()) {
						MovimientoBean mb = (MovimientoBean)mI.next();
						s.delete(mb);
					}
					s.delete(svc);
				}
			}
			s.flush();
			s.clear();
			Persistencia.clearSession();
			transaction.commit();

		} catch(Exception e) {
			informe.add(e.getMessage());
		}
		return this.informeToString();
	}
	
	public String[] limpiarClientes(String usuario) {
		Session s = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Query q = s.getNamedQuery("ClienteBean.getAll");
			Iterator<ClienteBean> itCli = q.list().iterator();
			while(itCli.hasNext()) {
				ClienteBean cli = (ClienteBean)(itCli.next());
				ClienteDTO cliD = new ClienteDTO(cli.toString());
				if(cliD.getEsClienteDe() == ClienteDTO.SERVINA) {
				}
			}
			s.flush();
			s.clear();
			Persistencia.clearSession();
		} catch(Exception e) {
			informe.add(e.getMessage());
		} finally {
			transaction.commit();
		}
		return this.informeToString();
	}
	*/
	public String[] agregarMov(Long idDelegacion, String fecha, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		this.clearInforme();
		EstadoMovBean estadoMov = (EstadoMovBean) this.leer("estadoMov", "4", sesion);		
		objectosPersistir.clear();
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			Iterator<ClienteBean> cI = cliente.getClienteFindDelegacion(idDelegacion, sesion).iterator();
			while(cI.hasNext()) {
				ClienteBean cb = cI.next();
				Iterator<ServicioBean> sI = servicioServ.getServicios(cb.getId(), sesion).iterator();
				while(sI.hasNext()) {
					ServicioBean sb = sI.next();
					
					MovimientoBean m = new MovimientoBean();
					m.setCliente(cb);
					m.setIdCliente(cb.getId());
					m.setIdServicio(sb.getId());
					m.setFecha(DateTimeUtil.getDateAAAAMMDD(fecha));
						
					m.setImporte(sb.getImporteCuota());
					m.setDescripcion("Agregas por Maxi");
					m.setNroCuota(0);
					m.setTotalCuotas(sb.getCantCuota());
					m.setEstado(estadoMov);

					DisparoBean disparo = (DisparoBean)this.leer("disparo", "1", sesion);
					m.setDisparo(disparo);
						
					m.setAccion(Persistible.INSERT);
					objectosPersistir.add(m);
				}
			}
			persistencia.persistirMasivo(objectosPersistir, informe, sesion);

		} catch (Exception e) {
			informe.add(new String(e.getMessage()));
		} finally {
			transaction.commit();
			sesion.flush();
		}
		return this.informeToString();
	}
	public String actualizarServicio(String sDTO, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			ServicioDTO servicioDTO = new ServicioDTO(sDTO);
			ServicioBean sBean = this.armarServicio(servicioDTO, sesion);
			if(sBean.getId().compareTo(ServicioDTO.NUEVO) == 0) {
				sBean.setFechaIngreso(DateTimeUtil.getDate());
				sBean.setPerPrimerDisparo(sBean.getPeriodoComenzarCuotas());
				sBean.setAccion(Persistible.INSERT);
			} else
				sBean.setAccion(Persistible.UPDATE);
			 
			servicio.persistir(sBean, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		return new String("Actualizaci�n Exitosa");
	}

	public String renovarServicio(String sDTO, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			
			ServicioDTO servicioDTO = new ServicioDTO(sDTO);
			ServicioBean sBean = (ServicioBean)sesion.get(ServicioBean.class, servicioDTO.getId());
			
			ClienteBean  cBean = cliente.getClienteById(servicioDTO.getIdCliente(), sesion);
			Query q = sesion.getNamedQuery("VendedorBean.findById");
			q.setLong("id", servicioDTO.getVendedor().getId());
			VendedorBean vBean = (VendedorBean)q.list().iterator().next();
			q = sesion.getNamedQuery("ProductoBean.findById");
			q.setLong("id", servicioDTO.getProducto().getId());
			ProductoBean pBean = (ProductoBean)q.list().iterator().next();

			sBean.setId(servicioDTO.getId());
			sBean.setCliente(cBean);
			
			sBean.setProducto(pBean);
			sBean.setImporteTotal(servicioDTO.getImporteTotal());
			sBean.setCantCuota(servicioDTO.getCantCuota());
			sBean.setImporteCuota(servicioDTO.getImporteCuota());
			
			sBean.setVendedor(vBean);
			sBean.setComision(servicioDTO.getComision());
			sBean.setFechaVenta(servicioDTO.getFechaVenta());
			sBean.setPerPrimerDisparo(servicioDTO.getPerPrimerDisparo());
			sBean.setUltCuotaDebitada(servicioDTO.getUltCuotaDebitada());
			sBean.setEstado(ServicioDTO.FINALIZADO);
			sBean.setMotivo(servicioDTO.getMotivo());
			sBean.setFechaIngreso(servicioDTO.getFechaIngreso());
			sBean.setContId(servicioDTO.getContId());

			sesion.update(sBean);
			
			
//			ServicioBean sBean = this.armarServicio(servicioDTO, sesion);
//			sBean.setEstado(ServicioDTO.FINALIZADO);
//			sBean.setAccion(Persistible.UPDATE);
//			servicio.persistir(sBean, sesion);

			sBean = new ServicioBean();

			cBean = cliente.getClienteById(servicioDTO.getIdCliente(), sesion);
			q = sesion.getNamedQuery("VendedorBean.findById");
			q.setLong("id", servicioDTO.getVendedor().getId());
			vBean = (VendedorBean)q.list().iterator().next();
			q = sesion.getNamedQuery("ProductoBean.findById");
			q.setLong("id", servicioDTO.getProducto().getId());
			pBean = (ProductoBean)q.list().iterator().next();

			sBean.setCliente(cBean);
			
			sBean.setProducto(pBean);
			sBean.setImporteTotal(servicioDTO.getImporteTotal());
			sBean.setCantCuota(servicioDTO.getCantCuota());
			sBean.setImporteCuota(servicioDTO.getImporteCuota());
			
			sBean.setVendedor(vBean);
			sBean.setComision(servicioDTO.getComision());
			sBean.setUltCuotaDebitada(servicioDTO.getUltCuotaDebitada());
			sBean.setEstado(servicioDTO.APROBADO);
			sBean.setMotivo(servicioDTO.getMotivo());

			sBean.setFechaVenta(DateTimeUtil.getDate());
			sBean.setPerPrimerDisparo(sBean.getPeriodoComenzarCuotas());
			sBean.setFechaIngreso(DateTimeUtil.getDate());
			sBean.setContId(servicioDTO.getId());

			sesion.save(sBean);
//			sBean.setAccion(Persistible.INSERT);
//			servicio.persistir(sBean, sesion);
			
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		return new String("Actualizaci�n Exitosa");
	}
	private ServicioBean armarServicio(ServicioDTO sDTO, Session sesion) {
		ServicioBean sBean = new ServicioBean();
		ClienteBean  cBean = cliente.getClienteById(sDTO.getIdCliente(), sesion);
		
		Query q = sesion.getNamedQuery("VendedorBean.findById");
		q.setLong("id", sDTO.getVendedor().getId());
		VendedorBean vBean = (VendedorBean)q.list().iterator().next();

		q = sesion.getNamedQuery("ProductoBean.findById");
		q.setLong("id", sDTO.getProducto().getId());
		ProductoBean pBean = (ProductoBean)q.list().iterator().next();

		sBean.setId(sDTO.getId());
		sBean.setCliente(cBean);
		
		sBean.setProducto(pBean);
		sBean.setImporteTotal(sDTO.getImporteTotal());
		sBean.setCantCuota(sDTO.getCantCuota());
		sBean.setImporteCuota(sDTO.getImporteCuota());
		
		sBean.setVendedor(vBean);
		sBean.setComision(sDTO.getComision());
		sBean.setFechaVenta(sDTO.getFechaVenta());
		sBean.setPerPrimerDisparo(sDTO.getPerPrimerDisparo());
		sBean.setUltCuotaDebitada(sDTO.getUltCuotaDebitada());
		sBean.setEstado(sDTO.getEstado());
		sBean.setMotivo(sDTO.getMotivo());
		sBean.setFechaIngreso(sDTO.getFechaIngreso());
		sBean.setContId(sDTO.getContId());
		
		return sBean;
	}
}