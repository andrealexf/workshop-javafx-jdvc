package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{

	@FXML
	private Department entity;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button buttonSave;
	@FXML
	private Button buttonCancel;
	
	@FXML
	public void onButtonSaveAction() {
		
		System.out.println("onButtonSaveAction");
	}
	
	@FXML
	public void onButtonCancelAction() {
		
		System.out.println("onButtonCancelAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void setDepartment(Department entity) {
		
		this.entity = entity;
	}
	
	public void updateFormData(Department entity) {
		
		if(entity == null) {
			
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		//txtName.setText(String.valueOf(entity.getName())); - String.valueOf retorna null se não tiver nada
		txtName.setText(entity.getName() == null ? "" : String.valueOf(entity.getName()));//(condition) ? (return if true) : (return if false);
	}
	
}
