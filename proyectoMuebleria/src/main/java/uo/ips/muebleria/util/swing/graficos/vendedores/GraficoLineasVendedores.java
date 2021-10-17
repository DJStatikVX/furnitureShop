package uo.ips.muebleria.util.swing.graficos.vendedores;

import java.awt.BasicStroke;
import java.time.LocalDate;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;

import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.grafico.GraficoData.VendedorGrafico;
import uo.ips.muebleria.logic.grafico.GraficoData.VentaGrafico;

public class GraficoLineasVendedores extends JDialog {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String periodo;
	private GraficoData gd;

	private JFreeChart chart;

	public GraficoLineasVendedores(String titulo, String periodo) {
		switch (periodo) {
		case GraficoData.MES:
			titulo = titulo + "(mensual)";
			break;
		case GraficoData.AÑO:
			titulo = titulo + "(anual)";
			break;
		}
		this.titulo = titulo;
		this.periodo = periodo;
		this.gd = new GraficoData();

		crearGraficoLineas();

		configuracion();
	}

	@SuppressWarnings("deprecation")
	private void crearGraficoLineas() {
		DefaultXYDataset dataset = null;
		String str = "";
		switch (periodo) {
		case GraficoData.MES:
			str = "Días del mes";
			dataset = crearDatasetMes();
			break;
		case GraficoData.AÑO:
			str = "Meses";
			dataset = crearDatasetAño();
			break;
		}

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setStroke(new BasicStroke(2));
		chart = ChartFactory.createXYLineChart(titulo, str, "Dinero acumulado", dataset);
		chart.getXYPlot().setRenderer(renderer);
	}

	private DefaultXYDataset crearDatasetMes() {
		DefaultXYDataset dataset = new DefaultXYDataset();
		int days = LocalDate.now().lengthOfMonth();
		LocalDate o = LocalDate.now();
		double[] p = new double[days / 3 + 1];

		for (int i = 0; i < (days / 3); i++)
			p[i] = 1 + 3 * i;
		p[(days / 3)] = days;

		double[] aux;
		double total;
		for (VendedorGrafico vendedor : gd.getVendedores()) {
			aux = new double[p.length];
			for (int i = 0; i < aux.length; i++) {
				total = 0;
				for (VentaGrafico v : vendedor.getVentas()) {
					if (v.getFecha().isBefore(LocalDate.of(o.getYear(), o.getMonth(), (int) p[i]))
							&& v.getFecha().isAfter(LocalDate.of(o.getYear(), o.getMonth(), 1)))
						total += v.getPrecio();
				}
				aux[i] = total;
			}
			dataset.addSeries(vendedor.getNombre() + " " + vendedor.getApellido(), new double[][] { p, aux });
		}
		return dataset;
	}

	private DefaultXYDataset crearDatasetAño() {
		DefaultXYDataset dataset = new DefaultXYDataset();
		LocalDate o = LocalDate.now();
		double[] p = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		double[] aux;
		double total;
		for (VendedorGrafico vendedor : gd.getVendedores()) {
			aux = new double[p.length];
			for (int i = 0; i < aux.length; i++) {
				total = 0;
				for (VentaGrafico v : vendedor.getVentas()) {
					if (v.getFecha().isBefore(LocalDate.of(o.getYear(), (int) p[i], 1))
							&& v.getFecha().isAfter(LocalDate.of(o.getYear(), 1, 1)))
						total += v.getPrecio();
				}
				aux[i] = total;
			}
			dataset.addSeries(vendedor.getNombre() + " " + vendedor.getApellido(), new double[][] { p, aux });
		}
		return dataset;
	}

	private void configuracion() {
		ChartPanel chartPanel = new ChartPanel(chart);
		this.setContentPane(chartPanel);
		this.setTitle(titulo);
		this.setModal(true);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

}
