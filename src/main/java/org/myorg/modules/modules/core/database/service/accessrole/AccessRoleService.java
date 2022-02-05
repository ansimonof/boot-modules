package org.myorg.modules.modules.core.database.service.accessrole;

import org.apache.commons.lang3.StringUtils;
import org.myorg.modules.modules.core.database.dao.AccessRoleDAO;
import org.myorg.modules.modules.core.database.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.database.domainobjects.PrivilegeEmbeddable;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccessRoleService implements DomainObjectService<DbAccessRole, AccessRoleBuilder, AccessRoleDto> {

    private final AccessRoleDAO accessRoleDAO;

    public AccessRoleService(AccessRoleDAO accessRoleDAO) {
        this.accessRoleDAO = accessRoleDAO;
    }

    @Override
    public AccessRoleDto findById(long id) throws ModuleException {
        return AccessRoleDto.from(accessRoleDAO.findById(id));
    }

    @Override
    public Set<AccessRoleDto> findAll() throws ModuleException {
        return accessRoleDAO.findAll().stream()
                .map(AccessRoleDto::from)
                .collect(Collectors.toSet());
    }

    @Override
    public AccessRoleDto create(AccessRoleBuilder builder) throws ModuleException {
        if (!builder.isContainName() || StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbAccessRole.class, DbAccessRole.FIELD_NAME);
        }

        DbAccessRole dbAccessRole = new DbAccessRole();
        setFields(dbAccessRole, builder);

        return AccessRoleDto.from(accessRoleDAO.makePersistent(dbAccessRole));
    }

    @Override
    public AccessRoleDto update(long id, AccessRoleBuilder builder) throws ModuleException {
        DbAccessRole dbAccessRole = accessRoleDAO.checkExistenceAndGet(id);

        if (builder.isContainName() && StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbAccessRole.class, DbAccessRole.FIELD_NAME);
        }

        setFields(dbAccessRole, builder);

        return AccessRoleDto.from(accessRoleDAO.makePersistent(dbAccessRole));
    }

    @Override
    public void remove(long id) throws ModuleException {
        DbAccessRole dbAccessRole = accessRoleDAO.findById(id);
        if (dbAccessRole != null) {
            accessRoleDAO.makePersistent(dbAccessRole);
        }
    }

    public Set<PrivilegeDto> getPrivileges(long accessId) throws ModuleException {
        DbAccessRole dbAccessRole = accessRoleDAO.checkExistenceAndGet(accessId);
        AccessRoleDto accessRoleDto = AccessRoleDto.from(dbAccessRole);
        return accessRoleDto.getPrivileges();
    }

    public AccessRoleDto addPrivileges(long accessRoleId, Set<PrivilegeBuilder> builders) throws ModuleException {
        DbAccessRole dbAccessRole = accessRoleDAO.checkExistenceAndGet(accessRoleId);

        Set<PrivilegeEmbeddable> privileges = new HashSet<>();
        for (PrivilegeBuilder builder : builders) {
            if (!builder.isContainKey() || StringUtils.isEmpty(builder.getKey())) {
                throw ModuleExceptionBuilder.buildEmptyPrivilegeKeyException();
            }

            if (!builder.isContainOps() || builder.getOps() == null) {
                throw ModuleExceptionBuilder.buildEmptyAccessOpsException();
            }

            PrivilegeEmbeddable privilege = new PrivilegeEmbeddable();
            privilege.setKey(builder.getKey());
            privilege.setValue(builder.getOps());
            privileges.add(privilege);
        }

        dbAccessRole.setPrivileges(privileges);
        return AccessRoleDto.from(accessRoleDAO.makePersistent(dbAccessRole));
    }

    private void setFields(DbAccessRole dbAccessRole, AccessRoleBuilder builder) throws ModuleException {
        if (builder.isContainName()) {
            accessRoleDAO.checkUniqueness(
                    dbAccessRole,
                    () -> accessRoleDAO.findByName(builder.getName()),
                    () -> ModuleExceptionBuilder.buildNotUniqueDomainObjectException(DbAccessRole.class, DbAccessRole.FIELD_NAME, builder.getName())
            );
            dbAccessRole.setName(builder.getName());
        }
    }
}
