package main.java.ro.unibuc.hello.dto;

import java.util.Locale.Category;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long identifier;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private Category category;
}
