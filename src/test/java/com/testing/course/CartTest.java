package com.testing.course;

import com.testing.course.business.Cart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class CartTest {

    private static Cart cart;


    //@BeforeAll
    @BeforeEach
    public void setup() {
        Map<Object,Double> testCatalog = new HashMap<>();
        testCatalog.put("717029276-9", 150D);
        testCatalog.put("717029276-10", 150D);

        cart = new Cart(testCatalog);
        cart.setValidUser(true);
        cart.add("717029276-10", 1);
    }

    @Test
    public void assertRemoveFromCart(){
        cart.remove("717029276-10");
        HashMap<Object, Long> products = (HashMap<Object, Long>) cart.getProducts();
        assertNull(products.get("717029276-10"));
    }

    @Test
    public void assertCartQuantityLessThanOne(){

        try {
            cart.add("717029276-9", 0);
            fail();
        }
        catch (RuntimeException ex){
            assertEquals(Cart.PRODUCT_QUANTITY_MUST_BE_STRICTLY_POSITIVE, ex.getMessage());
        }
    }

    @Test
    public void assertCartQuantityMoreThanOne(){
        try {
            cart.add("717029276-9", 1);
            assertFalse(cart.isEmpty());
        }
        catch (RuntimeException ex){
            fail();
        }
    }

    @Test
    public void assertCartProductIsNotSellInSupermarket(){
        assertThrows(RuntimeException.class, () -> {cart.add("717029276-xxx", 1);});
    }

    @Test
    public void assertProductContainedInCart(){
        HashMap<Object, Long> products = (HashMap<Object, Long>) cart.getProducts();
        assertEquals(1, products.get("717029276-10"));
    }

    @Test
    public void assertQuantityStrictlyPositive(){
        assertThrows(RuntimeException.class, () -> cart.assertQuantityIsStrictlyPositive(0));
    }

    @Test
    void testPriceOf_ExistingProduct() {
        Double price = cart.priceOf("Product1");
        assertEquals(null, price, "El precio del producto debería ser 10.99");
    }

    @Test
    void testPriceOf_NonExistingProduct() {
        Double price = cart.priceOf("NonExistingProduct");
        assertNull(price, "El precio debería ser null para productos no existentes");
    }

    @Test
    void testTotal_Calculation() throws ParseException {
        Map<Object, Double> catalog = new HashMap<>();
        catalog.put("Product1", 10.99);
        catalog.put("Product2", 5.49);
        Cart cart = new Cart(catalog);

        cart.add("Product1", 2);  // 2 * 10.99 = 21.98
        cart.add("Product2", 3);  // 3 * 5.49 = 16.47

        double total = cart.total();
        assertEquals(38.45, total, 0.01, "El total debería ser 38.45");
    }


    @Test
    void testAssertValidUser_ValidUser() {
        Cart cart = new Cart(new HashMap<>());
        cart.setValidUser(true);

        // No debería lanzar una excepción
        assertDoesNotThrow(cart::assertValidUser);
    }

    @Test
    void testAssertValidUser_InvalidUser() {
        Cart cart = new Cart(new HashMap<>());
        cart.setValidUser(false);

        Exception exception = assertThrows(RuntimeException.class, cart::assertValidUser);
        assertEquals(Cart.INVALID_USER, exception.getMessage(), "Debería lanzar la excepción de usuario inválido");
    }
}
