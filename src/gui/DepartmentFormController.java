package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	@FXML
	private Department entity;
	@FXML
	private DepartmentService service;
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
	public void onButtonSaveAction(ActionEvent event) {

		if (entity == null) {

			throw new IllegalStateException("Entity was null");
		}

		if (entity == null) {

			throw new IllegalStateException("Service was null");
		}
		
		try {
			
			entity = getFormData();// esse metodo pega o texto e o id inserido e coloca num obj department que vira o entity
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
			
		} catch (DbException e) {
			
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onButtonCancelAction(ActionEvent event) {

		Utils.currentStage(event).close();
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

		if (entity == null) {

			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		// txtName.setText(String.valueOf(entity.getName())); - String.valueOf retorna null se não tiver nada
		txtName.setText(entity.getName() == null ? "" : String.valueOf(entity.getName()));
		// (condition) ? (return if true) : (return if false);
	}

	public void setDepartmentService(DepartmentService service) {

		this.service = service;
	}

	public Department getFormData() {

		Department obj = new Department();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());

		return obj;
	}
}
