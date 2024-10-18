package is.lab1.is_lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Car {

    @Column(nullable = false)
    private String name; //Поле может быть null

    @Column(nullable = false)
    private Boolean cool; //Поле может быть null
}