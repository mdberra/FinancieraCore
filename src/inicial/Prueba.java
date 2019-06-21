package inicial;
//
//  Restore
// private void restore() {
//
// OperacionesServicioImpl
// public String[] Backup(String usuario) {
//
// GenerarDisparoServicioImpl 
// private String[] dispararBaproSVille
//
import java.io.Console;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ClienteDTO;
import com.dto.CtacteDTO;
import com.dto.Estad1DTO;
import com.dto.Estad3DTO;
import com.dto.Parser;
import com.dto.ResultadoDTO;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.server.ServiceNegocioLocator;
import com.financiera.core.service.ArchivoService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.core.util.Util;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;
import com.financiera.domain.service.DiasCobroService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.service.UsuarioService;
import com.financiera.domain.service.VendedorService;
import com.financiera.domain.servicio.CtacteServicio;
import com.financiera.domain.servicio.DelegacionesServicio;
import com.financiera.domain.servicio.EstadisticaServicio;
import com.financiera.domain.servicio.ExportacionExcelServicio;
import com.financiera.domain.servicio.GenerarDisparoServicio;
import com.financiera.domain.servicio.OperacionesServicio;
import com.financiera.domain.servicio.VendedoresServicio;

public class Prueba {
	private ArchivoService archivoService = (ArchivoService) ServiceLocator.getInstance().getService(ArchivoService.class);
	private ExportacionExcelServicio exportacionExcel = (ExportacionExcelServicio)ServiceNegocioLocator.getInstance().getService(ExportacionExcelServicio.class);
	private GenerarDisparoServicio generarDisparo = (GenerarDisparoServicio)ServiceNegocioLocator.getInstance().getService(GenerarDisparoServicio.class);
	private ClienteService cliente = (ClienteService) ServiceLocator.getInstance().getService(ClienteService.class);
	private DelegacionService delegacion = (DelegacionService) ServiceLocator.getInstance().getService(DelegacionService.class);
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	private OperacionesServicio operaciones = (OperacionesServicio) ServiceNegocioLocator.getInstance().getService(OperacionesServicio.class);
	private CtacteServicio ctacte = (CtacteServicio) ServiceNegocioLocator.getInstance().getService(CtacteServicio.class);
	private DelegacionesServicio delegaciones = (DelegacionesServicio) ServiceNegocioLocator.getInstance().getService(DelegacionesServicio.class);
	private EstadisticaServicio estadistica = (EstadisticaServicio) ServiceNegocioLocator.getInstance().getService(EstadisticaServicio.class);
	private ServicioService servicio = (ServicioService) ServiceLocator.getInstance().getService(ServicioService.class);
	private VendedoresServicio vendedor = (VendedoresServicio) ServiceNegocioLocator.getInstance().getService(VendedoresServicio.class);
	private VendedorService vend = (VendedorService) ServiceLocator.getInstance().getService(VendedorService.class);
	private UsuarioService usuario = (UsuarioService) ServiceLocator.getInstance().getService(UsuarioService.class);
	private DiasCobroService diasCobro = (DiasCobroService) ServiceLocator.getInstance().getService(DiasCobroService.class);
	
	private String usuarioString = new String("Administrador");
	Session sesion;
	private boolean validarMovExistente = true;
	private String pathLinux = new String("//home//marce//Desktop//Liberdina//Servina//");
	private String pathWindows = new String("c:\\Servina\\");
	
