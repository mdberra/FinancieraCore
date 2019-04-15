package com.financiera.domain.servicio;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.MovimientoService;

public interface DisparoServicio extends Service {

	public void generarCuotasDelegacion(Long idDelegacion, String fechaMesDisparo, String usuario) throws Exception;
	
	public Collection<String> generarMovimiento(List<ClienteBean> clientes, Date fechaReferencia, MovimientoService movimiento, Session sesion);
	public Collection<String> generarMovimientoDisparo(List<ServicioBean> servicioAFact, Date fechaReferencia, MovimientoService movimiento, boolean validarMovExistente, Session sesion);
	public Collection<String> generarMovimientoDisparoDupInteres(List<ServicioBean> servicioAFact, Date fechaReferencia, MovimientoService movimiento, Session sesion);

	public Collection<String> generarDisparoBaproSVilleItauBica(List<ServicioBean> clientes, String periodo, String periodoAnterior, Date fechaGeneracionArchivo, PrintWriter printerBapro, PrintWriter printerSVille, PrintWriter printerItau, PrintWriter printerBica, Session sesion);
	public Collection<String> generarDisparoNacion(String mes, String nroDisquette, String fechaTope, List<ServicioBean> clientes, String periodo, String periodoAnterior, PrintWriter printerNacion, Session sesion);

}