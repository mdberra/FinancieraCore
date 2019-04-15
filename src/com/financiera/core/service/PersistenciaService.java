package com.financiera.core.service;

import java.util.Collection;

import org.hibernate.Session;

import com.financiera.core.server.Service;

public interface PersistenciaService extends Service {
	
	public void persistirMasivo(Collection<Object> c, Collection<String> informe, Session sesion) throws Exception;
	public String[] informeToString(Collection<String> informe);
}