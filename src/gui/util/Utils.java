package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		
		return (Stage) ((Node) event.getSource()).getScene().getWindow();//acessa o stage onde o controller que recebeu o event está
		//por ex. clica em um botão, pega o stage do local onde o botão foi apertado
	}
	
	public static Integer tryParseToInt(String id) {
		
		try {
			
			return Integer.parseInt(id);
			
		} catch (NumberFormatException e) {
			
			return null;
		}
	}
}
