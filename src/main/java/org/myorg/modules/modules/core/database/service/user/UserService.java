package org.myorg.modules.modules.core.database.service.user;

import org.apache.commons.lang3.StringUtils;
import org.myorg.modules.modules.core.database.dao.AccessRoleDAO;
import org.myorg.modules.modules.core.database.dao.UserDAO;
import org.myorg.modules.modules.core.database.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.database.domainobjects.DbUser;
import org.myorg.modules.modules.core.database.service.accessrole.AccessRoleDto;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements DomainObjectService<DbUser, UserBuilder, UserDto> {

    private final UserDAO userDAO;
    private final AccessRoleDAO accessRoleDAO;

    @Autowired
    public UserService(UserDAO userDAO, AccessRoleDAO accessRoleDAO) {
        this.userDAO = userDAO;
        this.accessRoleDAO = accessRoleDAO;
    }

    @Override
    public UserDto findById(long id) throws ModuleException {
        return UserDto.from(userDAO.findById(id));
    }

    @Override
    public Set<UserDto> findAll() throws ModuleException {
        return userDAO.findAll().stream()
                .map(UserDto::from)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto create(UserBuilder builder) throws ModuleException {
        if (!builder.isContainUsername() || StringUtils.isEmpty(builder.getUsername())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_USERNAME);
        }

        if (!builder.isContainPasswordHash() || StringUtils.isEmpty(builder.getPasswordHash())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_PASSWORD_HASH);
        }

        if (!builder.isContainEnabled() || builder.getIsEnabled() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_IS_ENABLED);
        }

        if (!builder.getIsAdmin() || builder.getIsAdmin() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_IS_ADMIN);
        }

        DbUser dbUser = new DbUser();
        setFields(dbUser, builder);

        return UserDto.from(userDAO.makePersistent(dbUser));
    }

    @Override
    public UserDto update(long id, UserBuilder builder) throws ModuleException {
        DbUser dbUser = userDAO.checkExistenceAndGet(id);

        if (builder.isContainUsername() && StringUtils.isEmpty(builder.getUsername())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_USERNAME);
        }

        if (builder.isContainPasswordHash() && StringUtils.isEmpty(builder.getPasswordHash())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_PASSWORD_HASH);
        }

        if (builder.isContainEnabled() && builder.getIsEnabled() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_IS_ENABLED);
        }

        if (builder.isContainAdmin() && builder.getIsAdmin() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbUser.class, DbUser.FIELD_IS_ADMIN);
        }

        setFields(dbUser, builder);

        return UserDto.from(userDAO.makePersistent(dbUser));
    }

    @Override
    public void remove(long id) throws ModuleException {
        DbUser dbUser = userDAO.findById(id);
        if (dbUser != null) {
            userDAO.makeTransient(dbUser);
        }
    }

    public UserDto findByUsername(String username) throws ModuleException {
        return UserDto.from(userDAO.findByUsername(username));
    }

    public AccessRoleDto getAccessRole(long userId) throws ModuleException {
        DbUser dbUser = userDAO.findById(userId);
        if (dbUser == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbUser.class, userId);
        }

        return AccessRoleDto.from(dbUser.getAccessRole());
    }

    public UserDto addAccessRole(long userId, long accessRoleId) throws ModuleException {
        DbUser dbUser = userDAO.findById(userId);
        if (dbUser == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbUser.class, userId);
        }

        DbAccessRole dbAccessRole = accessRoleDAO.findById(accessRoleId);
        if (dbAccessRole == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbAccessRole.class, accessRoleId);
        }

        dbUser.setAccessRole(dbAccessRole);
        return UserDto.from(userDAO.makePersistent(dbUser));
    }

    private void setFields(DbUser dbUser,
                           UserBuilder builder) throws ModuleException {
        if (builder.isContainUsername()) {
            userDAO.checkUniqueness(
                    dbUser,
                    () -> userDAO.findByUsername(builder.getUsername()),
                    () -> ModuleExceptionBuilder.buildNotUniqueDomainObjectException(DbUser.class, DbUser.FIELD_USERNAME, builder.getUsername()));
            dbUser.setUsername(builder.getUsername());
        }

        if (builder.isContainPasswordHash()) {
            dbUser.setPasswordHash(builder.getPasswordHash());
        }

        if (builder.isContainEnabled()) {
            dbUser.setEnabled(builder.getIsEnabled());
        }

        if (builder.isContainAdmin()) {
            dbUser.setAdmin(builder.getIsAdmin());
        }

        if (builder.isContainAdmin() || builder.isContainEnabled()) {
            if (builder.getIsAdmin() && !builder.getIsAdmin()) {
                throw ModuleExceptionBuilder.buildAdminCannotBeDisabledException();
            }
        }

    }

}
