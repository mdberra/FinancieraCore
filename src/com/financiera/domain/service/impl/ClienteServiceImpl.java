package com.financiera.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ServicioDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.bean.Persistible;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;


public class ClienteServiceImpl extends AbstractService implements ClienteService {
	private LocalizacionBean locVacio = null;
	
	public ClienteServiceImpl(){
		super();
		this.setDescription("Servicio para Clientes");
		this.setName("ClienteService");
	}

	public void agregar(ClienteBean c, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			sesion.save(c);
			
		} finally {
			sesion.flush();
			transaction.commit();
		}
		CacheManager.getInstance().restart(usuario);
	}
	
	public void persistir(ClienteBean c, String usuario) {
		ClienteBean clienteBean;
		LocalizacionBean locPartBean;
		LocalizacionBean locLabBean;
		LocalizacionBean locInfBean;

		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
		
			if(c.getAccion() == Persistible.UPDATE) {
				locPartBean = (LocalizacionBean)sesion.get(LocalizacionBean.class, c.getLocParticular().getId());
				this.movesLocalizacion(locPartBean, c.getLocParticular());
				
				locLabBean  = (LocalizacionBean)sesion.get(LocalizacionBean.class, c.getLocLaboral().getId());
				this.movesLocalizacion(locLabBean, c.getLocLaboral());
				
				locInfBean  = (LocalizacionBean)sesion.get(LocalizacionBean.class, c.getLocInformado().getId());
				this.movesLocalizacion(locInfBean, c.getLocInformado());
				
				clienteBean = (ClienteBean)sesion.get(ClienteBean.class, c.getId());
				this.movesCliente(c, clienteBean);

				sesion.update(locPartBean);
				sesion.update(locLabBean);
				sesion.update(locInfBean);
				sesion.update(clienteBean);
			}
			if(c.getAccion() == Persistible.INSERT) {
				locPartBean = new LocalizacionBean();
				this.movesLocalizacion(locPartBean, c.getLocParticular());
				
				locLabBean  = new LocalizacionBean();
				this.movesLocalizacion(locLabBean, c.getLocLaboral());
				
				locInfBean  = new LocalizacionBean();
				this.movesLocalizacion(locInfBean, c.getLocInformado());
				
				clienteBean = new ClienteBean();
				this.movesCliente(c, clienteBean);
				
				sesion.save(locPartBean);
				sesion.save(locLabBean);
				sesion.save(locInfBean);
				clienteBean.setLocParticular(locPartBean);
				clienteBean.setLocLaboral(locLabBean);
				clienteBean.setLocInformado(locInfBean);
				sesion.save(clienteBean);
			}
		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
		finally {
			sesion.flush();
			transaction.commit();
			locPartBean = null;
			locLabBean  = null;
			locInfBean  = null;
			clienteBean = null;
		}
		CacheManager.getInstance().restart(usuario);
	}
	private void movesCliente(ClienteBean input,ClienteBean output) {
		output.setTipoDoc(input.getTipoDoc());
		output.setNroDoc(input.getNroDoc());
		output.setCbu(input.getCbu());
		output.setSucursalCA(input.getSucursalCA());
		output.setNroCA(input.getNroCA());
		output.setNombre(input.getNombre());
		output.setApellido(input.getApellido());
		output.setFechaIngreso(input.getFechaIngreso());
		output.setComentarios(input.getComentarios());
		output.setLocLaboral(input.getLocLaboral());
		output.setLocParticular(input.getLocParticular());
		output.setLocInformado(input.getLocInformado());
		output.setEstado(input.getEstado());
		output.setFechaEstado(input.getFechaEstado());
		output.setEstadoAnterior(input.getEstadoAnterior());
		
		output.setIdDelegacion(CacheManager.getInstance().getIdDelegacion(input.getDelegacion().getDescripcion()));
	}
	private void movesLocalizacion(LocalizacionBean output, LocalizacionBean input) {
		output.setId(input.getId());
		output.setCalle(input.getCalle());
		output.setNro(input.getNro());
		output.setPiso(input.getPiso());
		output.setDepto(input.getDepto());
		output.setTelefCelular(input.getTelefCelular());
		output.setTelefLinea(input.getTelefLinea());
		output.setCodPostal(input.getCodPostal());
		output.setBarrio(input.getBarrio());
		output.setLocalidad(input.getLocalidad());
		output.setProvincia(input.getProvincia());
		output.setPais(input.getPais());
	}
	public List<ClienteBean> getClientes(String usuario, Session sesion) {
		List<ClienteBean> clientes =  new ArrayList<ClienteBean>();
		if(usuario.compareTo("Emiliano Arca")!= 0) {
			try{
				Query q = sesion.getNamedQuery("ClienteBean.getAll");
				Iterator cI = q.list().iterator();
				while(cI.hasNext()) {
					clientes.add((ClienteBean)cI.next());
				}
			} catch(Exception e) {
				System.out.println("ClienteService : " + e.getMessage());
			}
		}
		return clientes;
	}
	public String[] getClientesStr(String usuario) {
		String[] aa = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			List<ClienteBean> clientes = this.getClientes(usuario, sesion);
			String[] cli = new String[clientes.size()];
			int j = 0;
			Iterator cI = clientes.iterator();
			while(cI.hasNext()) {
				ClienteBean cliente = (ClienteBean)cI.next();
				if (cliente.isActivo()) {
					cli[j++] = this.armarCliente(cliente, usuario, sesion);
				}
			}
			aa = new String[j];
			for(int i=0; i<j; i++) {
				aa[i] = cli[i]; 
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return aa;
	}	
	
	public List<ClienteBean> getClienteByApellido(String apellido, Session sesion) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		int i = 0;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.getAllbyApellido");
			q.setString("apellido", apellido);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				ClienteBean c = (ClienteBean)cI.next();
				clientes.add(i++,c);
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}

		return clientes;
	}
	public String[] getClienteByApellidoStr(String apellido, int desde, int hasta, String usuario) {
		String[] salida = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			List<ClienteBean> clientes = this.getClienteByApellido(apellido, sesion);
			
			int longitud = hasta - desde + 1;
			String[] cli = new String[longitud];
			int i = 0, j = 0;
			Iterator<ClienteBean> cI = clientes.iterator();
			while(cI.hasNext()) {
				ClienteBean cliente = (ClienteBean)cI.next();
				if(i>=desde && i<= hasta) {
					cli[j++] = this.armarCliente(cliente, usuario, sesion);
				}
				i++;
			}
			salida = new String[j+1];
			int k;
			for(k = 0; k < j; k++) {
				salida[k] = cli[k];
			}
			salida[k++] = new String("total;"+ i);
			
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return salida;
	}
	
	public List<ClienteBean> getClienteByDelegacion(Session sesion) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		int i = 0;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.getAllbyDelegacion");
			Iterator<ClienteBean> cI = q.list().iterator();
			while(cI.hasNext()) {
				ClienteBean c = (ClienteBean)cI.next();
				clientes.add(i++,c);
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}
		return clientes;
	}
	
	public List<ClienteBean> getClienteFindDelegacion(Long idDelegacion, Session sesion) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		int i = 0;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.findByDelegacion");
			q.setLong("idDelegacion", idDelegacion);
			Iterator<ClienteBean> cI = q.list().iterator();
			while(cI.hasNext()) {
				ClienteBean c = (ClienteBean)cI.next();
				clientes.add(i++,c);
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}
		return clientes;
	}
	public List<ClienteBean> getClienteFindDelegacionEstado(Long idDelegacion, int idEstado, Session sesion) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		int i = 0;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.findByDelegacionEstado");
			q.setLong("idDelegacion", idDelegacion);
			q.setInteger("idEstado", idEstado);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				ClienteBean c = (ClienteBean)cI.next();
				clientes.add(i++,c);
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}
		return clientes;
	}
	public List<ClienteBean> getClienteFindDelegacion(Collection<DelegacionBean> delegaciones, Session sesion) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		try {
//			Recorro las delegaciones que se eligieron
			Iterator i = delegaciones.iterator();
			while(i.hasNext()) {
				DelegacionBean d = (DelegacionBean)i.next();
				clientes.addAll(this.getClienteFindDelegacion(d.getId(), sesion));
			}
		} catch(Exception e) {
			System.out.println("ClienteService - getClienteFindDelegacion: " + e.getMessage());
		}
		return clientes;
	}

	public String[] getClienteByDelegacionStr(Long idDelegacion, String usuario) {
		String[] cli = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			List<ClienteBean> clientes = this.getClienteFindDelegacion(idDelegacion, sesion);
			cli = new String[clientes.size()+1];
			int i = 0;
			Iterator cI = clientes.iterator();
			while(cI.hasNext()) {
				ClienteBean cliente = (ClienteBean)cI.next();
				cli[i++] = this.armarCliente(cliente, usuario, sesion);
			}
			cli[i++] = new String("total;"+ i);
			
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return cli;
	}
	public String[] getClienteByDelegacionStr(Long idDelegacion, int desde, int hasta, String usuario) {
		String[] cli = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			List<ClienteBean> clientes = this.getClienteFindDelegacion(idDelegacion, sesion);
			cli = seleccionarClientes(clientes.iterator(), desde, hasta, usuario, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return cli;
	}
	private String[] seleccionarClientes(Iterator cI, int desde, int hasta, String usuario, Session sesion) {
		if(desde > hasta) {
			desde = 0;
			hasta = 0;
		}
		int longitud = hasta - desde + 1;
		String[] cli = new String[longitud];
		int i = 0, j = 0;
		while(cI.hasNext()) {
			ClienteBean cliente = (ClienteBean)cI.next();
			if(i>=desde && i<= hasta) {
				cli[j++] = this.armarCliente(cliente, usuario, sesion);
			}
			i++;
		}
		String[] salida = new String[j+1];
		int k;
		for(k = 0; k < j; k++) {
			salida[k] = cli[k];
		}
		salida[k++] = new String("total;"+ i);
		return salida;
	}
	public String[] getClienteByDelegacionEstadoStr(Long idDelegacion, int idEstado, int desde, int hasta, String usuario) {
		String[] cli = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			List<ClienteBean> clientes = this.getClienteFindDelegacionEstado(idDelegacion, idEstado, sesion);
			cli = seleccionarClientes(clientes.iterator(), desde, hasta, usuario, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return cli;
	}
	public ClienteBean getClienteById(Long id) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(new String("Administrador"));
		return this.getClienteById(id, sesion);
	}
	public ClienteBean getClienteById(Long id, Session sesion) {
		ClienteBean cliente = null;
		try {
			Query q = sesion.getNamedQuery("ClienteBean.getId");
			q.setLong("id", id);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				 cliente = (ClienteBean)cI.next();
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}
		return cliente;
	}
	public String getClienteByIdStr(Long id, String usuario) {
		String cli = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			cli = this.armarCliente(this.getClienteById(id, sesion), usuario, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return cli;
	}
	
	public ClienteBean getClienteByNroDoc(int nroDoc, Session sesion) throws Exception {
		ClienteBean cliente = null;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.getNroDoc");
			q.setLong("nroDoc", nroDoc);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				cliente = (ClienteBean)cI.next();
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
			throw e;
		}
		return cliente;
	}
	public ClienteBean getClienteByNroDocActivo(int nroDoc, Session sesion) throws Exception {
		ClienteBean cliente = new ClienteBean();
		cliente.setEstado(1);
		try{
			Query q = sesion.getNamedQuery("ClienteBean.getNroDoc");
			q.setLong("nroDoc", nroDoc);
			Iterator cI = q.list().iterator();
			while(cI.hasNext() && !cliente.isActivo()) {
				cliente = (ClienteBean)cI.next();
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
			throw e;
		}
		return cliente;
	}
	public String getClienteByNroDocStr(int nroDoc, String usuario) {
		String cli = null;
		ClienteBean cliente;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			cliente = this.getClienteByNroDoc(nroDoc, sesion);
			cli = this.armarCliente(cliente, usuario, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return cli;
	}
	
	public String[] getClienteByNroDocStr(int nrodoc, int desde, int hasta, String usuario) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		int longitud = hasta - desde + 1;
		String[] cli = new String[longitud];
		int i = 0, j = 0;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ClienteBean.getNroDoc");
			q.setInteger("nroDoc", nrodoc);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				clientes.add(i++, (ClienteBean)cI.next());
			}
			i = 0;
			cI = clientes.iterator();
			while(cI.hasNext()) {
				ClienteBean cliente = (ClienteBean)cI.next();
				if(i>=desde && i<= hasta) {
					cli[j++] = this.armarCliente(cliente, usuario, sesion);
				}
				i++;
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		String[] salida = new String[j+1];
		int k;
		for(k = 0; k < j; k++) {
			salida[k] = cli[k];
		}
		salida[k++] = new String("total;"+ i);
		return salida;
	}
	
	public void persistir(LocalizacionBean l, String usuario) {
		LocalizacionBean localizacionBean;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
		
			if(l.getAccion() == Persistible.UPDATE) {
				localizacionBean = (LocalizacionBean)sesion.get(LocalizacionBean.class, l.getId());
				this.movesLocalizacion(localizacionBean, l);
				sesion.update(localizacionBean);
			}
			if(l.getAccion() == Persistible.INSERT) {
				localizacionBean = new LocalizacionBean();
				this.movesLocalizacion(localizacionBean, l);
				sesion.save(localizacionBean);
			}
		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
		finally {
			sesion.flush();
			transaction.commit();
		}
	}
	private String armarCliente(ClienteBean cli, String usuario, Session sesion) {
		boolean esArca = false;
		if(usuario.compareTo("Emiliano Arca")== 0) {
			esArca = true;
		}
		if(esArca) {
			Iterator<ServicioBean> it = cli.getServicios().iterator();
			while(it.hasNext()) {
				ServicioBean sss = (ServicioBean) it.next();
				ServicioDTO sd = new ServicioDTO(sss.toString());
				if(sd.getVendedor().esServina()) {

				}
			}
		}

		String s = cli.toString();
		if(!CacheManager.getInstance().veoTodosLosDatos(s, esArca)) {
			if(locVacio == null) {
				locVacio = (LocalizacionBean)sesion.get(LocalizacionBean.class, 3529l);
			}
			ClienteBean a = new ClienteBean();
			a.setId(cli.getId());
			a.setTipoDoc(cli.getTipoDoc());
			a.setNroDoc(cli.getNroDoc());
			a.setFechaIngreso(cli.getFechaIngreso());
			a.setIdDelegacion(cli.getIdDelegacion());
			a.setDelegacion(cli.getDelegacion());
			Set<ServicioBean> aa = new TreeSet<ServicioBean>();
			a.setServicios(aa);
			a.setApellido("-");
			a.setNombre("-");
			a.setCbu("-");
			a.setComentarios("-");
			a.setLocInformado(locVacio);
			a.setLocLaboral(locVacio);
			a.setLocParticular(locVacio);
			a.setNroCA("-");
			a.setSucursalCA("-");
			s = a.toString();
		}
		return s;
	}
	
	public ClienteBean getClienteBySucNroCA(String sucursalCA, String	nroCA, Session sesion) throws Exception {
		ClienteBean cliente = null;
		try{
			Query q = sesion.getNamedQuery("ClienteBean.getSucNroCA");
			q.setString("sucCA", sucursalCA);
			q.setString("nroCA", nroCA);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				cliente = (ClienteBean)cI.next();
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
			throw e;
		}
		return cliente;
	}
	public String[] getClienteByNroCAStr(String nroCA, int desde, int hasta, String usuario) {
		List<ClienteBean> clientes = new ArrayList<ClienteBean>();
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			int i = 0;
			transaction.begin();
			Query q = sesion.getNamedQuery("ClienteBean.getNroCA");
			q.setString("nroCA", nroCA);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				clientes.add(i++, (ClienteBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("ClienteService : " + e.getMessage());
		}
		int longitud = hasta - desde + 1;
		String[] cli = new String[longitud];
		int i = 0, j = 0;
		Iterator cI = clientes.iterator();
		while(cI.hasNext()) {
			ClienteBean cliente = (ClienteBean)cI.next();
			if(i>=desde && i<= hasta) {
				cli[j++] = this.armarCliente(cliente, usuario, sesion);
			}
			i++;
		}
		String[] salida = new String[j+1];
		int k;
		for(k = 0; k < j; k++) {
			salida[k] = cli[k];
		}
		salida[k++] = new String("total;"+ i);
		return salida;
	}
	public ParametroBean getParametro(Session sesion) {
		ParametroBean param = ParametroBean.getParametro1(sesion);
		return param;
	}
	public ParametroBean getParametro() {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(new String("Administrador"));
		ParametroBean param = ParametroBean.getParametro1(sesion);
		return param;
	}
}