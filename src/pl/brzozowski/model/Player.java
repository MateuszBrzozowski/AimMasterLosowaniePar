package pl.brzozowski.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {

    private StringProperty nick =  new SimpleStringProperty();
    private FloatProperty kd = new SimpleFloatProperty();

    public Player(String nick, float kd) {
        StringProperty nickPro = new SimpleStringProperty(nick);
        this.nick = nickPro;
        FloatProperty kdPro = new SimpleFloatProperty(kd);
        this.kd = kdPro;
    }

    public Player() {
    }

    public String getNick() {
        return nick.get();
    }

    public StringProperty nickProperty() {
        return nick;
    }

    public float getKd() {
        return kd.get();
    }

    public FloatProperty kdProperty() {
        return kd;
    }

    public void setNick(String nick) {
        this.nick.set(nick);
    }

    public void setKd(float kd) {
        this.kd.set(kd);
    }
}
