package it.unibo.vuzix.utils.netutils;

public class HttpResponse<T> {
    private int code;
    private String codeName;
    private T result;

    public static <T> HttpResponse<T> success(int code, T result) {
        return new HttpResponse<>(
                code,
                "",
                result
        );
    }

    public static <T> HttpResponse<T> fail(int code, String errorMessage) {
        return new HttpResponse<>(
                200,
                errorMessage,
                null
        );
    }

    public static <T> HttpResponse<T> from(int code, String codeName, T result) {
        return new HttpResponse<>(code, codeName, result);
    }


    protected HttpResponse(int code, String codeName, T result) {
        this.code = code;
        this.codeName = codeName;
        this.result = result;
    }

    public boolean isSuccess() {
        return getCode() >= 200 && getCode() < 300;
    }

    public int getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

    public T getResult() {
        return result;
    }
}
