package org.myorg.modules.modules.core.domainobjects;

import org.myorg.modules.modules.core.CoreModuleConsts;

import javax.persistence.*;

@Entity
@Table(
        name = CoreModuleConsts.DB_PREFIX + "privilege",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { DbPrivilege.FIELD_KEY }
                )
        }
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbPrivilege.QUERY_FIND_ALL,
                        query = "select p from DbPrivilege p"
                ),
                @NamedQuery(
                        name = DbPrivilege.QUERY_FIND_BY_NAME,
                        query = "select p from DbPrivilege p where p.key = :" + DbPrivilege.FIELD_KEY
                )
        }
)
public class DbPrivilege extends DomainObject {

    public static final String FIELD_KEY = "key";
    public static final String FIELD_VALUE = "value";

    public static final String QUERY_FIND_ALL = "DbPrivilege.findAll";
    public static final String QUERY_FIND_BY_NAME = "DbPrivilege.findByName";

    private String key;
    private int value;
    private DbAccessRole accessRole;

    @Column(name = FIELD_KEY, nullable = false)
    public String getKey() {
        return key;
    }

    @Column(name = FIELD_VALUE, nullable = false)
    public int getValue() {
        return value;
    }

    @ManyToOne(optional = false)
    public DbAccessRole getAccessRole() {
        return accessRole;
    }

    //--------------------

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setAccessRole(DbAccessRole accessRole) {
        this.accessRole = accessRole;
    }
}
