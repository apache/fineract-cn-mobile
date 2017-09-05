package org.apache.fineract.data.models.payment;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */

public class ChargeName {

    private String identifier;
    private String name;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
