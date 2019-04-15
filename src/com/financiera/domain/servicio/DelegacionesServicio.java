package com.financiera.domain.servicio;

import org.hibernate.Session;

import com.financiera.core.server.Service;

public interface DelegacionesServicio extends Service {
	public void actualizarDelegacion(Long idDelegacion, String codigo, String descripcion, Long idBanco, int utilizar, String usuario);
	public void actualizarBanco(Long idBanco, String codigo, String descripcion, String codigoDebito, String descripPrestacion, String bancoRecaudador, String usuario);
	public String[] getDisparoEntidadesStrAll(int desde, int hasta, String usuario);
}