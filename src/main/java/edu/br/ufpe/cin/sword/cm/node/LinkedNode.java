package edu.br.ufpe.cin.sword.cm.node;

import java.util.Objects;
import java.util.Optional;

public class LinkedNode<T> {

    private final Optional<LinkedNode<T>> previous;

    private final T value;

    public LinkedNode(T value) {
        this.value = value;
        this.previous = Optional.empty();
    }

    private LinkedNode(LinkedNode<T> previous, T value) {
        this.previous = Optional.of(previous);
        this.value = value;
    }

    public LinkedNode<T> push(T value) {
        return new LinkedNode<>(this, value);
    }

    public boolean contains(T value) {
        return Objects.equals(this.value, value)
                || this.previous
                        .map(previous -> previous.contains(value))
                        .orElse(false);
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinkedNode other = (LinkedNode) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
