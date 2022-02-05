package org.myorg.modules.modules.core.database.domainobjects;

import lombok.*;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.database.DomainObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbAccessRole extends DomainObject {

    public static final String FIELD_NAME = "name";

    public static final String QUERY_FIND_ALL = "DbAccessRole.findAll";
    public static final String QUERY_FIND_BY_NAME = "DbAccessRole.findByName";

    @Column(name = FIELD_NAME, nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
            name = CoreModuleConsts.DB_PREFIX + "privilege",
            joinColumns = @JoinColumn(name = "fk_access_role")
    )
    //@OneToMany(mappedBy = "accessRole", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PrivilegeEmbeddable> privileges;

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


    public void addPrivilege(PrivilegeEmbeddable privilege) {
        if (privileges == null) {
            privileges = new HashSet<>();
        }
        privileges.add(privilege);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DbAccessRole that = (DbAccessRole) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
