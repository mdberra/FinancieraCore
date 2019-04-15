package com.financiera.domain.servicio;

import java.util.Collection;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.ServicioBean;

public interface CtacteServicio extends Service {
	public static final String ESQ_BAPRO = "Esquema Bapro";
	public static final String ESQ_SUPERVILLE = "Esquema SuperVille";
	public static final String ESQ_NACION = "Esquema Nacion";
	public static final String ESQ_ITAU = "Esquema Itau";
	public static final String ESQ_BICA = "Esquema Bica";

	public Collection<String> generarCtacteCuota(String fechaMesDisparo, String usuario);
	public String procesarRespuesta(String tipo, String inputFileName, String usuario);
	
	public void delMovimientoADisparar(Long idCliente, String usuario) throws Exception;
	
	public Collection<String> guardianes(String fDesde, String usuario);
	public Collection<String> guardianImporteMovimiento(Session sesion);
	public Collection<String> guardianCuotasMensuales(String fechaDesde, Session sesion);
	public void guardian38(Session sesion);
	public void actualizarHistorico(String usuario);
	public void actualizarHistorico(ServicioBean sb, String usuario);
	public void depurarHistorico(String usuarioString, String fecha);
}