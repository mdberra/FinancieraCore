package com.financiera.domain.servicio.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ClienteDTO;
import com.dto.CtacteDTO;
import com.financiera.core.server.CacheManager;
import com.financiera.domain.servicio.ExportacionExcelServicio;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportacionExcelServicioImpl {// implements ExportacionExcelServicio {
}
/**
public class ExportacionExcelServicioImpl extends AbstractService implements ExportacionExcelServicio {
	private ClienteService    cliente    = (ClienteService)ServiceLocator.getInstance().getService(ClienteService.class);
	private MovimientoService movimiento = (MovimientoService)ServiceLocator.getInstance().getService(MovimientoService.class);
	private DelegacionService delegacion = (DelegacionService)ServiceLocator.getInstance().getService(DelegacionService.class);
	private ServicioService   servicio   = (ServicioService)ServiceLocator.getInstance().getService(ServicioService.class);
	private BancoService	  banco		 = (BancoService)ServiceLocator.getInstance().getService(BancoService.class);
	private VendedorService	  vendedor	 = (VendedorService)ServiceLocator.getInstance().getService(VendedorService.class);

	private WritableCellFormat datosCliCellFormat = new WritableCellFormat();
	private WritableCellFormat tituloCellFormat   = new WritableCellFormat();
	private WritableCellFormat tituloCentreCellFormat = new WritableCellFormat();
	private WritableCellFormat titulo1CellFormat  = new WritableCellFormat();
	private WritableCellFormat periodoCellFormat  = new WritableCellFormat();
	private WritableCellFormat labelCellFormat    = new WritableCellFormat();
	private WritableCellFormat numberCellFormat   = new WritableCellFormat();
	
	private Long idServicio = 0L;
	private String periodo = " ";
	private boolean hayDebito = false;
	private boolean hayRechazo = false;
	
	public void ctacteToExcel(String fileName, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Collection<TotalesDesgloseDTO> matriz = new ArrayList<TotalesDesgloseDTO>();
		
		File f = new File(fileName.concat(".xls"));
		CtacteDTO mov = null;
		String periodo = " ", per = " ";
		Long idServicio = 0l;
		Long idDelegacion = 0l;
		int k = 0;
		int fila = 0, filaInic = 0, filaFin = 0;
		int columna = 0, colInic = 0;
		int hoja = 0, vez = 0;
		boolean privez = true;

		String antFecha = null;
		double antImporte  = 0.0;

		this.lookAndFeel();
		
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		TotalesDesgloseDTO tdDTO = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			workbook = Workbook.createWorkbook(f);
			Iterator it1 = delegacion.getDelegacionAllOrderDescripcion(sesion).iterator();
			while(it1.hasNext()) {
				DelegacionBean d = (DelegacionBean)it1.next();
				Iterator it = cliente.getClienteFindDelegacion(d.getId(), sesion).iterator();
				while(it.hasNext()) {
					periodo = " ";
					idServicio = 0l;
					k = 0;
					ClienteBean cliente = (ClienteBean) it.next();
					if(!cliente.getIdDelegacion().equals(idDelegacion)) {
						if(idDelegacion.compareTo(new Long(0)) != 0) {
							if(hayDebito) {
								tdDTO.setRechazoCant(0);
								tdDTO.setRechazoImporte(0.0);
							}
							matriz.add(tdDTO);
						}
// 2						
						sheet = workbook.createSheet(cliente.getDelegacion().getDescripcion(), hoja++);
						sheet.addCell(new Label(0, 0, cliente.getDelegacion().getDescripcion(), tituloCellFormat));
						idDelegacion = cliente.getIdDelegacion();
						fila = 0; filaInic = 0; filaFin = 0; columna = 0; colInic = 0; vez = 0;

						tdDTO = new TotalesDesgloseDTO();
						tdDTO.setDelegacion(cliente.getDelegacion());
					}
	//	datos del cliente
					filaFin += 2; columna = 0;
// 1
					sheet.addCell(new Label(columna, filaFin, cliente.getDatosCliente(), datosCliCellFormat));

					antFecha = null;
					antImporte = 0.0;
					String[] movimientos = movimiento.getMovimientoByClienteStr(cliente.getId(), usuario, sesion);
					for(int i = 0; i < movimientos.length; i++) {
						if(movimientos[i] != null) {
							mov = new CtacteDTO(movimientos[i]);
							if(!mov.getIdServicio().equals(idServicio)) {

//								if(antFecha != null) {
//				       				System.out.println(cliente.getDelegacion().getDescripcion() + ";" + cliente.getNyA() + ";" + antFecha + ";" + antImporte);
//			       				}

							// armar barra
				       			columna = 0; k = 1;
				       			filaFin++;
				       			filaInic = filaFin;
				       			idServicio = mov.getIdServicio();
				       			privez = true;
				       			periodo = " ";
				       		}
		       				antFecha      = mov.getFecha();
		       				antImporte    = mov.getImporte();

							// cambiar periodo
				       		if(mov.getPeriodo().compareTo(periodo) != 0) {
								if(privez) {
									privez = false;
									vez = 0;
								} else {
									columna += 3;
									vez++;
								}
								if(tdDTO.getPeriodo() != null) {
									if(hayDebito) {
										tdDTO.setRechazoCant(0);
										tdDTO.setRechazoImporte(0.0);
									}
									matriz.add(tdDTO);
								}
								tdDTO = new TotalesDesgloseDTO();
								tdDTO.setDelegacion(cliente.getDelegacion());
								tdDTO.setPeriodo(mov.getPeriodo());

								fila = filaInic;
// 1
								this.lookAndFeel();
								
								if(mov.getDescripcion().compareTo("VACIO") == 0)
									per =  " ";
								else
									per = new String("(").concat(String.valueOf(k++)).concat(") ").concat(mov.getPeriodo());
// 2
								sheet.addCell(new Label(columna, filaInic, per, periodoCellFormat));
								sheet.mergeCells(columna, filaInic, columna+2, filaInic);
								periodo = mov.getPeriodo();
							}
							//agregar
				       		if(mov.getDescripcion().compareTo("VACIO") != 0) {
								fila++; colInic = columna;
// 3			
								this.lookAndFeel(mov.getEstadoColor(), vez);
								sheet.addCell(new Label(columna++, fila, mov.getFecha(), labelCellFormat));
								sheet.addCell(new Number(columna++, fila, mov.getImporte(), numberCellFormat));
								if(mov.getCodRechazo().compareTo("   ") == 0)
									per = " ";
								else
									per = new String("(" + mov.getCodRechazo() + ")");
// 1
								sheet.addCell(new Label(columna, fila, per, labelCellFormat));
								
								if(fila > filaFin)
									filaFin = fila;
								columna = colInic;
				       		}
// 3
				       		if(mov.getDescripcion().compareTo(MovimientoBean.VACIO) != 0) {
				       			tdDTO = this.acumular(tdDTO, mov);
				       		}
						}
					}
//					if(cliente.getId().compareTo(new Long(186))== 0) {
//						System.out.println(cliente.getDelegacion().getDescripcion() + ";" + cliente.getNyA() + ";" + antFecha + ";" + antImporte);
//					}
//					System.out.println(cliente.getDelegacion().getDescripcion() + ";" + cliente.getNyA() + ";" + antFecha + ";" + antImporte); 
				}
			}

			System.out.println("FIN");
			workbook.write();
			workbook.close();

			
			if(idDelegacion.compareTo(new Long(0)) != 0) {
				matriz.add(tdDTO);
// reagrupo
				Hashtable<String, TotalesDesgloseDTO> matrizTotal = new Hashtable<String, TotalesDesgloseDTO>();
				TotalesDesgloseDTO TDTO = null;
				Iterator itdDTO = matriz.iterator();
				while(itdDTO.hasNext()) {
					tdDTO = (TotalesDesgloseDTO)itdDTO.next();
					
					String key = tdDTO.getDelegacion().getDescripcion() + tdDTO.getPeriodo();

					if(matrizTotal.containsKey(key)) {
						TDTO = matrizTotal.get(key);
					} else {
						TDTO = new TotalesDesgloseDTO();
						TDTO.setDelegacion(tdDTO.getDelegacion());
						TDTO.setPeriodo(tdDTO.getPeriodo());
					}
					TDTO.addPendienteCant(tdDTO.getPendienteCant());
					TDTO.addPendienteImporte(tdDTO.getPendienteImporte());
					
					TDTO.addDebitoCant(tdDTO.getDebitoCant());
					TDTO.addDebitoImporte(tdDTO.getDebitoImporte());
					
					TDTO.addRechazoCant(tdDTO.getRechazoCant());
					TDTO.addRechazoImporte(tdDTO.getRechazoImporte());
					
					TDTO.addReversionCant(tdDTO.getReversionCant());
					TDTO.addReversionImporte(tdDTO.getReversionImporte());
					
					matrizTotal.put(key, TDTO);
				}
				
				f = new File(fileName.concat("Totales.xls"));
				workbook = Workbook.createWorkbook(f);
				sheet = workbook.createSheet("Totales", 0);
				columna = 0; fila = 0;
				sheet.addCell(new Label(columna, fila, "Delegacion", tituloCentreCellFormat));
				Hashtable<String, Integer> periodoColumna = new Hashtable<String, Integer>();
				Date fecha = DateTimeUtil.getDateAAAAMMDD("20100601");
				columna = 1;
				int colAnt;
				while(columna <= 255) {
					colAnt = columna;
					fila = 0;
					fecha = DateTimeUtil.add1Mes(fecha);
					
					sheet.addCell(new Label(columna, fila, DateTimeUtil.getPeriodo(fecha), tituloCentreCellFormat));
					periodoColumna.put(DateTimeUtil.getPeriodo(fecha), columna);
					int col7 = columna + 7;
					sheet.mergeCells(columna, fila, col7, fila);
				
					fila = 1;
					sheet.addCell(new Label(columna, fila, "Pendiente", titulo1CellFormat)); sheet.mergeCells(columna++, fila, columna++, fila);
					sheet.addCell(new Label(columna, fila, "Debitado", titulo1CellFormat));	 sheet.mergeCells(columna++, fila, columna++, fila);
					sheet.addCell(new Label(columna, fila, "Rechazado", titulo1CellFormat)); sheet.mergeCells(columna++, fila, columna++, fila);
					sheet.addCell(new Label(columna, fila, "Revertido", titulo1CellFormat)); sheet.mergeCells(columna++, fila, columna++, fila);
					
					columna = colAnt; fila = 2;
					for(int jj=0;jj<8;jj++) {
						sheet.addCell(new Label(columna++, fila, "Cantidad", titulo1CellFormat));
						sheet.addCell(new Label(columna++, fila, "Importe", titulo1CellFormat));
					}
					columna = col7 + 1;
				}
				fila = 3;
			    Vector<String> v = new Vector<String>(matrizTotal.keySet());
			    Collections.sort(v);
			    String descripcion = " ";
			    for (Enumeration e = v.elements(); e.hasMoreElements();) {
			        String key = (String)e.nextElement();
//			        if(key.lastIndexOf("Julio-2009") >=0) {
//			        	continue;
//			        }
//			        System.out.println(key);
			        if(key.endsWith("null")) {
			        	continue;
			        }
					tdDTO = (TotalesDesgloseDTO)matrizTotal.get(key);
										
					if(tdDTO.getDelegacion().getDescripcion().compareTo(descripcion) != 0) {
						this.lookAndFeel(EstadoMovBean.C2_AZUL, 1);
						descripcion = tdDTO.getDelegacion().getDescripcion();
						columna = 0; fila++;
						sheet.addCell(new Label(columna, fila, descripcion, datosCliCellFormat));
					}
					
					this.lookAndFeel(EstadoMovBean.C2_AZUL, DateTimeUtil.getNroMes(tdDTO.getPeriodo()));
					
					if(periodoColumna.get(tdDTO.getPeriodo()) == null) {
						System.out.println("");
					} else {
//						columna = periodoColumna.get(tdDTO.getPeriodo());
						
						sheet.addCell(new Number(columna++, fila, tdDTO.getPendienteCant(),		numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getPendienteImporte(),	numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getDebitoCant(),		numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getDebitoImporte(),		numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getRechazoCant(),		numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getRechazoImporte(),	numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getReversionCant(),		numberCellFormat));
						sheet.addCell(new Number(columna++, fila, tdDTO.getReversionImporte(),	numberCellFormat));
					}
				}
				workbook.write();
				workbook.close();
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}
	private TotalesDesgloseDTO acumular(TotalesDesgloseDTO tdDTO, CtacteDTO m) {
		if(m.getIdServicio().compareTo(idServicio) != 0 ||
		   m.getPeriodo().compareTo(periodo) != 0) {
			idServicio = m.getIdServicio();
			periodo = m.getPeriodo();
			hayDebito = false;
			hayRechazo = false;
		}
		EstadoMovBean em = new EstadoMovBean();
		em.setColor(m.getEstadoColor());
		switch(em.analisisEstado()) {
			case EstadoMovBean.PENDIENTE: {
				tdDTO.incrPendienteCant();
				tdDTO.addPendienteImporte(m.getImporte());
				break;
			}
			case EstadoMovBean.DEBITO: {
				hayDebito = true;
				tdDTO.incrDebitoCant();
				tdDTO.addDebitoImporte(m.getImporte());
				break;
			}
			case EstadoMovBean.RECHAZO: {
				if(!hayRechazo) {
					hayRechazo = true;
					tdDTO.incrRechazoCant();
					tdDTO.addRechazoImporte(m.getImporte());
				}
				break;
			}
			case EstadoMovBean.REVERSION: {
				tdDTO.incrReversionCant();
				tdDTO.addReversionImporte(m.getImporte());
				break;
			}
			case EstadoMovBean.DEVUELTO: {
				tdDTO.incrDevueltoCant();
				tdDTO.addDevueltoImporte(m.getImporte());
				break;
			}
		}
		return tdDTO;
	}
	private void lookAndFeel() {
		try {
			WritableFont fontTitulo = new WritableFont(WritableFont.TAHOMA, 14, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
			tituloCellFormat = new WritableCellFormat(fontTitulo);
			tituloCellFormat.setAlignment(Alignment.LEFT);
			tituloCellFormat.setBackground(Colour.WHITE);

			tituloCentreCellFormat = new WritableCellFormat(fontTitulo);
			tituloCentreCellFormat.setAlignment(Alignment.CENTRE);
			tituloCentreCellFormat.setBackground(Colour.WHITE);
			
			WritableFont fontTitulo1 = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
			titulo1CellFormat = new WritableCellFormat(fontTitulo1);
			titulo1CellFormat.setAlignment(Alignment.CENTRE);
			titulo1CellFormat.setBackground(Colour.WHITE);
			
			WritableFont fontDatosClientes = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
			datosCliCellFormat = new WritableCellFormat(fontDatosClientes);
			datosCliCellFormat.setAlignment(Alignment.LEFT);
			datosCliCellFormat.setBackground(Colour.WHITE);
			
			WritableFont fontPeriodos = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
			periodoCellFormat = new WritableCellFormat(fontPeriodos);
			periodoCellFormat.setAlignment(Alignment.CENTRE);
			periodoCellFormat.setBackground(Colour.LIGHT_GREEN);
		} catch(Exception e) {}
	}
	private void lookAndFeel(int color, int vez) {
		try {
			WritableFont fontGeneral = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
			switch(color) {
			case EstadoMovBean.C1_NARANJA  : fontGeneral.setColour(Colour.ORANGE); break; 
			case EstadoMovBean.C2_AZUL     : fontGeneral.setColour(Colour.BLUE); break;
			case EstadoMovBean.C3_VERDE    : fontGeneral.setColour(Colour.GREEN); break;
			case EstadoMovBean.C4_ROJO     : fontGeneral.setColour(Colour.RED); break;
			case EstadoMovBean.C5_NEGRO    : fontGeneral.setColour(Colour.BLACK); break;
			case EstadoMovBean.C6_AMARILLO : {
								fontGeneral.setColour(Colour.DARK_YELLOW);
								fontGeneral.setBoldStyle(WritableFont.BOLD);
								break;
							}
			}
			labelCellFormat = new WritableCellFormat(fontGeneral);
			labelCellFormat.setAlignment(Alignment.LEFT);
			
			numberCellFormat = new WritableCellFormat(fontGeneral);
			if(vez % 2 == 0) {
				labelCellFormat.setBackground(Colour.IVORY);
				numberCellFormat.setBackground(Colour.IVORY);
			} else {
				labelCellFormat.setBackground(Colour.ICE_BLUE);
				numberCellFormat.setBackground(Colour.ICE_BLUE);
			}
		} catch(Exception e) {}
	}
	
	public void ventasToExcel(String fDesde, String fHasta, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			
			File f = new File("C:\\Servina\\VentasXVendedor.xls");
			String[] s=null;
			try {
				s = this.getVentas(DateTimeUtil.getDate(fDesde), DateTimeUtil.getDate(fHasta), usuario);
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			ClienteBean clienteBean;
			ServicioDTO servicio;
			Long idVendedor = 0l;
			int fila = 0;
			int columna = 0;
			int hoja = 0;
			double comisionTotal = 0.0;
			this.lookAndFeel();
		
			WritableWorkbook workbook = null;
			WritableSheet sheet = null;
			try {
				workbook = Workbook.createWorkbook(f);
	
				for(int i=0; i< s.length; i++) {
					servicio = new ServicioDTO(s[i]);
				
					if(!servicio.getVendedor().getId().equals(idVendedor)) {
						if(hoja > 0) {
							this.lookAndFeel(EstadoMovBean.C2_AZUL, 1);
							fila += 2; columna = 3;
							sheet.addCell(new Label(columna++, fila, "TOTAL" , labelCellFormat));
							sheet.addCell(new Number(columna++, fila, comisionTotal , numberCellFormat));
						}
						this.lookAndFeel(EstadoMovBean.C3_VERDE, 1);
						sheet = workbook.createSheet(servicio.getVendedor().getNombre(), hoja++);
						sheet.addCell(new Label(0, 0, servicio.getVendedor().getNombre(), tituloCellFormat));
						idVendedor = servicio.getVendedor().getId();
						fila = 2; columna = 0; comisionTotal = 0.0;
						sheet.addCell(new Label(columna, fila, "Fecha Ingreso" , datosCliCellFormat));
						sheet.addCell(new Label(columna++, fila, "Producto" , datosCliCellFormat));
						sheet.addCell(new Label(columna++, fila, "Cliente" , datosCliCellFormat));
						sheet.addCell(new Label(columna++, fila, "Importe Total" , datosCliCellFormat));
						sheet.addCell(new Label(columna++, fila, "Delegacion" , datosCliCellFormat));					
						sheet.addCell(new Label(columna++, fila, "Comision" , datosCliCellFormat));
						sheet.addCell(new Label(columna++, fila, "Fecha Venta" , datosCliCellFormat));
					}
	//	Venta
					clienteBean = cliente.getClienteById(servicio.getIdCliente(), sesion);
					Double comision = servicio.getComision() * servicio.getImporteTotal() / 100;
					comisionTotal += comision;
					fila += 1; columna = 0;
					sheet.addCell(new Label(columna,   fila, servicio.getFechaIngreso() , labelCellFormat));
					sheet.addCell(new Label(columna++, fila, servicio.getProducto().getDescripcion() , labelCellFormat));
					sheet.addCell(new Label(columna++, fila, clienteBean.getNyA() , labelCellFormat));
					sheet.addCell(new Number(columna++, fila, servicio.getImporteTotal() , numberCellFormat));
					sheet.addCell(new Label(columna++, fila, clienteBean.getDelegacion().getDescripcion(), labelCellFormat));
					sheet.addCell(new Number(columna++, fila, comision , numberCellFormat));
					sheet.addCell(new Label(columna++, fila, servicio.getFechaVenta() , labelCellFormat));
				}
				if(hoja > 0) {
					this.lookAndFeel(EstadoMovBean.C2_AZUL, 1);
					fila += 2; columna = 3;
					sheet.addCell(new Label(columna++, fila, "TOTAL" , labelCellFormat));
					sheet.addCell(new Number(columna++, fila, comisionTotal , numberCellFormat));
				}
				workbook.write();
				workbook.close();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
		finally {
			transaction.commit();
			sesion.flush();
		}
	}
	public String[] getVentas(Date fDesde, Date fHasta, String usuario) {
		return servicio.getServicioByFechaIngresoOrderVendedor(fDesde, fHasta, usuario);
	}
	
	public void delegacionesToExcel(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List dlg = delegacion.getDelegacionAllOrderDescripcion(sesion);
		File f = new File("C:\\Servina\\Delegaciones.xls");
		
		DelegacionBean dlgBean;
		DiasCobroBean dcBean;
		int fila = 0;
		int columna = 0;
		int hoja = 0;
		this.lookAndFeel();
	
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		try {
			workbook = Workbook.createWorkbook(f);
			sheet = workbook.createSheet("Delegaciones", hoja++);
			sheet.addCell(new Label(columna++, fila, "Nro" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Delegacion" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Banco" , tituloCellFormat));
			sheet.addCell(new Label(columna, fila, "Dias de Cobro" , tituloCellFormat));
			sheet.mergeCells(columna, fila,columna+9, fila);
			
			Iterator dlgIt = dlg.iterator();
			while(dlgIt.hasNext()) {
				dlgBean = (DelegacionBean)dlgIt.next(); 

				fila += 1; columna = 0;
				sheet.addCell(new Number(columna++, fila, dlgBean.getId(), numberCellFormat));
				sheet.addCell(new Label(columna++, fila, dlgBean.getDescripcion() , labelCellFormat));
				sheet.addCell(new Label(columna++, fila, dlgBean.getBanco().getDescripcion() , labelCellFormat));
				
				Iterator dcBeanIt = dlgBean.getDiasCobro(sesion).iterator();
				while(dcBeanIt.hasNext()) {
					dcBean = (DiasCobroBean)dcBeanIt.next();
					sheet.addCell(new Label(columna++, fila, dcBean.getFechaDisparoStr() , labelCellFormat));
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public void bancosToExcel(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List bancos = banco.getBancoAllOrderDescripcion(sesion);
		File f = new File("C:\\Servina\\Bancos.xls");
		
		BancoBean bancoBean;
		int fila = 0;
		int columna = 0;
		int hoja = 0;
		this.lookAndFeel();
	
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		try {
			workbook = Workbook.createWorkbook(f);
			sheet = workbook.createSheet("Bancos", hoja++);
			sheet.addCell(new Label(columna++, fila, "Nro" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Codigo" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Descripcion" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Codigo del Debito" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Descripcion de la Prestacion" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Banco Recaudador" , tituloCellFormat));
			
			Iterator dlgIt = bancos.iterator();
			while(dlgIt.hasNext()) {
				bancoBean = (BancoBean)dlgIt.next(); 

				fila += 1; columna = 0;
				sheet.addCell(new Number(columna++, fila, bancoBean.getId(), numberCellFormat));
				sheet.addCell(new Label(columna++, fila, bancoBean.getCodigo() , labelCellFormat));
				sheet.addCell(new Label(columna++, fila, bancoBean.getDescripcion() , labelCellFormat));
				sheet.addCell(new Label(columna++, fila, String.valueOf(bancoBean.getCodigoDebito()), numberCellFormat));
				sheet.addCell(new Label(columna++, fila, bancoBean.getDescripPrestacion() , labelCellFormat));
				sheet.addCell(new Label(columna++, fila, bancoBean.getBancoRecaudador() , labelCellFormat));
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public void venedoresToExcel(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List vendedores = vendedor.getVendedorAll(sesion);
		File f = new File("C:\\Servina\\Vendedores.xls");
		
		VendedorBean vendedorBean;
		int fila = 0;
		int columna = 0;
		int hoja = 0;
		this.lookAndFeel();
	
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		try {
			workbook = Workbook.createWorkbook(f);
			sheet = workbook.createSheet("Vendedores", hoja++);
			sheet.addCell(new Label(columna++, fila, "Nro" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Codigo" , tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Nombre" , tituloCellFormat));
			
			Iterator dlgIt = vendedores.iterator();
			while(dlgIt.hasNext()) {
				vendedorBean = (VendedorBean)dlgIt.next(); 

				fila += 1; columna = 0;
				sheet.addCell(new Number(columna++, fila, vendedorBean.getId(), numberCellFormat));
				sheet.addCell(new Label(columna++, fila, vendedorBean.getCodigo() , labelCellFormat));
				sheet.addCell(new Label(columna++, fila, vendedorBean.getNombre() , labelCellFormat));
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
/**
	public String noCobradosToExcel(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		TotalNoCobradosDTOdddd tncDTO = new TotalNoCobradosDTOdddd();
		
		CtacteDTO mov = null;
		String periodo = null;
		String codRechazo = null;
		boolean pago = false;
		int cantidad = 0;
		long idAux = 0l;
		boolean seguir = true;
		int iper = 0;
		int iiper = 0;
		
		// clientes activos
		String[] clientes = cliente.getClientesStr(usuario);
		String[] intermedio = new String[clientes.length * 20];
		int k = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
		  for(int i=0; (i<(clientes.length-1) && seguir); i++) {
			ClienteDTO cliDTO = new ClienteDTO(clientes[i]);
			String[] movimientos = movimiento.getMovimientoByClienteStr(cliDTO.getId(), usuario, sesion);

			periodo = null;
			
			for(int j=0; j<movimientos.length; j++) {
				if(movimientos[j] == null) {
					continue;
				}
				mov = new CtacteDTO(movimientos[j]);
	       		if(j==0) {
	       			periodo = mov.getPeriodo();
	       			iper = DateTimeUtil.getNroMes(periodo);
	       			pago = true;
	       		}
	       		if(periodo.compareTo(mov.getPeriodo()) != 0) {
	       			if(!pago) {
	       				StringBuffer sb = new StringBuffer();
	       				sb.append(cliDTO.toString());
	       				sb.append(periodo);
	       				sb.append(";");
	       				sb.append(codRechazo);
	       				
	       				intermedio[k++] = sb.toString();
	       			}
	       			periodo = mov.getPeriodo();
	       			iper = DateTimeUtil.getNroMes(periodo);
	       			codRechazo = mov.getCodRechazo();
	       			pago = false;
	       		}
	       		if(mov.getEstadoColor() <=3) {
// El estado es > a debitado y != a Devuelto
	       			pago = true;
	       		}
			}
			if(movimientos.length > 0 && !pago) {
   				StringBuffer sb = new StringBuffer();
   				sb.append(cliDTO.toString());
   				sb.append(";");
   				sb.append(periodo);
   				sb.append(";");
   				sb.append(codRechazo);
   				
   				intermedio[k++] = sb.toString();
//   				seguir =false;
			}
		  }
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
//		System.out.println("Fin Recorrida clientes");
//-----------------------------------------------------------------------------------------------------------------
		String[] clientesNoCobrados = new String[k];
		for(int i=0; i<k; i++) {
			clientesNoCobrados[i] = intermedio[i]; 
		}
		
		File f = new File("C:\\Servina\\NoCobrados.xls");
		int fila = 0;
		int columna = 0;
		int hoja = 0;
		this.lookAndFeel();
	
		WritableWorkbook workbook = null;
		WritableSheet sheet = null;
		try {
			workbook = Workbook.createWorkbook(f);
			sheet = workbook.createSheet("Clientes No Cobrados", hoja++);
			sheet.addCell(new Label(columna++, fila, "Id"					, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Apellido y Nombre"	, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Laboral"				, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Particular"			, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "NroDoc"				, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Delegacion"			, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Periodo"				, tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "CodRechazo"			, tituloCellFormat));

			Parser p = new Parser();
			int i;
			try {
				for(i=0; i<k; i++) {
					ClienteDTO cliDTO = new ClienteDTO(clientesNoCobrados[i]);
					p.setK(cliDTO.getK());
					p.parsear(clientesNoCobrados[i]);
					periodo = p.getTokenString().trim();
					if(periodo.compareTo(new String("")) == 0) {
						periodo = p.getTokenString().trim();
					}
					codRechazo = p.getTokenString();

					fila += 1; columna = 0;
					sheet.addCell(new Number(columna++, fila, cliDTO.getId(), 				numberCellFormat));
					sheet.addCell(new Label (columna++, fila, cliDTO.getApellidoyNombre(),	labelCellFormat));
					sheet.addCell(new Label (columna++, fila, cliDTO.getTelefonosLaboral(),		labelCellFormat));
					sheet.addCell(new Label (columna++, fila, cliDTO.getTelefonosParticular(),	labelCellFormat));
					sheet.addCell(new Number(columna++, fila, cliDTO.getNroDoc(), 			numberCellFormat));
					sheet.addCell(new Label (columna++, fila, cliDTO.getDelegDescripcion(),	labelCellFormat));
					sheet.addCell(new Label (columna++, fila, periodo,						labelCellFormat));
					if(codRechazo.startsWith("R"))
						sheet.addCell(new Label (columna++, fila, codRechazo,				labelCellFormat));
					else
						sheet.addCell(new Label (columna++, fila, "Devolucion",				labelCellFormat));
					
					if(cliDTO.getId().compareTo(idAux) !=0) {
						idAux = cliDTO.getId(); 
						cantidad++;
					}
					if(codRechazo.startsWith("R"))
						tncDTO.incrementar(periodo, codRechazo);
					else
						tncDTO.incrementar(periodo, "Dev");
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			columna = 0;
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, "Total de Clientes",	tituloCellFormat));
			sheet.addCell(new Number(columna++, fila, cantidad, 			numberCellFormat));
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			sheet.addCell(new Label(columna++, fila, " ",	tituloCellFormat));
			
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return tncDTO.toString();
	} 
	
	public void extraerInfoGraf(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		File f = new File("C:\\Servina\\infoGral.xls");
		int hoja=0;
		int columna=0;
		int fila2006=0;
		int fila2007=0;
		int fila2008=0;
		int fila2009=0;
		int fila2010=0;
		int fila2011=0;
		int aa=0;
		
		WritableWorkbook workbook = null;
		WritableSheet sheet2006 = null;
		WritableSheet sheet2007 = null;
		WritableSheet sheet2008 = null;
		WritableSheet sheet2009 = null;
		WritableSheet sheet2010 = null;
		WritableSheet sheet2011 = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			workbook = Workbook.createWorkbook(f);
			sheet2006 = workbook.createSheet("2006", hoja++);
			sheet2006.addCell(new Label(columna++, fila2006, "Delegacion"			, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Id"					, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "NroDoc"				, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Apellido y Nombre"	, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Fecha"				, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Periodo"				, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Estado"				, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "CodRechazo"			, tituloCellFormat));
			sheet2006.addCell(new Label(columna++, fila2006, "Importe"				, tituloCellFormat));

			columna=0;
			sheet2007 = workbook.createSheet("2007", hoja++);
			sheet2007.addCell(new Label(columna++, fila2007, "Delegacion"			, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Id"					, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "NroDoc"				, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Apellido y Nombre"	, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Fecha"				, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Periodo"				, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Estado"				, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "CodRechazo"			, tituloCellFormat));
			sheet2007.addCell(new Label(columna++, fila2007, "Importe"				, tituloCellFormat));

			columna=0;
			sheet2008 = workbook.createSheet("2008", hoja++);
			sheet2008.addCell(new Label(columna++, fila2008, "Delegacion"			, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Id"					, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "NroDoc"				, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Apellido y Nombre"	, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Fecha"				, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Periodo"				, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Estado"				, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "CodRechazo"			, tituloCellFormat));
			sheet2008.addCell(new Label(columna++, fila2008, "Importe"				, tituloCellFormat));

			columna=0;
			sheet2009 = workbook.createSheet("2009", hoja++);
			sheet2009.addCell(new Label(columna++, fila2009, "Delegacion"			, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Id"					, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "NroDoc"				, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Apellido y Nombre"	, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Fecha"				, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Periodo"				, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Estado"				, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "CodRechazo"			, tituloCellFormat));
			sheet2009.addCell(new Label(columna++, fila2009, "Importe"				, tituloCellFormat));

			columna=0;
			sheet2010 = workbook.createSheet("2010", hoja++);
			sheet2010.addCell(new Label(columna++, fila2010, "Delegacion"			, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Id"					, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "NroDoc"				, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Apellido y Nombre"	, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Fecha"				, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Periodo"				, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Estado"				, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "CodRechazo"			, tituloCellFormat));
			sheet2010.addCell(new Label(columna++, fila2010, "Importe"				, tituloCellFormat));

			columna=0;
			sheet2011 = workbook.createSheet("2011", hoja++);
			sheet2011.addCell(new Label(columna++, fila2011, "Delegacion"			, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Id"					, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "NroDoc"				, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Apellido y Nombre"	, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Fecha"				, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Periodo"				, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Estado"				, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "CodRechazo"			, tituloCellFormat));
			sheet2011.addCell(new Label(columna++, fila2011, "Importe"				, tituloCellFormat));

			CtacteDTO mov = null;
			String[] clientes = cliente.getClientesStr(usuario);
			for(int i=0; i<(clientes.length-1); i++) {
				
				ClienteDTO cliDTO = new ClienteDTO(clientes[i]);
				
				String[] movimientos = movimiento.getMovimientoByClienteStr(cliDTO.getId(), usuario, sesion);
	
				for(int j=0; j<movimientos.length; j++) {
					if(movimientos[j] == null) {
						continue;
					}
					mov = new CtacteDTO(movimientos[j]);
	
					if(aa>2) {
						workbook.write();
					}
					if(aa>10) {
						workbook.close();
					}
					aa++;
					
					columna=0;
					if(mov.getPeriodo().endsWith("2006")) {
						fila2006++; 
						sheet2006.addCell(new Label (columna++, fila2006, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2006.addCell(new Number(columna++, fila2006, cliDTO.getId(), 				numberCellFormat));
						sheet2006.addCell(new Number(columna++, fila2006, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2006.addCell(new Label (columna++, fila2006, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2006.addCell(new Label (columna++, fila2006, mov.getFecha(),				labelCellFormat));
						sheet2006.addCell(new Label (columna++, fila2006, mov.getPeriodo(),				labelCellFormat));
						sheet2006.addCell(new Number(columna++, fila2006, mov.getEstadoColor(),			numberCellFormat));
						sheet2006.addCell(new Label (columna++, fila2006, mov.getCodRechazo(),			labelCellFormat));
						sheet2006.addCell(new Number(columna++, fila2006, mov.getImporte(),				numberCellFormat));
					}
					if(mov.getPeriodo().endsWith("2007")) {
						fila2007++; 
						sheet2007.addCell(new Label (columna++, fila2007, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2007.addCell(new Number(columna++, fila2007, cliDTO.getId(), 				numberCellFormat));
						sheet2007.addCell(new Number(columna++, fila2007, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2007.addCell(new Label (columna++, fila2007, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2007.addCell(new Label (columna++, fila2007, mov.getFecha(),				labelCellFormat));
						sheet2007.addCell(new Label (columna++, fila2007, mov.getPeriodo(),				labelCellFormat));
						sheet2007.addCell(new Number(columna++, fila2007, mov.getEstadoColor(),			numberCellFormat));
						sheet2007.addCell(new Label (columna++, fila2007, mov.getCodRechazo(),			labelCellFormat));
						sheet2007.addCell(new Number(columna++, fila2007, mov.getImporte(),				numberCellFormat));
					}
					if(mov.getPeriodo().endsWith("2008")) {
						fila2008++; 
						sheet2008.addCell(new Label (columna++, fila2008, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2008.addCell(new Number(columna++, fila2008, cliDTO.getId(), 				numberCellFormat));
						sheet2008.addCell(new Number(columna++, fila2008, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2008.addCell(new Label (columna++, fila2008, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2008.addCell(new Label (columna++, fila2008, mov.getFecha(),				labelCellFormat));
						sheet2008.addCell(new Label (columna++, fila2008, mov.getPeriodo(),				labelCellFormat));
						sheet2008.addCell(new Number(columna++, fila2008, mov.getEstadoColor(),			numberCellFormat));
						sheet2008.addCell(new Label (columna++, fila2008, mov.getCodRechazo(),			labelCellFormat));
						sheet2008.addCell(new Number(columna++, fila2008, mov.getImporte(),				numberCellFormat));
					}
					if(mov.getPeriodo().endsWith("2009")) {
						fila2009++; 
						sheet2009.addCell(new Label (columna++, fila2009, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2009.addCell(new Number(columna++, fila2009, cliDTO.getId(), 				numberCellFormat));
						sheet2009.addCell(new Number(columna++, fila2009, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2009.addCell(new Label (columna++, fila2009, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2009.addCell(new Label (columna++, fila2009, mov.getFecha(),				labelCellFormat));
						sheet2009.addCell(new Label (columna++, fila2009, mov.getPeriodo(),				labelCellFormat));
						sheet2009.addCell(new Number(columna++, fila2009, mov.getEstadoColor(),			numberCellFormat));
						sheet2009.addCell(new Label (columna++, fila2009, mov.getCodRechazo(),			labelCellFormat));
						sheet2009.addCell(new Number(columna++, fila2009, mov.getImporte(),				numberCellFormat));
					}
					if(mov.getPeriodo().endsWith("2010")) {
						fila2010++; 
						sheet2010.addCell(new Label (columna++, fila2010, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2010.addCell(new Number(columna++, fila2010, cliDTO.getId(), 				numberCellFormat));
						sheet2010.addCell(new Number(columna++, fila2010, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2010.addCell(new Label (columna++, fila2010, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2010.addCell(new Label (columna++, fila2010, mov.getFecha(),				labelCellFormat));
						sheet2010.addCell(new Label (columna++, fila2010, mov.getPeriodo(),				labelCellFormat));
						sheet2010.addCell(new Number(columna++, fila2010, mov.getEstadoColor(),			numberCellFormat));
						sheet2010.addCell(new Label (columna++, fila2010, mov.getCodRechazo(),			labelCellFormat));
						sheet2010.addCell(new Number(columna++, fila2010, mov.getImporte(),				numberCellFormat));
					}
					if(mov.getPeriodo().endsWith("2011")) {
						fila2011++; 
						sheet2011.addCell(new Label (columna++, fila2011, cliDTO.getDelegDescripcion(),	labelCellFormat));
						sheet2011.addCell(new Number(columna++, fila2011, cliDTO.getId(), 				numberCellFormat));
						sheet2011.addCell(new Number(columna++, fila2011, cliDTO.getNroDoc(), 			numberCellFormat));
						sheet2011.addCell(new Label (columna++, fila2011, cliDTO.getApellidoyNombre(),	labelCellFormat));
						sheet2011.addCell(new Label (columna++, fila2011, mov.getFecha(),				labelCellFormat));
						sheet2011.addCell(new Label (columna++, fila2011, mov.getPeriodo(),				labelCellFormat));
						sheet2011.addCell(new Number(columna++, fila2011, mov.getEstadoColor(),			numberCellFormat));
						sheet2011.addCell(new Label (columna++, fila2011, mov.getCodRechazo(),			labelCellFormat));
						sheet2011.addCell(new Number(columna++, fila2011, mov.getImporte(),				numberCellFormat));
					}
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public void extraerInfo(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
	        FileWriter fichero = new FileWriter("C:\\Servina\\infoGral.xls");
	        PrintWriter pw = new PrintWriter(fichero);

			pw.println("Delegacion;Id;NroDoc;Apellido y Nombre;Fecha;Periodo;Estado;CodRechazo;Importe");

			CtacteDTO mov = null;
			String[] clientes = cliente.getClientesStr(usuario);
			for(int i=0; i<(clientes.length-1); i++) {
				
				ClienteDTO cliDTO = new ClienteDTO(clientes[i]);
				
				String[] movimientos = movimiento.getMovimientoByClienteStr(cliDTO.getId(), usuario, sesion);
	
				for(int j=0; j<movimientos.length; j++) {
					if(movimientos[j] == null) {
						continue;
					}
					mov = new CtacteDTO(movimientos[j]);
					
					if(mov.getEstadoColor() == 1) {
						continue;
					}
					if(mov.getImporte() == 0.0) {
						continue;
					}

       				StringBuffer sb = new StringBuffer();
       				sb.append(cliDTO.getDelegDescripcion());	sb.append(";");
       				sb.append(cliDTO.getId());					sb.append(";");
       				sb.append(cliDTO.getNroDoc());				sb.append(";");
       				sb.append(cliDTO.getApellidoyNombre());		sb.append(";");
       				sb.append(mov.getFecha());					sb.append(";");
       				sb.append(mov.getPeriodo());				sb.append(";");
       				sb.append(mov.getEstadoColor());			sb.append(";");
       				sb.append(mov.getCodRechazo());				sb.append(";");
       				sb.append(mov.getImporte());
       				
       				pw.println(sb.toString());
				}
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}*/