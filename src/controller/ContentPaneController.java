package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ContentPaneController implements Initializable {

    @FXML
    private TextArea textArea;
    
    public TextArea getTextArea()
	{
		return textArea;
	}

	@Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

}
