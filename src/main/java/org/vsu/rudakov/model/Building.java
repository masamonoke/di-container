package org.vsu.rudakov.model;

import lombok.*;
import org.vsu.rudakov.annotation.Entity;
import org.vsu.rudakov.annotation.OneToMany;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Building {
    private Long id;
    @OneToMany
    private Set<Educator> educators;
    @OneToMany
    private Set<Child> children;
}
