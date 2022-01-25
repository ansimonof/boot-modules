package org.myorg.modules.modules.core.domainobjects;

import org.myorg.modules.modules.core.CoreModuleConsts;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = CoreModuleConsts.DB_PREFIX + "access_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { DbAccessRole.FIELD_NAME }
                )
        }
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbAccessRole.QUERY_FIND_ALL,
                        query = "select ar from DbAccessRole ar"
                ),
                @NamedQuery(
                        name = DbAccessRole.QUERY_FIND_BY_NAME,
                        query = "select ar from DbAccessRole ar where ar.name = :" + DbAccessRole.FIELD_NAME
                )
        }
)
public class DbAccessRole extends DomainObject {

    public static final String FIELD_NAME = "name";

    public static final String QUERY_FIND_ALL = "DbAccessRole.findAll";
    public static final String QUERY_FIND_BY_NAME = "DbAccessRole.findByName";

    private String name;
    private Set<DbPrivilege> privileges;

    @Column(name = FIELD_NAME, nullable = false)
    public String getName() {
        return name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accessRole", cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "access_roles_privileges",
//            joinColumns = @JoinColumn(
//                    name = "access_role_id",
//                    referencedColumnName = "id"
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "privilege_id",
//                    referencedColumnName = "id"
//            )
//    )
    public Set<DbPrivilege> getPrivileges() {
        return privileges;
    }

    //---------------------------

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivileges(Set<DbPrivilege> privileges) {
//        if (privileges != null) {
//            for (DbPrivilege privilege : privileges) {
//                if (privilege != null) {
//                    privilege.setAccessRole(this);
//                }
//            }
//        }
        this.privileges = privileges;
    }

    public void addPrivilege(DbPrivilege privilege) {
        if (privileges == null) {
            privileges = new HashSet<>();
        }
        privileges.add(privilege);
        privilege.setAccessRole(this);
    }
}
