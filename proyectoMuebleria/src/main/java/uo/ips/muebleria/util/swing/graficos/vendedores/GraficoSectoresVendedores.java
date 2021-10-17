package uo.ips.muebleria.util.swing.graficos.vendedores;

import java.time.LocalDate;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Rotation;

import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.grafico.GraficoData.VendedorGrafico;

public class GraficoSectoresVendedores extends JDialog {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String periodo;
	private GraficoData gd;

	private JFreeChart chart;

	public GraficoSectoresVendedores(String titulo, String periodo) {
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
		DefaultPieDataset dataset = null;
		switch (periodo) {
		case GraficoData.MES:
			dataset = crearDatasetMes();
			break;
		case GraficoData.AÑO:
			dataset = crearDatasetAño();
			break;
		}

		chart = ChartFactory.createPieChart3D(titulo, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
	}

	private DefaultPieDataset crearDatasetMes() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		LocalDate o = LocalDate.now();
		o = LocalDate.of(o.getYear(), o.getMonth(), 1);
		for (VendedorGrafico ven : gd.getVendedores())
			dataset.setValue(ven.getNombre() + " " + ven.getApellido(), gd.getGeneradoDespuesDe(ven.getVentas(), o));
		return dataset;
	}

	private DefaultPieDataset crearDatasetAño() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		LocalDate o = LocalDate.now();
		o = LocalDate.of(o.getYear(), 1, 1);
		for (VendedorGrafico ven : gd.getVendedores())
			dataset.setValue(ven.getNombre() + " " + ven.getApellido(), gd.getGeneradoDespuesDe(ven.getVentas(), o));
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
