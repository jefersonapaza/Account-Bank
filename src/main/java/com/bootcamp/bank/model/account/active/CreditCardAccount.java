package com.bootcamp.bank.model.account.active;

import com.bootcamp.bank.model.account.IAccount;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "creditcardaccount")
public class CreditCardAccount implements IAccount {

    private String code;
    private String idCustomer;
    private String typeCustomer;
    private Float amount;


    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getTypeCustomer() {
        return this.typeCustomer;
    }
}
