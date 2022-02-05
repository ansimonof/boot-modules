package org.myorg.modules.modules.core.database.service.apikey;

import org.apache.commons.lang3.StringUtils;
import org.myorg.modules.crypto.CryptoService;
import org.myorg.modules.modules.core.database.dao.AccessRoleDAO;
import org.myorg.modules.modules.core.database.dao.ApiKeyDAO;
import org.myorg.modules.modules.core.database.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.database.domainobjects.DbApiKey;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiKeyService implements DomainObjectService<DbApiKey, ApiKeyBuilder, ApiKeyDto> {

    private final ApiKeyDAO apiKeyDAO;
    private final AccessRoleDAO accessRoleDAO;
    private final CryptoService cryptoService;

    @Autowired
    public ApiKeyService(ApiKeyDAO apiKeyDAO, AccessRoleDAO accessRoleDAO, CryptoService cryptoService) {
        this.apiKeyDAO = apiKeyDAO;
        this.accessRoleDAO = accessRoleDAO;
        this.cryptoService = cryptoService;
    }

    @Override
    public ApiKeyDto findById(long id) throws ModuleException {
        DbApiKey dbApiKey = apiKeyDAO.findById(id);
        if (dbApiKey == null) {
            return null;
        }

        String value = cryptoService.decodeAsString(dbApiKey.getValue());
        return ApiKeyDto.create(dbApiKey.getId(), dbApiKey.getName(), value);
    }

    @Override
    public Set<ApiKeyDto> findAll() throws ModuleException {
        return apiKeyDAO.findAll().stream()
                .map(dbApiKey -> ApiKeyDto.create(dbApiKey.getId(), dbApiKey.getName(),
                        cryptoService.decodeAsString(dbApiKey.getValue())))
                .collect(Collectors.toSet());
    }


    @Override
    public ApiKeyDto create(ApiKeyBuilder builder) throws ModuleException {
        if (!builder.isContainName() || StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbApiKey.class, DbApiKey.FIELD_NAME);
        }

        if (!builder.isContainValue() || StringUtils.isEmpty(builder.getValue())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbApiKey.class, DbApiKey.FIELD_VALUE);
        }

        DbApiKey dbApiKey = new DbApiKey();
        setFields(dbApiKey, builder);

        dbApiKey = apiKeyDAO.makePersistent(dbApiKey);

        return ApiKeyDto.create(dbApiKey.getId(), dbApiKey.getName(), builder.getValue());
    }

    @Override
    public ApiKeyDto update(long id, ApiKeyBuilder builder) throws ModuleException {
        DbApiKey dbApiKey = apiKeyDAO.checkExistenceAndGet(id);

        if (builder.isContainName() && StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbApiKey.class, DbApiKey.FIELD_NAME);
        }

        if (builder.isContainValue() && StringUtils.isEmpty(builder.getValue())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbApiKey.class, DbApiKey.FIELD_VALUE);
        }

        setFields(dbApiKey, builder);

        dbApiKey = apiKeyDAO.makePersistent(dbApiKey);

        String value = cryptoService.decodeAsString(dbApiKey.getValue());
        return ApiKeyDto.create(dbApiKey.getId(), dbApiKey.getName(), value);
    }

    @Override
    public void remove(long id) throws ModuleException {
        DbApiKey dbApiKey = apiKeyDAO.findById(id);
        if (dbApiKey != null) {
            apiKeyDAO.makeTransient(dbApiKey);
        }
    }

    public AccessRoleDto getAccessRole(long apiKeyId) throws ModuleException {
        DbApiKey dbApiKey = apiKeyDAO.checkExistenceAndGet(apiKeyId);
        return AccessRoleDto.from(dbApiKey.getAccessRole());
    }

    public ApiKeyDto addAccessRole(long apiKeyId, long accessRoleId) throws ModuleException {
        DbApiKey dbApiKey = apiKeyDAO.checkExistenceAndGet(apiKeyId);
        DbAccessRole dbAccessRole = accessRoleDAO.checkExistenceAndGet(accessRoleId);

        dbApiKey.setAccessRole(dbAccessRole);
        dbApiKey = apiKeyDAO.makePersistent(dbApiKey);
        String value = cryptoService.decodeAsString(dbApiKey.getValue());
        return ApiKeyDto.create(dbApiKey.getId(), dbApiKey.getName(), value);
    }

    public ApiKeyDto findByValue(String value) throws ModuleException {
        for (ApiKeyDto apiKeyDto : findAll()) {
            if (Objects.equals(apiKeyDto.getValue(), value)) {
                return apiKeyDto;
            }
        }

        return null;
    }

    private void setFields(DbApiKey dbApiKey, ApiKeyBuilder builder) throws ModuleException {
        if (builder.isContainName()) {
            apiKeyDAO.checkUniqueness(
                    dbApiKey,
                    () -> apiKeyDAO.findByName(builder.getName()),
                    () -> ModuleExceptionBuilder.buildNotUniqueDomainObjectException(DbApiKey.class, DbApiKey.FIELD_NAME, builder.getName())
            );
            dbApiKey.setName(builder.getName());
        }

        if (builder.isContainValue()) {
            dbApiKey.setValue(cryptoService.encode(builder.getValue()));
        }
    }
}
