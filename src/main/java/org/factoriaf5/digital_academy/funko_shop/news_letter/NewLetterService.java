package org.factoriaf5.digital_academy.funko_shop.news_letter;

import java.security.SecureRandom;
import java.util.stream.Collectors;

import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewLetterService {

    private final NewLetterRepository newLetterRepository;

    public NewLetterService(NewLetterRepository newLetterRepository) {
        this.newLetterRepository = newLetterRepository;
    }

    public NewLetterDTO createNewLetter(NewLetterDTO newLetterDTO) {

        if (newLetterRepository.existsByEmail(newLetterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        NewLetter newLetter = mapToEntity(newLetterDTO);

        NewLetter savedNewLetter = newLetterRepository.save(newLetter);
        return mapToDTO(savedNewLetter);

    }

    public void deleteProduct(String code) {
        NewLetter newLetter = newLetterRepository.findByCode(code);
        if (newLetter == null) {
            throw new ProductNotFoundException("Product not found with code: " + code);
        }
        newLetterRepository.deleteById(newLetter.getId());
    }

    private NewLetter mapToEntity(NewLetterDTO dto) {

        String code = new SecureRandom().ints(8, 0, 36)
                .mapToObj(i -> Integer.toString(i, 36))
                .collect(Collectors.joining()).toUpperCase(

                );
        NewLetter newLetter = new NewLetter();
        newLetter.setCode(code);
        newLetter.setEmail(dto.getEmail());

        return newLetter;
    }

    private NewLetterDTO mapToDTO(NewLetter newLetter) {
        return new NewLetterDTO(
                newLetter.getId(),
                newLetter.getCode(),
                newLetter.getEmail(),
                newLetter.getCreatedAt());
    }
}