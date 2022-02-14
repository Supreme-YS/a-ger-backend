package com.ireland.ager.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardUrlId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "board_id")
    private Board board;

    private String url;


}
