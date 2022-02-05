package org.myorg.modules.modules.exception;

import java.util.HashMap;

public class ModuleException extends Exception {

    private String code;
    private String message;

    private static String convertParamsToString(HashMap<String, Object> params) {
        StringBuilder stringParams = new StringBuilder("params: {");
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
        return "ModuleException{" +
                "code='" + code + '\'' +
                ", message='" + (message != null ? message : "") + '\'' +
                '}';
    }
}
