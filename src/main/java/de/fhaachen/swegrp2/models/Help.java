package de.fhaachen.swegrp2.models;

/**
 * <p><b>Titel:</b>Help</p>
 * <p><b>Beschreibung:</b>Stellt die Datenstruktur der Hilfeeinträge dar.</p>
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
