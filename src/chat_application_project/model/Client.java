package chat_application_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Client {
    private String name;
    private int port;
    private ServerSocket serverSocket;
    private Socket accept;
    private Socket imgSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private InputStream imgInputStream;
    private OutputStream imgOutputStream;
    private String message = "";

    public Client(int port) {
        this.port = port;
    }

    public void acceptConnection() throws IOException {
        serverSocket = new ServerSocket(port);
        this.accept = serverSocket.accept();
    }

    public void setInputAndOutput() throws IOException {
        this.dataInputStream = new DataInputStream(accept.getInputStream());
        this.dataOutputStream = new DataOutputStream(accept.getOutputStream());
    }

}