	public static void main(String[] args) {
		Prueba p = new Prueba();
	//  Disparar/Respuestas   True/False
		String accion = args[0];
		
//		if(accion.compareTo("FixIdServicio") == 0) {
//			p.FixIdServicio(BancoBean.ITAU);
//		} else {
		if(accion.compareTo("Restore")==0) {
			p.restore();
		} else {
		if(accion.compareTo("Backup")==0) {
			p.backup();
		} else {
		if(accion.compareTo("Disparar")==0) {
			String duplicar = new String("True");
			try {
				duplicar = args[1];
			} catch (Exception e) {
				
			}
			if(duplicar.compareTo("False")==0) {
				p.disparar(false);
			} else {
				p.disparar(true);
			}
		} else {
			if(accion.compareTo("Respuestas")==0) {
				p.respuestas();
			} else {
				System.out.println("No hay nada para hacer - Accion Incorrecto");
			}
		}}}}
	//}
	private void disparar(boolean bb) {
		this.validarMovExistente = bb;
		
		Console console = System.console();
		try {
			System.out.println("Ingrese el mes (MM): ");
			Scanner scanner = new Scanner(System.in);
			String mes = scanner.next();
			if(mes.length() > 2) {
				System.out.println("El mes debe ser un numero entre 1 y 12");
				throw new Exception();				
			}
			int mm = Integer.parseInt(mes);
			if(mm == 0 || mm > 12){
				System.out.println("El mes debe ser un numero entre 1 y 12");
				throw new Exception();
			}
			System.out.println("Ingrese el A�O (AAAA): ");
			int aaaa = Integer.parseInt(scanner.next());
			if(aaaa < 2018 || aaaa>2020) {
				System.out.println("El A�O es incorrecto");
				throw new Exception();
			}
			String mMes;
			if(mm<10) {
				mMes = new String("0"+ mm );
			} else {
				mMes = String.valueOf(mm);
			}
			String fechaMesDisparo = new String("01/" + mMes + "/" + aaaa);
			System.out.println("Realizando el Backup");
			this.backup();
			System.out.println("Generando el disparo");
			this.disparar(fechaMesDisparo);
			System.out.println("Finalizamos");
		} catch (Exception e) {
			System.out.println("periodo INCORRECTO");
		}
	}
	private void respuestas() {
		archivoService.buscarFilesCommon(new File("c:\\Servina\\Rendiciones\\"), "*");
		String pathDestino = new String("c:\\Servina\\RendicionesProcesadas\\");
		
		Iterator<File> files = archivoService.getFiles().iterator();
		while(files.hasNext()) {
			File f = (File)files.next();
			if(f.getName().startsWith("Bapro")) {
				System.out.println("Procesando : " + f.getName()); 
//				String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_BAPRO, "C:\\Servina\\Rendiciones\\resp.txt", usuarioString);
				String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_BAPRO, f.getAbsolutePath(), usuarioString);
				ResultadoDTO res = new ResultadoDTO(resDTO);
				this.mostrar(res);
				
				f.renameTo(new File(pathDestino+f.getName()));
			}
			if(f.getName().startsWith("NO30709956570")) {  // Itau
				System.out.println("Procesando : " + f.getName());
				String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_ITAU, f.getAbsolutePath(), usuarioString);
				ResultadoDTO res = new ResultadoDTO(resDTO);
				this.mostrar(res);
				
				f.renameTo(new File(pathDestino+f.getName()));
			}
			if(f.getName().startsWith("BICA")) {
				System.out.println("Procesando : " + f.getName());
				String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_BICA, f.getAbsolutePath(), usuarioString);
				ResultadoDTO res = new ResultadoDTO(resDTO);
				this.mostrar(res);
				
