package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MainPaneController implements Initializable
{
	@FXML
	private ContentPaneController contentPaneController;

	@FXML
	private ControlPaneController controlPaneController;

	@FXML
	private ParameterPaneController parameterPaneController;

	private SerialPort serialPort;
	private BufferedReader input;
	private BufferedWriter output;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		configureButtons();
		configureSend();
	}

	/**
	 * Funkcja odpowiedzialna za obslugiwanie zdarzen zwiazanych z czescia
	 * widoku "ParameterPane", czyli miejsca wprowadzania parametrow do
	 * wyslania. Wykorzystywany jest tutaj BufferedWriter, deklarowany w
	 * serialConnect(). Wysylane sa dane wprowadzone w pola kd, kp i ki.
	 */
	private void configureSend()
	{
		Button sendButton = parameterPaneController.getSendButton();
		TextField kpText = parameterPaneController.getKpText();
		TextField kdText = parameterPaneController.getKdText();
		TextField kiText = parameterPaneController.getKiText();

		sendButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				try
				{
					output.write(
							"kp=" + kpText.getText() + ";kd=" + kdText.getText() + ";ki=" + kiText.getText() + ";");
					output.flush();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Konfiguracja zadan przyciskow i innych nalezacych do "ControlPane". W
	 * przypadku ComboBox-a, po nacisnieciu na niego, aktualizowana jest lista
	 * COM-ow, z ktorymi mozliwe jest polaczenie aktualnie. Dla przycisku
	 * "Polacz" nawiazywane jest polaczenie z wybranym na ComboBox-ie COM-em i
	 * na stale wpisana wartosc 9600 baudRate, czyli czestotliwoscia komunikacji.
	 * W przypadku przycisku "Rozlacz" wywolywana jest metoda serialDisconnect().
	 */

	private void configureButtons()
	{
		ComboBox<String> comboBox = controlPaneController.getComboBox();
		Button connectButton = controlPaneController.getConnectButton();
		Button disconnectButton = controlPaneController.getDisconnectButton();

		comboBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				Enumeration enumComm;
				CommPortIdentifier serialPortx;
				enumComm = CommPortIdentifier.getPortIdentifiers();
				ObservableList<String> list = FXCollections.observableArrayList();
				while (enumComm.hasMoreElements())
				{
					serialPortx = (CommPortIdentifier) enumComm.nextElement();
					if (serialPortx.getPortType() == CommPortIdentifier.PORT_SERIAL)
					{
						list.add(serialPortx.getName());
					}
				}
				comboBox.setItems(list);
			}
		});

		connectButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				serialConnect(comboBox.getSelectionModel().getSelectedItem().toString(), 9600);
			}
		});

		disconnectButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				serialDisconnect();
			}
		});

	}

	/**
	 * Funkcja odpowiedzialna za rozlaczenie obecnie wystepujacego polaczenia
	 * szeregowego.
	 */

	private void serialDisconnect()
	{
		if (serialPort != null)
		{
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Funkcja ma za zadanie nawiazanie polaczenia o zadanych parametrach. Nie
	 * wyroznione parametry jako dane wejsciowe zostaly zadeklarowane w funkcji
	 * setSetialPortParams(). W tej funkcji rowniez deklarowane sa "obudowane"
	 * strumienie, ktore nastepnie sa wykorzystywane do odbierania jak i
	 * wysylania danych. Nastepnie deklarowane jest zachowanie aplikacji jesli
	 * pojawia sie nowe dane. Sa one dopisywane w postaci tekstowej do juz
	 * wystepujacych w polu TextArea.
	 *
	 *
	 * @param com
	 *            Podawany numer com-u, z ktorym ma zostac nawiazane polaczenie
	 * @param baudRate
	 *            Podawana czestotliwosc komunikacji
	 */

	private void serialConnect(String com, int baudRate)
	{
		CommPortIdentifier serialPortId;
		try
		{
			serialPortId = CommPortIdentifier.getPortIdentifier(com);
			serialPort = (SerialPort) serialPortId.open(this.getClass().getName(), 2000);
			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));
			serialPort.addEventListener(new SerialPortEventListener()
			{
				@Override
				public void serialEvent(SerialPortEvent arg0)
				{
					if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE)
					{
						try
						{
							TextArea textArea = contentPaneController.getTextArea();
							textArea.setText(textArea.getText() + "\n" + input.readLine());
						} catch (Exception e)
						{
							System.out.println("Blad wczytania odebranego tekstu");
							System.err.println(e.toString());
						}
					}
				}
			});
			serialPort.notifyOnDataAvailable(true);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
