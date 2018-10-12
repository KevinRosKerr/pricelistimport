
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JavaFXGUI extends Application {
	
	private Desktop desktop = Desktop.getDesktop();
	private static Connection con;
	private Database DBC;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("PriceList Upload");
		final FileChooser filechooser = new FileChooser();
		final Button openButton = new Button("Pick PriceList");
		
		openButton.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						configureFileChooser(filechooser);
						File file = filechooser.showOpenDialog(primaryStage);
						if(file != null) {
							//to-do
							openFile(file);
						}
					}
				});
		
		final GridPane inputGridPane = new GridPane();
		
		GridPane.setConstraints(openButton,0,1);
		inputGridPane.getChildren().add(openButton);
		final Pane rootGroup = new VBox(12);
		rootGroup.getChildren().add(inputGridPane);
		rootGroup.setPadding(new Insets(12,12,12,12));
		primaryStage.setScene(new Scene(rootGroup));
		primaryStage.show();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private static void configureFileChooser(FileChooser filechooser) {
		filechooser.setTitle("Pick PriceList File");
		filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel","*.xlsx"));
	}
	
	private void openFile(File file) {
        try {
        	Excel ExcelFile = new Excel(file.getPath());
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(JavaFXGUI.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }
    }
}
