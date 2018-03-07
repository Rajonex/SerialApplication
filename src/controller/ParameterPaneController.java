package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ParameterPaneController implements Initializable {

    @FXML
    private TextField kpText;

    @FXML
    private TextField kdText;

    @FXML
    private TextField kiText;

    @FXML
    private Label kpLabel;

    @FXML
    private Label kdLabel;

    @FXML
    private Label kiLabel;

    @FXML
    private Button sendButton;

    public TextField getKpText()
	{
		return kpText;
	}

	public TextField getKdText()
	{
		return kdText;
	}

	public TextField getKiText()
	{
		return kiText;
	}

	public Label getKpLabel()
	{
		return kpLabel;
	}

	public Label getKdLabel()
	{
		return kdLabel;
	}

	public Label getKiLabel()
	{
		return kiLabel;
	}

	public Button getSendButton()
	{
		return sendButton;
	}

	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
    	
    }
}
