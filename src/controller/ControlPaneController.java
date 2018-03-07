package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ControlPaneController implements Initializable
{

//	@FXML
//	private Button refreshButton;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private Button connectButton;

	@FXML
	private Button disconnectButton;

//	public Button getRefreshButton()
//	{
//		return refreshButton;
//	}

	public ComboBox<String> getComboBox()
	{
		return comboBox;
	}

	public Button getConnectButton()
	{
		return connectButton;
	}

	public Button getDisconnectButton()
	{
		return disconnectButton;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

	}
}
