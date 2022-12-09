package com.bootcamp.bank.model.generic;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movements")
public class Movements {

    @Id
    private ObjectId _id;

    @NotEmpty
    private Long id_table;

    @Null
    private String code_customer;

    @NotEmpty
    private String type;

    @NotEmpty
    private Date creation;

    @Null
    private String description;

    @NotEmpty
    private Integer status;


}
