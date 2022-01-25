package org.myorg.modules.modules.core.domainobjects;

import org.myorg.modules.modules.core.CoreModuleConsts;

import javax.persistence.*;

@Entity
@Table(
        name = CoreModuleConsts.DB_PREFIX + "api_key"
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbApiKey.QUERY_FIND_ALL,
                        query = "select ak from DbApiKey ak"
                )
        }
)
public class DbApiKey extends DomainObject {

    public static final String VALUE = "value";

    public static final String QUERY_FIND_ALL = "DbApiKey.findAll";

    private byte[] value;
    private DbAccessRole accessRole;

    @Column(name = VALUE, nullable = false)
    public byte[] getValue() {
        return value;
    }

    @ManyToOne
    @JoinColumn(
            name = "access_role_id",
            referencedColumnName = "id"
    )
    public DbAccessRole getAccessRole() {
        return accessRole;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setAccessRole(DbAccessRole accessRole) {
        this.accessRole = accessRole;
    }
}
