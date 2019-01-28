package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.annotation.SimpleAnnotation;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
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
 * Note that due the flexible nature of annotations this only supports SimpleAnnotions and 
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

		Button button = new Button("Export Clips..."); 
		button.setTooltip(new Tooltip("Exports annotated clips to folders named with the annotation group name"));
		button.setOnAction((action) -> aiPamView.exportAnnotations());

		Label labelEditAnno = new Label("Edit Annotations");
		labelEditAnno.getStyleClass().add("label-title2");

		//create the table
		table = new TableView<SimpleAnnotation>(); 
		table.setEditable(true);
		table.setItems(aiPamView.getAnnotationsManager().getAnnotationsList());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //stop the annoying extra column

		//first column of the table allows users to pick the colour. 
		TableColumn<SimpleAnnotation, String> colorColumn = new TableColumn<>("Symbol");
		//colorColumn.setMinWidth(110);
		colorColumn.setStyle( "-fx-padding: 0em 0em 0em 0em;");
		colorColumn.setEditable(true);

		//		ObservableList<String> colourList = FXCollections.observableArrayList();
		//		colourList.addAll(
		//		UtilsFX.toRGBCode(Color.ORANGE),
		//		UtilsFX.toRGBCode(Color.DODGERBLUE),
		//		UtilsFX.toRGBCode(Color.GOLDENROD),
		//		UtilsFX.toRGBCode(Color.PURPLE),
		//		UtilsFX.toRGBCode(Color.SPRINGGREEN),
		//		UtilsFX.toRGBCode(Color.LIGHTBLUE),
		//		UtilsFX.toRGBCode(Color.DARKRED),
		//		UtilsFX.toRGBCode(Color.CYAN),
		//		UtilsFX.toRGBCode(Color.RED),
		//		UtilsFX.toRGBCode(Color.MEDIUMVIOLETRED),
		//		UtilsFX.toRGBCode(Color.GREENYELLOW),
		//		UtilsFX.toRGBCode(Color.LAWNGREEN));
		//		
		//		colorColumn.setCellFactory(param -> {
		//			GraphicBoxTableCell<SimpleAnnotation, String> comboBoxTableCell = new GraphicBoxTableCell<>(colourList);
		//			comboBoxTableCell.setListCell(new SymbolListCell()); 
		//			
		//			//comboBoxTableCell.setPickOnBounds(true);
		//			//comboBoxTableCell.updateSelected(true);
		//			
		//			return comboBoxTableCell;
		//		});
		//		colorColumn.setCellValueFactory(v -> v.getValue().colorProperty);
		//		
		//		colorColumn.setOnEditCommit((t)-> {
		//			System.out.println("I'm committing a colour thing..."+ t.getNewValue());
		//			SimpleAnnotation annot = (SimpleAnnotation) t.getTableView().getItems().get(t.getTablePosition().getRow()); 
		//			annot.colorProperty.setValue(t.getNewValue());
		//		});


		colorColumn.setCellFactory(col -> {
			TableCell<SimpleAnnotation, String> c = new TableCell<SimpleAnnotation, String>();

			final ComboBox<String> comboBox = createColorComboBox();

			c.itemProperty().addListener((observable, oldValue, newValue) -> {
				//System.out.println("New value: ");
				if (oldValue != null) {
					comboBox.valueProperty().unbindBidirectional(new SimpleStringProperty(oldValue));
				}
				if (newValue != null) {
					comboBox.valueProperty().bindBidirectional(new SimpleStringProperty(newValue));
				}

			});

			comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
				//this a hack. When the combo box is selected the mouse action doe snot 
				//pass to the table cell, which requires a double click anyway. So need to 
				//make sure the cell is in edit mode otherwise no editsa will be committed and no chnages passed 
				//to the menus. 
				//table.requestFocus();
				if (c.getTableRow()!=null) {
					table.getSelectionModel().select(c.getTableRow().getIndex(), colorColumn);
					table.getFocusModel().focus(c.getTableRow().getIndex(), colorColumn); 
					table.edit(c.getTableRow().getIndex(), colorColumn);
					c.requestFocus();
				}

				if (c.isEditing()) {
					//System.out.println("Hello edit: ");
					c.commitEdit(newValue);
				}
			});

			c.graphicProperty().bind(Bindings.when(c.emptyProperty()).
					then((Node) null).otherwise(comboBox));

			return c;
		});

		colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty );

		colorColumn.setOnEditCommit((t)-> {
			//System.out.println("I'm committing a colour thing..."+ t.getNewValue());
			SimpleAnnotation annot = (SimpleAnnotation) t.getTableView().getItems().get(t.getTablePosition().getRow()); 
			annot.colorProperty.setValue(t.getNewValue());
		});


		TableColumn<SimpleAnnotation, String> nameColumn =
				new TableColumn<SimpleAnnotation, String> ("Name");
		nameColumn.setEditable(true);
		//nameProperty.setMinWidth(110);
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit((t)-> {
			((SimpleAnnotation) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
					).nameProperty().set(t.getNewValue());});

		TableColumn<SimpleAnnotation, Integer> numClipsColumn = 
				new TableColumn<SimpleAnnotation, Integer>("No.\nClips");
		numClipsColumn.setPrefWidth(60);
		numClipsColumn.setCellValueFactory(cellData -> cellData.getValue().numClipsProperty().asObject());

		table.getColumns().addAll(colorColumn, nameColumn, numClipsColumn);
		
		
		//remove items menu item
		MenuButton buttonRemove = new MenuButton("Remove"); 

		//add button
		Button buttonAdd = new Button("Add"); 
		buttonAdd.setOnAction((action)->{
			aiPamView.getAnnotationsManager().add(
					new SimpleAnnotation(aiPamView.getAnnotationsManager().getNAnnotations()+1
							, UtilsFX.toRGBCode(Color.LAWNGREEN))); 
			
			//update the remove menu button
			populateRemoveMenu(buttonRemove);
		});
		buttonAdd.prefHeightProperty().bind(buttonRemove.heightProperty());

		//make sure populated on start. 
		populateRemoveMenu(buttonRemove);

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
	 * Populates the remove button menu
	 */
	private void populateRemoveMenu(MenuButton buttonRemove) {
		//update the remove menu button
		buttonRemove.getItems().clear();
		MenuItem item; 
		for (int i=0; i<table.getItems().size(); i++) {
			item= new MenuItem(table.getItems().get(i).getAnnotationGroupName()); 
			final int ii=i; 
			item.setOnAction((action1)->{
				//remove pam clips so they have null annotation reference
				table.getItems().get(ii).clearClips(); 
				//remove from list. 
				table.getItems().remove(ii);
				populateRemoveMenu(buttonRemove);
			});
			buttonRemove.getItems().add(item); 
		}
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


		comboBoxColors.getSelectionModel().select(UtilsFX.toRGBCode(Color.ORANGE));

		return comboBoxColors; 

	}

	public class SymbolListCell extends ListCell<String> {

		@Override protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null || empty) {
				//.out.println("Hello: " + item);
				setGraphic(null);
				setText("");
			} else {
				//System.out.println("Hello: " + item);
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
