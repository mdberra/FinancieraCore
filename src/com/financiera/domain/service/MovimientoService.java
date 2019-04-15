package com.financiera.domain.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.dto.MovTotTipoDTO;
import com.dto.RespuestaDTO;
import com.financiera.core.server.Service;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.ServicioBean;

public interface MovimientoService extends Service {

	public void agregar(MovimientoBean e, String usuario) throws Exception;
	public void agregar(Collection<Object> m, String usuario) throws Exception;
	public void agregarHist(Collection<Object> m, String usuario) throws Exception;
	public void actMovimiento(Long id, Date fecha, double importe, String codRechazo, String usuario) throws Exception;
	public void agrDevolucion(Long id, Date fecha, double importe, String usuario) throws Exception;
	public void delMovimiento(Long id, String usuario) throws Exception;
	public void dividirMovimiento(Long id, String usuario) throws Exception;
	public void delMovimientoADisparar(ClienteBean cliente, String usuario) throws Exception;
	public void delMovimiento(Collection<MovimientoBean> m, String usuario) throws Exception;
	public void agrRechazo(Long id, Date fecha, double importe, String codRechazo, String usuario) throws Exception;
	public void agrDebito(Long id, Date fecha, double importe, String usuario) throws Exception;
	public void agrADisparar(Long id, Date fecha, double importe, String usuario) throws Exception;
	public void agrReversion(Long id, Date fecha, double importe, String usuario) throws Exception;
	public void agrDisparo(Long id, Date fecha, double importe, String usuario) throws Exception;
	
	public List<MovimientoBean> getMovimientoByCliente(Long cliente, String usuario, Session sesion);
	public String[] getMovimientoByClienteStr(Long cliente, String usuario);
	public String[] getMovimientoByClienteStr(Long cliente, String usuario, Session sesion) throws Exception;
	public String[] getMovimientoHistByClienteStr(Long cliente, String usuario);
	public List<MovimientoBean> getMovimientoHistByCliente(Long cliente, String usuario, Session sesion);
	public List<MovimientoBean> getMovimientoByClientePeriodo(Long cliente, String periodo, String usuario, Session sesion);
	public MovTotTipoDTO getMovimientoByServicio(Long servicio, Session sesion) throws Exception;
	
	public MovimientoBean getMovimiento(ClienteBean cliente, Date fecha, double importe, String codigoInternoBanco, Session sesion) throws Exception;
	public MovimientoBean getMovimiento(Long idServicio, Date fecha, Session sesion) throws Exception;

	public void generarMovimiento(ClienteBean cliente, Date fecha, String periodo, Session session, EstadoMovBean estadoAdisparar) throws Exception;
	public Collection<String> generarMovimiento(Date maxFecha, ServicioBean servicio, String periodo, Session session, EstadoMovBean estadoAdisparar, boolean validarMovExistente, long estado1, long estado2, long estado3) throws Exception;
	public void generarMovPorDiaDisparo(ServicioBean servicio, MovimientoBean movimientoBean, EstadoMovBean estadoAdisparar, Session session, boolean validarMovExistente, double importe);

	public void actualizarSvc(ClienteBean c, Session sesion);
	public void actualizarSvc(ServicioBean svcb, Session sesion);
	
	public void generarCuotaInteresAll(ClienteService cliente, String usuario);
	public String actualizarRespuesta(ClienteService cliente, ServicioService servicioService, Collection<RespuestaDTO> respuestas, String resDTO, String usuario);
	public Long fixIdservicio(ServicioService servicio, MovimientoBean m, Session sesion);

	public String limpiarRespuestas(String resDTO, String usuario);
	
	public EstadoMovBean getEstadoMovId(Long id, Session sesion);
	public EstadoMovBean getEstadoMovEstado(int estado, Session sesion);
	public EstadoMovBean getEstadoMovColor(int color, Session sesion);
	public EstadoMovBean getEstadoCodRechazo(String codRechazo, Session sesion);
	public Collection<EstadoMovBean> getAllCodRechazos(Session sesion);
	public String[] getAllCodRechazosStr(String usuario);
	public List<MovimientoBean> getMovimientoByFecha(Date fDesde, Date fHasta, Session sesion);
	public List<MovimientoBean> findEstadoyRangoFecha(Date fDesde, Date fHasta, int estado, Session sesion);
	public String[] getMovimientoByClienteVertStr(Long cliente, String usuario);
	public List<MovimientoBean> getMovimientoFindEstado(int estado1, int estado2, Session sesion);
	public List<MovimientoBean> getMovimientoByDescripcion(String descripcion, Session sesion);
}