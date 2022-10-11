package org.vsu.rudakov.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Educator {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer groupNumber;
    private Float rating;
}
