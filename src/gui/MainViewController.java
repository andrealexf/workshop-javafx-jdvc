package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}); //uma ação como parâmetro
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		
		loadView("/gui/About.fxml", x ->{});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
			//função parametrizada com um tipo qualquer T
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();//janela a ser aberta
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //getRoot pega o primeiro elemento da View (no caso da main é o ScrollPane)
			
			Node mainMenu = mainVBox.getChildren().get(0);//primeiro filho da Vbox (main menu)
			mainVBox.getChildren().clear();//limpa o que tem no mainVbox
			mainVBox.getChildren().add(mainMenu);//adiciona o que tinha antes
			mainVBox.getChildren().addAll(newVBox.getChildren());//adiciona o conteudo do newVbox
			
			T controller = loader.getController(); //o get controller retorna o tipo de controlador que foi passado no parâmetro
			initializingAction.accept(controller);
			
		} catch (Exception e) {

			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
