package de.fhaachen.swegrp2.models;

/**
 * Die ExceptionSuite sammelt alle für das Projekt erstellen Exceptions in einer zentralen public-Klasse
 * Created by simon on 30.11.2016.
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
