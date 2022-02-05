package org.myorg.modules.utils;

public class DomainObjectBuilder {

    public static class BuilderField<T> {
        private T value;
        private boolean isContain;

        public BuilderField() {
            this.value = null;
            this.isContain = false;
        }

        public boolean isContain() {
            return isContain;
        }

        public void setContain(boolean contain) {
            isContain = contain;
        }

        public void setValue(T value) {
            this.isContain = true;
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}
