package com.payments.client;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class TestsSuite {
	

    @Test 
    public void run() {
        JUnitCore.runClasses(TestDefaultPaymentWithRefund.class,TestDefaultPaymentWithReverse.class);
    }
}
