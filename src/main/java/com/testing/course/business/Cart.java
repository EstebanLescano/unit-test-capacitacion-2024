package com.testing.course.business;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    public static final String INVALID_USER = "The user is invalid";
    public static final String PRODUCT_IS_NOT_SELL_BY_SUPERMARKET = "Product is not sell by supermarket";
    public static final String PRODUCT_QUANTITY_MUST_BE_STRICTLY_POSITIVE = "Product quantity must be strictly positive";
   
    private Map<Object,Double> catalog;
    private Map<Object, Long> products = new HashMap<>();
    private Boolean validUser;

    public Cart(Map<Object,Double> catalog){
        this.validUser = true;
        this.catalog = catalog;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void add(Object aProduct, int aQuantity) {
        assertValidUser();
        assertQuantityIsStrictlyPositive(aQuantity);
        assertProductIsSellBySupermarket(aProduct);
        if(products.containsKey(aProduct)){
            products.put(aProduct, products.get(aProduct) + (long) aQuantity);
        }else{
            products.put(aProduct, (long) aQuantity);
        }
    }

    public void remove(Object aProduct) {
        assertValidUser();
        assertProductIsSellBySupermarket(aProduct);
        if(products.containsKey(aProduct)){
            products.remove(aProduct);
        }else{
            products.remove(aProduct);
        }
    }

    public void assertProductIsSellBySupermarket(Object aProduct) {
        if(!catalog.containsKey(aProduct)) throw new RuntimeException(PRODUCT_IS_NOT_SELL_BY_SUPERMARKET);
    }

    public void assertQuantityIsStrictlyPositive(int aQuantity) {
        if (aQuantity<1) throw new RuntimeException(PRODUCT_QUANTITY_MUST_BE_STRICTLY_POSITIVE);
    }

    public void assertValidUser() {
        if (!validUser) throw new RuntimeException(INVALID_USER);
    }

    public double total() throws ParseException {
            DecimalFormat df = new DecimalFormat("0.00");
            Double unformattedCartTotal = products.keySet().stream().
                    mapToDouble(addedProduct -> priceOf(addedProduct) * products.get(addedProduct)).
                    reduce(0.0, Double::sum);
            String formate = df.format(unformattedCartTotal);
            return  (Double) df.parse(formate);
    }

    public Double priceOf(Object aProduct) {
        return catalog.get(aProduct);
    }

    public Map<Object, Long> getProducts() {
        return products;
    }

    public void setValidUser(Boolean validUser) {
        this.validUser = validUser;
    }

    public Map<Object, Double> getCatalog() {
        return catalog;
    }
}
