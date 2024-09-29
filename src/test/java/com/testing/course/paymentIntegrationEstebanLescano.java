package com.testing.course;

import com.testing.course.business.PaymentService;
import com.testing.course.model.CreditCard;
import com.testing.course.model.Payment;
import com.testing.course.model.User;
import com.testing.course.repository.CreditCardRepository;
import com.testing.course.repository.PaymentRepository;
import com.testing.course.repository.UserList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class paymentIntegrationEstebanLescano {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private UserList userList;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;
    private User user;
    private CreditCard creditCard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        payment = new Payment();
        payment.setUsername("user123");
        payment.setTotal(100.00);
        payment.setCreditCardNumber("1234-5678-9876-5432");

        user = new User();
        user.setUsername("user123");

        creditCard = new CreditCard();
        creditCard.setNumber("1234-5678-9876-5432");
        creditCard.setValid(true);
    }

    @Test
    public void testSavePayment_successful() {
        when(userList.userList()).thenReturn(List.of(user));
        when(creditCardRepository.findByNumber(payment.getCreditCardNumber())).thenReturn(Optional.of(creditCard));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.savePayment(payment);

        assertNotNull(savedPayment);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    public void testSavePayment_userNotValid() {
        user.setValid(false);
        when(userList.userList()).thenReturn(List.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(payment);
        });

        assertEquals("User must be validated.", exception.getMessage());
        verify(paymentRepository, never()).save(payment);
    }

    @Test
    public void testSavePayment_creditCardNotFound() {
        when(userList.userList()).thenReturn(List.of(user));
        when(creditCardRepository.findByNumber(payment.getCreditCardNumber())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(payment);
        });

        assertEquals("Credit card not exist", exception.getMessage());
        verify(paymentRepository, never()).save(payment);
    }

    @Test
    public void testSavePayment_creditCardInvalid() {
        creditCard.setValid(false);

        when(userList.userList()).thenReturn(List.of(user));
        when(creditCardRepository.findByNumber(payment.getCreditCardNumber())).thenReturn(Optional.of(creditCard));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(payment);
        });

        assertEquals("Credit card is invalid", exception.getMessage());
        verify(paymentRepository, never()).save(payment);
    }

    @Test
    public void testSavePayment_totalAmountInvalid() {
        payment.setTotal(0.0);
        when(userList.userList()).thenReturn(List.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(payment);
        });
        assertEquals("Total amount must be greater than zero.", exception.getMessage());
        verify(paymentRepository, never()).save(payment);
    }
}
