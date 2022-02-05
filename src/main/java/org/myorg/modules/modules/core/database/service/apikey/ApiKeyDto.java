package org.myorg.modules.modules.core.database.service.apikey;

import lombok.*;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyDto implements AbstractDto {

    private long id;

    private String name;

    private String value;

    public static ApiKeyDto create(long id, String name, String value) {
        return ApiKeyDto.builder()
                .id(id)
                .name(name)
                .value(value)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiKeyDto apiKeyDto = (ApiKeyDto) o;
        return Objects.equals(name, apiKeyDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
