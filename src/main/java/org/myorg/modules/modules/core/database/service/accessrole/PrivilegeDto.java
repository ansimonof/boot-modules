package org.myorg.modules.modules.core.database.service.accessrole;

import lombok.*;
import org.myorg.modules.access.privilege.AccessOp;
import org.myorg.modules.modules.core.database.domainobjects.PrivilegeEmbeddable;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeDto implements AbstractDto {

    private String key;

    private AccessOp[] ops;

    public static PrivilegeDto from(PrivilegeEmbeddable privilege) {
        if (privilege == null) {
            return null;
        }

        return PrivilegeDto.builder()
                .key(privilege.getKey())
                .ops(privilege.getValue())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivilegeDto that = (PrivilegeDto) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
