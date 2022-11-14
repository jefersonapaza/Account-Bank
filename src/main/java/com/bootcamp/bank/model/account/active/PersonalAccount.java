package com.bootcamp.bank.model.account.active;

import com.bootcamp.bank.model.account.IAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "personalaccount")
public class PersonalAccount implements IAccount {

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
