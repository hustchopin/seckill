package com.zyw.shopping.entity;

import lombok.Data;


import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@Table(name = "t_order")
public class Order implements Serializable {
    private static final long serialVersionUID = -8867272732777764701L;

    @Id
    private Integer id;

    private String orderName;

    private String orderUser;
}
