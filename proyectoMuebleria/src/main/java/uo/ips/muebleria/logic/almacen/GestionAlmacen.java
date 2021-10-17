package uo.ips.muebleria.logic.almacen;

import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.producto.ProductosConsultas;

public class GestionAlmacen {

	private List<Producto> productos;
	private List<Producto> productosVenta;
	private List<Producto> productosPedidoAutomatico;

	public GestionAlmacen() {
		this.productos = new ArrayList<Producto>();
		this.productos.addAll(ProductosConsultas.getProductosCatalogo());
		this.productosVenta = new ArrayList<Producto>();
		this.productosPedidoAutomatico = new ArrayList<Producto>();
	}

	public void añadirProducto(String codigo, int unidades) {
		Producto p = getProducto(codigo);
		if (p != null) {
			p.setUnidades(unidades);
			productosVenta.add(p);
		}
	}

	private Producto getProducto(String codigo) {
		for (Producto p : productos) {
			if (p.getCodigo().equals(codigo))
				return p.copy();
		}
		return null;
	}

	public void restarUnidadesAlmacen() {
		int stockAlmacen;
		int stockPedir;
		Producto pedir;
		for (Producto p : productosVenta) {
			stockAlmacen = AlmacenConsultas.getStock(p.getCodigo());
			// System.out.println("Codigo producto: "+p.getCodigo());
			// System.out.println("En el almacen hay: "+stockAlmacen);
			// System.out.println("Se necesitan: "+p.getUnidades());
			if (p.getUnidades() <= stockAlmacen) {
				stockAlmacen = stockAlmacen - p.getUnidades();
				stockPedir = 0;
			} else {
				stockPedir = p.getUnidades() - stockAlmacen;
				stockAlmacen = 0;
			}
			// System.out.println("Se van a pedir: "+stockPedir);
			if (stockPedir > 0) {
				pedir = p.copy();
				pedir.setUnidades(stockPedir);
				productosPedidoAutomatico.add(pedir);
				// System.out.println("Producto "+pedir.getCodigo()+" añadido a la lista para
				// pedidos automaticos");
			}
			AlmacenConsultas.updateStock(p.getCodigo(), stockAlmacen);
		}
	}

	public boolean seNecesitaPedidoAutomatico() {
		if (productosPedidoAutomatico.size() > 0)
			return true;
		return false;
	}

	public List<Producto> getProductosPedidoAutomatico() {
		return productosPedidoAutomatico;
	}

	public List<Producto> getProductosEnAlmacen() {
		List<Producto> productosEnElAlmacen = new ArrayList<>();
		Producto aux;
		for (Producto p : productos) {
			aux = p.copy();
			aux.setUnidades(AlmacenConsultas.getStock(aux.getCodigo()));
			productosEnElAlmacen.add(aux);
		}
		return productosEnElAlmacen;
	}

	public Producto getProductosEnAlmacen(String codigo) {
		List<Producto> l = getProductosEnAlmacen();
		for (Producto p : l)
			if (p.getCodigo().equals(codigo))
				return p;
		return null;
	}

	public void actualizarStock(String codigoPedido) {
		List<String> codigosProducto = AlmacenConsultas.getCodigoProductoDePedido(codigoPedido);
		String[] aux;
		for (String s : codigosProducto) {
			aux = s.split("@");
			AlmacenConsultas.updateStock(aux[0], AlmacenConsultas.getStock(aux[0]) + Integer.parseInt(aux[1]));
		}
	}

}
