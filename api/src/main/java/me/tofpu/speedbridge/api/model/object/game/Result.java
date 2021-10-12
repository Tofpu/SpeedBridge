package me.tofpu.speedbridge.api.model.object.game;

public class Result {
    public static Result of(final TransactionResult result) {
        return new Result(result);
    }

    public static Result of(final TransactionResult result,
            TransactionReason reason) {
        return new Result(result, reason);
    }
    
    private final TransactionResult result;
    private final TransactionReason reason;

    private Result(final TransactionResult result) {
        this(result, TransactionReason.NONE);
    }

    private Result(final TransactionResult result, final TransactionReason reason) {
        this.result = result;
        this.reason = reason;
    }

    public TransactionResult result() {
        return result;
    }

    public TransactionReason reason() {
        return reason;
    }

    enum TransactionResult {
        SUCCESS, FAIL
    }

    enum TransactionReason {
        INVALID_LOBBY,
        INVALID_ISLAND,
        NONE;
    }
}
