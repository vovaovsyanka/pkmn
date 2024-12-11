package ovsyannikovvi.pkmn.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ovsyannikovvi.pkmn.controllers.CardController;
import ovsyannikovvi.pkmn.entities.CardEntity;
import ovsyannikovvi.pkmn.services.CardService;

import java.io.File;

@Component
@RequiredArgsConstructor
public class DataTesting {

    private final ObjectMapper objectMapper;

    private final CardController cardController;

    @PostConstruct
    @SneakyThrows
    public void init() {
        System.out.println("Post construct init");
        File jsonFile = new File("D:\\pkmn\\src\\main\\resources\\cards.json");
        CardEntity card = objectMapper.readValue(jsonFile, CardEntity.class);

        // Создание карточки на сервере
        // Установите уникальный идентификатор
        CardEntity createdCard = cardController.createCard(card).getBody();
        System.out.println("Created Card: " + createdCard);

        // Получение изображения карты по имени карты
        String cardName = createdCard.getName();
        String imageUrl = cardController.getCardImageByName(cardName).getBody();
        if (imageUrl != null) {
            System.out.println("Image URL: " + imageUrl);
        } else {
            System.out.println("Image not found for card: " + cardName);
        }
    }
}
