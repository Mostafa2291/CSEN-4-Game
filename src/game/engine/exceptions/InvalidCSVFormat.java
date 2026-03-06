package game.engine.exceptions;

public class InvalidCSVFormat extends GameActionException {
    private static final String MSG="Invalid input detected while reading csv file, input = \n";
    private String inputLine;
    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public String getInputLine() {
        return inputLine;
    }

    public InvalidCSVFormat(String inputLine) {
        super(MSG + inputLine);
    }

    public InvalidCSVFormat(String message, String inputLine) {
        super(message + inputLine);
    }

}
