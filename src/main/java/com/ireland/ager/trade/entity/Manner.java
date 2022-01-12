package com.ireland.ager.trade.entity;

import com.ireland.ager.config.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Manner extends BaseEntity {

    @Id @GeneratedValue
    private Long mannerId;

    private String comment;

    @OneToOne(mappedBy = "manner")
    private Trade trade;
}
