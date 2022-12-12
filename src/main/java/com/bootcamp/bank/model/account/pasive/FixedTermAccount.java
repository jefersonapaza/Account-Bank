package com.bootcamp.bank.model.account.pasive;

import com.bootcamp.bank.model.account.Account;
import com.bootcamp.bank.model.account.IAccount;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "fixedtermaccount")
/* Cuenta a plazo fijo */
public class FixedTermAccount extends Account {

    @Id
    private ObjectId _id;



    private Integer transaction;

    public FixedTermAccount(){
        super();
    }
}
