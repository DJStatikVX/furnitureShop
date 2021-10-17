package uo.ips.muebleria.logic.trabajador;

import java.util.List;

import uo.ips.muebleria.logic.venta.Venta;

public class EstadisticasVendedor {
	
	private Vendedor vendedor;
	private List<Venta> ventas;
	
	public EstadisticasVendedor() {
	}

	public EstadisticasVendedor(Vendedor vendedor, List<Venta> ventas) {
		super();
		this.vendedor = vendedor;
		this.ventas = ventas;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public List<Venta> getVentas() {
		return ventas;
	}
	
	public double getGananciasTotalesPorFecha(String fecha) {
		boolean getTodos = false;
		if (fecha.equals(""))
			getTodos = true;
		double ganancias = 0;
		for (Venta v : ventas) {
			if (getTodos || v.getFechaVentaCreada().equals(fecha))
				ganancias+=v.getImporteVenta();
		}
		ganancias = Math.round(ganancias*100.0)/100.0;
		return ganancias;
	}
	
	public int getNumeroDeVentasPorFecha(String fecha) {
		boolean getTodos = false;
		if (fecha.equals(""))
			getTodos = true;
		int total = 0;
		for (Venta v : ventas) {
			if (getTodos || v.getFechaVentaCreada().equals(fecha))
				total++;
		}
		return total;
	}
	
}
