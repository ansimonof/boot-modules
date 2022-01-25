package org.myorg.modules.modules.core.service.database.user;

import org.apache.commons.lang3.StringUtils;
import org.myorg.modules.modules.core.domainobjects.DbUser;
import org.myorg.modules.modules.core.domainobjects.DomainObject;
import org.myorg.modules.modules.core.service.database.DbAccessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserService extends DbAccessService<DbUser> {

    public DbUserService() {
        super(DbUser.class);
    }

    public List<DbUser> findAll() {
        return DomainObject.getListResult(
                em.createNamedQuery(DbUser.QUERY_FIND_ALL, DbUser.class)
        );
    }

    public DbUser findByUsername(String username) {
        return DomainObject.getSingleResult(
                em.createNamedQuery(DbUser.QUERY_FIND_BY_USERNAME, DbUser.class)
                        .setParameter(DbUser.USERNAME, username)
        );
    }

    public DbUser merge(DbUser dbUser) {
        if (StringUtils.isEmpty(dbUser.getUsername())) {
            // TODO
        }

        if (StringUtils.isEmpty(dbUser.getPasswordHash())) {
            // TODO
        }

        validateUniqueUsername(dbUser.getUsername());

        return em.merge(dbUser);
    }

    public void remove(long id) {
        DbUser dbUser = get(id);
        if (dbUser != null) {
            em.remove(dbUser);
        }
    }


    private void validateUniqueUsername(String email) {
        if (findByUsername(email) != null) {
            // TODO
        }
    }

}
