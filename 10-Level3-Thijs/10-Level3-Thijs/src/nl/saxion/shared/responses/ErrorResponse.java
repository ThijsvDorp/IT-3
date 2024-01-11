package nl.saxion.shared.responses;

public class ErrorResponse extends Response{
    public ErrorResponse(Integer code) {
        super("ERROR", code);
    }
}
