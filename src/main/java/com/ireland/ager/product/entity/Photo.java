package com.ireland.ager.product.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Photo {
    @Id
    String imageUrl;
}
