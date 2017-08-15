package com.mifos.apache.fineract.data.models.deposit;

public class Currency {

    private String code;
    private String name;
    private String sign;
    private Integer scale;

    public Currency() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(final String sign) {
        this.sign = sign;
    }

    public Integer getScale() {
        return this.scale;
    }

    public void setScale(final Integer scale) {
        this.scale = scale;
    }
}