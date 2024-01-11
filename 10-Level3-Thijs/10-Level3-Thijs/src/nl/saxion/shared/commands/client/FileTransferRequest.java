package nl.saxion.shared.commands.client;

public class FileTransferRequest {
    public static final String REQUEST_HEADER = "TRANSFER_FILE_REQ";
    public static final String RESPONSE_HEADER = "TRANSFER_FILE_RESP";

    public final String receiver;
    public final String message;

    public FileTransferRequest(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }
}
