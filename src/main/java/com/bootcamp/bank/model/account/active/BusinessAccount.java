package com.bootcamp.bank.model.account.active;

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
@NoArgsConstructor
@Document(collection = "bussinessaccount")
public class BusinessAccount implements IAccount {



    @Id
    private ObjectId _id;

    @NotEmpty
    private Long id;

    private String code;
    private String idCustomer;
    private String typeCustomer;
    private Float amount;
    private List<String> holder;
    private List<String> signatureAuthorized;


    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getTypeCustomer() {
        return this.typeCustomer;
    }
}
