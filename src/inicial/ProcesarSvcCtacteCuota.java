package inicial;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.Persistencia;
import com.financiera.core.server.ServiceLocator;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.DisparoBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.MovimientoBean;
import com.financiera.domain.bean.Persistible;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.MovimientoService;

public class ProcesarSvcCtacteCuota {
	private MovimientoService movimiento = (MovimientoService) ServiceLocator.getInstance().getService(MovimientoService.class);
	
	public static void main(String[] args) {
		new ProcesarSvcCtacteCuota();
	}
	public ProcesarSvcCtacteCuota() {
		Session s = Persistencia.getSession();
		Transaction transaction = s.beginTransaction();
		try{
			transaction.begin();
			Iterator aI  = s.getNamedQuery("ServicioBean.getAll").list().iterator();
			while(aI.hasNext()) {
				ServicioBean svcb = (ServicioBean)aI.next();

				movimiento.actualizarSvc(svcb, s);
			}
			s.flush();
			s.clear();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			transaction.commit();
		}
/**		
		s = Persistencia.getSession();

		EstadoMovBean estado = movimiento.getEstadoMovEstado(EstadoMovBean.E0_A_DISPARAR);
//		transaction = s.beginTransaction();
		try{
			DisparoBean disparo = (DisparoBean)s.get(DisparoBean.class, new Long(1));
			o.clear();
			transaction.begin();
			Iterator aI  = s.getNamedQuery("ServicioBean.getAll").list().iterator();
			while(aI.hasNext()) {
				ServicioBean svcb = (ServicioBean)aI.next();
				this.generarCtacte(svcb, estado, disparo);
			}
			persistencia.persistirMasivo(o, informe);
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
*/
	}
	
	private void generarCtacte(ServicioBean svcb, EstadoMovBean estado, DisparoBean disparo) {
		Date fecha = null;
		try {
			fecha = DateTimeUtil.getDateDDMMAAAA("01072007");
		} catch(Exception e) {
			System.out.println("Error al obtener la fecha " + e.getMessage());
		}
		
		int u = svcb.getUltCuotaDebitada();
		for(int i=u; i<12; i++) {
			MovimientoBean m = new MovimientoBean();
			
			m.setAccion(Persistible.INSERT);
			m.setCliente(svcb.getCliente());
			m.setIdCliente(m.getCliente().getId());
			m.setIdServicio(svcb.getId());
			m.setImporte(svcb.getImporteCuota());
			m.setDescripcion(svcb.getCliente().getDelegacion().getBanco().getDescripcion());
			m.setNroCuota(0);
			m.setTotalCuotas(svcb.getCantCuota());
			m.setEstado(estado);
			m.setFecha(fecha);
			m.setDisparo(disparo);
			
			try {
				fecha = DateTimeUtil.add1Mes(fecha);
			} catch(Exception e) {
				System.out.println("Error al obtener la fecha " + e.getMessage());
			}
		}
	}
}