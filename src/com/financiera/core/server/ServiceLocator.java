package com.financiera.core.server;

import java.util.HashMap;
import java.util.Map;

import com.financiera.core.service.ArchivoService;
import com.financiera.core.service.PersistenciaService;
import com.financiera.core.service.impl.ArchivoServiceImpl;
import com.financiera.core.service.impl.PersistenciaServiceImpl;
import com.financiera.domain.service.BancoService;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;
import com.financiera.domain.service.DiasCobroService;
import com.financiera.domain.service.EntidadService;
import com.financiera.domain.service.LocalizacionService;
import com.financiera.domain.service.MovimientoService;
import com.financiera.domain.service.ProductoService;
import com.financiera.domain.service.ServicioService;
import com.financiera.domain.service.UsuarioService;
import com.financiera.domain.service.VendedorService;
import com.financiera.domain.service.impl.BancoServiceImpl;
import com.financiera.domain.service.impl.ClienteServiceImpl;
import com.financiera.domain.service.impl.DelegacionServiceImpl;
import com.financiera.domain.service.impl.DiasCobroServiceImpl;
import com.financiera.domain.service.impl.EntidadServiceImpl;
import com.financiera.domain.service.impl.LocalizacionServiceImpl;
import com.financiera.domain.service.impl.MovimientoServiceImpl;
import com.financiera.domain.service.impl.ProductoServiceImpl;
import com.financiera.domain.service.impl.ServicioServiceImpl;
import com.financiera.domain.service.impl.UsuarioServiceImpl;
import com.financiera.domain.service.impl.VendedorServiceImpl;

public class ServiceLocator {
	
	static ServiceLocator singletonInstance;
	
	private Map<Class, Service> services = null;
	
	static public ServiceLocator getInstance(){
		if(singletonInstance==null)
			singletonInstance = initialize();
		return singletonInstance;
	}

	private ServiceLocator(){
		super();
		this.services = new HashMap<Class, Service>();
	}

	static private ServiceLocator  initialize() {
		ServiceLocator serviceLocator = new ServiceLocator();
		serviceLocator.addServervice(PersistenceService.class,	new PersistenceService());
		serviceLocator.addServervice(PersistenciaService.class, new PersistenciaServiceImpl());
		serviceLocator.addServervice(ArchivoService.class,		new ArchivoServiceImpl());
		serviceLocator.addServervice(BancoService.class,		new BancoServiceImpl());
		serviceLocator.addServervice(ClienteService.class,		new ClienteServiceImpl());
		serviceLocator.addServervice(DelegacionService.class,	new DelegacionServiceImpl());
		serviceLocator.addServervice(DiasCobroService.class,	new DiasCobroServiceImpl());
		serviceLocator.addServervice(EntidadService.class,		new EntidadServiceImpl());		
		serviceLocator.addServervice(LocalizacionService.class,	new LocalizacionServiceImpl());
		serviceLocator.addServervice(MovimientoService.class,	new MovimientoServiceImpl());
		serviceLocator.addServervice(ProductoService.class,		new ProductoServiceImpl());
		serviceLocator.addServervice(ServicioService.class,		new ServicioServiceImpl());
		serviceLocator.addServervice(VendedorService.class,		new VendedorServiceImpl());
		serviceLocator.addServervice(UsuarioService.class,		new UsuarioServiceImpl());
		return serviceLocator;
	}
	
	@SuppressWarnings("unchecked")
	public void addServervice(Class clazz,Service service){
		this.services.put(clazz,service);
	}
	
	public  void removeService(Service service){
	}

	public Service getService(Class clazz){
		return this.services.get(clazz);
	}
}