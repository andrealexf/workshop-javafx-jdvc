package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListeners;
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
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	@FXML
	private Seller entity;
	@FXML
	private SellerService service;
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

	private List<DataChangeListeners> dataChangeListeners = new ArrayList<>();
	
	public void subscribeDataChangeListener(DataChangeListeners listener) {
		
		dataChangeListeners.add(listener);
	}

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
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		} catch (ValidationException e) {
			
			setErrorMessages(e.getErrors());
			
		} catch (DbException e) {
			
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
			
		}
	}

	private void notifyDataChangeListeners() {

		for (DataChangeListeners listerner : dataChangeListeners) {
			
			listerner.onDataChanged();//executa o metodo em cada listerner
			//emite o evento para a outra classe (departmentListController) e esta executa o updateView
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

	public void setSeller(Seller entity) {

		this.entity = entity;
	}

	public void updateFormData(Seller entity) {

		if (entity == null) {

			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		// txtName.setText(String.valueOf(entity.getName())); - String.valueOf retorna null se não tiver nada
		txtName.setText((entity.getName() == null) ? ("") : (String.valueOf(entity.getName())));
		// (condition) ? (return if true) : (return if false);
	}

	public void setSellerService(SellerService service) {

		this.service = service;
	}

	public Seller getFormData() {

		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			
			exception.addError("name", "Field can't be empty");
		}

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0) { //verificando se na exceção existe algum erro
			
			throw exception;
		}

		return obj;
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			
			labelErrorName.setText(errors.get("name"));
		}
	}
	
}
