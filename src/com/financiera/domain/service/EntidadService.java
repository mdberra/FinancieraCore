package com.financiera.domain.service;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.EntidadBean;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.bean.ParametroBean;

public interface EntidadService extends Service {

	public void agregar(EntidadBean e, Session sesion);
	public EntidadBean getEntidad(Long id, Session sesion);
	public ParametroBean getParametro(Session sesion);
	public LocalizacionBean getLocalizacion(Long id, Session sesion);
}