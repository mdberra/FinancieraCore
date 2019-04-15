package com.financiera.core.server;

import java.util.HashMap;
import java.util.Map;

import com.financiera.domain.servicio.CtacteServicio;
import com.financiera.domain.servicio.DelegacionesServicio;
import com.financiera.domain.servicio.DisparoServicio;
import com.financiera.domain.servicio.EstadisticaServicio;
import com.financiera.domain.servicio.ExportacionExcelServicio;
import com.financiera.domain.servicio.GenerarDisparoServicio;
import com.financiera.domain.servicio.OperacionesServicio;
import com.financiera.domain.servicio.VendedoresServicio;
import com.financiera.domain.servicio.impl.CtacteServicioImpl;
import com.financiera.domain.servicio.impl.DelegacionesServicioImpl;
import com.financiera.domain.servicio.impl.DisparoServicioImpl;
import com.financiera.domain.servicio.impl.EstadisticaServicioImpl;
import com.financiera.domain.servicio.impl.ExportacionExcelServicioImpl;
import com.financiera.domain.servicio.impl.GenerarDisparoServicioImpl;
import com.financiera.domain.servicio.impl.OperacionesServicioImpl;
import com.financiera.domain.servicio.impl.VendedoresServicioImpl;

public class ServiceNegocioLocator {
	
	static ServiceNegocioLocator singletonInstance;
	
	private Map<Class, Service> services = null;
	
	static public ServiceNegocioLocator getInstance(){
		if(singletonInstance==null)
			singletonInstance = initialize();
		return singletonInstance;
	}
	private ServiceNegocioLocator(){
		super();
		this.services = new HashMap<Class, Service>();
	}
	static private ServiceNegocioLocator  initialize() {
		ServiceNegocioLocator serviceLocator = new ServiceNegocioLocator();
		serviceLocator.addServervice(CtacteServicio.class,			 new CtacteServicioImpl());
		serviceLocator.addServervice(DisparoServicio.class,		 	 new DisparoServicioImpl());
		serviceLocator.addServervice(ExportacionExcelServicio.class, new ExportacionExcelServicioImpl());
		serviceLocator.addServervice(GenerarDisparoServicio.class,	 new GenerarDisparoServicioImpl());
		serviceLocator.addServervice(DelegacionesServicio.class,	 new DelegacionesServicioImpl());
		serviceLocator.addServervice(VendedoresServicio.class,	 	 new VendedoresServicioImpl());
		serviceLocator.addServervice(OperacionesServicio.class,	 	 new OperacionesServicioImpl());
		serviceLocator.addServervice(EstadisticaServicio.class,	 	 new EstadisticaServicioImpl());
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