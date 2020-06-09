package com.zyw.shopping.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_stock")
public class Stock implements Serializable {
    private static final long serialVersionUID = 2451194410162873075L;

    @Id
    private Integer id;

    private String name;

    private Integer stock;
}
