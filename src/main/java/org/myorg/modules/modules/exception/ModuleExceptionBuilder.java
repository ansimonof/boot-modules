package org.myorg.modules.modules.exception;

import org.myorg.modules.modules.database.DomainObject;

import java.io.Serializable;
import java.util.HashMap;

public class ModuleExceptionBuilder {

    public static final String INVALID_VALUE_CODE = "invalid_value";
    public static final String INTERNAL_SERVER_ERROR_CODE = "internal_server_error";
    public static final String NOT_FOUND_DOMAIN_OBJECT_CODE = "not_found_domain_object";
    public static final String NOT_UNIQUE_DOMAIN_OBJECT_CODE = "not_unique_domain_object";
    public static final String EMPTY_VALUE_CODE = "empty_value";
    public static final String ADMIN_CANNOT_BE_DISABLED_CODE = "admin_cannot_be_disabled";

    public static ModuleException buildInvalidValueException(String fieldName) {
        return new ModuleException("invalid_value", new HashMap<String, Object>() {{
            put("field_name", fieldName);
        }});
    }

    public static ModuleException buildInternalServerErrorException(Throwable e) {
        return new ModuleException("internal_server_error", e.getMessage());
    }

    public static ModuleException buildInternalServerErrorException(String message) {
        return new ModuleException("internal_server_error", message);
    }

    public static ModuleException buildNotFoundDomainObjectException(Class<? extends DomainObject> clazz, long id) {
        return new ModuleException("not_found_domain_object", new HashMap<String, Object>() {{
            put("id", id);
            put("class", clazz.getSimpleName());
        }});
    }

    public static ModuleException buildNotUniqueDomainObjectException(Class<? extends DomainObject> clazz,
                                                                      String fieldName,
                                                                      Serializable value) {
        return new ModuleException("not_unique_domain_object", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("field_name", fieldName);
            put("value", value);
        }});
    }

    public static ModuleException buildEmptyValueException(Class<? extends DomainObject> clazz,
                                                           String fieldName) {
        return new ModuleException("empty_value", new HashMap<String, Object>() {{
            put("class", clazz.getSimpleName());
            put("field_name", fieldName);
        }});
    }

    public static ModuleException buildEmptyValueException(String fieldName) {
        return new ModuleException("empty_value", new HashMap<String, Object>() {{
            put("field_name", fieldName);
        }});
    }

    public static ModuleException buildAdminCannotBeDisabledException() {
        return new ModuleException("admin_cannot_be_disabled");
    }

}
