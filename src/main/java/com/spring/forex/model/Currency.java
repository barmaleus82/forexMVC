package com.spring.forex.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "currencies")
public class Currency implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @Column(nullable = false)
    private int pointRatio;

    private double swap;

    private double spread;

    @Override
    public int compareTo(Object obj) {
        return name.compareTo(((Currency) obj).getName());
    }
}
