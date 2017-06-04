package com.paniclab.persistory.domainTest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by Сергей on 04.06.2017.
 */
public class Toy implements Serializable {
    private String name;
    private String vendor;
    private BigDecimal price;

    public Toy() {
        name = "";
        vendor = "unknown";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor.toLowerCase();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vendor, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Toy)) return false;
        Toy other = (Toy) obj;
        if (!name.equals(other.getName())) return false;
        if (!vendor.equals(other.getVendor())) return false;
        return price.equals(other.getPrice());
    }
}
