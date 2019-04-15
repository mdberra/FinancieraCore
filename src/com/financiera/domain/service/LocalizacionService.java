package com.financiera.domain.service;

import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.LocalizacionBean;

public interface LocalizacionService extends Service {

	public void agregar(LocalizacionBean d, Session sesion);
	public LocalizacionBean getLocalizacion(Long id, Session sesion);
	public List getLocalizacion(Session sesion);
	
}