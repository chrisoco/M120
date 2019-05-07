package rcas.controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import rcas.model.RaceCar;
import rcas.util.CorneringAnalyserUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * MMM Diagram / BarChart Controller Class for GUI
 *
 * @author Christopher O'Connor, Umut Savas
 * @version 1.0
 * @since 2019-04-16
 */

public class RCASMMMController {

	@FXML
	private LineChart<Number, Number> mainChart;
	@FXML
	private BarChart<String, Number> balanceChart, gripChart, controlChart, stabilityChart;
	@FXML
	private JFXTextField ctrl_B, ctrl_D, ctrl_toD, stab_D, stab_B, stab_toB;
	@FXML
	private VBox legend;

	// Utility Class for the analysis of the cornering capabilities of a RaceCar
	private CorneringAnalyserUtil util;
	// List containing all added RaceCars
	private ArrayList<RaceCar> raceCars;


	/**
	 * Constructs a new RCASMMMController
	 */
	public RCASMMMController() {

		this.util     = new CorneringAnalyserUtil();
		this.raceCars = new ArrayList<>();

	}

	/**
	 * Initialize on Startup
	 * All TextField Validators {@link #initDoubleVal(JFXTextField)} are Added
	 */
	@FXML
	private void initialize() {

		initDoubleValTextFields();

	}

	/**
	 * All TextField Validators {@link #initDoubleVal(JFXTextField)} are Added
	 */
	private void initDoubleValTextFields() {

		initDoubleVal(ctrl_B  ).setText( "0");
		initDoubleVal(ctrl_D  ).setText( "0");
		initDoubleVal(ctrl_toD).setText("10");
		initDoubleVal(stab_D  ).setText( "0");
		initDoubleVal(stab_B  ).setText( "0");
		initDoubleVal(stab_toB).setText( "1");

	}

