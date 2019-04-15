package com.financiera.domain.servicio;

import java.util.Collection;

import com.financiera.core.server.Service;

public interface EstadisticaServicio extends Service {
	public Collection<String> generarCobrosReversion(String fechaDesde, String fechaHasta, String usuario);
	public Collection<String> ventas(String fechaDesde, String fechaHasta, String usuario);
	public Collection<String> rankingCobros(String cantCuotasCobradas, String idDelegacion, String usuario);
	public Collection<String> clientesEmbargar(String meses, String usuario);
	public Collection<String> clientesEstado(int estado, String usuario);
	public Collection<String> obtMovimientosEstado(String fechaDesde, String fechaHasta, int estado, String usuario);
	public Collection<String> cantMovPer(String usuario);
}