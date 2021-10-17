package uo.ips.muebleria.util.swing.graficos.balance;

import java.awt.Color;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Rotation;

import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.grafico.GraficoData.PedidoGrafico;
import uo.ips.muebleria.logic.grafico.GraficoData.VentaGrafico;

public class GraficoSectoresBalance extends JDialog {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String periodo;
	private GraficoData gd;

	private JFreeChart chart;

	public GraficoSectoresBalance(String titulo, String periodo) {
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

	@SuppressWarnings("deprecation")
	private void crearGrafico() {
		LocalDate o = LocalDate.now();
		DefaultPieDataset dataset = null;
		switch (periodo) {
		case GraficoData.MES:
			o = LocalDate.of(o.getYear(), o.getMonth(), 1);
			dataset = crearDatasetMes(gd.getVentasDespues(o), gd.getPedidosDespues(o));
			break;
		case GraficoData.AÑO:
			o = LocalDate.of(o.getYear(), 1, 1);
			dataset = crearDatasetAño(gd.getVentasDespues(o), gd.getPedidosDespues(o));
			break;
		}

		chart = ChartFactory.createPieChart3D(titulo, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setSectionPaint(0, Color.GREEN);
		plot.setSectionPaint(1, Color.RED);
	}

	private DefaultPieDataset crearDatasetMes(List<VentaGrafico> ventasMes, List<PedidoGrafico> pedidos) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		LocalDate o = LocalDate.now();
		o = LocalDate.of(o.getYear(), o.getMonth(), 1);
		dataset.setValue("ganacias mensuales", gd.getGeneradoVentasDespuesDe(o));
		dataset.setValue("gastos mensuales", gd.getGastadoPedidosDespuesDe(o));
		return dataset;
	}

	private DefaultPieDataset crearDatasetAño(List<VentaGrafico> ventasAño, List<PedidoGrafico> pedidosAño) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		LocalDate o = LocalDate.now();
		o = LocalDate.of(o.getYear(), 1, 1);
		dataset.setValue("ganacias mensuales", gd.getGeneradoVentasDespuesDe(o));
		dataset.setValue("gastos mensuales", gd.getGastadoPedidosDespuesDe(o));
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
