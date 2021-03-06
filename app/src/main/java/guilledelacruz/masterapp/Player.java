package guilledelacruz.masterapp;

/**
 * Created by guilledelacruz
 */
public class Player {

    private String nickname;
    private String ip;

    public Player (String nick, String ip){
        nickname = nick;
        this.ip = ip;
    }

    public String getNickname(){
        return nickname;
    }
    public String getIP(){
        return ip;
    }
    public String toString(){
        return "[ "+nickname+" // "+ip+" ]";
    }
}
