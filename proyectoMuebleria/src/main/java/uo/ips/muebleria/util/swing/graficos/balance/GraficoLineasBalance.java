package uo.ips.muebleria.util.swing.graficos.balance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RefineryUtilities;

import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.grafico.GraficoData.PedidoGrafico;
import uo.ips.muebleria.logic.grafico.GraficoData.VentaGrafico;

public class GraficoLineasBalance extends JDialog {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String periodo;
	private GraficoData gd;

	private JFreeChart chart;

	public GraficoLineasBalance(String titulo, String periodo) {
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

	private void crearGraficoLineas() {
		LocalDate o = LocalDate.now();
		DefaultXYDataset dataset = null;
		String str = "";
		switch (periodo) {
		case GraficoData.MES:
			str = "Días del mes";
			o = LocalDate.of(o.getYear(), o.getMonth(), 1);
			dataset = crearDatasetMes(gd.getVentasDespues(o), gd.getPedidosDespues(o));
			break;
		case GraficoData.AÑO:
			str = "Meses";
			o = LocalDate.of(o.getYear(), 1, 1);
			dataset = crearDatasetAño(gd.getVentasDespues(o), gd.getPedidosDespues(o));
			break;
		}

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(3));
		renderer.setSeriesStroke(1, new BasicStroke(3));

		chart = ChartFactory.createXYLineChart(titulo, str, "Dinero acumulado", dataset);
		chart.getXYPlot().setRenderer(renderer);
	}

	private DefaultXYDataset crearDatasetMes(List<VentaGrafico> ventasMes, List<PedidoGrafico> pedidos) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		int days = LocalDate.now().lengthOfMonth();
		LocalDate o = LocalDate.now();
		double[] p = new double[days / 3 + 1];
		double[] ganancias = new double[p.length];
		double[] gastos = new double[p.length];

		for (int i = 0; i < (days / 3); i++)
			p[i] = 1 + 3 * i;
		p[(days / 3)] = days;

		double total;
		for (int i = 0; i < ganancias.length; i++) {
			total = 0;
			for (VentaGrafico v : ventasMes) {
				if (v.getFecha().isBefore(LocalDate.of(o.getYear(), o.getMonth(), (int) p[i]))
						&& v.getFecha().isAfter(LocalDate.of(o.getYear(), o.getMonth(), 1)))
					total += v.getPrecio();
			}
			ganancias[i] = total;
		}
		for (int i = 0; i < gastos.length; i++) {
			total = 0;
			for (PedidoGrafico v : pedidos) {
				if (v.getFecha().isBefore(LocalDate.of(o.getYear(), o.getMonth(), (int) p[i]))
						&& v.getFecha().isAfter(LocalDate.of(o.getYear(), o.getMonth(), 1)))
					total += v.getPrecio();
			}
			gastos[i] = total;
		}
		dataset.addSeries("ganancias mensuales", new double[][] { p, ganancias });
		dataset.addSeries("gastos mensuales", new double[][] { p, gastos });
		return dataset;
	}

	private DefaultXYDataset crearDatasetAño(List<VentaGrafico> ventasAño, List<PedidoGrafico> pedidosAño) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		LocalDate o = LocalDate.now();
		double[] p = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		double[] ganancias = new double[p.length];
		double[] gastos = new double[p.length];

		double total;
		for (int i = 0; i < ganancias.length; i++) {
			total = 0;
			for (VentaGrafico v : ventasAño) {
				if (v.getFecha().isBefore(LocalDate.of(o.getYear(), (int) p[i], 1))
						&& v.getFecha().isAfter(LocalDate.of(o.getYear(), 1, 1)))
					total += v.getPrecio();
			}
			ganancias[i] = total;
		}
		for (int i = 0; i < gastos.length; i++) {
			total = 0;
			for (PedidoGrafico v : pedidosAño) {
				if (v.getFecha().isBefore(LocalDate.of(o.getYear(), (int) p[i], 1))
						&& v.getFecha().isAfter(LocalDate.of(o.getYear(), 1, 1)))
					total += v.getPrecio();
			}
			gastos[i] = total;
		}
		dataset.addSeries("ganancias anuales", new double[][] { p, ganancias });
		dataset.addSeries("gastos anuales", new double[][] { p, gastos });
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
