package pl.rezerwacje.serwer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Serwer {
    public static void main(String[] args) throws IOException, SQLException {

        ServerSocket ss = new ServerSocket(5056);

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        Connection conn = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moja", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        while (true)
        {
            Socket s = null;
            try
            {
                s = ss.accept();
                System.out.println("A new client is connected : " + s);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                Thread t = new ClientHandler(s,dos,dis, conn,rs);
                t.start();
            }
            catch (Exception e)
            {
                s.close();
                conn.close();
                e.printStackTrace();
            }
        }
    }
}
