package org.apache.fineract;

import com.google.gson.reflect.TypeToken;

import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.data.models.accounts.LedgerPage;
import org.apache.fineract.data.models.accounts.AccountPage;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.data.models.deposit.ProductDefinition;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.LoanAccountPage;
import org.apache.fineract.data.models.payment.PlannedPaymentPage;
import org.apache.fineract.data.models.product.ProductPage;
import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.data.models.teller.Teller;

import java.util.List;

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

    public static Authentication getAuth() {
        return testDataFactory.getObjectTypePojo(Authentication.class,
                FakeJsonName.AUTHENTICATION);
    }

    public static CustomerPage getCustomerPage() {
        return testDataFactory.getObjectTypePojo(CustomerPage.class,
                FakeJsonName.CUSTOMER_PAGE);
    }

    public static List<Role> getRoles() {
        return testDataFactory.getListTypePojo(new TypeToken<List<Role>>() {
        }, FakeJsonName.ROLES);
    }

    public static LoanAccountPage getloanAccountPage() {
        return testDataFactory.getObjectTypePojo(LoanAccountPage.class,
                FakeJsonName.LOAN_ACCOUNT_PAGE);
    }

    public static List<DepositAccount> getCustomerDepositAccounts() {
        return testDataFactory.getListTypePojo(new TypeToken<List<DepositAccount>>() {
        }, FakeJsonName.DEPOSIT_ACCOUNTS);
    }

    public static List<Identification> getIdentifications() {
        return testDataFactory.getListTypePojo(new TypeToken<List<Identification>>() {
        }, FakeJsonName.IDENTIFICATIONS);
    }

    public static List<ScanCard> getScanCards() {
        return testDataFactory.getListTypePojo(new TypeToken<List<ScanCard>>() {
        }, FakeJsonName.SCAN_CARDS);
    }

    public static List<Command> getCustomerCommands() {
        return testDataFactory.getListTypePojo(new TypeToken<List<Command>>() {
        }, FakeJsonName.CUSTOMER_COMMANDS);
    }

    public static LoanAccount getloanAccount() {
        return testDataFactory.getObjectTypePojo(LoanAccount.class,
                FakeJsonName.LOAN_ACCOUNT);
    }

    public static PlannedPaymentPage getPlannedPaymentPage() {
        return testDataFactory.getObjectTypePojo(PlannedPaymentPage.class,
                FakeJsonName.PLANNED_PAYMENT_PAGE);
    }

    public static LedgerPage getLedgerPage() {
        return testDataFactory.getObjectTypePojo(LedgerPage.class, FakeJsonName.LEDGER_PAGE);
    }

    public static AccountPage getAccountPage() {
        return testDataFactory.getObjectTypePojo(AccountPage.class, FakeJsonName.ACCOUNT_PAGE);
    }

    public static List<Teller> getTeller() {
        return testDataFactory.getListTypePojo(new TypeToken<List<Teller>>() {
        }, FakeJsonName.TELLER);
    }

    public static ProductPage getProductPage() {
        return testDataFactory.getObjectTypePojo(ProductPage.class,
                FakeJsonName.PRODUCT_PAGE);
    }

    public static List<ProductDefinition> getProductDefinition() {
            return testDataFactory.getListTypePojo(new TypeToken<List<ProductDefinition>>() {
            }, FakeJsonName.PRODUCT_DEFINITION);
    }
}