				f.renameTo(new File(pathDestino+f.getName()));
			}
		}
	}
	private void mostrar(ResultadoDTO res) {
		System.out.println("Leidos          : " + res.getLeidos());
		System.out.println("YaRespondido    : " + res.getYaRespondido());
		System.out.println("Grabados        : " + res.getGrabados());
		System.out.println("Errores         : " + res.getErrores());
		System.out.println("Deleteados      : " + res.getDeleteados());
		System.out.println("Enviadas        : " + res.getEnviadas());
		System.out.println("Sin tratamiento : " + res.getEstadoTransitorio());
	}
	public Prueba() {
//		CacheManager.getInstance().clearUsuarioSesion(usuarioString);
		usuario.validar("admin", "cacatua01");
		sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
	}
	
	public void prueba() {
		try {
			Date d = DateTimeUtil.get2MesesAtras();
		} catch(Exception e) {}
		try {
			delegacion.inicializarDisparoEntidades(usuarioString);
			delegacion.selectAllEntidadesBaproSVille(usuarioString);
//			String[] s = delegaciones.getDisparoEntidadesStrAll(0, 9, usuarioString);
			String[] s = servicio.getProductosStr(usuarioString);
/**			
			String ret = usuario.validar(usuarioString, new String("cacatua01"));
			
			
			String[] s = cliente.getClienteByApellidoStr("Villegas",1,9, usuarioString);
			for(int i=0; i<s.length; i++) {
				if(!Util.isBlank(s[i]))
					System.out.println(s[i]);
			}

			s = cliente.getClienteByDelegacionEstadoStr(new Long(1), 0, 0, 9, usuarioString);
			for(int i=0; i<s.length; i++) {
				if(!Util.isBlank(s[i]))
					System.out.println(s[i]);
			}
			s = movimiento.getMovimientoByClienteStr(new Long(2577), usuarioString);
			for(int i=0; i<s.length; i++) {
				if(!Util.isBlank(s[i]))
					System.out.println(s[i]);
			}
			
			movimiento.agrDevolucion(new Long(40892), DateTimeUtil.getDate("29/07/2011"), Double.parseDouble("191.2"), usuarioString);
	
			s = movimiento.getMovimientoByClienteStr(new Long(2577), usuarioString);
			*/
			for(int i=0; i<s.length; i++) {
				if(!Util.isBlank(s[i]))
					System.out.println(s[i]);
			}
		} catch (Exception e) {}
	}
	private void restore() {
		String[] informeOut = operaciones.Restore("c:\\Servina\\Backups\\subida.txt", usuarioString);
//		String[] informeOut = operaciones.Restore("//home//marce//Liberdina//Servina//Backups//subida", usuarioString);
		for(int i=0; i<informeOut.length; i++) {
			if(!Util.isBlank(informeOut[i]))
				System.out.println(informeOut[i]);
		}
	}
	private void backup() {
		String[] informeOut = operaciones.Backup(usuarioString);
		for(int i=0; i<informeOut.length; i++) {
			if(!Util.isBlank(informeOut[i]))
				System.out.println(informeOut[i]);
		}
	}
	private void disparar(String fechaMesDisparo) {
		String[] s = generarDisparo.dispararBaproSVille(fechaMesDisparo, validarMovExistente, usuarioString);

		// duplicar si
//		boolean validarMovExistente = false;
//		String[] s = generarDisparo.dispararNacion("01/12/2013", "31/12/2013", "01", validarMovExistente, usuarioString);
		for(int i=0; i<s.length; i++) {
			if(!Util.isBlank(s[i]))
				System.out.println(s[i]);
		}
	}
