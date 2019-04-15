package com.financiera.domain.service;

import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DiasCobroBean;

public interface DiasCobroService extends Service {
	public List<DiasCobroBean> getDiasCobro(Long idDelegacion, Session sesion);
	public String[] getDiasCobroStr(Long idDelegacion, String usuario);
	public void eliminar(DelegacionBean delegacion, String usuario);
	public void agregarDiaCobro(DiasCobroBean d, String usuario);
	public void reemplazar(Long idDelegacion, String[] fechas, String usuario);
}