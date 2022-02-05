package org.myorg.modules.modules.exception;

import org.myorg.modules.modules.database.DomainObject;

import java.io.Serializable;
import java.util.HashMap;

public class ModuleExceptionBuilder {

    public static ModuleException buildInternalServerException(Throwable e) {
        return new ModuleException("internal_server_exception", e.getMessage());
    }

    public static ModuleException buildInternalServerException(String message) {
        return new ModuleException("internal_server_exception", message);
    }

    public static ModuleException buildNotFoundDomainObjectException(Class<? extends DomainObject> clazz, long id) {
        return new ModuleException("not_found_domain_object", new HashMap<String, Object>() {{
            put("id", id);
            put("class", clazz);
        }});
    }

    public static ModuleException buildNotUniqueDomainObjectException(Class<? extends DomainObject> clazz,
                                                                      String fieldName,
                                                                      Serializable value) {
        return new ModuleException("not_unique_domain_object", new HashMap<String, Object>() {{
            put("class", clazz);
            put("field_name", fieldName);
            put("value", value);
        }});
    }

    public static ModuleException buildEmptyValueException(Class<? extends DomainObject> clazz,
                                                           String fieldName) {
        return new ModuleException("empty_value_domain_object", new HashMap<String, Object>() {{
            put("class", clazz);
            put("field_name", fieldName);
        }});
    }

    public static ModuleException buildAdminCannotBeDisabledException() {
        return new ModuleException("admin_cannot_be_disabled");
    }

    public static ModuleException buildEmptyPrivilegeKeyException() {
        return new ModuleException("empty_privilege_key");
    }

    public static ModuleException buildEmptyAccessOpsException() {
        return new ModuleException("empty_access_ops");
    }
}
