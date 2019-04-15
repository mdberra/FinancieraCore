package com.financiera.domain.service;

import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.BancoBean;

public interface BancoService extends Service {

	public void actualizar(BancoBean b, Session sesion);
	public BancoBean getBanco(Long id, Session sesion);
	public String getBancoStr(Long id, String usuario);
	public List<BancoBean> getBancoAllOrderDescripcion(Session sesion);
	public String[] getBancoStrAllOrderDescripcion(String usuario);
}