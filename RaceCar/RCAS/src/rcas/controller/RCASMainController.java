package rcas.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.Notifications;
import rcas.RCASMain;
import rcas.model.MagicFormulaTireModel;
import rcas.model.RaceCar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Main Controller Class for GUI
 *
 * @author Christopher O'Connor, Umut Savas
 * @version 1.0
 * @since 2019-04-16
 */

@SuppressWarnings("Duplicates")
public class RCASMainController {

	@FXML
	private JFXSlider cog_Slider, cwFL_Slider, cwFR_Slider, cwRL_Slider, cwRR_Slider, frd_Slider;
	@FXML
	private JFXTextField cog, cwFL, cwFR, cwRL, cwRR, frd, wb, fTrack, rTrack, name;
	@FXML
	private JFXButton delete, tm, save, showMMM, saveAxle;
	@FXML
	private JFXListView<Label> listView;
	@FXML
	private JFXColorPicker colorPicker;
	@FXML
	private StackPane stackPane;
	@FXML
	private JFXDialog jfxDialog;

	// List containing all TextFields for SlipAngleCoefficients & LoadCoefficients
	private ArrayList<JFXTextField> advTextList = new ArrayList<>();
	// List containing all other TextFields
	private ArrayList<JFXTextField> valTextList = new ArrayList<>();
	// Controller for MMMView
	private RCASMMMController mmmController;


	/**
	 * Initialize on Startup
	 * - All Wheelweight TextFields + Slider Bindings {@link #createBindings()}
	 * - All TextField Validators {@link #initValidators()}
	 * - Add Label "New RaceCar Model" to Listview {@link #initListView()}
	 * - Add Default RaceCars {@link #initDefaultRaceCars()}
	 */
	@FXML
	private void initialize() {

		createBindings();
		initValidators();
		initListView();
		initDefaultRaceCars();

	}

	/**
	 * Add Label "New RaceCar Model" to ListView which contains all RaceCarModels
	 */
	private void initListView() {

		listView.setExpanded(true);

		Label label = new Label("New RaceCar Model");
		label.setStyle("-fx-text-fill: green;");

		listView.getItems().add(label);

	}

	/**
	 * Add Validators to TextFields
	 * 2 Validators per TextField:
	 *  - RequiredValidator {@link #addVal(JFXTextField, double, double)}
	 *  - Min / Max Value Validator {@link #addVal(JFXTextField, double, double)}
	 *
	 */
	private void initValidators() {

		addVal(name                           );
		addVal(cwFL,  50.0, 1_000.0);
		addVal(cwFR,  50.0, 1_000.0);
		addVal(cwRR,  50.0, 1_000.0);
		addVal(cwRL,  50.0, 1_000.0);
		addVal(wb,     0.5,     6.0);
		addVal(fTrack, 1.2,     2.0);
		addVal(rTrack, 1.2,     2.0);
		addVal(cog,   10.0,   200.0);
		addVal(frd,   20.0,    80.0);

	}

	/**
	 * Add Validators to TextFields
	 * 2 Validators per TextField:
	 *  - RequiredValidator
	 *  - Min / Max Value Validator
	 *
	 *  Add TextField to {@link #valTextList}
	 *
	 * @param tf TextField to add Validator
	 * @param min Min Value TextField must contain
	 * @param max Max Value TextField must contain
	 */
	private void addVal(JFXTextField tf, double min, double max) {

		RequiredFieldValidator reqVal = new RequiredFieldValidator("Required");
		RangeVal rangeVal             = new RangeVal(min, max);

		tf.getValidators().addAll(reqVal, rangeVal);

		valTextList.add(tf);

		tf.setOnKeyReleased(e -> tf.validate());

	}

	/**
	 * Add RequiredFieldValidator to TextField and add TextField to {@link #valTextList}
	 * @param tf TextField to add Validator
	 */
	private void addVal(JFXTextField tf) {
		tf.getValidators().add(new RequiredFieldValidator("Required"));
		tf.setOnKeyReleased(e -> tf.validate());
		valTextList.add(tf);
	}

