package com.financiera.domain.servicio;

import org.hibernate.Session;

import com.financiera.core.server.Service;

public interface GenerarDisparoServicio extends Service {
	
	public String[] dispararBaproSVille(String fechaMesDisparo, boolean validarMovExistente, String usuario);
	public String[] dispararNacion(String fechaMesDisparo, String fechaTope, String nroDisquette, boolean validarMovExistente, String usuario);
	public String[] guardianClienteSinServicio(Session sesion);
}