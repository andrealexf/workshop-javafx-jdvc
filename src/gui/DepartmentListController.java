package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableCollumnId;
	@FXML
	private TableColumn<Department, String> tableCollumnName;
	@FXML
	private Button buttonNew;
	
	private ObservableList<Department> obsList;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();

	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	private void initializeNodes() {

		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id"));//iniciar o comportamento das colunas
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());//tableView acompanhar a altura da janela
		//setFitToHeight não é definido para tableView
	}

	@FXML
	public void onButtonNewAction() {
		System.out.println("botão new");
	}
	
	public void updateTableView() {
		
		if(service == null) {
			
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);//instância o obsList pegando os dados da list de cima 
		tableViewDepartment.setItems(obsList);
	}
}
