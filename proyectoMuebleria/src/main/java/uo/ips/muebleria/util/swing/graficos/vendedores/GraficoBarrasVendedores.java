package uo.ips.muebleria.util.swing.graficos.vendedores;

import java.time.LocalDate;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.grafico.GraficoData.VendedorGrafico;
import uo.ips.muebleria.logic.grafico.GraficoData.VentaGrafico;

public class GraficoBarrasVendedores extends JDialog {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String periodo;
	private GraficoData gd;

	private JFreeChart chart;

	public GraficoBarrasVendedores(String titulo, String periodo) {
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

		crearGrafico();

		configuracion();
	}

	private void crearGrafico() {
		LocalDate o = LocalDate.now();
		DefaultCategoryDataset dataset = null;
		String str = "";
		switch (periodo) {
		case GraficoData.MES:
			str = "Días del mes";
			dataset = crearDatasetMes();
			break;
		case GraficoData.AÑO:
			str = "Meses";
			o = LocalDate.of(o.getYear(), 1, 1);
			dataset = crearDatasetAño();
			break;
		}

		chart = ChartFactory.createBarChart(titulo, str, "Dinero acumulado", dataset);
		CategoryPlot plot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();

		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0);
	}

	private DefaultCategoryDataset crearDatasetMes() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int days = LocalDate.now().lengthOfMonth();
		LocalDate o = LocalDate.now();
		double[] p = new double[days / 3 + 1];

		for (int i = 0; i < (days / 3); i++)
			p[i] = 1 + 3 * i;
		p[(days / 3)] = days;

		double total;
		for (VendedorGrafico ven : gd.getVendedores()) {
			for (int i = 0; i < p.length; i++) {
				total = 0;
				for (VentaGrafico v : ven.getVentas()) {
					if (v.getFecha().isBefore(LocalDate.of(o.getYear(), o.getMonth(), (int) p[i]))
							&& v.getFecha().isAfter(LocalDate.of(o.getYear(), o.getMonth(), 1)))
						total += v.getPrecio();
				}
				dataset.addValue(total, ven.getNombre() + " " + ven.getApellido(), ((int) p[i]) + "");
			}
		}
		return dataset;
	}

	private DefaultCategoryDataset crearDatasetAño() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		LocalDate o = LocalDate.now();
		double[] p = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		double total;
		for (VendedorGrafico ven : gd.getVendedores()) {
			for (int i = 0; i < p.length; i++) {
				total = 0;
				for (VentaGrafico v : ven.getVentas()) {
					if (v.getFecha().isBefore(LocalDate.of(o.getYear(), (int) p[i], 1))
							&& v.getFecha().isAfter(LocalDate.of(o.getYear(), 1, 1)))
						total += v.getPrecio();
				}
				dataset.addValue(total, ven.getNombre() + " " + ven.getApellido(), ((int) p[i]) + "");
			}
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
