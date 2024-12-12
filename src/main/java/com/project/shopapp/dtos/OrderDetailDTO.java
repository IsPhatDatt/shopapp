package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be greater than 0")
    private int orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be greater than 0")
    private int productId;

    @Min(value = 0, message = "Order's ID must be greater than or equal 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Order's ID must be greater than 0")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Order's ID must be greater than or equal 0")
    private Float totalMoney;

    private String color;

}
