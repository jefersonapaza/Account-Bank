package com.bootcamp.bank.model.account.active;

import com.bootcamp.bank.model.account.Account;
import com.bootcamp.bank.model.account.IAccount;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotEmpty;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "bussinessaccount")
public class BusinessAccount extends Account {



    @Id
    private ObjectId _id;

    private Float limitAmount;
    private Integer transaction;
    private List<String> holder;
    private List<String> signatureAuthorized;

    public BusinessAccount(){
        super();
    }



}
