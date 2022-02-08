package pl.rezerwacje;

public class User {

    public static String nick = null;
    public static int id = 0;

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        User.nick = nick;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static void logout() {
        setNick(null);
        setId(0);
    }
}
