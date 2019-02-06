package ua.in.javac.entity.onetomany;

import javax.persistence.*;

/**
 * Created by kernel32 on 22.01.2019.
 */
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String street;

    private String popstalCode;

    //@ManyToOne and @OneToOne by default eager
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name="customer_id")
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPopstalCode() {
        return popstalCode;
    }

    public void setPopstalCode(String popstalCode) {
        this.popstalCode = popstalCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != address.id) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (popstalCode != null ? !popstalCode.equals(address.popstalCode) : address.popstalCode != null) return false;
        return customer != null ? customer.equals(address.customer) : address.customer == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (popstalCode != null ? popstalCode.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        return result;
    }
}
