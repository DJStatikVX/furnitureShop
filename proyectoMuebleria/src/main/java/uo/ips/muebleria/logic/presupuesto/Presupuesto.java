package uo.ips.muebleria.logic.presupuesto;

public class Presupuesto {

	private String codigoPresupuesto;
	private double importePresupuesto;
	private String fechaPresupuesto;
	private String estado;
	private String dniCliente;
	private String codVendedor;
	private String nombre;

	public Presupuesto() {

	}

	public Presupuesto(String codigoPresupuesto, double importePresupuesto, String fechaPresupuesto, String estado,
			String dniCliente, String codVendedor) {
		this.codigoPresupuesto = codigoPresupuesto;
		this.importePresupuesto = importePresupuesto;
		this.fechaPresupuesto = fechaPresupuesto;
		this.estado = estado;
		this.dniCliente = dniCliente;
		this.codVendedor = codVendedor;
	}

	public Presupuesto(String codigoPresupuesto, double importePresupuesto, String fechaPresupuesto, String estado,
			String dniCliente, String codVendedor, String nombre) {
		this.codigoPresupuesto = codigoPresupuesto;
		this.importePresupuesto = importePresupuesto;
		this.fechaPresupuesto = fechaPresupuesto;
		this.estado = estado;
		this.dniCliente = dniCliente;
		this.codVendedor = codVendedor;
		this.nombre = nombre;
	}

	public void setCliente(String dni) {
		dniCliente = dni;
	}

	public String getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	public double getImportePresupuesto() {
		return importePresupuesto;
	}

	public String getFechaPresupuesto() {
		return fechaPresupuesto;
	}

	public String getEstado() {
		return estado;
	}

	public String getDniCliente() {
		if (dniCliente == null)
			return "Sin cliente asignado";
		return dniCliente;
	}

	public String getCodVendedor() {
		return codVendedor;
	}

	public void setImportePresupuesto(double importePresupuesto) {
		this.importePresupuesto = importePresupuesto;
	}

	public String getNombre() {
		return nombre;
	}

	public String toString() {
		String cadena = "";
		cadena += "Presupuesto: " + codigoPresupuesto;
		if (dniCliente == null) {
			cadena += ", sin cliente asignado";
		} else {
			cadena += ", cliente asignado: " + dniCliente;
		}
		cadena += ", creado en: " + fechaPresupuesto;
		return cadena;
	}
}
