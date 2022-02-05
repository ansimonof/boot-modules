package org.myorg.modules.modules.core.database.service.accessrole;

import lombok.*;
import org.myorg.modules.modules.core.database.domainobjects.DbAccessRole;
import org.myorg.modules.modules.core.database.domainobjects.PrivilegeEmbeddable;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRoleDto implements AbstractDto {

    private long id;

    private String name;

    private Set<PrivilegeDto> privileges;

    public static AccessRoleDto from(DbAccessRole dbAccessRole) {
        if (dbAccessRole == null) {
            return null;
        }

        Set<PrivilegeEmbeddable> privileges;
        if (dbAccessRole.getPrivileges() != null) {
            privileges = dbAccessRole.getPrivileges();
        } else {
            privileges = new HashSet<>();
        }

        return AccessRoleDto.builder()
                .id(dbAccessRole.getId())
                .name(dbAccessRole.getName())
                .privileges(
                        privileges.stream()
                                .map(PrivilegeDto::from)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessRoleDto that = (AccessRoleDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
