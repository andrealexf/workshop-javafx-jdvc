package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entites.Department;

public class DeparmentListController implements Initializable{

	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableCollumnId;
	@FXML
	private TableColumn<Department, String> tableCollumnName;
	@FXML
	private Button buttonNew;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();

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
}
