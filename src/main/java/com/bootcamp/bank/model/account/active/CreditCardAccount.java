package com.bootcamp.bank.model.account.active;

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

@Document(collection = "creditcardaccount")
public class CreditCardAccount extends Account {

    @Id
    private ObjectId _id;

    private Float limitAmount;
    private Integer transaction;

    public CreditCardAccount(){
        super();
    }

}