	/**
	 * Create Bindings between TextField and Slider of Wheelweights
	 * Add Double Validator to TextFields that must contain a Double
	 */
	private void createBindings() {

		initDoubleVal(cwFL).textProperty().bindBidirectional(cwFL_Slider.valueProperty(), new NumberStringConverter("#"));
		initDoubleVal(cwFR).textProperty().bindBidirectional(cwFR_Slider.valueProperty(), new NumberStringConverter("#"));
		initDoubleVal(cwRL).textProperty().bindBidirectional(cwRL_Slider.valueProperty(), new NumberStringConverter("#"));
		initDoubleVal(cwRR).textProperty().bindBidirectional(cwRR_Slider.valueProperty(), new NumberStringConverter("#"));
		initDoubleVal(cog ).textProperty().bindBidirectional( cog_Slider.valueProperty(), new NumberStringConverter("#.0"));
		initDoubleVal(frd ).textProperty().bindBidirectional( frd_Slider.valueProperty(), new NumberStringConverter("#.0"));

		initDoubleVal(rTrack);
		initDoubleVal(fTrack);
		initDoubleVal(wb);

		clearAllFields();

	}

	/**
	 * Double Validator to ensure only Double Values are Entered into TextField
	 * @param tf TextField to apply Validator to
	 * @return TextField which the Validator was applied to
	 */
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

