package com.ireland.ager.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "boardUrlId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class BoardUrl implements Serializable {

    @Id
    @GeneratedValue
    private Long boardUrlId;

    @ManyToOne
    @JsonIgnore
    private Board board;

    private String url;


}
