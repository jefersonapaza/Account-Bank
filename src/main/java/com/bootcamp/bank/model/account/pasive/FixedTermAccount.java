package com.bootcamp.bank.model.account.pasive;

import com.bootcamp.bank.model.account.IAccount;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
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
