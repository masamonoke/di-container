package org.vsu.rudakov.model;

import lombok.*;
import org.vsu.rudakov.annotation.Entity;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Child {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer groupNumber;
}
