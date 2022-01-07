package com.ireland.ager.account.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.ireland.ager.config.BaseEntity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Account extends BaseEntity {
    @Id @GeneratedValue
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    String refreshToken;
}