/**	public void FixIdServicio(String descripcion) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			Query q = sesion.getNamedQuery("MovimientoBean.getMovimientoByDescripcion");
			q.setString("descripcion", descripcion);
			Iterator mI = q.list().iterator();
			while(mI.hasNext()) {
				MovimientoBean m = (MovimientoBean)mI.next();
				
				Long auxIdServicio = movimiento.fixIdservicio(servicio, m, sesion);				
				if(auxIdServicio == null) {
					System.out.println("No encontre idServicio para " + m.getId());
				} else {
					m.setIdServicio(auxIdServicio);
					sesion.save(m);
					System.out.println("Actualizando " + m.getId() + " " + m.getIdCliente() + " " + m.getIdServicio());
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}*/
	public void prueba4() {
//		String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_NACION, "C:\\Servina\\Rendiciones\\SERVNA1S_20121130.TXT", usuarioString);
//		String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_NACION, "C:\\Servina\\Rendiciones\\Recaudaci�n_20140530.txt", usuarioString);
		String resDTO = ctacte.procesarRespuesta(CtacteServicio.ESQ_BAPRO, "C:\\Servina\\Rendiciones\\resp.txt", usuarioString);
		ResultadoDTO res = new ResultadoDTO(resDTO);
		System.out.println(res.getMensajes().toString());

//		movimiento.generarCuotaInteresAll(cliente);
	}
	public void prueba5() {
		String fechaDesde = new String("01/02/2008");
		String fechaHasta = new String("29/02/2008");
		Collection<String> s = estadistica.generarCobrosReversion(fechaDesde, fechaHasta, usuarioString);
		Iterator<String> i = s.iterator();
		while(i.hasNext()) {
			Estad1DTO e = new Estad1DTO((String)i.next());
		
			System.out.println(e.getFecha() + " " + e.getCobrosCant()  + " " + e.getCobrosImporte() + " " +
					e.getReversionCant() + " " + e.getReversionImporte());
		}
	}
	public void prueba6() {
/**
		try {
			Session sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
			ServicioBean sb = servicio.getServicioById(new Long(4879), sesion);
			Iterator it = sb.getCliente().getDelegacion().getDiasCobro(sesion).iterator();
			
			ServicioDTO sDTO = new ServicioDTO(sb.toString());

//			operaciones.actualizarServicio(sDTO.toString());
			operaciones.renovarServicio(sDTO.toString(), usuarioString);
			

//			System.out.println(cli);
			
		} catch (Exception e) {
			System.out.println(e);
		}

		String fechaDesde = new String("01/01/2007");
		String fechaHasta = new String("29/08/2009");
		
		Collection<String> s = estadistica.ventas(fechaDesde, fechaHasta);
		Iterator i = s.iterator();
		while(i.hasNext()) {
			Estad2DTO e = new Estad2DTO((String)i.next());
		
			System.out.println(e.getFecha() + " " + e.getCliente()  + " " +
					String.valueOf(e.getImporte()));
		}
		
		Collection<String> s1 = estadistica.clientesEstado(3);
		i = s1.iterator();
		while(i.hasNext()) {
			Estad3DTO e = new Estad3DTO((String)i.next());
		
			System.out.println(e.toString());
		}
//		Collection<String> s1 = estadistica.obtMovimientosEstado(fechaDesde, fechaHasta, 5);
**/
		Collection<String> s1 = estadistica.rankingCobros("11", "0", usuarioString);
		Iterator i = s1.iterator();
		while(i.hasNext()) {
			Estad3DTO e = new Estad3DTO((String)i.next());
		
			System.out.println(e.toString());
		}

	}
	public void prueba7() {
		String[] s = movimiento.getMovimientoByClienteStr(new Long(149), usuarioString);
		for(int i=0;i<s.length; i++) {
			System.out.println(s[i]);
		}

//		String[] s = movimiento.getMovimientoHistByClienteStr(new Long(422));
//		for(int i=0;i<s.length; i++) {
//			System.out.println(s[i]);
//		}

/**		DisparoEntidadesBean deb = new DisparoEntidadesBean();
		deb.setId(new Long(0));
		deb.setIdentificador(new Long(1));
		deb.setTipo(DisparoEntidadesBean.DELEGACION_NACION);
		delegacion.persistirDisparoEntidades(deb);
		*/
		

//		vendedor.actualizarVendedor(new Long(1), new String("Pablo"), new String("pablo"), 0);
	}
	public void prueba8() {
/**
		Persistencia p = new Persistencia();
		Session s = p.abrirSesion();
		DisparoBean d = (DisparoBean)s.get(DisparoBean.class, new Long(1));
		EstadoMovBean e = (EstadoMovBean)s.get(EstadoMovBean.class, new Long(1));
		p.cerrarSesion(s);
		
		MovimientoBean m = new MovimientoBean();
		ClienteBean c = cliente.getClienteById(new Long(1));
		m.setCliente(c);
		m.setDescripcion("dd");
		m.setDisparo(d);
		m.setEstado(e);
		m.setFecha("20110614");
		m.setIdCliente(c.getId());
		m.setIdServicio(new Long(1));
		m.setImporte(10.0);
		m.setNroCuota(1);
		m.setTotalCuotas(12);
		try {
			movimiento.agregar(m);
		} catch (Exception x) {
			
		}
		
//		this.mostrar(operaciones.agregarMov(new Long(142), "20110601"));
//		long l = CacheManager.getInstance().getIdDelegacion("Municipalidad de Lanus");
//		System.out.println(l);
 */
		String[] s = cliente.getClienteByDelegacionEstadoStr(new Long(164), 0, 1, 9, usuarioString);
		
//		String[] s = CacheManager.getInstance().getDelegacionStrDispOrderDescripcion("Municipalidad de Lanus");
//		CacheManager.getInstance().resetearDisparoEntidades();
//		DisparoEntidadesBean de = CacheManager.getInstance().getDisparoEntidades(new Long(190), DisparoEntidadesBean.DELEGACION_NACION);
//		System.out.println(de);
//		String[] e = CacheManager.getInstance().getDelegacionStrNacionOrderDescripcion();
//		for(int i=0;i<e.length; i++) {
//			System.out.println(e[i]);
//		}
/**		
		System.out.println("ALL");
		s = delegacion.getDelegacionStrAllOrderDescripcion();
		for(int i=0;i<s.length; i++) {
			System.out.println(s[i]);
		}
		System.out.println("BaproSVille");
		s = delegacion.getDelegacionStrBaproSVilleOrderDescripcion();
		for(int i=0;i<s.length; i++) {
			System.out.println(s[i]);
		}
		System.out.println("Nacion");
		s = delegacion.getDelegacionStrNacionOrderDescripcion();
		*/
		for(int i=0;i<s.length; i++) {
			System.out.println(s[i]);
		}
		
	}
	public void guardianes() {
		ctacte.actualizarHistorico(usuarioString);

//		ctacte.depurarHistorico(usuarioString, new String("20140101"));
 	}
	public void varios() {
		try {
			
			String s = new String("cliente;715;D.N.I.;8603221;0140117803504706982882;;;ROGELIO ANTONIO;DIAZ;null; ;			 ;localizacion;1455;Sarmiento;2708; ; ; ;153-614-8161; ; ;Merlo;Bs. As.;Argentin			a;localizacion;1456;Libertad; ; ; ;0220-4941222;153-311-7926; ; ;Merlo;Bs. As.;A			rgentina;localizacion;4415; ; ; ; ; ; ; ; ; ; ; ;0; ;0;0;Afip (aduana);null;");
			ClienteDTO clienteDTO = new ClienteDTO(s);
			ClienteBean clienteBean = new ClienteBean();
			clienteBean.setId(clienteDTO.getId());
			clienteBean.setTipoDoc(clienteDTO.getTipoDoc());
			clienteBean.setNroDoc(clienteDTO.getNroDoc());
			clienteBean.setCbu(clienteDTO.getCbu());
			clienteBean.setSucursalCA(clienteDTO.getSucursalCA());
			clienteBean.setNroCA(clienteDTO.getNroCA());
			clienteBean.setNombre(clienteDTO.getNombre());
			clienteBean.setApellido(clienteDTO.getApellido());
			clienteBean.setFechaIngreso(clienteDTO.getFechaIngreso());
			clienteBean.setComentarios(clienteDTO.getComentarios());
//			clienteBean.setLocLaboral(this.armarLocalizacion(clienteDTO.getLocLaboral()));
//			clienteBean.setLocParticular(this.armarLocalizacion(clienteDTO.getLocParticular()));
//			clienteBean.setLocInformado(this.armarLocalizacion(clienteDTO.getLocInformado()));
			
			clienteBean.setIdDelegacion(clienteDTO.getIdDelegacion());
			DelegacionBean db = new DelegacionBean();
			db.setDescripcion(clienteDTO.getDelegDescripcion());
			clienteBean.setDelegacion(db);
			clienteBean.setEstado(clienteDTO.getEstado());
			clienteBean.setFechaEstado(clienteDTO.getFechaEstado());
			clienteBean.setEstadoAnterior(clienteDTO.getEstadoAnterior());
			
			cliente.persistir(clienteBean, usuarioString);			

		} catch (Exception e) {
			
		}
	}
	
	public void prueba9() {
		String[] movimientos = movimiento.getMovimientoByClienteVertStr(new Long(2546), usuarioString);

		String periodoAux = new String();
		String cartelAux = new String();
		int fila = 0;
		Parser p = null;

		for(int i=0; i<movimientos.length; i++) {
			System.out.println(movimientos[i]);

			p = new Parser();
			p.changeSeparador('|');
			p.setK(0);
			p.parsear(movimientos[i]);
			String periodo = p.getTokenString();

			if(periodoAux.compareTo(periodo)!=0) {
				periodoAux = new String("(" + ++fila + ") " + periodo);
//				Label l = new Label(periodo);
//				l.setStyleName("gwt-Barra-Disparado");
//				ft.setWidget(++fila, 0, l);
//				ft.getFlexCellFormatter().setHorizontalAlignment(fila, 0, HasHorizontalAlignment.ALIGN_LEFT);
//				ft.getCellFormatter().setWidth(fila, 0, "120");
			}
			int col = Integer.parseInt(p.getTokenString());
//			celda = new Celda(financiera);
			
			String cartel = p.getTokenString();
			if(cartel.compareTo(cartelAux)!=0) {
				cartelAux = cartel;
//				celda.agregarCartel(cartel);
			}
			while(p.hayMasIg()) {
				CtacteDTO mov = new CtacteDTO(p.getTokenString());
//				celda.agregarMovimiento(mov.getId(), mov.getFecha(), String.valueOf(mov.getImporte()), mov.getEstadoColor(), mov.getCodRechazo());
			}
//			ft.setWidget(fila, col, celda.getCeldaSinTit());	
//			ft.getFlexCellFormatter().setHorizontalAlignment(fila, col, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	private void mostrar(String[] s) {
		for(int i=0;i<s.length; i++) {
			System.out.println(s[i]);
		}
	}
	public void prueba10() {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
		List<MovimientoBean> movimientos = movimiento.getMovimientoFindEstado(5, 6, sesion);
		try {
			Session session = Persistencia.getSession();
			Iterator mi = movimientos.iterator();
			while(mi.hasNext()) {
				MovimientoBean m = (MovimientoBean)mi.next();
				ServicioBean sb = (ServicioBean)session.get(ServicioBean.class, m.getIdServicio());
				if(sb.getVendedor().getId() == 24 || sb.getVendedor().getId() == 26) {
					System.out.println(m.backupBajar());
				}
			}
		}
		catch(Exception e) {}
	}
	public void lista() {
		String fecha = new String("2013-6-01");
		this.desdoblarPendiente(fecha, 14001516, 100.00);
		this.desdoblarPendiente(fecha, 14001516, 135.20);
	}
	public void desdoblarPendiente(String fecha, int dni, double importe) {
// Se asume que el cliente tiene un solo servicio activo dado que la cuota se la coloca a ese
		sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
		EstadoMovBean em = (EstadoMovBean)sesion.get(EstadoMovBean.class, new Long(41));
		DisparoBean db = (DisparoBean)sesion.get(DisparoBean.class, new Long(1));
		try {
			ClienteBean c = cliente.getClienteByNroDoc(dni, sesion);
			Iterator si = c.getServicios().iterator();
			while(si.hasNext()) {
				ServicioBean sb = (ServicioBean)si.next();
				if(sb.isAprobado()) {
					MovimientoBean m = new MovimientoBean();
					m.setCliente(c);
					m.setDescripcion("A Disparar Manual");
					m.setEstado(em);
					m.setDisparo(db);
					m.setFecha(DateTimeUtil.getDateAAAAbMMbDD(fecha));
					m.setIdCliente(c.getId());
					m.setIdServicio(sb.getId());
					m.setImporte(importe);
					movimiento.agregar(m, usuarioString);
					sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public void lista1() {
		this.agregarDisparoCliente(33013232);
	}
	public void duplicarPendiente(int dni) {
		System.out.println(dni);
		sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
		EstadoMovBean em = (EstadoMovBean)sesion.get(EstadoMovBean.class, new Long(43));
		DisparoBean db = (DisparoBean)sesion.get(DisparoBean.class, new Long(1));
		try {
			ClienteBean c = cliente.getClienteByNroDoc(dni, sesion);
			Iterator si = c.getServicios().iterator();
			while(si.hasNext()) {
				ServicioBean sb = (ServicioBean)si.next();
				if(sb.isAprobado()) {
					MovimientoBean m = new MovimientoBean();
					m.setCliente(c);
					m.setDescripcion("A Disparar Manual");
					m.setEstado(em);
					m.setDisparo(db);
					m.setFecha(DateTimeUtil.getDateAAAAbMMbDD("2014-12-01"));
					m.setIdCliente(c.getId());
					m.setIdServicio(sb.getId());
					m.setImporte(sb.getImporteCuota());
					movimiento.agregar(m, usuarioString);
					sesion = CacheManager.getInstance().getUsuarioSesion(usuarioString);
				}
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public void agregarDisparoCliente(int dni) {
		System.out.println(dni);
		try {
			ClienteBean c = cliente.getClienteByNroDoc(dni, sesion);
			DisparoEntidadesBean deb = new DisparoEntidadesBean();
			deb.setId(new Long(0));
			deb.setIdentificador(c.getId());
			deb.setTipo(DisparoEntidadesBean.CLIENTE);
			delegacion.persistirDisparoEntidadesSesion(deb, sesion);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}