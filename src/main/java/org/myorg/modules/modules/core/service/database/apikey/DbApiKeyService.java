package org.myorg.modules.modules.core.service.database.apikey;

import org.myorg.modules.modules.core.domainobjects.DbApiKey;
import org.myorg.modules.modules.core.domainobjects.DomainObject;
import org.myorg.modules.modules.core.service.database.DbAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class DbApiKeyService extends DbAccessService<DbApiKey> {

    public DbApiKeyService() {
        super(DbApiKey.class);
    }

    @Transactional
    public DbApiKey merge(DbApiKey dbApiKey) {
        return em.merge(dbApiKey);
    }

    public List<DbApiKey> findAll() {
        return DomainObject.getListResult(
                em.createNamedQuery(DbApiKey.QUERY_FIND_ALL, DbApiKey.class));
    }

    public DbApiKey findByValue(byte[] value) {
        for (DbApiKey dbApiKey : findAll()) {
            if (Arrays.equals(value, dbApiKey.getValue())) {
                return dbApiKey;
            }
        }

        return null;
    }

}
