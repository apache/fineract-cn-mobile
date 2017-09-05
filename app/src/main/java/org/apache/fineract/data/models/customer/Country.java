package org.apache.fineract.data.models.customer;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 26/07/17.
 */

public class Country {

    @SerializedName("translations")
    Translations translations;

    @SerializedName("name")
    String name;

    @SerializedName("alpha2Code")
    String alpha2Code;

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlphaCode() {
        return alpha2Code;
    }

    public void setAlphaCode(String alphaCode) {
        this.alpha2Code = alphaCode;
    }
}
