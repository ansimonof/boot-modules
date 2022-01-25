package org.myorg.modules.access.context.source;

public class ApiKeySource implements Source {

    private long id;

    public ApiKeySource(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
