package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListeners;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	private SellerService service;
	private DepartmentService departmentService;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button buttonSave;
	@FXML
	private Button buttonCancel;

	private List<DataChangeListeners> dataChangeListeners = new ArrayList<>();
	
	private ObservableList<Department> obsList;
	
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
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void setSeller(Seller entity) {

		this.entity = entity;
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {

		this.service = service;
		this.departmentService = departmentService;
	}

	public void updateFormData(Seller entity) {

		if (entity == null) {

			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		// txtName.setText(String.valueOf(entity.getName())); - String.valueOf retorna null se não tiver nada
		txtName.setText((entity.getName() == null) ? ("") : (String.valueOf(entity.getName())));
		txtEmail.setText((entity.getEmail() == null) ? ("") : (String.valueOf(entity.getEmail())));
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		
		if(entity.getBirthDate() != null) {
			
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));//Zone pega o fuso do computador sendo utilizado e o instante passa para instante a data
		}
		
		if(entity.getDepartment() == null) {
			
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		comboBoxDepartment.setValue(entity.getDepartment());
		
	}

	public void loadAssociatedObjects() {
		
		if(departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
	
	public Seller getFormData() {

		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation Error");
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			
			exception.addError("name", "Field can't be empty");
		}
				
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {

			exception.addError("email", "Field can't be empty");
		}
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {

			exception.addError("baseSalary", "Field can't be empty");
		}

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		obj.setEmail(txtEmail.getText());
		
		if (dpBirthDate.getValue() == null) {

			exception.addError("birthDate", "Field can't be empty");
			
		} else {
			
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		obj.setDepartment(comboBoxDepartment.getValue());

		
		if(exception.getErrors().size() > 0) { //verificando se na exceção existe algum erro
			
			throw exception;
		}

		return obj;
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name")) ? (errors.get("name")) : (""));
		labelErrorEmail.setText((fields.contains("email")) ? (errors.get("email")) : (""));
		labelErrorBirthDate.setText((fields.contains("birthDate")) ? (errors.get("birthDate")) : (""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary")) ? (errors.get("baseSalary")) : (""));
		
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
