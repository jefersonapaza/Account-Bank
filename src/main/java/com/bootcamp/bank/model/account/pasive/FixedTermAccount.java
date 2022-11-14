package com.bootcamp.bank.model.account.pasive;

import com.bootcamp.bank.model.account.IAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fixedtermaccount")
/* Cuenta a plazo fijo */
public class FixedTermAccount implements IAccount {

    private String code;
    private Float amount;
    private String idCustomer;
    private String typeCustomer;

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getTypeCustomer() {
        return this.typeCustomer;
    }
}
