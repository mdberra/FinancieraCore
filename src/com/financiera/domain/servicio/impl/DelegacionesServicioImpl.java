package com.financiera.domain.servicio.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.DisparoEntidadesDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.service.BancoService;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;
import com.financiera.domain.service.EntidadService;
import com.financiera.domain.servicio.DelegacionesServicio;

public class DelegacionesServicioImpl extends AbstractService implements DelegacionesServicio {
	private ClienteService    cliente    = (ClienteService)	   ServiceLocator.getInstance().getService(ClienteService.class);
	private EntidadService    entidad    = (EntidadService)	   ServiceLocator.getInstance().getService(EntidadService.class);
	private DelegacionService delegacion = (DelegacionService) ServiceLocator.getInstance().getService(DelegacionService.class);
	private BancoService      banco		 = (BancoService)	   ServiceLocator.getInstance().getService(BancoService.class);
	
	public DelegacionesServicioImpl(){
		super();
		this.setDescription("Servicio para Delegaciones");
		this.setName("DelegacionesServicio");
	}

	public void actualizarDelegacion(Long idDelegacion, String codigo, String descripcion, Long idBanco, int utilizar, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		DelegacionBean d = new DelegacionBean();
		d.setId(idDelegacion);
		d.setCodigo(codigo);
		d.setDescripcion(descripcion);
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			d.setLocalizacion(entidad.getLocalizacion(new Long(1), sesion));
			d.setEntidad(entidad.getEntidad(new Long(1), sesion));
			d.setBanco(banco.getBanco(idBanco, sesion));
			d.setUtilizar(utilizar);
			if(d.getId() == 0) {
				d.setF_alta(DateTimeUtil.getFechaInicio(ParametroBean.getParametro1(sesion).getDiaCorteAltaDelegacion()));
			}
		} catch(Exception e) {}
		
		delegacion.actualizar(d, sesion);
		CacheManager.getInstance().restart(usuario);
	}
	public void actualizarBanco(Long idBanco, String codigo, String descripcion, String codigoDebito, String descripPrestacion, String bancoRecaudador, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		BancoBean b = new BancoBean();
		b.setId(idBanco);
		b.setCodigo(codigo);
		b.setDescripcion(descripcion);
		b.setCodigoDebito(Integer.parseInt(codigoDebito));
		b.setDescripPrestacion(descripPrestacion);
		b.setBancoRecaudador(bancoRecaudador);
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			b.setLocalizacion(entidad.getLocalizacion(new Long(1), sesion));
		} catch(Exception e) {}
		
		banco.actualizar(b, sesion);
		CacheManager.getInstance().restart(usuario);
	}
	public String[] getDisparoEntidadesStrAll(int desde, int hasta, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DisparoEntidadesBean> dispEnt = delegacion.getDisparoEntidadesAll(desde, hasta, sesion);
		String[] de = new String[dispEnt.size()];
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			
			Iterator dI = dispEnt.iterator();
			while(dI.hasNext()) {
				DisparoEntidadesBean deBean = ((DisparoEntidadesBean)dI.next());
				
				DisparoEntidadesDTO deDTO = new DisparoEntidadesDTO();
				deDTO.setId(deBean.getIdentificador());
				deDTO.setTipo(deBean.getTipo());
				if(deBean.esCliente()) {
					deDTO.setDatos(cliente.getClienteById(deBean.getIdentificador(), sesion).getDatosCliente());
				}
				if(deBean.esDelegacion()) {
					deDTO.setDatos(delegacion.getDelegacion(deBean.getIdentificador(), sesion).getDatosDelegacion(sesion));
				}
				de[i++] = deDTO.toString();
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return de; 
	}
}