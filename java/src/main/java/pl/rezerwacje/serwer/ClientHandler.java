package pl.rezerwacje.serwer;

import pl.rezerwacje.Main;
import pl.rezerwacje.Password;
import pl.rezerwacje.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

class ClientHandler extends Thread
{
    final Socket s;
    final DataOutputStream dos;
    final DataInputStream dis;

    Connection con = null;
    ResultSet rs;

    public ClientHandler(Socket s, DataOutputStream dos, DataInputStream dis, Connection con, ResultSet rs)
    {
        this.dos = dos;
        this.s = s;
        this.dis = dis;
        this.con = con;
        this.rs = rs;
    }

    @Override
    public void run()
    {
        String received;

        while (true)
        {
            try
            {
                received = dis.readUTF();

                if(received.equals("Login"))
                {
                    String nick = dis.readUTF();

                    String sql_query = "SELECT * FROM `users` WHERE nick=?";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, nick);
                    rs = pstr.executeQuery();
                    String password2 = null;
                    int id = 0;

                    if(rs.next()) {
                        dos.writeBoolean(true);
                        String password = dis.readUTF();
                        sql_query = "SELECT `id`, `password` FROM `users` WHERE nick=?";
                        pstr = con.prepareStatement(sql_query);
                        pstr.setString(1, nick);
                        rs = pstr.executeQuery();

                        while(rs.next()) {
                            id = rs.getInt(1);
                            password2 = rs.getString(2);
                        }

                        if(Password.check(password, password2)) {
                            dos.writeBoolean(true);
                            dos.writeUTF(String.valueOf(id));
                            dos.writeUTF(nick);

                        } else {
                            dos.writeBoolean(false);
                        }
                    } else {
                        dos.writeBoolean(false);
                    }

                }

                if(received.equals("Register"))
                {

                    String nick = dis.readUTF();

                    String sql_query = "SELECT * FROM `users` WHERE nick=?";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, nick);
                    rs = pstr.executeQuery();

                    if(!rs.next())
                    {
                        sql_query = "INSERT INTO `users` (`id`, `first_name`, `last_name`, `nick`, `password`, `email`,  `or_admin`) VALUES (NULL, ?, ?, ?, ?, ?, '0');";
                        pstr = con.prepareStatement(sql_query);
                        pstr.setString(1,dis.readUTF());
                        pstr.setString(2,dis.readUTF());
                        pstr.setString(3,nick);
                        pstr.setString(4,Password.getSaltedHash(dis.readUTF()));
                        pstr.setString(5,dis.readUTF());

                        pstr.execute();

                        dos.writeBoolean(true);
                    }
                    else
                    {
                        dos.writeBoolean(false);
                    }
                }

                if(received.equals("Add reservation")) {
                    String dateFrom = dis.readUTF();
                    String dateTo = dis.readUTF();
                    String nameRoom = dis.readUTF();
                    String nameType = dis.readUTF();
                    int userId = dis.readInt();
                    int idRoom = 0;
                    int idType = 0;

                    String sql_query = "SELECT `id` FROM `room` WHERE `name` =?";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, nameRoom);
                    rs = pstr.executeQuery();

                    while(rs.next()) {
                        idRoom = rs.getInt(1);
                    }

                    sql_query = "SELECT `id` FROM `type_of_reservation` WHERE `name`=?";
                    pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, nameType);
                    rs = pstr.executeQuery();

                    while(rs.next()) {
                        idType = rs.getInt(1);
                    }

                    sql_query = "SELECT * FROM `reservation` WHERE `room_id`=? and (? BETWEEN `date_from`and `date_to` or ? BETWEEN `date_from`and `date_to`)";
                    pstr = con.prepareStatement(sql_query);

                    pstr.setString(1, String.valueOf(idRoom));
                    pstr.setString(2, dateFrom);
                    pstr.setString(3, dateTo);

                    rs = pstr.executeQuery();

                    int freeTime = 0;

                    while(rs.next()) {
                        freeTime++;
                    }

