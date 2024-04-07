import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
public class App extends Application  {
   

        @Override
        public void start(Stage primaryStage) {
            // Create a button
            // Button button = new Button("Click Me");
            // setUserAgentStylesheet(STYLESHEET_CASPIAN);
            primaryStage.setTitle("MKP");
            


        try {
              Parent root = FXMLLoader.load(getClass().getResource("final.fxml"));
            Scene scene = new Scene(root);
            // scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
            // cupertino-dark.css
            Application.setUserAgentStylesheet("/themes/primer-dark.css");

            
            
            
            primaryStage.setTitle("MKP");
                  primaryStage.setScene(scene);
                  primaryStage.show();
                  
                } catch (Exception e) {
                  System.out.println("Error: " + e.getMessage());
          
                }




    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
