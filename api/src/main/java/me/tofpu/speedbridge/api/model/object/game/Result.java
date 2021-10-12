package me.tofpu.speedbridge.api.model.object.game;

public class Result {
    public final static Result SUCCESS = of(TransactionResult.SUCCESS);
    public final static Result FAIL = of(TransactionResult.FAIL);
    public final static Result INVALID_LOBBY = of(TransactionResult.FAIL,
            TransactionReason.INVALID_LOBBY);
    public final static Result INVALID_ISLAND = of(TransactionResult.FAIL,
            TransactionReason.INVALID_ISLAND);

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
