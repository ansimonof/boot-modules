package org.myorg.modules.modules.core.database.domainobjects;

import lombok.*;
import org.myorg.modules.modules.core.CoreModuleConsts;
import org.myorg.modules.modules.database.DomainObject;

import javax.persistence.*;

@Entity
@Table(
        name = CoreModuleConsts.DB_PREFIX + "api_key"
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbApiKey.QUERY_FIND_BY_NAME,
                        query = "select ak from DbApiKey ak where ak.name = :name"
                ),
                @NamedQuery(
                        name = DbApiKey.QUERY_FIND_ALL,
                        query = "select ak from DbApiKey ak"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbApiKey extends DomainObject {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_VALUE = "value";

    public static final String QUERY_FIND_BY_NAME = "DbApiKey.findByName";
    public static final String QUERY_FIND_ALL = "DbApiKey.findAll";

    @Column(name = FIELD_NAME, nullable = false)
    private String name;

    @Column(name = FIELD_VALUE, nullable = false)
    private byte[] value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fk_access_role",
            referencedColumnName = "id"
    )
    private DbAccessRole accessRole;

}
