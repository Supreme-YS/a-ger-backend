package com.ireland.ager.product.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Photo {
    @Id
    String imageUrl;
    public Photo(String url) {
        this.imageUrl=url;
    }
}
