package chat_application_project.controller;

import chat_application_project.model.CRC;
import chat_application_project.model.StufferDeStufferCrcChecker;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client02FormController {
    public ScrollPane msgContext;
    public TextField txtMessage;
    public AnchorPane context = new AnchorPane();

    final int PORT = 60000;
    public Label lblClient;
    public AnchorPane emoji;
    Socket socket;
    Socket imgSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String message = "";
    int i = 10;
    String path = "";
    public static boolean isImageChoose = false;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    File file;
    OutputStream imgOutputStream;
    InputStream imgInputStream;
    public static String name;
    boolean isUsed = false;

    public void initialize() {
        Platform.setImplicitExit(false);
        msgContext.setContent(context);
        msgContext.vvalueProperty().bind(context.heightProperty());
        msgContext.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        msgContext.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lblClient.setText(chat_application_project.controller.LoginForm02Controller.name);
        name = lblClient.getText();

        new Thread(() -> {
            try {
                socket = new Socket("localhost", PORT);

                while (true) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    message = dataInputStream.readUTF();
                    System.out.println(message);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (message.startsWith("/")) {
                                BufferedImage sendImage = null;
                                try {
                                    sendImage = ImageIO.read(new File(message));
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                }
                                Image img = SwingFXUtils.toFXImage(sendImage, null);
                                ImageView imageView = new ImageView(img);
                                imageView.setFitHeight(150);
                                imageView.setFitWidth(150);
                                imageView.setLayoutY(i);
                                context.getChildren().add(imageView);
                                i += 150;
                            } else if (message.startsWith(chat_application_project.controller.LoginForm02Controller.name)) {
                                message = message.replace(chat_application_project.controller.LoginForm02Controller.name, "You");
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #85b6ff; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            } else {
                                Label label = new Label(message);
                                label.setStyle(" -fx-font-family: Ubuntu; -fx-font-size: 20px; -fx-background-color: #CDB4DB; -fx-text-fill: #5c5c5c");
                                label.setLayoutY(i);
                                context.getChildren().add(label);
                            }
                            i += 30;
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void btnSendOnAction(MouseEvent actionEvent) throws IOException {
        if (isImageChoose) {
            dataOutputStream.writeUTF(path.trim());
            dataOutputStream.flush();
            isImageChoose = false;
        } else {
            StufferDeStufferCrcChecker dc = new StufferDeStufferCrcChecker();
            CRC crc = new CRC();
            String msg = dc.binaryToMessage(dc.stuffing(dc.messageToBinary(txtMessage.getText().trim())));
            crc.initializer(dc.messageToBinary(msg));
            dataOutputStream.writeUTF(lblClient.getText() + " : " + msg.trim());
            dataOutputStream.flush();

        }
        txtMessage.clear();
    }

    public void btnImageChooserOnAction(MouseEvent actionEvent) throws IOException {
        // get the file selected
        FileChooser chooser = new FileChooser();
        Stage stage = new Stage();
        file = chooser.showOpenDialog(stage);

        if (file != null) {
//            dataOutputStream.writeUTF(file.getPath());
            path = file.getPath();
            System.out.println("selected");
            System.out.println(file.getPath());
            isImageChoose = true;
        }
    }

    public void btnExitOnAction(MouseEvent actionEvent) throws IOException {
        if (socket != null) {
            dataOutputStream.writeUTF("exit".trim());
            dataOutputStream.flush();
            System.exit(0);
        }
        System.exit(0);
    }

    public void btnEmojiOnAction(MouseEvent mouseEvent) {
        if (isUsed) {
            emoji.getChildren().clear();
            isUsed = false;
            return;
        }
        isUsed = true;
        VBox dialogVbox = new VBox(20);
        ImageView smile = new ImageView(new Image("chat_application_project/assets/smile.png"));
        smile.setFitWidth(30);
        smile.setFitHeight(30);
        dialogVbox.getChildren().add(smile);
        ImageView heart = new ImageView(new Image("chat_application_project/assets/heart.png"));
        heart.setFitWidth(30);
        heart.setFitHeight(30);
        dialogVbox.getChildren().add(heart);
        ImageView sadFace = new ImageView(new Image("chat_application_project/assets/sad-face.png"));
        sadFace.setFitWidth(30);
        sadFace.setFitHeight(30);
        dialogVbox.getChildren().add(sadFace);
        smile.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "☺");
        });
        heart.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "♥");
        });
        sadFace.setOnMouseClicked(event -> {
            txtMessage.setText(txtMessage.getText() + "☹");
        });
        emoji.getChildren().add(dialogVbox);
    }
}
