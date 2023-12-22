import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerInitializer extends Application {


    void m() {
        System.out.println("oh");
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("chat_application_project/view/server-form.fxml"))));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

}
