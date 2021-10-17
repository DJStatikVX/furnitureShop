package uo.ips.muebleria.logic.presupuesto.cp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uo.ips.muebleria.logic.presupuesto.Presupuesto;
import uo.ips.muebleria.logic.presupuesto.PresupuestosConsultas;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.producto.ProductosConsultas;
import uo.ips.muebleria.logic.trabajador.Trabajador;

public class CrearPresupuesto {

	public static final String FILTRAR_MAYOR = "mayor";
	public static final String FILTRAR_MENOR = "menor";
	private List<Producto> productosCatalogo;
	private List<Producto> listproductosCatalogo;
	private List<Producto> productosPresupuesto;
	private Trabajador usuario;

	public CrearPresupuesto() {
		productosCatalogo = new ArrayList<Producto>();
		productosPresupuesto = new ArrayList<Producto>();
		inicializar();
	}

	private void inicializar() {
		productosCatalogo.addAll(ProductosConsultas.getProductosCatalogo());
	}

	public List<Producto> getProductosCatalogo() {
		if (listproductosCatalogo == null)
			return productosCatalogo;
		else
			return listproductosCatalogo;
	}

	public List<Producto> getProductosPresupuesto() {
		return productosPresupuesto;
	}

	public Producto addPresupuesto(String codigo) {
		Producto prod = null;
		for (Producto p : productosCatalogo) {
			if (p.getCodigo().equals(codigo)) {
				prod = p;
				break;
			}
		}
		if (prod == null)
			return null;
		else {
			for (Producto p : productosPresupuesto) {
				if (p.getCodigo().equals(codigo)) {
					return null;
				}
			}
			productosPresupuesto.add(prod);
			return prod;
		}
	}
	
	public Producto addPresupuesto(String codigo,int number) {
		Producto prod = null;
		for (Producto p : productosCatalogo) {
			if (p.getCodigo().equals(codigo)) {
				prod = p;
				break;
			}
		}
		if (prod == null)
			return null;
		else {
			for (Producto p : productosPresupuesto) {
				if (p.getCodigo().equals(codigo)) {
					return null;
				}
			}
			productosPresupuesto.add(prod);
			prod.setUnidades(number);
			return prod;
		}
	}

	public void removePresupuesto(String codigo) {
		Producto prod = null;
		for (Producto p : productosPresupuesto) {
			if (p.getCodigo().equals(codigo)) {
				prod = p;
				break;
			}
		}
		if (prod == null)
			return;
		else
			productosPresupuesto.remove(prod);
	}

	public double getTotal() {
		Double total = 0.0;
		for (Producto p : productosPresupuesto)
			total += p.getPrecio()*p.getUnidades();
		return total;
	}

	public List<String> getTipos() {
		return ProductosConsultas.getTipoProductos();
	}

	public void filtrar(String categoria, Double d, String tipoPrecio) {
		if (categoria == null && d == null)
			listproductosCatalogo = null;
		else {
			if (categoria != null && d == null)
				listproductosCatalogo = filtrarCategoria(productosCatalogo, categoria);
			else if (categoria == null && d != null)
				listproductosCatalogo = filtrarPrecio(productosCatalogo, d.doubleValue(), tipoPrecio);
			else {
				listproductosCatalogo = filtrarCategoria(productosCatalogo, categoria);
				listproductosCatalogo = filtrarPrecio(listproductosCatalogo, d.doubleValue(), tipoPrecio);
			}
		}
	}

	private List<Producto> filtrarCategoria(List<Producto> list, String categoria) {
		List<Producto> l = new ArrayList<Producto>();
		for (Producto p : list) {
			if (p.getTipo().equals(categoria))
				l.add(p);
		}
		return l;
	}

	private List<Producto> filtrarPrecio(List<Producto> list, double precio, String tipoPrecio) {
		List<Producto> l = new ArrayList<Producto>();
		if (tipoPrecio.equals(FILTRAR_MAYOR)) {
			for (Producto p : list) {
				if(p.getPrecio()>precio)
					l.add(p);
			}
		} else if (tipoPrecio.equals(FILTRAR_MENOR)) {
			for (Producto p : list) {
				if(p.getPrecio()<=precio)
					l.add(p);
			}
		}
		return l;
	}

	public Presupuesto crearPresupuesto() {
		String codigo = UUID.randomUUID().toString();
		Presupuesto p = registrarPresupuesto(codigo);
		relacionarProductosAPresupuesto(codigo);
		return p;
	}


	private Presupuesto registrarPresupuesto(String codigo) {
		return PresupuestosConsultas.insertarPresupuesto(codigo, getTotal(), usuario.getCodigo());
	}

	private void relacionarProductosAPresupuesto(String codigo) {
		PresupuestosConsultas.relacionarPresupuestoProducto(codigo, productosPresupuesto);
	}

	public void resetPresupuesto() {
		productosPresupuesto = new ArrayList<Producto>();
	}
	
	public void asignarUnidades(String id,int unidades) {
		for(Producto p: productosPresupuesto) {
			if(p.getCodigo().equals(id))
				p.setUnidades(unidades);
		}
	}

	public void setUsuario(Trabajador usuarioActivo) {
		this.usuario = usuarioActivo;
	}

	public void marcarComoModelo(String codigoPresupuesto) {
		PresupuestosConsultas.marcarComoModelo(codigoPresupuesto);
	}

	public void renombrar(String codigoPresupuesto, String nombre) {
		PresupuestosConsultas.marcarComoModelo(codigoPresupuesto,nombre);
	}
	
	public List<Presupuesto> getModelos(){
		return PresupuestosConsultas.getPresupuestosModelo();
	}
	
	public List<Producto> getProductosModelo(String codigo){
		return ProductosConsultas.getProductosModelo(codigo);
	}
}
