package org.myorg.modules.modules.core.database.service.user;

import lombok.*;
import org.myorg.modules.modules.core.database.domainobjects.DbUser;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements AbstractDto {

    private long id;

    private String username;

    private boolean isEnabled;

    private boolean isAdmin;

    public static UserDto from(DbUser dbUser) {
        if (dbUser == null) {
            return null;
        }

        return UserDto.builder()
                .id(dbUser.getId())
                .username(dbUser.getUsername())
                .isAdmin(dbUser.isAdmin())
                .isEnabled(dbUser.isEnabled())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
