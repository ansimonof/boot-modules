package org.myorg.modules.access.context.source;

public class UserSource implements Source {

    private long id;

    public UserSource(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