	/**
	 * Double Validator to ensure only Double Values are Entered into TextField
	 * @param tf TextField to apply Validator to
	 * @return TextField which the Validator was applied to
	 */
	@SuppressWarnings("Duplicates")
	private JFXTextField initDoubleVal(JFXTextField tf) {

		ChangeListener<String> doubleValidator = new ChangeListener<String>() {
			// If the NewValue of a TextField matches Regex pattern change the TextField Value
			// (No Letters can be Typed into the TextField or other Symbols)
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[-]?\\d{0,7}[\\.]?\\d{0,4}")) {
					tf.setText(oldValue);
				}
			}

		};

		tf.textProperty().addListener(doubleValidator);
		tf.getValidators().add(new RequiredFieldValidator());
		tf.setOnKeyReleased(e -> tf.validate());

		return tf;

	}

	/**
	 * Get Values from all TextFields and save them in a Array[]
	 * @return Array[] containing all TextFieldValues entered by User
	 */
	private double [] getTFData() {

		double [] data = new double[6];

		data [0] = getTFData(ctrl_B  );
		data [1] = getTFData(ctrl_D  );
		data [2] = getTFData(ctrl_toD);
		data [3] = getTFData(stab_D  );
		data [4] = getTFData(stab_B  );
		data [5] = getTFData(stab_toB);

		return data;
	}

	/**
	 * Validate and Get Data from Textfield
	 * if no value has been given set it to default 0.
	 * @param tf Textfield to get value from
	 * @return Textfield value as double or if validation fails: default 0.
	 */
	private double getTFData(JFXTextField tf) {

		String data = tf.getText();

		if(data != null && !data.isEmpty()) return Double.valueOf(data);

		tf.setText("0");
		return 0;

	}

	/**
	 * Add RaceCar to MMMDiagram, BarCharts and Legend with its selected Color
	 * @param raceCar RaceCar which will be added
	 */
	public void addRaceCar(RaceCar raceCar) {

		if(!raceCars.contains(raceCar)) {

			MMMChart(raceCar);

			String color     = raceCar.getColor();
			double [] tfData = getTFData();

			addChartData(util.getMMMBalanceValue  (raceCar), color, balanceChart);
			addChartData(util.getMMMGripValue     (raceCar), color, gripChart   );
			addChartData(util.getMMMControlValue  (raceCar, tfData[0], tfData[1], tfData[2]), color, controlChart  );
			addChartData(util.getMMMStabilityValue(raceCar, tfData[3], tfData[4], tfData[5]), color, stabilityChart);

			raceCars.add(raceCar);
			addRaceCarLegend(raceCar);

			changeChartSize();

		}

	}

	/**
	 * Add RaceCar to Legend (Name and Color of RaceCar)
	 * @param raceCar RaceCar which will be Added to the Legend {@link #legend}
	 */
	private void addRaceCarLegend(RaceCar raceCar) {

		Label label = new Label(raceCar.getName());
		label.setPrefWidth(350);
		label.setPrefHeight(30);
		label.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-weight: bold; -fx-background-color: " + raceCar.getColor() + ";");

		legend.getChildren().add(label);

	}

	/**
	 * Recalculate & Update:
	 * BarChart (Control & Stability) when the User changed Settings (Betta Delta etc.)
	 */
	@FXML
	private void upDateChart(){

		double [] tfData = getTFData();
		int i = 0;

		for (RaceCar raceCar :raceCars) {

			// Recalculate Values
			final double controlValue   = (Math.round(util.getMMMControlValue  (raceCar, tfData[0], tfData[1], tfData[2]) * 10.0)) / 10.0;
			final double stabilityValue = (Math.round(util.getMMMStabilityValue(raceCar, tfData[3], tfData[4], tfData[5]) * 10.0)) / 10.0;

			// Set new Values
			controlChart  .getData().get(i).getData().get(0).setYValue(  controlValue);
			stabilityChart.getData().get(i).getData().get(0).setYValue(stabilityValue);

			i++;
		}

	}

	/**
	 * Change the Size of the Bars in BarChart depending on how many RaceCars there are.
	 */
	private void changeChartSize() {

		switch(raceCars.size()) {

			case  2: setChartSize(120);
					 balanceChart  .setBarGap(2);
					 gripChart     .setBarGap(2);
					 controlChart  .setBarGap(2);
					 stabilityChart.setBarGap(2);
					 break;
			case  3: setChartSize(100); break;
			case  4: setChartSize( 60); break;
			case  5: setChartSize( 30); break;
			case  6: setChartSize( 10); break;

		}

	}

	/**
	 * Set the Gap between Categories of a Chart
	 * @param len double value of length
	 */
	private void setChartSize(double len) {
		balanceChart  .setCategoryGap(len);
		gripChart     .setCategoryGap(len);
		controlChart  .setCategoryGap(len);
		stabilityChart.setCategoryGap(len);
	}

	/**
	 * Add RaceCar MMMChartData to MMMDiagram and set the Color to the RaceCar selected Color
	 * @param raceCar RaceCar which will be added
	 */
	private void MMMChart(RaceCar raceCar) {

		ObservableList<XYChart.Series<Number, Number>> dataList = util.generateMMMChartData(raceCar);
		mainChart.getData().addAll(dataList);

		for (Iterator<XYChart.Series<Number, Number>> iterator = dataList.iterator(); iterator.hasNext();) {
			XYChart.Series<Number, Number> curve = (XYChart.Series<Number, Number>) iterator.next();
			curve.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + raceCar.getColor() + "; -fx-stroke-width: 1px;");
		}

	}

	/**
	 * Add ChartData of a RaceCar
	 * @param value Value to Add
	 * @param color Color of the RaceCar
	 * @param chart Chart to add the Value to
	 */
	private void addChartData(double value, String color, BarChart<String, Number> chart) {

		// Round Value to .0
		final double VALUE  = (Math.round(value * 10.0)) / 10.0;
	
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		XYChart.Data  <String, Number> data   = new XYChart.Data<>("", VALUE);

		// Set Color of the Bar
		data.nodeProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, final Node node) {
				data.getNode().setStyle("-fx-bar-fill: " + color + ";");
				displayLabelForData(data, chart);
			}
		});

		series.getData().add(data  );
		chart .getData().add(series);

	}

	/**
	 * Display Value Label on top of the Bar
	 * @param data Data of the Bar where the Label will be Added to
	 * @param chart Chart of the Bar specified
	 */
	private void displayLabelForData(XYChart.Data<String, Number> data, BarChart<String, Number> chart) {

		Node node     = data.getNode();
		Text dataText = new Text(data.getYValue() + "");
		dataText.setStyle("-fx-font-size: 14");

		// Add Label with Value
		node.parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {

				Group parentGroup = (Group) parent;

				if (parentGroup != null) {

					ObservableList<Node> children = parentGroup.getChildren();
					if (children != null) children.add(dataText);

				}
			}
		});

		// Set Label Location in BarChart
		node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {

				dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));

				if(Double.valueOf(dataText.getText()) > 0) {
					dataText.setLayoutY(Math.round((bounds.getMinY()) - dataText.prefHeight(-1) * 0.1) + 15);
				} else {
					dataText.setLayoutY(Math.round((bounds.getMaxY()) + dataText.prefHeight(-1)) - 25);
				}

				/**
				 *  Update Text Value
				 */
				dataText.setText(data.getYValue() + "");

				/**
				 * Set Font Size depending on how many RaceCars are shown
				 */
				if (raceCars.size() > 2) dataText.setStyle("-fx-font-size: 11");

				if (raceCars.size() > 5) dataText.setRotate(-40);

				if (raceCars.size() > 7) dataText.setStyle("-fx-font-size: 9");

			}
		});


	}

}
