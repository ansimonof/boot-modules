package org.myorg.modules.modules.core.domainobjects;

import org.myorg.modules.modules.core.CoreModuleConsts;

import javax.persistence.*;

@Entity
@Table(
        name = CoreModuleConsts.DB_PREFIX + "user",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { DbUser.USERNAME }
                )
        }
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbUser.QUERY_FIND_ALL,
                        query = "select u from DbUser u"
                ),
                @NamedQuery(
                        name = DbUser.QUERY_FIND_BY_USERNAME,
                        query = "select u from DbUser u where u.username = :" + DbUser.USERNAME
                )
        }
)
public class DbUser extends DomainObject {

    public static final String USERNAME = "username";
    public static final String FIELD_PASSWORD_HASH = "password_hash";
    public static final String FIELD_IS_ENABLED = "is_enabled";

    public static final String QUERY_FIND_ALL = "DbUser.findAll";
    public static final String QUERY_FIND_BY_USERNAME = "DbUser.findByUsername";

    private String username;
    private String passwordHash;
    private boolean isEnabled;

    private DbAccessRole accessRole;

    @Column(name = USERNAME, nullable = false)
    public String getUsername() {
        return username;
    }

    @Column(name = FIELD_PASSWORD_HASH, nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    @Column(name = FIELD_IS_ENABLED, nullable = false)
    public boolean isEnabled() {
        return isEnabled;
    }

    @ManyToOne
    @JoinColumn(
            name = "access_role_id",
            referencedColumnName = "id"
    )
    public DbAccessRole getAccessRole() {
        return accessRole;
    }

    //-----------------------------------

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setAccessRole(DbAccessRole accessRole) {
        this.accessRole = accessRole;
    }
}
