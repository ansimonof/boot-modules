package org.myorg.modules.modules.core.service.database.accessrole;

import org.myorg.modules.modules.core.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.domainobjects.DomainObject;
import org.myorg.modules.modules.core.service.database.DbAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccessRoleService extends DbAccessService<DbAccessRole> {

    public AccessRoleService() {
        super(DbAccessRole.class);
    }

    public List<DbAccessRole> findAll() {
        return DomainObject.getListResult(
                em.createNamedQuery(DbAccessRole.QUERY_FIND_ALL, DbAccessRole.class)
        );
    }

    @Transactional
    public DbAccessRole merge(DbAccessRole dbAccessRole) {
        return em.merge(dbAccessRole);
    }
}
