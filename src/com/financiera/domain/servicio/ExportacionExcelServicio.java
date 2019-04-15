
package com.financiera.domain.servicio;

import java.util.Date;

import org.hibernate.Session;

import com.financiera.core.server.Service;

public interface ExportacionExcelServicio extends Service {
	
	public void ctacteToExcel(String fileName, String usuario);
	public void ventasToExcel(String fDesde, String fHasta, String usuario);
	public String[] getVentas(Date fDesde, Date fHasta, String usuario);
	public void delegacionesToExcel(String usuario);
	public void bancosToExcel(String usuario);
	public void venedoresToExcel(String usuario);
	public String noCobradosToExcel(String usuario);
	public void extraerInfoGraf(String usuario);
	public void extraerInfo(String usuario);
}