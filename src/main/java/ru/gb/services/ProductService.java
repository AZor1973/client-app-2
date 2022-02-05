package ru.gb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.api.category.api.CategoryGateway;
import ru.gb.api.category.dto.CategoryDto;
import ru.gb.api.product.dto.ProductDto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryGateway categoryGateway;

    public Set<CategoryDto> getCategoryDtoFromString(ProductDto productDto) {
        String category = productDto.getCategory();
        Set<String> categories;
        Set<CategoryDto> result = new HashSet<>();
        if (category.contains(",")) {
            categories = Set.copyOf(Arrays.asList(category.split(",")));
        } else if (category.contains(" ")) {
            categories = Set.copyOf(Arrays.asList(category.split(" ")));
        } else {
            categories = Set.of(category);
        }
        categories = categories.stream().map(String::trim).collect(Collectors.toSet());
        for (String s : categories) {
            int size = result.size();
            for (CategoryDto categoryDto : categoryGateway.getCategoryList()) {
                if (categoryDto.getTitle().equals(s)) {
                    result.add(categoryDto);
                }
            }
            if (size == result.size()){
                throw new NoSuchElementException("There isn't category with name " + s);
            }
        }
        return result;
    }
}
