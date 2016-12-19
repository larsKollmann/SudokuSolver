package de.fhaachen.swegrp2.models;

/** <p><b>Titel:</b> ExceptionSuide</p>
 * <p><b>Beschreibung:</b> Sammelt alle für das Projekt erstellen Exceptions in einer zentralen public-Klasse.</p>
 */
public class ExceptionSuite {

    public static class SizeNotSupportedException extends Exception {
        public SizeNotSupportedException(String err) {
            super(err);
        }
    }

    public static class FaultyFormatException extends Exception {
        public FaultyFormatException (String err) {
            super(err);
        }
    }

    public static class EmptyArrayException extends Exception {
        public EmptyArrayException(String err) {
            super(err);
        }
    }
}
