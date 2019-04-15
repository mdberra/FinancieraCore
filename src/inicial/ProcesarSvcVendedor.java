package inicial;

import java.util.Hashtable;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.bean.VendedorBean;

public class ProcesarSvcVendedor {
	private Hashtable<String, String> tabla = new Hashtable<String, String>(); 
	
	public static void main(String[] args) {
		new ProcesarSvcVendedor();
	}
	public ProcesarSvcVendedor() {
		this.cargarTabla();
		Session s = Persistencia.getSession();
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Iterator aI  = s.getNamedQuery("ServicioBean.getAll").list().iterator();
			while(aI.hasNext()) {
				ServicioBean svcb = (ServicioBean)aI.next();
				Long delId = svcb.getCliente().getDelegacion().getId();
				String delegacion = new String(String.valueOf(delId));
				if(tabla.containsKey(delegacion)) {
					String vendedor = tabla.get(delegacion);
					VendedorBean v = (VendedorBean)s.get(VendedorBean.class, new Long(vendedor));
					
					svcb.setVendedor(v);
//					System.out.println(svcb.getId() + " DelId " + delId + " Del " + delegacion + " Ven " + vendedor);
					s.update(svcb);
				} else {
					System.out.println("No encontre: " + delegacion); 
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			s.flush();
			s.clear();
			transaction.commit();
		}
	}
	
	private void cargarTabla() {
// 1 Pablo;
// 2 Enrique Maciel;
// 3 Narciso diaz;
// 4 Raul;
// 5 Edgardo;
// 6 Juan Utea;
// 7 Luis Utea;
// 8 Ale San Luis;
// 9 Juan Jose San Juan;
//10 Roxana Catamarca;

	//  Tabla Delegacion, Vendedor
		tabla.put("1", "2"); //LANUS
		tabla.put("2", "1"); //Municipalidad de la Costa
		tabla.put("3", "2"); //Municipalidad De Quilmes
		tabla.put("4", "3"); //Misiones Pami
		tabla.put("5", "3"); //Misiones Macro
		tabla.put("6", "4"); //Formosa
		tabla.put("7", "1"); //Municipalidad Merlo
		tabla.put("8", "5"); //INTA
		tabla.put("9", "3"); //Prefectura Misiones
		tabla.put("10", "2"); //Municipalidad Avellaneda
		tabla.put("11", "3"); //Municipalidad Posadas
		tabla.put("12", "2"); //Municipalidad Lomas
//		tabla.put("13", ""); //Suplente Educacion
//		tabla.put("14", ""); //Jubilado IPS
//		tabla.put("15", ""); //Educacion
//		tabla.put("16", ""); //Municipalidad Hurlingham
		tabla.put("17", "1"); //Municipalidad Moreno
//		tabla.put("18", ""); //Salud
		tabla.put("19", "3"); //Jubilado Misiones
		tabla.put("20", "3"); //Fuerza Aerea Misiones
		tabla.put("21", "7"); //Municipalidad San Nicolas
		tabla.put("22", "8"); //San Luis dia 30
		tabla.put("23", "8"); //San Luis dia 15
		tabla.put("24", "8"); //Municipalidad San Luis
		tabla.put("25", "8"); //Jubilado San Luis
		tabla.put("26", "8"); //Pension San Luis
		tabla.put("27", "3"); //Ejercito Misiones
		tabla.put("28", "9"); //San Juan
		tabla.put("29", "9"); //Vialidad San Juan
		tabla.put("30", "10"); //Catamarca
		tabla.put("31", "10"); //Municipalidad Catamarca
	}
}