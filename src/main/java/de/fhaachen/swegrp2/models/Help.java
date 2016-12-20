package de.fhaachen.swegrp2.models;

/**
 * Created by lars on 19.12.16.
 */
public class Help {

    public String titel;
    public String text;

    public Help(String titel, String text) {
        this.text = text;
        this.titel = titel;
    }

    public String toString() {
        return titel;
    }
}
