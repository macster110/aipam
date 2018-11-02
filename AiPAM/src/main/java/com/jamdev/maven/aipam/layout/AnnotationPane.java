package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.annotation.SimpleAnnotation;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * Pane which allows users to edit annotations and export annotation data. 
 * <p>
 * 	Note that due the flexible nature of annotations this only supports SimpleAnnotions and 
 * subclasses thereof. 
 * 
 * @author Jamie Macaulay
 *
 */
public class AnnotationPane implements SettingsPane<AIPamParams> {


	private AIPamView aiPamView;

	/**
	 * The main pane
	 */
	private Pane mainPane; 


	private TableView<SimpleAnnotation> table; 

	/**
	 * List of simple annotations. 
	 */
	private final ObservableList<SimpleAnnotation> data =
			FXCollections.observableArrayList(
					new SimpleAnnotation(1));

	public AnnotationPane(AIPamView aiPamView) {
		this.aiPamView= aiPamView; 
		this.mainPane = createPane();
	}

	@Override
	public Text getIcon() {
		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.PENCIL); 
		iconView.setGlyphSize(AIPamView.iconSize);
		iconView.setFill(Color.WHITE);
		return iconView;
	}

	private Pane createPane() {

		Label label = new Label("Annotations");
		label.getStyleClass().add("label-title1");

		Button button = new Button("Export Data..."); 
		button.setOnAction((action) -> aiPamView.exportAnnotations());

		Label labelEditAnno = new Label("Edit Annotations");
		labelEditAnno.getStyleClass().add("label-title2");

		//create the table
		table = new TableView<SimpleAnnotation>(); 
		table.setEditable(true);
		table.setItems(data);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //stop the annoying extra column

		//first column of the table allows users to pick the colour. 
		TableColumn<SimpleAnnotation, StringProperty> colorColumn = new TableColumn<>("Symbol");
		//colorColumn.setMinWidth(110);
		colorColumn.setStyle( "-fx-padding: 0em 0em 0em 0em;");
		
//		
//		colorColumn.setCellValueFactory(i -> {
//			final StringProperty value = i.getValue().colorProperty(); 
//			// binding to constant value
//			return Bindings.createObjectBinding(() -> value);
//		});

		colorColumn.setCellFactory(col -> {
			TableCell<SimpleAnnotation, StringProperty> c = new TableCell<>();
			final ComboBox<String> comboBox = createColorComboBox();
			c.itemProperty().addListener((observable, oldValue, newValue) -> {
				if (oldValue != null) {
					comboBox.valueProperty().unbindBidirectional(oldValue);
				}
				if (newValue != null) {
					comboBox.valueProperty().bindBidirectional(newValue);
				}
			});
			c.graphicProperty().bind(Bindings.when(c.emptyProperty()).
					then((Node) null).otherwise(comboBox));
			return c;
		});

		
		TableColumn<SimpleAnnotation, String> nameProperty =
				new TableColumn<SimpleAnnotation, String> ("Name");
		nameProperty.setEditable(true);
		//nameProperty.setMinWidth(110);
		nameProperty.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		nameProperty.setCellFactory(TextFieldTableCell.forTableColumn());
		nameProperty.setOnEditCommit((t)-> {
		            ((SimpleAnnotation) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).nameProperty().set(t.getNewValue());});

		TableColumn<SimpleAnnotation, Integer> numClipsColumn = 
				new TableColumn<SimpleAnnotation, Integer>("No.\nClips");
		numClipsColumn.setPrefWidth(60);
		numClipsColumn.setCellValueFactory(cellData -> cellData.getValue().numClipsProperty().asObject());

		table.setItems(data);
		table.getColumns().addAll(colorColumn, nameProperty, numClipsColumn);

		//add button
		Button buttonAdd = new Button("Add"); 
		buttonAdd.setOnAction((action)->{
			data.add(new SimpleAnnotation(data.size()+1)); 
			//update the remove menu button
		});
		MenuButton buttonRemove = new MenuButton("Remove"); 
		buttonRemove.setOnAction((action)->{
			
		});	
		
		
		
		HBox buttonHolder = new HBox(); 
		buttonHolder.setSpacing(5);
		buttonHolder.getChildren().addAll(buttonAdd,buttonRemove);
		buttonHolder.setAlignment(Pos.CENTER_RIGHT);

		//the main holder pane 
		VBox vbox = new VBox(); 
		vbox.setSpacing(5);
		vbox.getChildren().addAll(label, button, labelEditAnno, table, buttonHolder);

		return vbox; 		
	}

	/**
	 * Combo box with some colours. 
	 * @return combobox with colours. 
	 */
	private ComboBox<String> createColorComboBox() {

		ComboBox<String> comboBoxColors = new ComboBox<String>(); 
		comboBoxColors.setButtonCell(new SymbolListCell());
		comboBoxColors.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override 
			public ListCell<String> call(ListView<String> p) {
				return new SymbolListCell();
			};
		});
		
		comboBoxColors.getItems().addAll(
				UtilsFX.toRGBCode(Color.ORANGE),
				UtilsFX.toRGBCode(Color.DODGERBLUE),
				UtilsFX.toRGBCode(Color.GOLDENROD),
				UtilsFX.toRGBCode(Color.PURPLE),
				UtilsFX.toRGBCode(Color.SPRINGGREEN),
				UtilsFX.toRGBCode(Color.LIGHTBLUE),
				UtilsFX.toRGBCode(Color.DARKRED),
				UtilsFX.toRGBCode(Color.CYAN),
				UtilsFX.toRGBCode(Color.RED),
				UtilsFX.toRGBCode(Color.MEDIUMVIOLETRED),
				UtilsFX.toRGBCode(Color.GREENYELLOW),
				UtilsFX.toRGBCode(Color.LAWNGREEN)
				); 

		
		comboBoxColors.getSelectionModel().select(	UtilsFX.toRGBCode(Color.ORANGE));

		return comboBoxColors; 

	}
	
	public class SymbolListCell extends ListCell<String> {

		@Override protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (item == null || empty) {
				setGraphic(null);
				setText("");
			} else {
				Circle circle = new Circle(12); 
				circle.setFill(Color.web(item));
				setGraphic(circle);
				setText("");
			}
		}
	};

	@Override
	public String getTitle() {
		return "Annotation";
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub

	}

}
