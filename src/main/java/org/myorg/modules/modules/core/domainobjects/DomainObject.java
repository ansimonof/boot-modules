package org.myorg.modules.modules.core.domainobjects;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public class DomainObject {

    private Long id;
    private int version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    //----------------------


    public static <T extends DomainObject> List<T> getListResult(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    public static <T extends DomainObject> T getSingleResult(TypedQuery<T> typedQuery) {
        return getListResult(typedQuery).stream()
                .findFirst()
                .orElse(null);
    }

}