                    if(freeTime != 0) {
                        dos.writeUTF("Bad");
                    } else {
                        sql_query = "INSERT INTO `reservation`(`id`, `date_from`, `date_to`, `user_id`, `room_id`, `type_of_reservation_id`) VALUES (NULL, ?, ?, ?, ?, ?);";
                        pstr = con.prepareStatement(sql_query);
                        pstr.setString(1, dateFrom);
                        pstr.setString(2, dateTo);
                        pstr.setInt(3, userId);
                        pstr.setString(4, String.valueOf(idRoom));
                        pstr.setString(5, String.valueOf(idType));

                        pstr.execute();

                        sql_query = "INSERT INTO `archives`(`id`, `date_from`, `date_to`, `user_id`, `room_id`, `type_of_reservation_id`) VALUES (NULL, ?, ?, ?, ?, ?);";
                        pstr = con.prepareStatement(sql_query);
                        pstr.setString(1, dateFrom);
                        pstr.setString(2, dateTo);
                        pstr.setInt(3, userId);
                        pstr.setString(4, String.valueOf(idRoom));
                        pstr.setString(5, String.valueOf(idType));

                        pstr.execute();

                        dos.writeUTF("Good");
                    }

                }

                if(received.equals("Show reservation")) {
                    String sql_query = "SELECT DISTINCT `date_from`,`date_to`,users.first_name, users.last_name,room.name,type_of_reservation.name FROM" +
                            "`reservation` LEFT JOIN users ON reservation.user_id = users.id LEFT JOIN room ON reservation.room_id = room.id " +
                            "LEFT JOIN type_of_reservation ON reservation.type_of_reservation_id = type_of_reservation.id ";
                    PreparedStatement pstr = con.prepareStatement(sql_query);

                    rs = pstr.executeQuery();

                    String allReservation = "";

                    while(rs.next()) {
                        allReservation += rs.getString(1)+"    "+rs.getString(2)+"    "+rs.getString(3)+"    "+rs.getString(4)+"    "+rs.getString(5)+"    "+rs.getString(6)+"!";
                    }
                    dos.writeUTF(allReservation);
                }

                if(received.equals("Show archive")) {
                    String sql_query = "SELECT DISTINCT `date_from`,`date_to`,users.first_name, users.last_name,room.name,type_of_reservation.name FROM" +
                            "`archives` LEFT JOIN users ON archives.user_id = users.id LEFT JOIN room ON archives.room_id = room.id " +
                            "LEFT JOIN type_of_reservation ON archives.type_of_reservation_id = type_of_reservation.id ";
                    PreparedStatement pstr = con.prepareStatement(sql_query);

                    rs = pstr.executeQuery();

                    String allArchive = "";

                    while(rs.next()) {
                        allArchive += rs.getString(1)+"    "+rs.getString(2)+"    "+rs.getString(3)+"    "+rs.getString(4)+"    "+rs.getString(5)+"    "+rs.getString(6)+"!";
                    }
                    dos.writeUTF(allArchive);
                }

                if(received.equals("Exit")) // bezpieczne zakonczenie polaczenia
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                if(received.equals("Add room"))
                {
                    String sql_query = "INSERT INTO `room` (`id`, `name`, `number_of_place`) VALUES (NULL, ?, ?);";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1,dis.readUTF());
                    pstr.setString(2,dis.readUTF());

                    pstr.execute();
                }

                if(received.equals("Load room")) {
                    String query = "SELECT `name` FROM `room`";
                    PreparedStatement pstr = con.prepareStatement(query);
                    rs = pstr.executeQuery(query);

                    while (rs.next()) {
                        dos.writeBoolean(true);
                        dos.writeUTF(rs.getString(1));
                    }
                    dos.writeBoolean(false);
                }

                if(received.equals("Add type"))
                {
                    String sql_query = "INSERT INTO `type_of_reservation` (`id`, `name`) VALUES (NULL, ?);";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1,dis.readUTF());

                    pstr.execute();
                }

                if(received.equals("Load type")) {
                    String query = "SELECT `name` FROM `type_of_reservation`";
                    PreparedStatement pstr = con.prepareStatement(query);
                    rs = pstr.executeQuery(query);

                    while (rs.next()) {
                        dos.writeBoolean(true);
                        dos.writeUTF(rs.getString(1));
                    }
                    dos.writeBoolean(false);
                }

                if(received.equals("Load reservation")) {
                    String query = "SELECT `date_from`, room.name, type_of_reservation.name FROM `reservation` LEFT JOIN room ON " +
                            "reservation.room_id = room.id LEFT JOIN type_of_reservation ON reservation.type_of_reservation_id = type_of_reservation.id ";
                    PreparedStatement pstr = con.prepareStatement(query);
                    rs = pstr.executeQuery(query);

                    while (rs.next()) {
                        dos.writeBoolean(true);
                        dos.writeUTF(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                    }
                    dos.writeBoolean(false);
                }

                if(received.equals("My reservation")) {
                    String query = "SELECT `date_from`, room.name, type_of_reservation.name FROM `reservation` LEFT JOIN room ON reservation.room_id = room.id " +
                            "LEFT JOIN type_of_reservation ON reservation.type_of_reservation_id = type_of_reservation.id WHERE reservation.user_id = ?";
                    PreparedStatement pstr = con.prepareStatement(query);
                    pstr.setInt(1, dis.readInt());
                    rs = pstr.executeQuery();

                    while (rs.next()) {
                        dos.writeBoolean(true);
                        dos.writeUTF(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                    }
                    dos.writeBoolean(false);
                }

                if(received.equals("Delete reservation")) {
                    String sql_query = "DELETE FROM `reservation` WHERE `date_from` =?";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, dis.readUTF());
                    pstr.execute();
                }

                if(received.equals("Text")) {
                    String query = "SELECT `first_name` FROM `users` WHERE id = ?";
                    PreparedStatement pstr = con.prepareStatement(query);
                    pstr.setString(1, dis.readUTF());
                    rs = pstr.executeQuery();

                    while (rs.next()) {
                        dos.writeBoolean(true);
                        dos.writeUTF(rs.getString(1));
                    }
                }

                if(received.equals("New password")) {
                    String sql_query = "UPDATE `users` SET `password`=? WHERE `nick`=?";
                    PreparedStatement pstr = con.prepareStatement(sql_query);
                    pstr.setString(1, Password.getSaltedHash(dis.readUTF()));
                    pstr.setString(2, dis.readUTF());
                    pstr.execute();
                }

            }
            catch (IOException | SQLException e)
            {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try
        {
            this.dis.close();
            this.dos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
