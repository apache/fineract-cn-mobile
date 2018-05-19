package org.apache.fineract;

import org.apache.fineract.data.models.customer.Customer;

/**
 * FakeRemoteDataSource is reading the local json files into the java object using gson.
 * Created by Rajan Maurya on 25/6/17.
 */
public class FakeRemoteDataSource {

    private static TestDataFactory testDataFactory = new TestDataFactory();

    public static Customer getCustomer() {
        return testDataFactory.getObjectTypePojo(Customer.class,
                FakeJsonName.CUSTOMER);
    }
}