		return tf;

	}

	/**
	 * Method to Clear all Field Values and set Default color to {@link #colorPicker}
	 */
	private void clearAllFields() {

		cog    .clear();
		cwFL   .clear();
		cwFR   .clear();
		cwRL   .clear();
		cwRR   .clear();
		frd    .clear();
		wb     .clear();
		fTrack .clear();
		rTrack .clear();
		name   .clear();
		colorPicker.setValue(Color.valueOf("#f44b42"));

	}

	/**
	 * Add Default RaceCars to the Application
	 */
	private void initDefaultRaceCars() {

		addRaceCarToList(new RaceCar("Car STD", 420, 420, 370, 370, "#f44b42"));
		addRaceCarToList(new RaceCar("Car MOD", 350, 350, 270, 270, "#00ACC1"));
		addRaceCarToList(new RaceCar("Car STD", 420, 420, 370, 370, "#f44b42"));
		addRaceCarToList(new RaceCar("Car MOD", 350, 350, 270, 270, "#00ACC1"));
		addRaceCarToList(new RaceCar("Car STD", 420, 420, 370, 370, "#f44b42"));
		addRaceCarToList(new RaceCar("Car MOD", 350, 350, 270, 270, "#00ACC1"));
		addRaceCarToList(new RaceCar("Car STD", 420, 420, 370, 370, "#f44b42"));
		addRaceCarToList(new RaceCar("Car MOD", 350, 350, 270, 270, "#00ACC1"));

	}

	/**
	 * Save RaceCar to List {@link #listView}
	 * Insert RaceCar at LastIndex -1 so the Label "New RaceCar Model" always stays at bottom of List
	 * @param raceCar RaceCar to be saved
	 */
	private void addRaceCarToList(RaceCar raceCar) {

		Label label = new Label(raceCar.getName());
		label.setUserData(raceCar);

		ObservableList<Label> labelList = listView.getItems();
		labelList.add(labelList.size() - 1, label);

	}

	/**
	 * Display the Selected Model of {@link #listView} in Application
	 * If the Selected Index ("New RaceCar Model") is Selected create a new RaceCarModel
	 *
	 * Labels of the {@link #listView} have as their UserData the RaceCar Model which they represent.
	 */
	@FXML
	private void displaySelectedModel() {

		if (!listView.getSelectionModel().isEmpty()) {

			Label selLabel = listView.getSelectionModel().getSelectedItem();

			if (selLabel.getUserData() != null) {

				displayRaceCar((RaceCar) listView.getSelectionModel().getSelectedItem().getUserData());
				tm     .setDisable(false);
				showMMM.setDisable(false);

				for (JFXTextField tf : valTextList) {
					tf.validate();
				}

			} else {

				newRaceCar();

			}
		}

	}

	/**
	 * Add new RaceCar:
	 * Clear all Fields and Disable TireModel + MMMDiagram Button
	 */
	private void newRaceCar() {

		clearAllFields();
		tm.setDisable(true);
		showMMM.setDisable(true);

	}

	/**
	 * Display the Selected RaceCar from {@link #listView} in Application TextFields.
	 * @param raceCar RaceCar which will be Displayed.
	 */
	private void displayRaceCar(RaceCar raceCar) {

		name.setText(raceCar.getName());

		cwFL_Slider.setValue(raceCar.getCornerWeightFL());
		cwFR_Slider.setValue(raceCar.getCornerWeightFR());
		cwRL_Slider.setValue(raceCar.getCornerWeightRL());
		cwRR_Slider.setValue(raceCar.getCornerWeightRR());

		// Multiply Value * 100 to make Value more Readable
		frd_Slider .setValue(raceCar.getFrontRollDist () * 100);
		cog_Slider .setValue(raceCar.getCogHeight     () * 100);

		fTrack.setText(String.valueOf(raceCar.getFrontTrack()));
		rTrack.setText(String.valueOf(raceCar.getRearTrack ()));
		wb    .setText(String.valueOf(raceCar.getWheelbase ()));

		colorPicker.setValue(Color.valueOf(raceCar.getColor()));

	}

	/**
	 * Create Dialog for Advanced AxleTireModel Configuration
	 */
	@FXML
	private void advancedAxleModelPopUp() {

		advTextList.clear();

		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("Tire Model Configuration"));
		content.setPrefWidth(600);

		GridPane pane = new GridPane();
		pane.setVgap(35);
		pane.setHgap(50);
		pane.setAlignment(Pos.CENTER);

		/**** Front Axle Tire Model ****/
		Label titleFront = new Label("Front Axle Tire Model");
		titleFront.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
		pane.add(titleFront, 0,0);


		JFXTextField cF  = createAdvInfoTF("Slip Angle Coefficient C", 0.1,15.0);
		pane.add(cF,  0,1);
		JFXTextField bF  = createAdvInfoTF("Slip Angle Coefficient B", 1.0, 45.0);
		pane.add(bF,  0,2);
		JFXTextField eF  = createAdvInfoTF("Slip Angle Coefficient E", -15.0, 20.0);
		pane.add(eF,  0,3);
		JFXTextField kaF = createAdvInfoTF("Load Coefficient KA", 1.0, 5.0);
		pane.add(kaF, 0,4);
		JFXTextField kbF = createAdvInfoTF("Load Coefficient KA", 0.1, 1.5);
		pane.add(kbF, 0,5);


		/**** Rear Axle Tire Model ****/
		Label titleRear = new Label("Rear Axle Tire Model");
		titleRear.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
		pane.add(titleRear, 2,0);

		JFXTextField cR  = createAdvInfoTF("Slip Angle Coefficient C", 0.1,15.0);
		pane.add(cR,  2,1);
		JFXTextField bR  = createAdvInfoTF("Slip Angle Coefficient B", 1.0, 45.0);
		pane.add(bR,  2,2);
		JFXTextField eR  = createAdvInfoTF("Slip Angle Coefficient E", -15.0, 20.0);
		pane.add(eR,  2,3);
		JFXTextField kaR = createAdvInfoTF("Load Coefficient KA", 1.0, 5.0);
		pane.add(kaR, 2,4);
		JFXTextField kbR = createAdvInfoTF("Load Coefficient KA", 0.1, 1.5);
		pane.add(kbR, 2,5);

		// Add Separator Between Front and Rear AxleTireModel TextFields
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		pane.add(separator, 1,0,1,6);


		/**** Calculate TireModels from Selected RaceCar from {@link #listView} and set Values in TextFields ****/

		RaceCar raceCar = (RaceCar) listView.getSelectionModel().getSelectedItem().getUserData();

		MagicFormulaTireModel tmF = (MagicFormulaTireModel) raceCar.getFrontAxleTireModel();
		MagicFormulaTireModel tmR = (MagicFormulaTireModel) raceCar.getRearAxleTireModel ();

		cF .setText(String.valueOf(tmF.getSlipAngleCoefficientC()));
		bF .setText(String.valueOf(tmF.getSlipAngleCoefficientB()));
		eF .setText(String.valueOf(tmF.getSlipAngleCoefficientE()));
		kaF.setText(String.valueOf(tmF.getLoadCoefficientKA()));
		kbF.setText(String.valueOf(tmF.getLoadCoefficientKB()));

		cR .setText(String.valueOf(tmR.getSlipAngleCoefficientC()));
		bR .setText(String.valueOf(tmR.getSlipAngleCoefficientB()));
		eR .setText(String.valueOf(tmR.getSlipAngleCoefficientE()));
		kaR.setText(String.valueOf(tmR.getLoadCoefficientKA()));
		kbR.setText(String.valueOf(tmR.getLoadCoefficientKB()));

		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

		jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
		jfxDialog.setOnDialogClosed(e -> listView.setDisable(false));

		// Add SaveButton to Dialog
		saveAxle = new JFXButton("Save Axle Tire Models");
		saveAxle.setPrefWidth(155);
		saveAxle.setPrefHeight(26);
		saveAxle.setButtonType(JFXButton.ButtonType.RAISED);
		saveAxle.setStyle("-fx-background-color:  lightgreen");


		saveAxle.setOnAction(e -> {

			// Validate all TextFields if they all are Valid, Save new TireModel to RaceCar
			if(valTextList(advTextList)) {

				raceCar.setFrontAxleTireModel(
						new MagicFormulaTireModel(
								Double.valueOf(cF.getText()),
								Double.valueOf(bF.getText()),
								Double.valueOf(eF.getText()),
								Double.valueOf(kaF.getText()),
								Double.valueOf(kbF.getText()) / 10_000));

				raceCar.setRearAxleTireModel(
						new MagicFormulaTireModel(
								Double.valueOf(cR.getText()),
								Double.valueOf(bR.getText()),
								Double.valueOf(eR.getText()),
								Double.valueOf(kaR.getText()),
								Double.valueOf(kbR.getText()) / 10_000));


				jfxDialog.close();

			} else {
				notifyErrAction("Invalid action", "Please fill every textfield with a value.");
			}

		});


		pane.add(saveAxle, 2,6);

		content.setBody(pane);

		jfxDialog.show();
		listView.setDisable(true);

	}

	/**
	 * Create TextField for the AxleTireModel with specified Layouts and FieldValidators.
	 * Add TextField to {@link #advTextList} to Validate all Inputs Later
	 *
	 * @param name Name of the TextFieldValue represented (Slip Angle Coefficient C)
	 * @param min Min Value for the FieldValidator {@link #initDoubleVal(JFXTextField)}
	 * @param max Max Value for the FieldValidator {@link #initDoubleVal(JFXTextField)}
	 * @return return new Created JFXTextField
	 */
	private JFXTextField createAdvInfoTF(String name, double min, double max) {

		JFXTextField tf = new JFXTextField();
		tf.setPromptText(name);
		tf.setLabelFloat(true);
		tf.setPrefWidth(155);

		initDoubleVal(tf);

		RequiredFieldValidator reqVal = new RequiredFieldValidator("Required");
		RangeVal rangeVal             = new RangeVal(min, max);

		tf.getValidators().addAll(reqVal, rangeVal);

		advTextList.add(tf);

		tf.setOnKeyReleased(e -> tf.validate());

		return tf;

	}

	/**
	 * Validate All TextFields in List. If a TextField isn't Valid it will be
	 * viably shown on the GUI by throwing an User Error msg.
	 *
	 * @param textFields Validate all TextFields from List
	 * @return If all Fields have a Valid Input return true
	 */
	private boolean valTextList(ArrayList<JFXTextField> textFields) {

		for (JFXTextField tf : textFields) {

			if(!tf.validate()) return false;

		}

		return true;

	}

	/**
	 * Checks if the MMMDiagram View is already Running:
	 *  If true, then Add the Selected RaceCar Model to the Charts.
	 *  If false, create the MMMDiagram View and then Add the Selected RaceCar Model to the Charts.
	 *
	 * @throws Exception fxml File not Found
	 */
	@FXML
	private void MMMDiagram() throws Exception {

		if (mmmController == null) initMMMDiagram();

		mmmController.addRaceCar((RaceCar) listView.getSelectionModel().getSelectedItem().getUserData());

	}

	/**
	 * Create new MMMDiagram Window to Display all Charts from Calculated RaceCar Models.
	 * @throws Exception FXML exception File Not Found
	 */
	private void initMMMDiagram() throws Exception {

		//Instantiate the Controller to this Controller to gain Access and Pass RaceCars Between both Classes.
		mmmController = new RCASMMMController();

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RCASMain.class.getResource("view/RCASMMMView.fxml"));
		fxmlLoader.setController(mmmController);

		JFXMasonryPane mmmPane = fxmlLoader.load();
		ScrollPane root = new ScrollPane(mmmPane);
		root.setFitToWidth(true);

		Stage mmmStage = new Stage();

		mmmStage.setScene(new Scene(root, 1300, 780));
		mmmStage.setTitle("MMM Diagram Mz / Fy (Milliken Moment Method)");
		mmmStage.getIcons().add(new Image(RCASMain.class.getResourceAsStream("view/imgs/icon2.png")));
		mmmStage.centerOnScreen();

		// If the Window will be closed also Delete reference to the Constructor to
		// determine if a new Window is required
		mmmStage.setOnCloseRequest(e -> mmmController = null);

		mmmStage.show();

	}

	/**
	 * Get the selected RaceCar model in the listView.
	 * If it is selected, remove it from listView, clear input fields and select "New RaceCar Model" item in listView
	 * Otherwise show notification for user to inform him to select a valid RaceCar
	 */
	@FXML
	public void deleteRaceCar() {

		if (listView.getSelectionModel().getSelectedItem().getText().equals("New RaceCar Model")) {

			notifyErrAction("Invalid action", "Please select a valid RaceCar");

		} else {

			clearAllFields();
			listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
			listView.getSelectionModel().selectLast();

		}

	}

	/**
	 * If all inputFields contain valid data:
	 * 	If "New RaceCar Model" is selected, then create a new RaceCar, add it to listView and show notification
	 * 	If an existing RaceCar is selected, then update the data and show notification
	 */
	@FXML
	public void saveRaceCar() {

	    if (valTextList(valTextList)) {

	    	RaceCar raceCar = new RaceCar(name.getText(), Double.parseDouble(fTrack.getText()), Double.parseDouble(rTrack.getText()), Double.parseDouble(wb.getText()), Double.parseDouble(cog.getText()) / 100, Double.parseDouble(frd.getText()) / 100, Double.parseDouble(cwFL.getText()), Double.parseDouble(cwFR.getText()), Double.parseDouble(cwRL.getText()), Double.parseDouble(cwRR.getText()), "#" + Integer.toHexString(colorPicker.getValue().hashCode()));

		    if(listView.getSelectionModel().getSelectedIndex() == listView.getItems().size() - 1) {

			    Label label = new Label(raceCar.getName());
			    label.setUserData(raceCar);
			    listView.getItems().add(listView.getItems().size() - 1, label);


			    notifyAction("RaceCar created", raceCar.getName() + " successfully created.");


		    } else {

		    	Label rcas = listView.getSelectionModel().getSelectedItem();
		    	rcas.setText(raceCar.getName());
		    	rcas.setUserData(raceCar);

			    notifyAction("RaceCar modified", raceCar.getName() + " successfully modified.");

		    }


        } else {

	    	notifyErrAction("Invalid action", "Please fill every textfield with a value.");

	    }


	}

	/**
	 * Select the "new RaceCar Model" item from listView
	 */
	public void moveToNewCar() {
		listView.getSelectionModel().selectLast();
		newRaceCar();
	}

	/**
	 * Create Notification on TopLeft Screen Corner
	 * @param title Title of the Notification
	 * @param text Text of the Notification
	 */
	private void notifyAction(String title, String text) {

		Notifications.create()
				.position(Pos.TOP_RIGHT)
				.title(title)
				.text(text)
				.darkStyle()
				.show();

	}

	/**
	 * Create Error Notification on TopLeft Screen Corner
	 * @param title Title of the Error - Notification
	 * @param text Text of the Error - Notification
	 */
	private void notifyErrAction(String title, String text) {

		Notifications.create()
				.position(Pos.TOP_RIGHT)
				.title("Invalid action")
				.text("Please fill every textfield with a value.")
				.darkStyle()
				.showError();

	}

}
