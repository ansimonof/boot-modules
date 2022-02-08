package org.myorg.modules.modules.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;

@JsonIgnoreProperties(value = {"stackTrace", "cause", "localizedMessage", "suppressed"})
@Getter
@EqualsAndHashCode(callSuper = false, of = {"code", "message"})
public class ModuleException extends Exception {

    private String code;
    private String message;

    private static String convertParamsToString(HashMap<String, Object> params) {
        StringBuilder stringParams = new StringBuilder("parameters: {");
        for (String key : params.keySet()) {
            String value = params.get(key).toString();
            stringParams.append(key).append("=").append(value).append("; ");
        }
        stringParams.append("}");
        return stringParams.toString();
    }

    public ModuleException(String code, String message, HashMap<String, Object> params) {
        this(code, message + ", " + convertParamsToString(params));
    }

    public ModuleException(String code, HashMap<String, Object> params) {
        this(code, convertParamsToString(params));
    }

    public ModuleException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ModuleException(String code) {
        this(code, (String) null);
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        if (!StringUtils.isEmpty(message)) {
            json.put("message", message);
        }
        return json.toString();
    }
}
