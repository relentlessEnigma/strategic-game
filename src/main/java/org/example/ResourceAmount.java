package org.example;

import java.util.Objects;

public class ResourceAmount {
    private Resource resource;
    private int amount;

    public ResourceAmount(Resource resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceAmount that = (ResourceAmount) o;
        return amount == that.amount && resource == that.resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, amount);
    }

    @Override
    public String toString() {
        return "ResourceAmount{" +
                "resource=" + resource +
                ", amount=" + amount +
                '}';
    }
}
