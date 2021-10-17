package uo.ips.muebleria.logic.presupuesto;

public class ContieneProducto {
	
	private String codigoProducto;
	private String codigoPresupuesto;
	private int unidadesProducto;
	private double precioProducto;
	
	public ContieneProducto() {
		
	}
	
	public ContieneProducto(String codigoProducto, String codigoPresupuesto, int unidadesProducto,
			double precioProducto) {
		super();
		this.codigoProducto = codigoProducto;
		this.codigoPresupuesto = codigoPresupuesto;
		this.unidadesProducto = unidadesProducto;
		this.precioProducto = precioProducto;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public String getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	public int getUnidadesProducto() {
		return unidadesProducto;
	}

	public double getPrecioProducto() {
		return precioProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public void setCodigoPresupuesto(String codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	public void setUnidadesProducto(int unidadesProducto) {
		this.unidadesProducto = unidadesProducto;
	}

	public void setPrecioProducto(double precioProducto) {
		this.precioProducto = precioProducto;
	}
	

}
