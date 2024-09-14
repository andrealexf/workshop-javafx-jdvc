package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListeners;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListeners{
	
	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	@FXML
	private TableColumn<Seller, Seller> tableColumnEdit;
	@FXML
	private TableColumn<Seller, Seller> tableColumnRemove;
	@FXML
	private Button buttonNew;
	
	private ObservableList<Seller> obsList;
	
	private void initEditButtons() {
		
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	private void removeEntity(Seller obj) {
		
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		//para acessar o objeto dentro do optional precisa do .get
		if(result.get() == ButtonType.OK) {
			
			if(service == null) {
				
				throw new IllegalStateException("Service was null");
			}
			
			try {
				
				service.remove(obj);
				updateTableView();
				
			} catch (DbIntegrityException e) {
				
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
		
	}

	public void onDataChanged() {
		
		updateTableView();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();

	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));//iniciar o comportamento das colunas
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());//tableView acompanhar a altura da janela
		//setFitToHeight não é definido para tableView
	}

	@FXML
	public void onButtonNewAction(ActionEvent event) {
		
		Stage parentStage = Utils.currentStage(event);//pega a referência do stage atual e passa para o createDialog
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
		
	}
	
	public void updateTableView() {
		
		if(service == null) {
			
			throw new IllegalStateException("Service was null");
		}
		
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);//instância o obsList pegando os dados da list de cima 
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		
//		try {
//			
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			
//			SellerFormController controller = loader.getController();
//			controller.setSellerService(new SellerService());
//			controller.setSeller(obj);
//			controller.updateFormData(obj);//carregar o obj no formulário
//			controller.subscribeDataChangeListener(this);//mando a propria classe (SellerListController)
//			
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller Data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);//janela não pode ser redimensionada
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);//vai ficar travada, enqunato não for acessada, não pode prosseguir
//			dialogStage.showAndWait();
//			
//		} catch(IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
	}
	
}
