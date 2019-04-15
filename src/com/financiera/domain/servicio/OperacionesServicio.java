package com.financiera.domain.servicio;

import com.financiera.core.server.Service;

public interface OperacionesServicio extends Service {
	public String[] Backup(String usuario);
	public String[] BackupArca(String usuario);
	public String[] Restore(String inputFileName, String usuario);
	public String[] RestoreArca(String inputFileName, String usuario);
//	public String[] subirLocalizacion(String inputFileName);
//	public String[] BackupCompletoArca();
//	public String[] eliminarNoArca(String usuario);
//	public String[] limpiarClientes(String usuario);
	public String[] agregarMov(Long idDelegacion, String fecha, String usuario);
	public String actualizarServicio(String sDTO, String usuario);
	public String renovarServicio(String sDTO, String usuario);
}