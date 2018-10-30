package interview.task.domain;

import java.util.Objects;

public class Product {
    private int count = 1;
    private int value;
    private String productType;

    public Product() {
    }

    public Product(int count, int value, String productType) {
        this.count = count;
        this.value = value;
        this.productType = productType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseCount(int count) {
        this.count += count;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return this.value == product.getValue() &&
                Objects.equals(productType, product.productType);
    }

    @Override
    public String toString() {
        return "Product{" +
                "count=" + count +
                ", value=" + value +
                ", productType='" + productType + '\'' +
                "}\n";
    }
}
