package tech.never.more.xmore.core.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Response implements Serializable {
    private Status status = Status.SUCCESS;
    private String code = StringUtils.EMPTY;
    private String message = StringUtils.EMPTY;
    private Map<String, Object> content = new HashMap<>();


    public static enum Status {
        SUCCESS("success"), ERROR("error");

        private String status;
        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }

    public Response put(String key, Object value) {
        this.getContent().put(key, value);
        return this;
    }

    public Response and(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Response message(String message) {
        this.setMessage(message);
        return this;
    }

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Response success() {
        return success(StringUtils.EMPTY);
    }

    public static Response error() {
        return error(StringUtils.EMPTY);
    }

    public static Response success(String message) {
        return new Response(Status.SUCCESS, message);
    }

    public static Response error(String message) {
        return new Response(Status.ERROR, message);
    }
}
